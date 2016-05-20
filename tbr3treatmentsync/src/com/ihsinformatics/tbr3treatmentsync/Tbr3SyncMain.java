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
package com.ihsinformatics.tbr3treatmentsync;

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

import com.ihsinformatics.tbr3treatmentsync.util.CommandType;
import com.ihsinformatics.tbr3treatmentsync.util.DatabaseUtil;
import com.ihsinformatics.tbr3treatmentsync.util.DateTimeUtil;
import com.ihsinformatics.tbr3treatmentsync.util.FileUtil;

/**
 * This application synchronizes Tbr3 Treatment database with Central repository
 * 
 * @author owais.hussain@ihsinformatics.com
 * 
 */
public class Tbr3SyncMain {

	public static final String VERSION = "0.0.1";
	public static String location;

	private static final Logger log = Logger.getLogger(Class.class.getName());
	public static final String dataPath = System.getProperty("user.home")
			+ "/tbr3treatmentsync/";
	public static DatabaseUtil tbr3TreatmentDb;
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
			String tbr3TreatmentUrl = props.getProperty("local.connection.url");
			String tbr3TreatmentDbName = props
					.getProperty("local.connection.db_name");
			String tbr3TreatmentDriverName = props
					.getProperty("local.connection.driver_class");
			String tbr3TreatmentUserName = props
					.getProperty("local.connection.username");
			String tbr3TreatmentPassword = props
					.getProperty("local.connection.password");
			location = props.getProperty("local.location.name", "CHS");

			dwDb = new DatabaseUtil(dwUrl, dwDbName, dwDriverName, dwUserName,
					dwPassword);
			tbr3TreatmentDb = new DatabaseUtil(tbr3TreatmentUrl,
					tbr3TreatmentDbName, tbr3TreatmentDriverName,
					tbr3TreatmentUserName, tbr3TreatmentPassword);
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
		String createClause = "(date_created between '"
				+ DateTimeUtil.getSqlDate(dateFrom) + "' and '"
				+ DateTimeUtil.getSqlDate(dateTo) + "')";
		String updateClause = "(date_changed is null or date_changed between '"
				+ DateTimeUtil.getSqlDate(dateFrom) + "' and '"
				+ DateTimeUtil.getSqlDate(dateTo) + "')";

		selectQueries.add("select * from data_log where " + createClause);
		insertQueries
				.add("INSERT INTO temp_data_log (log_id, log_type, entity_name, record, description, date_created, created_by, created_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from definition where " + createClause
				+ " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_definition (definition_id, definition_type_id, definition, description, is_default, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from definition_type");
		insertQueries
				.add("INSERT INTO temp_definition_type (definition_type_id, definition_type) VALUES (?, ?)");

		selectQueries.add("select * from element where " + createClause
				+ " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_element (element_id, element_name, validation_regex, data_type, description, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from encounter where " + createClause
				+ " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_encounter (encounter_id, encounter_type_id, user_id, person_id, duration_seconds, date_entered, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from encounter_prerequisite where "
				+ createClause + " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_encounter_prerequisite (encounter_prerequisite_id, encounter_type_id, prerequisite_encounter_type_id, element_id, should_be_regex, should_not_be_regex, description, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from encounter_result where "
				+ createClause + " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_encounter_result (encounter_result_id, encounter_id, element_id, result, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from encounter_type where " + createClause
				+ " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_encounter_type (encounter_type_id, encounter_type, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from location where " + createClause
				+ " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_location (location_id, location_name, category, description, address1, address2, address3, city_village, state_province, country, landmark1, landmark2, latitude, longitude, primary_contact, secondary_contact, email, fax, parent_location, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from location_attribute where "
				+ createClause + " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_location_attribute (location_attribute_id, attribute_type_id, location_id, attribute_value, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from location_attribute_type where "
				+ createClause + " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_location_attribute_type (location_attribute_type_id, attribute_name, validation_regex, required, description, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from patient where " + createClause
				+ " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_patient (person_id, patient_id, external_id1, external_id2, blood_group, weight, weight_unit, height, height_unit, date_closed, closed_by, reason_closed, died, date_died, death_cause, comments, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from person where " + createClause + " OR "
				+ updateClause);
		insertQueries
				.add("INSERT INTO temp_person (person_id, title, first_name, middle_name, last_name, family_name, gender, dob, dob_estimated, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from person_attribute where "
				+ createClause + " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_person_attribute (person_attribute_id, attribute_type_id, person_id, attribute_value, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from person_attribute_type where "
				+ createClause + " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_person_attribute_type (person_attribute_type_id, attribute_name, data_type, validation_regex, required, description, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from person_contact where " + createClause
				+ " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_person_contact (address_id, person_id, address1, address2, address3, city_village, state_province, country, landmark1, landmark2, latitude, longitude, primary_contact, secondary_contact, email, fax, preferred, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		// skipping privilege table

		selectQueries.add("select * from role where " + createClause + " OR "
				+ updateClause);
		insertQueries
				.add("INSERT INTO temp_role (role_id, role_name, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from role_privilege where " + createClause);
		insertQueries
				.add("INSERT INTO temp_role_privilege (role_id, privilege, date_created, created_by, created_at, uuid) VALUES (?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from user_location where " + createClause
				+ " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_user_location (user_id, location_id, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from user_role where " + createClause
				+ " OR " + updateClause);
		insertQueries
				.add("INSERT INTO temp_user_role (user_id, role_id, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

		selectQueries.add("select * from users where " + createClause + " OR "
				+ updateClause);
		insertQueries
				.add("INSERT INTO temp_users (user_id, username, full_name, global_data_access, disabled, reason_disabled, password_hash, secret_question, secret_answer_hash, date_created, created_by, created_at, date_changed, changed_by, changed_at, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		PreparedStatement source, target;
		for (int i = 0; i < selectQueries.size(); i++) {
			try {
				source = tbr3TreatmentDb.getConnection().prepareStatement(
						selectQueries.get(i));
				target = dwDb.getConnection().prepareStatement(
						insertQueries.get(i));
				ResultSet data = source.executeQuery();
				ResultSetMetaData metaData = data.getMetaData();
				while (data.next()) {
					for (int j = 1; j <= metaData.getColumnCount(); j++) {
						String columnDatatype = metaData.getColumnTypeName(j);
						if (columnDatatype.equals("BIT")) {
							if (data.getBoolean(j) == true)
								target.setBoolean(j, true);
							else
								target.setBoolean(j, false);
						} else {
							target.setString(j, data.getString(j));
						}
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
	 * Performs synchronization. Data in tbr3Treatment tables is processed and
	 * saved into dimension and fact tables
	 */
	private void synchronize() {
		System.out.println("Synchronizing...");
		try {
			// First update existing records
			System.out.println("Updating existing records...");
			ArrayList<String> updateQueries = new ArrayList<String>();

			updateQueries
					.add("update data_log AS a, temp_data_log AS t SET a.log_id = t.log_id, a.log_type = t.log_type, a.entity_name = t.entity_name, a.record = t.record, a.description = t.description, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at where a.uuid = t.uuid");
			updateQueries
					.add("update definition AS a, temp_definition AS t SET a.definition_type_id = t.definition_type_id, a.definition = t.definition, a.description = t.description, a.is_default = t.is_default, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			// not updating definition_type table
			updateQueries
					.add("update element AS a, temp_element AS t SET a.element_name = t.element_name, a.validation_regex = t.validation_regex, a.data_type = t.data_type, a.description = t.description, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update encounter AS a, temp_encounter AS t SET a.encounter_type_id = t.encounter_type_id, a.user_id = t.user_id, a.person_id = t.person_id, a.duration_seconds = t.duration_seconds, a.date_entered = t.date_entered, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update encounter_prerequisite AS a, temp_encounter_prerequisite AS t SET a.encounter_type_id = t.encounter_type_id , a.prerequisite_encounter_type_id = t.prerequisite_encounter_type_id, a.element_id = t.element_id, a.should_be_regex = t.should_be_regex, a.should_not_be_regex = t.should_not_be_regex, a.description = t.description, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update encounter_result AS a, temp_encounter_result AS t SET a.encounter_id = t.encounter_id, a.element_id = t.element_id, a.result = t.result, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update encounter_type AS a, temp_encounter_type AS t SET a.encounter_type = t.encounter_type, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update location AS a, temp_location AS t SET a.location_name = t.location_name, a.category = t.category, a.description = t.category, a.address1 = t.address1 , a.address2 = t.address2, a.address3 = t.address3, a.city_village = t.city_village, a.state_province = t.state_province, a.country = t.country, a.landmark1 = t.landmark1, a.landmark2 = t.landmark2, a.latitude =t.latitude, a.longitude = t.longitude, a.primary_contact = t.primary_contact, a.secondary_contact = t.secondary_contact, a.email = t.email, a.fax = t.fax, a.parent_location = t.parent_location, a.date_created = t.date_created, a.created_by =t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at Where a.uuid = t.uuid");
			updateQueries
					.add("update location_attribute AS a, temp_location_attribute AS t SET a.attribute_type_id = t.attribute_type_id, a.location_id = t.location_id, a.attribute_value = t.attribute_value, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update location_attribute_type AS a, temp_location_attribute_type AS t SET a.attribute_name = t.attribute_name, a.validation_regex = t.validation_regex, a.required = t.required, a.description = t.description, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update patient AS a, temp_patient AS t SET a.person_id = t.person_id, a.patient_id = t.patient_id, a.external_id1 = t.external_id1, a.external_id2 = t.external_id2, a.blood_group = t.blood_group, a.weight = t.weight, a.weight_unit = t.weight_unit, a.height = t.height, a.height_unit = t.height_unit, a.date_closed = t.date_closed, a.closed_by = t.closed_by, a.reason_closed = t.reason_closed, a.died = t.died, a.date_died = t.date_died, a.death_cause = t.death_cause, a.comments = t.comments, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update person AS a, temp_person AS t SET a.title = t.title, a.first_name = t.first_name, a.middle_name = t.middle_name, a.last_name = t.last_name, a.family_name = t.family_name, a.gender = t.gender, a.dob = t.dob, a.dob_estimated = t.dob_estimated, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update person_attribute AS a, temp_person_attribute AS t SET a.attribute_type_id = t.attribute_type_id, a.person_id = t.person_id, a.attribute_value = t.attribute_value, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update person_attribute_type AS a, temp_person_attribute_type AS t SET a.attribute_name = t.attribute_name, a.data_type = t.data_type, a.validation_regex = t.validation_regex, a.required = t.required, a.description = t.description, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update person_contact AS a, temp_person_contact AS t SET a.person_id = t.person_id, a.address1 = t.address1, a.address2 = t.address2, a.address3 = t.address3, a.city_village = t.city_village,  a.state_province = t.state_province, a.country = t.country, a.landmark1 = t.landmark1, a.landmark2 = t.landmark2, a.latitude = t.latitude, a.longitude = t.longitude, a.primary_contact = t.primary_contact, a.secondary_contact = t.secondary_contact, a.email = t.email, a.fax = t.fax, a.preferred = t.preferred, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			// skipping privilege table
			updateQueries
					.add("update role AS a, temp_role AS t SET a.role_name = t.role_name, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update role_privilege AS a, temp_role_privilege AS t SET a.privilege = t.privilege, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at where a.uuid = t.uuid");
			updateQueries
					.add("update user_location AS a, temp_user_location AS t SET a.user_id = t.user_id, a.location_id = t.location_id, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update user_role AS a, temp_user_role AS t SET a.user_id = t.user_id, a.role_id = t.role_id, a.date_created = t.date_created, a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");
			updateQueries
					.add("update users AS a, temp_users AS t SET a.username = t.username, a.full_name = t.full_name, a.global_data_access = t.global_data_access, a.disabled = t.disabled, a.reason_disabled = t.reason_disabled, a.password_hash = t.password_hash, a.secret_question = t.secret_question, a.secret_answer_hash = t.secret_answer_hash, a.date_created = t.date_created , a.created_by = t.created_by, a.created_at = t.created_at, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.changed_at = t.changed_at where a.uuid = t.uuid");

			for (String query : updateQueries) {
				System.out.println(query);
				dwDb.runCommand(CommandType.UPDATE, query);
			}
			// Secondly, insert records not present
			System.out.println("Inserting new records...");
			ArrayList<String> insertQueries = new ArrayList<String>();

			insertQueries
					.add("insert into data_log select 0, '"
							+ location
							+ "', a.* from temp_data_log as a where uuid NOT IN (select uuid from data_log)");

			insertQueries
			.add("insert into definition_type select 0, '"
					+ location
					+ "', a.* from temp_definition_type as a where definition_type_id NOT IN (select definition_type_id from definition_type)");
			
			insertQueries
					.add("insert into definition select 0, '"
							+ location
							+ "', a.* from temp_definition as a where uuid NOT IN (select uuid from definition)");

			insertQueries
					.add("insert into element select 0, '"
							+ location
							+ "', a.* from temp_element as a where uuid NOT IN (select uuid from element)");

			insertQueries
					.add("insert into element select 0, '"
							+ location
							+ "', a.* from temp_element as a where uuid NOT IN (select uuid from element)");
			insertQueries
					.add("insert into encounter select 0, '"
							+ location
							+ "', a.* from temp_encounter as a where uuid NOT IN (select uuid from encounter)");
			insertQueries
					.add("insert into encounter_prerequisite select 0, '"
							+ location
							+ "', a.* from temp_encounter_prerequisite as a where uuid NOT IN (select uuid from encounter_prerequisite)");
			insertQueries
					.add("insert into encounter_result select 0, '"
							+ location
							+ "', a.* from temp_encounter_result as a where uuid NOT IN (select uuid from encounter_result)");
			insertQueries
					.add("insert into encounter_type select 0, '"
							+ location
							+ "', a.* from temp_encounter_type as a where uuid NOT IN (select uuid from encounter_type)");
			insertQueries
					.add("insert into location select 0, '"
							+ location
							+ "', a.* from temp_location as a where uuid NOT IN (select uuid from location)");
			insertQueries
					.add("insert into location_attribute select 0, '"
							+ location
							+ "', a.* from temp_location_attribute as a where uuid NOT IN (select uuid from location_attribute)");
			insertQueries
					.add("insert into location_attribute_type select 0, '"
							+ location
							+ "', a.* from temp_location_attribute_type as a where uuid NOT IN (select uuid from location_attribute_type)");
			insertQueries
					.add("insert into person select 0, '"
							+ location
							+ "', a.* from temp_person as a where uuid NOT IN (select uuid from person)");
			insertQueries
					.add("insert into person_attribute select 0, '"
							+ location
							+ "', a.* from temp_person_attribute as a where uuid NOT IN (select uuid from person_attribute)");
			insertQueries
					.add("insert into person_attribute_type select 0, '"
							+ location
							+ "', a.* from temp_person_attribute_type as a where uuid NOT IN (select uuid from person_attribute_type)");
			insertQueries
					.add("insert into person_contact select 0, '"
							+ location
							+ "', a.* from temp_person_contact as a where uuid NOT IN (select uuid from person_contact)");
			insertQueries
					.add("insert into person_contact select 0, '"
							+ location
							+ "', a.* from temp_person_contact as a where uuid NOT IN (select uuid from person_contact)");

			// skipping privilege table

			insertQueries
					.add("insert into role select 0, '"
							+ location
							+ "', a.* from temp_role as a where uuid NOT IN (select uuid from role)");
			insertQueries
					.add("insert into role_privilege select 0, '"
							+ location
							+ "', a.* from temp_role_privilege as a where uuid NOT IN (select uuid from role_privilege)");
			insertQueries
					.add("insert into user_location select 0, '"
							+ location
							+ "', a.* from temp_user_location as a where uuid NOT IN (select uuid from user_location)");
			insertQueries
					.add("insert into user_role select 0, '"
							+ location
							+ "', a.* from temp_user_role as a where uuid NOT IN (select uuid from user_role)");
			insertQueries
					.add("insert into users select 0, '"
							+ location
							+ "', a.* from temp_users as a where uuid NOT IN (select uuid from users)");

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
