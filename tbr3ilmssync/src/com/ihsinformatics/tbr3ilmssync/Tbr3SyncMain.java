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
		insertQueries
				.add("insert into lms_tmp_account (account_id, account_name) values (?, ?)");

		selectQueries.add("select * from dbo.BRN_REG");
		insertQueries
				.add("insert into lms_tmp_branch (location, address, phone, code, is_registered, branch_type) values (?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.DEP_REG where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_department (department_id, department_name, account_id, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.DESIG_REG");
		insertQueries
				.add("insert into lms_tmp_designation (record_id, designation) values (?, ?)");

		selectQueries.add("select * from dbo.DOCT_REG where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_doctor ( record_no, doctor_id, doctor_name, clinic, address, contact, email, book_number, is_active, branch_code, medical_representative, routine_test, special_test, ultrasound, xray, ecg, no_discount_test, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.DOC_BS where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_doctor_beneficiary (record_id, doctor_record_no, routine_test, special_test, ultrasound, xray, ecg, no_discount_test, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.EMP_REG where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_employee (employee_id, employee_name, designation, age, age_unit, gender, address, contact, education, is_on_job, date_joined, date_left, branch_code, EMP_PWD, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.EXP_HEAD");
		insertQueries
				.add("insert into lms_tmp_expense_type (expense_type_id, expense_type) values (?, ?)");

		selectQueries.add("select * from dbo.EXP_VCHR");
		insertQueries
				.add("insert into lms_tmp_expense_voucher (voucher_id, voucher_date, expense_type, expense_reference, payment_mode, cheque_date, cheque_number, bank, narration, amount, amount_in_words, username, working_shift, exact_time, computer_id, is_cancelled, reason_cancelled, branch_code, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.LAB_REG");
		insertQueries
				.add("insert into lms_tmp_lab (lab_name, services, ntn, lab_code, is_registered) values (?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.LOC_BF_AMT where " + createClause);
		insertQueries
				.add("insert into lms_tmp_location_beneficiary_amount (record_id, location_id, location_name, patient_lab_no, beneficiary_date, routine_test, special_test, ultrasound, xray, ecg, no_discount_test, date_created, created_by) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.OTHER_LAB_REG");
		insertQueries
				.add("insert into lms_tmp_other_lab (lab_id, lab_name, contact, address, lab_code, date_created, is_active) values (?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.BS_MASTER where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_patient (patient_lab_no, branch_code, record_date, patient_name, age, age_unit, gender, contact_no, doctor_location, doctor_id, doctor_name, sample_collector_id, sample_collector_name, total_amount, discount, net_amount, cash_received, dues_remaining, is_referred, referred_by, patient_history, is_cancelled, reason_cancelled, username, working_shift, computer_id, exact_time, routine_test_discount, routine_test_discount_amount, routine_test_rate, special_test_discount, special_test_discount_amount, special_test_rate, ultrasound_disc, ultrasound_discount_amount, ultrasound_rate, xray_discount, xray_discount_amount, xray_rate, ecg_discount, ecg_discount_amount, ecg_rate, no_discount_test_discount, no_discount_test_discount_amount, no_discount_test_rate, reference_no, reference_relationship, reference_name, BS_PIDN, zakat_applicable, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.BS_DEUS_REC where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_patient_dues (voucher_id, date_recorded, amount_received, patient_lab_number, username, working_shift, exact_time, computer_id, branch, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.SYS_REG");
		insertQueries
				.add("insert into lms_tmp_system (system_name, system_code, branch_code) values (?, ?, ?)");

		selectQueries.add("select * from dbo.TEST_REG");
		insertQueries
				.add("insert into lms_tmp_test (test_id, test_name, test_fee, department_id, department_name, test_type, test_day, sputum_collection, sputum_quality, WLF_TEST, INC_TEST, INC_AMT, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from dbo.BS_TRANS where " + createClause
				+ " or " + updateClause);
		insertQueries
				.add("insert into lms_tmp_transaction (transaction_id, patient_lab_no, branch_code, test_id, test_name, test_fee, test_department, test_priority, reporting_date, sample_received, sample_date, sample_time, sample_receiver_computer, discount, discount_amount, discount_rate, date_created, created_by, date_updated, updated_by, uuid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		PreparedStatement source, target;
		for (int i = 0; i < selectQueries.size(); i++) {
			try {
				System.out.println(selectQueries.get(i) + "; "
						+ insertQueries.get(i));
				source = ilmsDb.getConnection().prepareStatement(
						selectQueries.get(i));
				target = dwDb.getConnection().prepareStatement(
						insertQueries.get(i));
				ResultSet data = source.executeQuery();
				ResultSetMetaData metaData = data.getMetaData();
				while (data.next()) {
					for (int j = 1; j <= metaData.getColumnCount(); j++) {
						target.setString(j, data.getString(j));
					}
					target.executeUpdate();
				}
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
			ArrayList<String> updateQueries = new ArrayList<String>();
			updateQueries
					.add("update lms_account as a, lms_tmp_account as t set a.account_id = t.account_id, a.account_name = t.account_name where a.uuid = t.uuid");
			updateQueries
					.add("update lms_branch as a, lms_tmp_branch as t set a.branch_name = t.location, a.address = t.address, a.phone = t.phone, a.code = t.code, a.is_registered = t.is_registered, a.branch_type = t.branch_type where a.uuid = t.uuid");
			updateQueries
					.add("update lms_department as a, lms_tmp_department as t set a.department_id = t.department_id, a.department_name = t.department_name, a.account_id = t.account_id, a.date_created = t.date_created, a.created_by = t.created_by, a.date_updated = t.date_updated, a.updated_by = t.updated_by where a.uuid = t.uuid");
			updateQueries
					.add("update lms_designation as a, lms_tmp_designation as t set a.record_id = t.record_id, a.designation = t.designation where a.uuid = t.uuid");
			updateQueries
					.add("update lms_doctor as a, lms_tmp_doctor as t set a.record_no = t.record_no, a.doctor_id = t.doctor_id, a.doctor_name = t.doctor_name, a.clinic = t.clinic, a.address = t.address, a.contact = t.contact, a.email = t.email, a.book_number = t.book_number, a.is_active = t.is_active, a.branch_code = t.branch_code, a.medical_representative = t.medical_representative, a.routine_test = t.routine_test, a.special_test = t.special_test, a.ultrasound = t.ultrasound, a.xray = t.xray, a.ecg = t.ecg, a.no_discount_test = t.no_discount_test, a.date_created = t.date_created, a.created_by = t.created_by, a.date_updated = t.date_updated, a.updated_by = t.updated_by where a.uuid = t.uuid");
			updateQueries
					.add("update lms_doctor_beneficiary as a, lms_tmp_doctor_beneficiary as t set a.record_id = t.record_id, a.doctor_record_no = t.doctor_record_no, a.routine_test = t.routine_test, a.special_test = t.special_test, a.ultrasound = t.ultrasound, a.xray = t.xray, a.ecg = t.ecg, a.no_discount_test = t.no_discount_test, a.date_created = t.date_created, a.created_by = t.created_by, a.date_updated = t.date_updated, a.updated_by = t.updated_by where a.uuid = t.uuid");
			updateQueries
					.add("update lms_employee as a, lms_tmp_employee as t set a.employee_id = t.employee_id, a.employee_name = t.employee_name, a.designation = t.designation, a.age = t.age, a.age_unit = t.age_unit, a.gender = t.gender, a.address = t.address, a.contact = t.contact, a.education = t.education, a.is_on_job = t.is_on_job, a.date_joined = t.date_joined, a.date_left = t.date_left, a.branch_code = t.branch_code, a.EMP_PWD = t.EMP_PWD, a.date_created = t.date_created, a.created_by = t.created_by, a.date_updated = t.date_updated, a.updated_by = t.updated_by where a.uuid = t.uuid");
			updateQueries
					.add("update lms_expense_type as a, lms_tmp_expense_type as t set a.expense_type_id = t.expense_type_id, a.expense_type = t.expense_type where a.uuid = t.uuid");
			updateQueries
					.add("update lms_expense_voucher as a, lms_tmp_expense_voucher as t set a.voucher_id = t.voucher_id, a.voucher_date = t.voucher_date, a.expense_type = t.expense_type, a.expense_reference = t.expense_reference, a.payment_mode = t.payment_mode, a.cheque_date = t.cheque_date, a.cheque_number = t.cheque_number, a.bank = t.bank, a.narration = t.narration, a.amount = t.amount, a.amount_in_words = t.amount_in_words, a.username = t.username, a.working_shift = t.working_shift, a.exact_time = t.exact_time, a.computer_id = t.computer_id, a.is_cancelled = t.is_cancelled, a.reason_cancelled = t.reason_cancelled, a.branch_code = t.branch_code where a.uuid = t.uuid");
			updateQueries
					.add("update lms_lab as a, lms_tmp_lab as t set a.lab_name = t.lab_name, a.services = t.services, a.ntn = t.ntn, a.lab_code = t.lab_code, a.is_registered = t.is_registered where a.uuid = t.uuid");
			updateQueries
					.add("update lms_location_beneficiary_amount as a, lms_tmp_location_beneficiary_amount as t set a.record_id = t.record_id, a.location_id = t.location_id, a.location_name = t.location_name, a.patient_lab_no = t.patient_lab_no, a.beneficiary_date = t.beneficiary_date, a.routine_test = t.routine_test, a.special_test = t.special_test, a.ultrasound = t.ultrasound, a.xray = t.xray, a.ecg = t.ecg, a.no_discount_test = t.no_discount_test, a.date_created = t.date_created, a.created_by = t.created_by where a.uuid = t.uuid");
			updateQueries
					.add("update lms_other_lab as a, lms_tmp_other_lab as t set a.lab_id = t.lab_id, a.lab_name = t.lab_name, a.contact = t.contact, a.address = t.address, a.lab_code = t.lab_code, a.date_created = t.date_created, a.is_active = t.is_active where a.uuid = t.uuid");
			updateQueries
					.add("update lms_patient as a, lms_tmp_patient as t set a.patient_lab_no = t.patient_lab_no, a.branch_code = t.branch_code, a.record_date = t.record_date, a.patient_name = t.patient_name, a.age = t.age, a.age_unit = t.age_unit, a.gender = t.gender, a.contact_no = t.contact_no, a.doctor_location = t.doctor_location, a.doctor_id = t.doctor_id, a.doctor_name = t.doctor_name, a.sample_collector_id = t.sample_collector_id, a.sample_collector_name = t.sample_collector_name, a.total_amount = t.total_amount, a.discount = t.discount, a.net_amount = t.net_amount, a.cash_received = t.cash_received, a.dues_remaining = t.dues_remaining, a.is_referred = t.is_referred, a.referred_by = t.referred_by, a.patient_history = t.patient_history, a.is_cancelled = t.is_cancelled, a.reason_cancelled = t.reason_cancelled, a.username = t.username, a.working_shift = t.working_shift, a.computer_id = t.computer_id, a.exact_time = t.exact_time, a.RT_discount = t.RT_discount, a.RT_discount_amount = t.RT_discount_amount, a.RT_rate = t.RT_rate, a.spirometry_discount = t.spirometry_discount, a.spirometry_discount_amount = t.spirometry_discount_amount, a.spirometry_rate = t.spirometry_rate, a.ultrasound_disc = t.ultrasound_disc, a.ultrasound_discount_amount = t.ultrasound_discount_amount, a.ultrasound_rate = t.ultrasound_rate, a.xray_discount = t.xray_discount, a.xray_discount_amount = t.xray_discount_amount, a.xray_rate = t.xray_rate, a.ecg_discount = t.ecg_discount, a.ecg_discount_amount = t.ecg_discount_amount, a.ecg_rate = t.ecg_rate, a.ND_discount = t.ND_discount, a.ND_discount_amount = t.ND_discount_amount, a.ND_rate = t.ND_rate, a.reference_no = t.reference_no, a.reference_relationship = t.reference_relationship, a.reference_name = t.reference_name, a.BS_PIDN = t.BS_PIDN, a.zakat_applicable = t.zakat_applicable, a.date_created = t.date_created, a.created_by = t.created_by, a.date_updated = t.date_updated, a.updated_by = t.updated_by where a.uuid = t.uuid");
			updateQueries
					.add("update lms_patient_dues as a, lms_tmp_patient_dues as t set a.voucher_id = t.voucher_id, a.date_recorded = t.date_recorded, a.amount_received = t.amount_received, a.patient_lab_number = t.patient_lab_number, a.username = t.username, a.working_shift = t.working_shift, a.exact_time = t.exact_time, a.computer_id = t.computer_id, a.branch = t.branch, a.date_created = t.date_created, a.created_by = t.created_by, a.date_updated = t.date_updated, a.updated_by = t.updated_by where a.uuid = t.uuid");
			updateQueries
					.add("update lms_system as a, lms_tmp_system as t set a.system_name = t.system_name, a.system_code = t.system_code, a.branch_code = t.branch_code where a.uuid = t.uuid");
			updateQueries
					.add("update lms_test as a, lms_tmp_test as t set a.test_id = t.test_id, a.test_name = t.test_name, a.test_fee = t.test_fee, a.department_id = t.department_id, a.department_name = t.department_name, a.test_type = t.test_type, a.test_day = t.test_day, a.sputum_collection = t.sputum_collection, a.sputum_quality = t.sputum_quality, a.WLF_TEST = t.WLF_TEST, a.INC_TEST = t.INC_TEST, a.INC_AMT = t.INC_AMT, a.date_created = t.date_created, a.created_by = t.created_by, a.date_updated = t.date_updated, a.updated_by = t.updated_by where a.uuid = t.uuid");
			updateQueries
					.add("update lms_transaction as a, lms_tmp_transaction as t set a.transaction_id = t.transaction_id, a.patient_lab_no = t.patient_lab_no, a.branch_code = t.branch_code, a.test_id = t.test_id, a.test_name = t.test_name, a.test_fee = t.test_fee, a.test_department = t.test_department, a.test_priority = t.test_priority, a.reporting_date = t.reporting_date, a.sample_received = t.sample_received, a.sample_date = t.sample_date, a.sample_time = t.sample_time, a.sample_receiver_computer = t.sample_receiver_computer, a.discount = t.discount, a.discount_amount = t.discount_amount, a.discount_rate = t.discount_rate, a.date_created = t.date_created, a.created_by = t.created_by, a.date_updated = t.date_updated, a.updated_by = t.updated_by where a.uuid = t.uuid");
			for (String query : updateQueries) {
				System.out.println(query);
				dwDb.runCommand(CommandType.UPDATE, query);
			}
			// Secondly, insert records not present
			System.out.println("Inserting new records...");
			ArrayList<String> insertQueries = new ArrayList<String>();
			insertQueries
					.add("insert into lms_account select 0, '"
							+ location
							+ "', a.* from lms_tmp_account as a where a.uuid not in (select uuid from lms_account)");
			insertQueries
					.add("insert into lms_branch select 0, '"
							+ location
							+ "', b.* from lms_tmp_branch as b where b.uuid not in (select uuid from lms_branch)");
			insertQueries
					.add("insert into lms_department select 0, '"
							+ location
							+ "', d.* from lms_tmp_department as d where d.uuid not in (select uuid from lms_department)");
			insertQueries
					.add("insert into lms_designation select 0, '"
							+ location
							+ "', d.* from lms_tmp_designation as d where d.uuid not in (select uuid from lms_designation)");
			insertQueries
					.add("insert into lms_doctor select 0, '"
							+ location
							+ "', d.* from lms_tmp_doctor as d where d.uuid not in (select uuid from lms_doctor)");
			insertQueries
					.add("insert into lms_doctor_beneficiary select 0, '"
							+ location
							+ "', d.* from lms_tmp_doctor_beneficiary as d where d.uuid not in (select uuid from lms_doctor_beneficiary)");
			insertQueries
					.add("insert into lms_employee select 0, '"
							+ location
							+ "', e.* from lms_tmp_employee as e where e.uuid not in (select uuid from lms_employee)");
			insertQueries
					.add("insert into lms_expense_type select 0, '"
							+ location
							+ "', e.* from lms_tmp_expense_type as e where e.uuid not in (select uuid from lms_expense_type)");
			insertQueries
					.add("insert into lms_expense_voucher select 0, '"
							+ location
							+ "', e.* from lms_tmp_expense_voucher as e where e.uuid not in (select uuid from lms_expense_voucher)");
			insertQueries
					.add("insert into lms_lab select 0, '"
							+ location
							+ "', l.* from lms_tmp_lab as l where l.uuid not in (select uuid from lms_lab)");
			insertQueries
					.add("insert into lms_location_beneficiary_amount select 0, '"
							+ location
							+ "', l.* from lms_tmp_location_beneficiary_amount as l where l.uuid not in (select uuid from lms_location_beneficiary_amount)");
			insertQueries
					.add("insert into lms_other_lab select 0, '"
							+ location
							+ "', o.* from lms_tmp_other_lab as o where o.uuid not in (select uuid from lms_other_lab)");
			insertQueries
					.add("insert into lms_patient select 0, '"
							+ location
							+ "', p.* from lms_tmp_patient as p where p.uuid not in (select uuid from lms_patient)");
			insertQueries
					.add("insert into lms_patient_dues select 0, '"
							+ location
							+ "', p.* from lms_tmp_patient_dues as p where p.uuid not in (select uuid from lms_patient_dues)");
			insertQueries
					.add("insert into lms_system select 0, '"
							+ location
							+ "', s.* from lms_tmp_system as s where s.uuid not in (select uuid from lms_system)");
			insertQueries
					.add("insert into lms_test select 0, '"
							+ location
							+ "', t.* from lms_tmp_test as t where t.uuid not in (select uuid from lms_test)");
			insertQueries
					.add("insert into lms_transaction select 0, '"
							+ location
							+ "', t.* from lms_tmp_transaction as t where t.uuid not in (select uuid from lms_transaction)");
			for (String query : insertQueries) {
				System.out.println(query);
				dwDb.runCommand(CommandType.INSERT, query);
			}
			System.out.println("New records inserted.");
			System.out.println("Synchronization complete.");
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Synchronization failed.");
		}
	}
}
