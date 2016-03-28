/**
 * Copyright(C) 2016 Interactive Health Solutions, Pvt. Ltd.
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. 
 */

package com.ihsinformatics.tbr3synchronizer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ihsinformatics.tbr3synchronizer.util.ConnectionProvider;
import com.ihsinformatics.tbr3synchronizer.util.Synchronize;
import com.ihsinformatics.tbr3synchronizer.util.OpenMrsMeta;
import com.ihsinformatics.tbr3synchronizer.util.QueryStringSync;

/**
 * 
 * Main class for TBR3 synchronization
 * 
 * @author Hera Rafique
 */

public class Tbr3SynchronizerMain {

	private Synchronize synchronize;
	private ConnectionProvider connectionProvider;
	private Connection connectMain;

	public Tbr3SynchronizerMain() {
		synchronize = new Synchronize();
		synchronize.insertDataIntoTempTable();
		connectionProvider = new ConnectionProvider();
		connectMain = connectionProvider.getOpenMrsMainConnection();
	}

	/**
	 * Synchronizes data from temporary tables to original tables. This method
	 * uses unsafe SQL updates.
	 */
	public void synchronize() {
		try {
			Statement statement = connectMain.createStatement();
			statement.execute("SET SQL_SAFE_UPDATES = 0");
			for (String query : QueryStringSync.SYNC_QUERIES) {
				Statement stmt = connectMain.createStatement();
				System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.format(new Date()) + " " + query);
				stmt.executeUpdate(query);
			}
			statement = connectMain.createStatement();
			statement.execute("SET SQL_SAFE_UPDATES = 1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes temporary tables created during synchronization process. This
	 * method must be called after synchronization.
	 */
	public void dropTempTables() {
		for (String[] tables : OpenMrsMeta.TABLE_NAME_LIST) {
			for (String table : tables) {
				try {
					Statement statement = connectMain.createStatement();
					statement.executeUpdate("DROP TABLE IF EXISTS " + "temp_"
							+ table);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		Tbr3SynchronizerMain sync = new Tbr3SynchronizerMain();
		sync.synchronize();
		sync.dropTempTables();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
				.format(new Date()) + " Synchronization process completed");
	}
}
