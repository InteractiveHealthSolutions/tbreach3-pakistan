/* Copyright(C) 2016 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
 */

/**
 * 
 */
package com.ihsinformatics.tbr3ilmssync;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import com.ihsinformatics.tbr3ilmssync.util.CommandType;
import com.ihsinformatics.tbr3ilmssync.util.DatabaseUtil;
import com.ihsinformatics.tbr3ilmssync.util.DateTimeUtil;
import com.ihsinformatics.tbr3ilmssync.util.FileUtil;

/**
 * This application synchronizes ILMS database with Central repository
 * 
 * @author owais.hussain@ihsinformatics.com
 *
 */
public class Tbr3SyncMain {

	public static final String version = "0.0.1";

	private static final Logger log = Logger.getLogger(Class.class.getName());
	public static final String dataPath = System.getProperty("user.home")
			+ "/tbr3ilmssync/";
	public static DatabaseUtil ilmsDb;
	public static DatabaseUtil dwDb;
	public static Properties props;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// check arguments first
		if (args.length == 0) {
			System.out
					.println("Arguments are invalid. Arguments must be provided as:\n"
							+ "-p path to properties file\n"
							+ "-u to update data for given number of days\n");
			return;
		}
		Tbr3SyncMain tbr3 = new Tbr3SyncMain();
		int days = 0;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-p")) {
				tbr3.initializeConnections(args[i + 1]);
			}
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-u")) {
				try {
					days = Integer.parseInt(args[i + 1]);
					Date dateFrom = new Date();
					Date dateTo = new Date();
					Calendar instance = Calendar.getInstance();
					instance.add(Calendar.DATE, -days);
					dateFrom = instance.getTime();
					tbr3.createTemporaryTables();
					tbr3.updateData(dateFrom, dateTo);
					tbr3.synchronize();
				} catch (Exception e) {
					System.out
							.println("Please enter the number of days in the argument \n"
									+ "i.e. -u 365");
				}
			}
		}
		System.exit(0);
	}

	/**
	 * Initiate source and target connections by reading parameters from
	 * properties file
	 * 
	 * @param propertiesFilePath
	 * @return
	 */
	private boolean initializeConnections(String propertiesFilePath) {
		try {
			// Read properties
			props = new Properties();
			props.load(new FileInputStream(propertiesFilePath));
			String dwUrl = props.getProperty("dw.connection.url");
			String dwDbName = props.getProperty("dw.connection.db_name");
			String dwDriverName = props
					.getProperty("dw.connection.driver_class");
			String dwUserName = props.getProperty("dw.connection.username");
			String dwPassword = props.getProperty("dw.connection.password");
			String ilmsUrl = props.getProperty("ilms.connection.url");
			String ilmsDbName = props.getProperty("ilms.connection.db_name");
			String ilmsDriverName = props
					.getProperty("ilms.connection.driver_class");
			String ilmsUserName = props.getProperty("ilms.connection.username");
			String ilmsPassword = props.getProperty("ilms.connection.password");

			dwDb = new DatabaseUtil(dwUrl, dwDbName, dwDriverName, dwUserName,
					dwPassword);
			ilmsDb = new DatabaseUtil(ilmsUrl, ilmsDbName, ilmsDriverName,
					ilmsUserName, ilmsPassword);
		} catch (IOException e) {
			log.severe(e.getMessage());
			return false;
		}
		return true;
	}

	private void createTemporaryTables() {
		// Read temporary table schema file
		FileUtil fileUtil = new FileUtil();
		fileUtil.getLines("res/temp_tables.sql");
		System.out.println("Create temporary tables...");
		// TODO: This is where I left last
	}

	/**
	 * Update data by importing from source to target tables
	 * 
	 * @param dateFrom
	 * @param dateTo
	 */
	private void updateData(Date dateFrom, Date dateTo) {
		ArrayList<String> selectQueries = new ArrayList<String>();
		ArrayList<String> insertQueries = new ArrayList<String>();
		String createClause = "(CREATE_DTP is null or CREATE_DTP between '"
				+ DateTimeUtil.getSqlDate(dateFrom) + "' and '"
				+ DateTimeUtil.getSqlDate(dateTo) + "')";
		String updateClause = "(UPDATE_DTP is null or UPDATE_DTP between '"
				+ DateTimeUtil.getSqlDate(dateFrom) + "' and '"
				+ DateTimeUtil.getSqlDate(dateTo) + "')";

		selectQueries.add("select * from dbo.ACC_REG");
		insertQueries.add("insert into lms_tmp_account values (?, ?)");
		selectQueries.add("select * from dbo.BRN_REG");
		insertQueries
				.add("insert into lms_tmp_branch values (?, ?, ?, ?, ?, ?)");
		selectQueries.add("select * from dbo.BS_DEUS_REC where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_patient_dues values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		selectQueries.add("select * from dbo.BS_MASTER where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_patient values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		selectQueries.add("select * from dbo.BS_TRANS where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_transaction values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		selectQueries.add("select * from dbo.DEP_REG where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_department values (?, ?, ?, ?, ?, ?, ?, ?)");
		selectQueries.add("select * from dbo.DESIG_REG");
		insertQueries.add("insert into lms_tmp_designation values (?, ?)");
		selectQueries.add("select * from dbo.DOC_BS where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_doctor_beneficiary values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		selectQueries.add("select * from dbo.DOCT_REG where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_doctor values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		selectQueries.add("select * from dbo.EMP_REG where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_employee values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		selectQueries.add("select * from dbo.EXP_HEAD");
		insertQueries.add("insert into lms_tmp_expense_type values (?, ?)");
		selectQueries.add("select * from dbo.EXP_VCHR");
		insertQueries
				.add("insert into lms_tmp_expense_voucher values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		selectQueries.add("select * from dbo.LAB_REG");
		insertQueries.add("insert into lms_tmp_lab values (?, ?, ?, ?, ?)");
		selectQueries.add("select * from dbo.LOC_BF_AMT where " + createClause);
		insertQueries
				.add("insert into lms_tmp_location_beneficiary_amount values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		selectQueries.add("select * from dbo.OTHER_LAB_REG");
		insertQueries
				.add("insert into lms_tmp_other_lab values (?, ?, ?, ?, ?, ?, ?)");
		selectQueries.add("select * from dbo.SYS_REG");
		insertQueries.add("insert into lms_tmp_system values (?, ?, ?)");
		selectQueries.add("select * from dbo.TEST_REG");
		insertQueries
				.add("insert into lms_tmp_test values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		for (int i = 0; i < selectQueries.size(); i++) {
			try {
				PreparedStatement source = ilmsDb.getConnection()
						.prepareStatement(selectQueries.get(i));
				PreparedStatement target = dwDb.getConnection()
						.prepareStatement(insertQueries.get(i));
				ResultSet data = source.executeQuery();
				ResultSetMetaData metaData = data.getMetaData();
				while (data.next()) {
					for (int j = 1; j <= metaData.getColumnCount(); j++) {
						target.setString(j, data.getString(j));
					}
					target.executeUpdate();
				}
				System.out.println(selectQueries.get(i) + "; "
						+ insertQueries.get(i));
			} catch (SQLException | InstantiationException
					| IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Performs synchronization. Data in ILMS tables is processed and saved into
	 * dimension and fact tables
	 */
	private void synchronize() {
		try {
			String query = "insert into lms_account select * from lms_tmp_account where account_id not in (select account_id from lms_account)";
			dwDb.runCommand(CommandType.INSERT, query);

			query = "insert into lms_branch select * from lms_tmp_branch where location not in (select location from lms_branch)";
			dwDb.runCommand(CommandType.INSERT, query);

		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
