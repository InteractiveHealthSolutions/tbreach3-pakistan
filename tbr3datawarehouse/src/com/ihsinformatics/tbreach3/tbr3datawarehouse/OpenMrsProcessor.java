/*
Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */
package com.ihsinformatics.tbreach3.tbr3datawarehouse;

import java.io.File;
import java.util.logging.Logger;

import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.CommandType;
import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.DatabaseUtil;
import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.FileUtil;

/**
 * @author owais.hussain@ihsinformatics.com
 *
 */
public class OpenMrsProcessor extends AbstractProcessor {

	private static final Logger log = Logger.getLogger(Class.class.getName());
	private static final String schemaName = "openmrs";
	private String scriptFilePath;
	private DatabaseUtil dwDb;
	private DatabaseUtil openMrsDb;
	String[] sourceTables = {"active_list_type", "cohort", "cohort_member",
			"concept", "concept_answer", "concept_class", "concept_complex",
			"concept_datatype", "concept_description", "concept_map_type",
			"concept_name", "concept_name_bkp", "concept_name_tag",
			"concept_name_tag_map", "concept_numeric", "concept_proposal",
			"concept_proposal_tag_map", "concept_reference_map",
			"concept_reference_source", "concept_reference_term",
			"concept_reference_term_map", "concept_set", "concept_set_derived",
			"concept_state_conversion", "concept_stop_word", "concept_word",
			"drug", "drug_ingredient", "drug_order", "encounter",
			"encounter_provider", "encounter_role", "encounter_type", "field",
			"field_answer", "field_type", "form", "form_field",
			"form_resource", "global_property", "location",
			"location_attribute", "location_attribute_type", "location_tag",
			"location_tag_map", "note", "notification_alert",
			"notification_alert_recipient", "notification_template", "obs",
			"order_type", "orders", "patient", "patient_identifier",
			"patient_identifier_type", "patient_program", "patient_state",
			"person", "person_address", "person_attribute",
			"person_attribute_type", "person_merge_log", "person_name",
			"privilege", "program", "program_workflow",
			"program_workflow_state", "provider", "provider_attribute",
			"provider_attribute_type", "relationship", "relationship_type",
			"role", "role_privilege", "role_role", "scheduler_task_config",
			"scheduler_task_config_property", "serialized_object",
			"test_order", "user_property", "user_role", "users", "visit",
			"visit_attribute", "visit_attribute_type", "visit_type"};

	/**
	 * Constructor to initialize the object
	 * 
	 * @param scriptFilePath
	 *            that contains SQL statements to execute for OpenMRS DB
	 * @param openMrsDb
	 *            connection to OpenMRS
	 * @param dwDb
	 *            connection to Data warehouse
	 */
	public OpenMrsProcessor(String scriptFilePath, DatabaseUtil openMrsDb,
			DatabaseUtil dwDb) {
		this.scriptFilePath = scriptFilePath;
		this.openMrsDb = openMrsDb;
		this.dwDb = dwDb;
		Object obj = dwDb.runCommand(CommandType.SELECT,
				"select count(*) from information_schema.tables");
		if (obj == null)
			log.severe("Unable to connect with Data Warehouse!");
		else
			log.info("Data Warehouse connection OK");
		obj = openMrsDb.runCommand(CommandType.SELECT,
				"select count(*) from users");
		if (obj == null)
			log.warning("Unable to connect with OpenMRS!");
		else
			log.info("OpenMRS connection OK");
	}

	// Create OpenMRS DB schema into DW
	public boolean createSchema(boolean fromScratch) {
		FileUtil fileUtil = new FileUtil();
		String[] queries = fileUtil.getLines(scriptFilePath);
		// Recreate tables
		for (String query : queries) {
			if (query.toUpperCase().startsWith("DROP")) {
				dwDb.runCommand(CommandType.DROP, query);
			} else {
				dwDb.runCommand(CommandType.CREATE, query);
			}
		}
		return true;
	}

	/**
	 * Extracts data from OpenMRS connection and stores as CSV files. If a file
	 * already exists, it will be recreated
	 * 
	 * @param dataPath
	 *            where CSV files will be stored
	 * @return
	 */
	public boolean extract(String dataPath) {
		log.info("Importing data from source into raw files");
		// Fetch file from source and generate CSVs
		for (String table : sourceTables) {
			String fileName = dataPath.replace("\\", "\\\\") + schemaName + "_"
					+ table + ".csv";
			File file = new File(fileName);
			if (file.exists()) {
				file.delete();
			}
			String query = "SELECT * FROM "
					+ table
					+ " INTO OUTFILE '"
					+ fileName
					+ "' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n'";
			Object obj = openMrsDb.runCommand(CommandType.EXECUTE, query);
			if (obj == null) {
				log.warning("No data was exported to CSV for table: " + table);
			}
		}
		return true;
	}
	/**
	 * Loads table data from CSV files into Data warehouse
	 * 
	 * @param dataPath
	 *            where CSV files will be loaded from
	 * @return
	 */
	public boolean load(String dataPath) {
		log.info("Importing data from raw files into data warehouse");
		for (String table : sourceTables) {
			String filePath = dataPath.replace("\\", "\\\\") + schemaName + "_"
					+ table + ".csv";
			File file = new File(filePath);
			if (!file.exists()) {
				log.warning("No CSV file exists for table " + table);
				continue;
			}
			String query = "LOAD DATA INFILE '"
					+ filePath
					+ "' INTO TABLE "
					+ table
					+ " FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n'";
			Object obj = dwDb.runCommand(CommandType.EXECUTE, query);
			if (obj == null) {
				log.warning("No data was from CSV for table: " + table);
			}
			// Try to delete the CSV
			try {
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}