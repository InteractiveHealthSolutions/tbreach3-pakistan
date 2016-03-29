/**
 * Copyright(C) 2016 Interactive Health Solutions, Pvt. Ltd.
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. 
 * 
 */

package com.ihsinformatics.tbr3synchronizer.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Synchronize class to transfer data between to databases
 * 
 * @author Hera Rafique
 *
 */

public class Synchronize {

	ConnectionProvider connectionProvider;
	/* required no. of days (declared in tbr3synchronizer.properties file) */
	int days;
	/* local database connection */
	Connection connectLocal;
	/* main database connection */
	Connection connectMain;
	ArrayList<String> autoIncrementKey;

	public Synchronize() {
		connectionProvider = new ConnectionProvider();
		days = connectionProvider.getDays();
		connectLocal = connectionProvider.getOpenMrsLocalConnection();
		connectMain = connectionProvider.getOpenMrsMainConnection();
		autoIncrementKey = new ArrayList<String>();
	}

	/**
	 * list of table having auto-increment primary key
	 */
	public void setAutoIncrementKeyList() {
		try {
			Statement statement = connectLocal.createStatement();
			ResultSet result = statement
					.executeQuery("SELECT table_name FROM information_schema.columns "
							+ "WHERE TABLE_SCHEMA = 'openmrs' AND EXTRA like '%auto_increment%' AND TABLE_NAME not like '%temp_%' AND COLUMN_KEY = 'PRI'");
			while (result.next()) {
				for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
					this.autoIncrementKey.add((String) result.getObject(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * select data from one database(A) and insert it into respective temp_table
	 * of other database(B) steps: drop old 'temp_table' in database 'B' if
	 * present create new 'temp_table' in database 'B' select data from database
	 * 'A' 'table' and insert it into 'temp_table' in database 'B'
	 * 
	 */
	public void insertDataIntoTempTable() {
		this.setAutoIncrementKeyList();
		for (int t = 0; t < OpenMrsMeta.TABLE_NAME_LIST.size(); t++) {
			for (String table : OpenMrsMeta.TABLE_NAME_LIST.get(t)) {
				boolean isAutoIncPk = (OpenMrsMeta.TABLE_NAME_LIST
						.contains("temp_" + table)) ? true : false;
				// System.out.println("* " + table);
				try {
					Statement statement = connectMain.createStatement();
					statement.executeUpdate("DROP TABLE IF EXISTS " + "temp_"
							+ table);
					statement = connectMain.createStatement();

					System.out.println(new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss").format(new Date())
							+ " "
							+ "CREATE TABLE temp_" + table);

					statement.executeUpdate("CREATE TABLE temp_" + table
							+ " LIKE " + table);

					PreparedStatement selectStatement = null;
					QueryStringLocal queryStringLocal = new QueryStringLocal();
					switch (t) {
					case 0:
						selectStatement = connectLocal
								.prepareStatement(queryStringLocal
										.getDataCreatedChangedNdaysAgo(table,
												days));
						break;
					case 1:
						selectStatement = connectLocal
								.prepareStatement(queryStringLocal
										.getDataChangedNdaysAgo(table, days));
						break;
					case 2:
						selectStatement = connectLocal
								.prepareStatement(queryStringLocal
										.getDataCreatedNdaysAgo(table, days));
						break;
					case 3:
						selectStatement = connectLocal
								.prepareStatement(queryStringLocal
										.getData(table));
						break;
					default:
						break;
					}
					System.out.println(new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss").format(new Date())
							+ " "
							+ selectStatement.toString());
					ResultSet result = selectStatement.executeQuery();
					ResultSetMetaData meta = result.getMetaData();
					StringBuilder columnNames = new StringBuilder();
					StringBuilder bindVariables = new StringBuilder();
					if (isAutoIncPk == false) {
						for (int i = 1; i <= meta.getColumnCount(); i++) {
							if (i > 1) {
								columnNames.append(", ");
								bindVariables.append(", ");
							}
							columnNames.append(meta.getColumnName(i));
							bindVariables.append('?');
						}
						PreparedStatement insertStatement = connectMain
								.prepareStatement("insert into temp_" + table
										+ " (" + columnNames + ") values ("
										+ bindVariables + ")");
						System.out.println(new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss").format(new Date())
								+ " "
								+ insertStatement.toString());

						while (result.next()) {
							for (int i = 1; i <= meta.getColumnCount(); i++)
								insertStatement.setObject(i,
										result.getObject(i));
							insertStatement.executeUpdate();
						}
					} else { // if table have auto-increment primary key
						for (int i = 1; i <= meta.getColumnCount(); i++) {
							if (i > 1) {
								if (i > 2) {
									columnNames.append(", ");
									bindVariables.append(", ");
								}
								columnNames.append(meta.getColumnName(i));
								bindVariables.append('?');
							}
						}
						PreparedStatement insertStatement = connectMain
								.prepareStatement("insert into temp_" + table
										+ " (" + columnNames + ") values ("
										+ bindVariables + ")");
						System.out.println(new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss").format(new Date())
								+ " "
								+ insertStatement.toString());

						while (result.next()) {
							for (int i = 1; i < meta.getColumnCount(); i++)
								insertStatement.setObject(i,
										result.getObject(i + 1));
							insertStatement.executeUpdate();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			connectLocal.close();
			connectMain.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
