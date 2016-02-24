/* Copyright(C) 2016 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either VERSION 3 of the License (GPLv3), or any later VERSION.
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

	public static final String VERSION = "0.0.1";
	public static String location;

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
			location = props.getProperty("ilms.location.name", "CHS");

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
		System.out.println("Create temporary tables...");
		// Read temporary table schema file
		FileUtil fileUtil = new FileUtil();
		String[] queries = fileUtil.getLines("res/temp_tables.sql");
		try {
			for (String query : queries) {
				if (query.toUpperCase().startsWith("DROP")) {
					dwDb.runCommand(CommandType.DROP, query);
				} else if (query.toUpperCase().startsWith("CREATE")) {
					dwDb.runCommand(CommandType.CREATE, query);
				}
			}
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Temporary tables created.");
	}

	/**
	 * Update data by importing from source to target tables
	 * 
	 * @param dateFrom
	 * @param dateTo
	 */
	private void updateData(Date dateFrom, Date dateTo) {
		System.out.println("Copying data to temporary tables...");
		ArrayList<String> selectQueries = new ArrayList<String>();
		ArrayList<String> insertQueries = new ArrayList<String>();
		String createClause = "(CREATE_DTP is null or CREATE_DTP between '"
				+ DateTimeUtil.getSqlDate(dateFrom) + "' and '"
				+ DateTimeUtil.getSqlDate(dateTo) + "')";
		String updateClause = "(UPDATE_DTP is null or UPDATE_DTP between '"
				+ DateTimeUtil.getSqlDate(dateFrom) + "' and '"
				+ DateTimeUtil.getSqlDate(dateTo) + "')";

		selectQueries.add("select * from dbo.ACC_REG");
		insertQueries.add("insert into lms_tmp_account (account_id, account_name) values (?, ?)");

		selectQueries.add("select * from dbo.BRN_REG");
		insertQueries.add("insert into lms_tmp_branch (location, address, phone, code, is_registered, branch_type) values (?, ?, ?, ?, ?, ?)");
		
		selectQueries.add("select * from dbo.DEP_REG where " + createClause + " or " + updateClause);
		insertQueries.add("insert into lms_tmp_department (department_id, department_name, account_id, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.DESIG_REG");
		insertQueries.add("insert into lms_tmp_designation (record_id, designation) values (?, ?)");
		
		selectQueries.add("select * from dbo.DOCT_REG where " + createClause + " or " + updateClause);
		insertQueries.add("insert into lms_tmp_doctor ( record_no, doctor_id, doctor_name, clinic, address, contact, email, book_number, is_active, branch_code, medical_representative, routine_test, special_test, ultrasound, xray, ecg, no_discount_test, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		selectQueries.add("select * from dbo.DOC_BS where " + createClause + " or " + updateClause);
		insertQueries.add("insert into lms_tmp_doctor_beneficiary (record_id, doctor_record_no, routine_test, special_test, ultrasound, xray, ecg, no_discount_test, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		selectQueries.add("select * from dbo.EMP_REG where " + createClause + " or " + updateClause);
		insertQueries.add("insert into lms_tmp_employee (employee_id, employee_name, designation, age, age_unit, gender, address, contact, education, is_on_job, date_joined, date_left, branch_code, EMP_PWD, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		selectQueries.add("select * from dbo.EXP_HEAD");
		insertQueries.add("insert into lms_tmp_expense_type (expense_type_id, expense_type) values (?, ?)");
		
		selectQueries.add("select * from dbo.EXP_VCHR");
		insertQueries.add("insert into lms_tmp_expense_voucher (voucher_id, voucher_date, expense_type, expense_reference, payment_mode, cheque_date, cheque_number, bank, narration, amount, amount_in_words, username, working_shift, exact_time, computer_id, is_cancelled, reason_cancelled, branch_code) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		selectQueries.add("select * from dbo.LAB_REG");
		insertQueries.add("insert into lms_tmp_lab (lab_name, services, ntn, lab_code, is_registered) values (?, ?, ?, ?, ?)");
		
		selectQueries.add("select * from dbo.LOC_BF_AMT where " + createClause);
		insertQueries.add("insert into lms_tmp_location_beneficiary_amount (record_id, location_id, location_name, patient_lab_no, beneficiary_date, routine_test, special_test, ultrasound, xray, ecg, no_discount_test, date_created, created_by) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		selectQueries.add("select * from dbo.OTHER_LAB_REG");
		insertQueries.add("insert into lms_tmp_other_lab (lab_id, lab_name, contact, address, lab_code, date_created, is_active) values (?, ?, ?, ?, ?, ?, ?)");
		
		selectQueries.add("select * from dbo.BS_MASTER where " + createClause + " or " + updateClause);
		insertQueries.add("insert into lms_tmp_patient (patient_lab_no, branch_code, record_date, patient_name, age, age_unit, gender, contact_no, doctor_location, doctor_id, doctor_name, sample_collector_id, sample_collector_name, total_amount, discount, net_amount, cash_received, dues_remaining, is_referred, referred_by, patient_history, is_cancelled, reason_cancelled, username, working_shift, computer_id, exact_time, routine_test_discount, routine_test_discount_amount, routine_test_rate, special_test_discount, special_test_discount_amount, special_test_rate, ultrasound_disc, ultrasound_discount_amount, ultrasound_rate, xray_discount, xray_discount_amount, xray_rate, ecg_discount, ecg_discount_amount, ecg_rate, no_discount_test_discount, no_discount_test_discount_amount, no_discount_test_rate, reference_no, reference_relationship, reference_name, BS_PIDN, zakat_applicable, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		selectQueries.add("select * from dbo.BS_DEUS_REC where " + createClause + " or " + updateClause);
		insertQueries.add("insert into lms_tmp_patient_dues (voucher_id, date_recorded, amount_received, patient_lab_number, username, working_shift, exact_time, computer_id, branch, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		selectQueries.add("select * from dbo.SYS_REG");
		insertQueries.add("insert into lms_tmp_system (system_name, system_code, branch_code) values (?, ?, ?)");
		
		selectQueries.add("select * from dbo.TEST_REG");
		insertQueries.add("insert into lms_tmp_test (test_id, test_name, test_fee, department_id, department_name, test_type, test_day, sputum_collection, sputum_quality, WLF_TEST, INC_TEST, INC_AMT, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		selectQueries.add("select * from dbo.BS_TRANS where " + createClause + " or " + updateClause);
		insertQueries.add("insert into lms_tmp_transaction (transaction_id, patient_lab_no, branch_code, test_id, test_name, test_fee, test_department, test_priority, reporting_date, sample_received, sample_date, sample_time, sample_receiver_computer, discount, discount_amount, discount_rate, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		PreparedStatement source, target;
		for (int i = 0; i < selectQueries.size(); i++) {
			try {
				source = ilmsDb.getConnection()
						.prepareStatement(selectQueries.get(i));
				target = dwDb.getConnection()
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
		System.out.println("Data copied to temporary tables.");
	}

	/**
	 * Performs synchronization. Data in ILMS tables is processed and saved into
	 * dimension and fact tables
	 */
	private void synchronize() {
		System.out.println("Synchronizing...");
		try {
			// First update existing records
			System.out.println("Updating existing records...");
			// TODO: Update statements

			// Secondly, insert records not present
			System.out.println("Inserting new records...");
			
			String query = "insert into lms_account select 0, '" + location + "', a.* from lms_tmp_account as a where a.uuid not in (select uuid from lms_account)";
			dwDb.runCommand(CommandType.INSERT, query);

			query = "insert into lms_branch select 0, '" + location + "', b.* from lms_tmp_branch as b where b.uuid not in (select uuid from lms_branch)";
			dwDb.runCommand(CommandType.INSERT, query);
			
			query = "insert into lms_department select 0, '" + location + "', d.* from lms_tmp_department as d where d.uuid not in (select uuid from lms_department)";
			dwDb.runCommand(CommandType.INSERT, query);
			
			query = "insert into lms_designation select 0, '" + location + "', d.* from lms_tmp_designation as d where d.uuid not in (select uuid from lms_designation)";
			dwDb.runCommand(CommandType.INSERT, query);
			
			query = "insert into lms_doctor select 0, '" + location + "', d.* from lms_tmp_doctor as d where d.uuid not in (select uuid from lms_doctor)";
			dwDb.runCommand(CommandType.INSERT, query);
			
			
			
			System.out.println("New records inserted.");

		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Synchronization complete.");
	}
}
