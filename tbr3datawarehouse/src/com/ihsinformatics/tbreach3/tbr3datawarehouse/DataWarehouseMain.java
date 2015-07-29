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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.CommandType;
import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.DatabaseUtil;
import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.FileUtil;

/**
 * Data warehousing process for TBR3 Sehatmand Zindagi
 * 
 * @author owais.hussain@ihsinformatics.com
 */
public final class DataWarehouseMain {

	public static final String version = "0.0.1";

	private static final Logger log = Logger.getLogger("DataWarehouse");
	public static final String directoryPath = "c:\\Users\\Owais\\git\\tbreach3-pakistan\\tbr3datawarehouse\\";
	public static final String filePath = directoryPath + new Date().getTime()
			+ ".sql";
	public static final String propertiesFilePath = directoryPath
			+ System.getProperty("file.separator")
			+ "tbr3datawarehouse.properties";
	public DatabaseUtil dwDb, openmrsDb, fmDb, ilmsDb;
	public static Properties props;

	/**
	 * Main executable. Arguments need to be provided as: -R to hard reset
	 * warehouse (Extract > Load > Transform > Dimensional modeling > Nightly
	 * process) -l to extract/load data from various sources (stage1) -t to
	 * transform schema from (stage2) -d to create dimension tables (data
	 * warehouse) -f to create fact tables -u to update data warehouse (nightly
	 * run)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0 || args[0] == null) {
			System.out
					.println("Arguments are invalid. Arguments must be provided as:\n"
							+ "-R to hard reset warehouse (Extract/Load > Transform > Dimensional modeling > Fact tables)\n"
							+ "-l to extract/load data from various sources (stage1)\n"
							+ "-t to transform schema from (stage2)\n"
							+ "-d to create dimension tables (data warehouse)\n"
							+ "-f to create fact tables\n"
							+ "-u to update data warehouse (nightly run)\n");
			return;
		}
		// Read properties
		props = new Properties();
		try {
			props.load(new FileInputStream(propertiesFilePath));
		} catch (IOException e) {
			log.severe("Properties file not found.");
		}
		DataWarehouseMain dw = new DataWarehouseMain();
		dw.setDataConnections();
		if (dw.hasSwitch(args, "R")) {
			dw.resetDataWarehouse();
			return;
		}
		if (dw.hasSwitch(args, "l")) {
			dw.extractLoad();
		}
		if (dw.hasSwitch(args, "t")) {
			dw.transform();
		}
		if (dw.hasSwitch(args, "d")) {
			dw.createDimensions();
		}
		if (dw.hasSwitch(args, "f")) {
			dw.createFacts();
		}
		if (dw.hasSwitch(args, "u")) {
			dw.updateWarehosue();
		}
	}

	/**
	 * Set connection for all Data repositories and data warehouse. Data
	 * warehouse user must have full privileges
	 */
	public void setDataConnections() {
		// Data warehoues credentials
		String driver = DataWarehouseMain
				.getProperty("dw.connection.driver_class");
		String url = DataWarehouseMain.getProperty("dw.connection.url");
		String username = DataWarehouseMain
				.getProperty("dw.connection.username");
		String password = DataWarehouseMain
				.getProperty("dw.connection.password");
		dwDb = new DatabaseUtil(url, driver, username, password);
		Object obj = dwDb.runCommand(CommandType.SELECT,
				"select count(*) from information_schema.tables");
		if (obj == null)
			log.severe("Unable to connect with Data Warehouse!");
		else
			log.info("Data Warehouse connection OK");

		// OpenMRS DB credentials
		driver = DataWarehouseMain
				.getProperty("openmrs.connection.driver_class");
		url = DataWarehouseMain.getProperty("openmrs.connection.url");
		username = DataWarehouseMain.getProperty("openmrs.connection.username");
		password = DataWarehouseMain.getProperty("openmrs.connection.password");
		openmrsDb = new DatabaseUtil(url, driver, username, password);
		obj = openmrsDb.runCommand(CommandType.SELECT,
				"select count(*) from users");
		if (obj == null)
			log.warning("Unable to connect with OpenMRS!");
		else
			log.info("OpenMRS connection OK");

		// Field Monitoring DB credentials
		driver = DataWarehouseMain
				.getProperty("fieldmonitoring.connection.driver_class");
		url = DataWarehouseMain.getProperty("fieldmonitoring.connection.url");
		username = DataWarehouseMain
				.getProperty("fieldmonitoring.connection.username");
		password = DataWarehouseMain
				.getProperty("fieldmonitoring.connection.password");
		fmDb = new DatabaseUtil(url, driver, username, password);
		obj = fmDb.runCommand(CommandType.SELECT, "select count(*) from users");
		if (obj == null)
			log.warning("Unable to connect with Field Monitoring!");
		else
			log.info("Field Monitoring connection OK");
	}

	/**
	 * Write properties to properties file and reads back
	 */
	public static boolean writeProperties(Map<String, String> properties) {
		boolean success = false;
		if (properties.isEmpty()) {
			System.out.println("No properties to write to file.");
		}
		Set<Entry<String, String>> entrySet = properties.entrySet();
		for (Iterator<Entry<String, String>> iter = entrySet.iterator(); iter
				.hasNext();) {
			Entry<String, String> pair = iter.next();
			props.setProperty(pair.getKey(), pair.getValue());
		}
		try {
			if (!(new File(directoryPath).exists())) {
				boolean checkDir = new File(directoryPath).mkdir();
				if (!checkDir) {
					log.severe("Could not create properties file. Please check the permissions of your home folder");
				}
			}
			props.store(new FileOutputStream(propertiesFilePath), null);
			props.load(new FileInputStream(propertiesFilePath));
			success = true;
		} catch (FileNotFoundException e) {
			log.severe("Could not create properties file. Please check the permissions of your home folder. Exception: "
					+ e.getMessage());
		} catch (IOException e) {
			log.severe("Could not create properties file. Exception: "
					+ e.getMessage());
		}
		return success;
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	public boolean hasSwitch(String[] args, String switchChar) {
		for (String s : args) {
			if (s.equals("-" + switchChar)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Drops and recreates Data warehouse tables to hard reset
	 */
	public void resetDataWarehouse() {
		log.info("Starting DW hard reset");
		Object[] tables = dwDb.getColumnData("information_schema.tables",
				"table_name", "table_schema='sz_dw'");
		for (Object t : tables) {
			dwDb.deleteTable(t.toString());
		}
		extractLoad();
		transform();
		createDimensions();
		createFacts();
		updateWarehosue();
		log.info("Finished DW hard reset");
	}

	public void extractLoad() {
		log.info("Starting ETL");
		// Create OpenMRS DB schema into DW
		FileUtil fileUtil = new FileUtil();
		String[] queries = fileUtil.getLines("openmrs_schema.sql");
		for (String query : queries) {
			dwDb.runCommand(CommandType.CREATE, query);
		}
		// Create Field Monitoring DB clone into DW
		log.info("Finished ETL");
	}

	public void transform() {
		log.info("Starting data transformation");
		log.info("Finished data transformation");
	}

	public void createDimensions() {
		log.info("Starting dimension modeling");
		log.info("Finished dimension modeling");
	}

	public void createFacts() {
		log.info("Starting fact tables");
		log.info("Finished fact tables");
	}

	public void updateWarehosue() {
		log.info("Starting DW update");
		log.info("Finished DW update");
	}
}
