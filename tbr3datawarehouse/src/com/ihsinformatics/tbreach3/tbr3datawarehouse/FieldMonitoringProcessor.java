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
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.CommandType;
import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.DatabaseUtil;
import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.FileUtil;

/**
 * Field Monitoring database processing class for TBR3 warehouse
 * 
 * @author owais.hussain@ihsinformatics.com
 *
 */
public class FieldMonitoringProcessor extends AbstractProcessor {

	private static final Logger log = Logger.getLogger(Class.class.getName());
	private String scriptFilePath;
	private DatabaseUtil dwDb;
	private DatabaseUtil fmDb;
	String[] sourceTables = { "defaults", "definition", "definition_type",
			"dictionary", "encounter", "encounter_element",
			"encounter_prerequisite", "encounter_results", "encounter_type",
			"encounter_value", "feedback", "lab_test", "location", "log_data",
			"log_login", "patient", "person", "person_role", "referral",
			"response", "screening", "sms", "sms_log", "sms_rule", "sms_text",
			"sputum_test", "user", "user_mapping", "user_rights", "visit" };

	/**
	 * Constructor to initialize the object
	 * 
	 * @param scriptFilePath
	 *            that contains SQL statements to execute for Field Monitoring
	 *            DB
	 * @param fmDb
	 *            connection to Field Monitoring DB
	 * @param dwDb
	 *            connection to Data warehouse
	 */
	public FieldMonitoringProcessor(String scriptFilePath, DatabaseUtil fmDb,
			DatabaseUtil dwDb) {
		this.scriptFilePath = scriptFilePath;
		this.fmDb = fmDb;
		this.dwDb = dwDb;
		Object obj = fmDb.runCommand(CommandType.SELECT,
				"select count(*) from user");
		if (obj == null)
			log.warning("Unable to connect with Field Monitoring!");
		else
			log.info("Field Monitoring connection OK");
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
	 * Extracts data from OpenMRS connection and stores as CSV files
	 * 
	 * @param dataPath
	 *            where CSV files will be stored
	 * @return
	 */
	public boolean extract(String dataPath) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1970);
		return extract(dataPath, calendar.getTime(), new Date());
	}

	/**
	 * Extracts data from OpenMRS connection, Picks new/changed data and updates
	 * Data warehouse and stores as CSV files
	 * 
	 * @param dataPath
	 *            where CSV files will be stored
	 * @param dateFrom
	 * 
	 * @param dateTo
	 * @return
	 */
	public boolean extract(String dataPath, Date dateFrom, Date dateTo) {
		// log.info("Importing data from source into raw files");
		log.info("Updating Field Monitoring data");
		// Fetch file from source and generate CSVs
		for (String table : sourceTables) {
			String fileName = dataPath + table + ".csv";
			File file = new File(fileName);
			if (file.exists()) {
				file.delete();
			}
			String query = "SELECT * FROM "
					+ table
					+ " INTO OUTFILE '"
					+ fileName
					+ "' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n'";
			Object obj = fmDb.runCommand(CommandType.EXECUTE, query);
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
	 * 
	 * @return
	 */
	public boolean load(String dataPath) {
		boolean noImport = true;
		log.info("Importing data from raw files into data warehouse");
		for (String table : sourceTables) {
			String filePath = dataPath + table + ".csv";
			File file = new File(filePath);
			if (!file.exists()) {
				log.warning("No CSV file exists for table " + table);
				continue;
			}
			String query = "LOAD DATA INFILE '"
					+ filePath
					+ "' INTO TABLE "
					+ "fm_"
					+ table
					+ " FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n'";
			Object obj = dwDb.runCommand(CommandType.EXECUTE, query);
			if (obj == null) {
				log.warning("No data was from CSV for table: " + table);
			} else {
				noImport = false;
			}
			// Try to delete the CSV
			try {
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return !noImport;
	}

	/**
	 * Denormalize and standardize tables according to the warehouse
	 */
	boolean transform() {
		// Fetch encounter types
		Object[] encounterTypes = dwDb.getColumnData("fm_encounter_type",
				"encounter_type", "");
		if (encounterTypes == null) {
			log.severe("No Encounter types found in Field Monitoring data.");
			return false;
		}
		for (Object encounterType : encounterTypes) {
			// Create a deencounterized table
			Object[] elements = dwDb.getColumnData("fm_encounter_element",
					"element", "encounter_type='" + encounterType.toString()
							+ "'");
			StringBuilder groupConcat = new StringBuilder();
			for (Object element : elements) {
				String str = element.toString().replace("'", "''");
				groupConcat
						.append("group_concat(if(er.element = '" + str
								+ "', er.value, NULL)) AS '"
								+ str.replace(" ", "_").toLowerCase() + "',");
			}
			String baseQuery = "select e.e_id, e.pid1, e.pid2, e.date_start, e.date_end, e.date_entered, "
					+ groupConcat.toString()
					+ "'' as BLANK "
					+ "from fm_encounter as e inner join fm_encounter_results as er using (e_id, pid1, pid2) "
					+ "where e.encounter_type = '"
					+ encounterType
					+ "' "
					+ " and er.encounter_type = '" + encounterType + "' "

					// and er.encounter_type = 'DAILY_VIS'
					+ "group by e.e_id, e.pid1, e.pid2, e.date_entered";
			// Drop previous table
			dwDb.runCommand(CommandType.DROP, "drop table if exists fm_enc_"
					+ encounterType);
			log.info("Generating table for " + encounterType);
			// Insert new data
			Object result = dwDb.runCommand(CommandType.CREATE,
					"create table fm_enc_"
							+ encounterType.toString().toLowerCase() + " "
							+ baseQuery);
			if (result == null) {
				log.warning("No data imported for Encounter " + encounterType);
			}
			// Creating Primary key
			dwDb.runCommand(CommandType.ALTER, "alter table fm_enc_"
					+ encounterType + " add primary key (e_id, pid1, pid2)");
		}
		return true;
	}

	/**
	 * Picks new/changed data and updates Data warehouse
	 */
	boolean update(String dataPath, Date dateFrom, Date dateTo) {
		boolean result = true;
		log.info("Updating Field Monitoring data");
		createSchema(true);
		extract(dataPath);
		load(dataPath);
		transform();
		return result;
	}
}
