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
public class FieldMonitoringProcessor extends AbstractProcessor {

	private static final Logger log = Logger.getLogger(Class.class.getName());
	private static final String schemaName = "tbr3_monitoring";
	private String scriptFilePath;
	private DatabaseUtil dwDb;
	private DatabaseUtil fmDb;
	String[] sourceTables = {};

	/**
	 * Constructor to initialize the object
	 * 
	 * @param scriptFilePath
	 *            that contains SQL statements to execute for OpenMRS DB
	 * @param fmDb
	 *            connection to OpenMRS
	 * @param dwDb
	 *            connection to Data warehouse
	 */
	public FieldMonitoringProcessor(String scriptFilePath, DatabaseUtil fmDb,
			DatabaseUtil dwDb) {
		this.scriptFilePath = scriptFilePath;
		this.fmDb = fmDb;
		this.dwDb = dwDb;
		Object 		obj = fmDb.runCommand(CommandType.SELECT, "select count(*) from users");
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
