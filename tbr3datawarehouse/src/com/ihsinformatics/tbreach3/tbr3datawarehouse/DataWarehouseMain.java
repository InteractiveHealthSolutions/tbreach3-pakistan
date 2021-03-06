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
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.CommandType;
import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.DatabaseUtil;
import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.DateTimeUtil;
import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.FileUtil;

/**
 * Data warehousing process for TBR3 Sehatmand Zindagi
 * 
 * @author owais.hussain@ihsinformatics.com
 */
public final class DataWarehouseMain {

	public static final String version = "1.2.0";

	private static final Logger log = Logger.getLogger(Class.class.getName());
	public static final String dwSchema = "sz_dw";
	public static String dataPath = System.getProperty("user.home").replace("\\", FileUtil.SEPARATOR);
	public static String propertiesFilePath = "tbr3datawarehouse.properties";
	public OpenMrsProcessor openMrs;
	public FieldMonitoringProcessor fm;
	// public IlmsProcessor ilms;
	public DatabaseUtil dwDb;
	public static Properties props;

	/**
	 * Main executable. Arguments need to be provided as: -r to hard reset
	 * warehouse (Extract > Load > Transform > Dimensional modeling > Nightly
	 * process) -l to extract/load data from various sources (stage1) -t to
	 * transform schema from (stage2) -d to create dimension tables (data
	 * warehouse) -f to create fact tables -u to update data warehouse (nightly
	 * run)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Create data directory if needed
		File file = new File(dataPath + FileUtil.SEPARATOR + dwSchema);
		if (!(file.exists() && file.isDirectory())) {
			file.mkdir();
			file.setWritable(true, false);
		}
		dataPath += FileUtil.SEPARATOR + dwSchema + FileUtil.SEPARATOR;
		// Check arguments first
		if (args.length == 0 || // args.length < 3 ||
				args[0] == null) {
			System.out
					.println("Arguments are invalid. Arguments must be provided as:\n"
							+ "-r to hard reset warehouse (Extract/Load > Transform > Dimensional modeling > Fact tables)\n"
							+ "-l to extract/load data from various sources (stage1)\n"
							+ "-t to transform schema from (stage2)\n"
							+ "-d to create dimension tables (data warehouse)\n"
							+ "-p path to properties file\n"
							+ "-f to create fact tables\n"
							+ "-u to update data warehouse (nightly run)\n");
			return;
		}
		DataWarehouseMain dw = new DataWarehouseMain();
		props = new Properties();
		// First, try to read properties file in class path
		try {
			InputStream propFile = DataWarehouseMain.class
					.getResourceAsStream("/tbr3datawarehouse.properties");
			if (propFile != null) {
				props.load(propFile);
				dw.setDataConnections();
			}
		} catch (IOException e) {
			log.warning("Properties file not found in class path.");
		}
		int days = 0;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-p")) {
				propertiesFilePath = args[i + 1];
				// Read properties
				try {
					props.load(new FileInputStream(propertiesFilePath));
					dw.setDataConnections();
				} catch (IOException e) {
					log.severe("Properties file not found.");
				}
			}
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-r")) {
				dw.resetDataWarehouse();
				return;
			} else if (args[i].equalsIgnoreCase("-l")) {
				dw.extractLoad(false);
			} else if (args[i].equalsIgnoreCase("-t")) {
				dw.transform();
			} else if (args[i].equalsIgnoreCase("-d")) {
				dw.createDimensions();
			} else if (args[i].equalsIgnoreCase("-f")) {
				dw.createFacts();
			} else if (args[i].equalsIgnoreCase("-u")) {
				try {
					days = Integer.parseInt(args[i + 1]);
					Date dateFrom = new Date();
					Date dateTo = new Date();
					Calendar instance = Calendar.getInstance();
					instance.add(Calendar.DATE, days);
					dateFrom = instance.getTime();
					dw.updateWarehosue(dataPath, dateFrom, dateTo);
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
	 * Load Properties
	 */

	public void loadProperties() {
		// Read properties
		props = new Properties();
		try {
			props.load(new FileInputStream(propertiesFilePath));
		} catch (IOException e) {
			try {
				props.load(new FileInputStream("tbr3datawarehouse.properties"));
			} catch (IOException e2) {
				log.severe("Properties file not found.");
				return;
			}
		}
	}

	/**
	 * Set connection for all Data repositories and data warehouse. Data
	 * warehouse user must have full privileges
	 */
	public void setDataConnections() {
		// Get Data warehoues credentials
		String driver = DataWarehouseMain
				.getProperty("dw.connection.driver_class");
		String url = DataWarehouseMain.getProperty("dw.connection.url");
		String username = DataWarehouseMain
				.getProperty("dw.connection.username");
		String password = DataWarehouseMain
				.getProperty("dw.connection.password");
		dwDb = new DatabaseUtil(url, driver, username, password);
		// Get OpenMRS DB credentials
		driver = DataWarehouseMain
				.getProperty("openmrs.connection.driver_class");
		url = DataWarehouseMain.getProperty("openmrs.connection.url");
		username = DataWarehouseMain.getProperty("openmrs.connection.username");
		password = DataWarehouseMain.getProperty("openmrs.connection.password");
		DatabaseUtil openMrsDb = new DatabaseUtil(url, driver, username,
				password);
		openMrs = new OpenMrsProcessor("openmrs_schema.sql", openMrsDb, dwDb);
		// Field Monitoring DB credentials
		driver = DataWarehouseMain
				.getProperty("fieldmonitoring.connection.driver_class");
		url = DataWarehouseMain.getProperty("fieldmonitoring.connection.url");
		username = DataWarehouseMain
				.getProperty("fieldmonitoring.connection.username");
		password = DataWarehouseMain
				.getProperty("fieldmonitoring.connection.password");
		DatabaseUtil fmDb = new DatabaseUtil(url, driver, username, password);
		fm = new FieldMonitoringProcessor("tbr3_monitoring_schema.sql", fmDb,
				dwDb);
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
			if (!(new File(propertiesFilePath).exists())) {
				boolean checkPropFile = new File(propertiesFilePath).mkdir();
				if (!checkPropFile) {
					log.severe("Could not read properties file.");
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
		Object[] tables = dwDb.getColumnData("information_schema.TABLES",
				"TABLE_NAME", "TABLE_SCHEMA='" + dwSchema + "'");
		for (Object t : tables) {
			dwDb.deleteTable(t.toString());
		}
		extractLoad(true);
		createDimensions();
		transform();
		createFacts();
		log.info("Finished DW hard reset");
	}

	public void extractLoad(boolean fromScratch) {
		log.info("Starting Extract/Load");
		// OpenMRS EL
		openMrs.createSchema(fromScratch);
		openMrs.extract(dataPath);
		openMrs.load(dataPath);
		// Field Monitoring EL
		fm.createSchema(fromScratch);
		fm.extract(dataPath);
		fm.load(dataPath);
		// ILMS EL
		// ilms.createSchema(fromScratch);
		// ilms.extract(dataPath);
		// ilms.load(dataPath);
		log.info("Finished Extract/Load");
	}

	public void createDimensions() {
		log.info("Starting dimension modeling");
		FileUtil fileUtil = new FileUtil();
		String[] queries = fileUtil.getLines("dimension_modeling.sql");
		// Recreate tables
		for (String query : queries) {
			if (query.toUpperCase().startsWith("DROP")) {
				dwDb.runCommand(CommandType.DROP, query);
			} else if (query.toUpperCase().startsWith("CREATE")) {
				dwDb.runCommand(CommandType.CREATE, query);
			} else if (query.toUpperCase().startsWith("UPDATE")) {
				dwDb.runCommand(CommandType.UPDATE, query);
			} else {
				dwDb.runCommand(CommandType.INSERT, query);
			}
		}
		// Fill in datetime_dim table
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.set(Calendar.YEAR, 2000);
		start.set(Calendar.MONTH, Calendar.JANUARY);
		start.set(Calendar.DATE, 1);
		end.set(Calendar.HOUR, 0);
		int i = 1;
		while (start.getTime().before(end.getTime())) {
			String sqlDate = "'" + DateTimeUtil.getSqlDate(start.getTime())
					+ "'";
			String query = "insert into dim_datetime values (" + (i++) + ", "
					+ sqlDate + ", year(" + sqlDate + "), month(" + sqlDate
					+ "), day(" + sqlDate + "), dayname(" + sqlDate
					+ "), monthname(" + sqlDate + "));";
			dwDb.runCommand(CommandType.INSERT, query);
			start.add(Calendar.DATE, 1);
		}
		log.info("Finished dimension modeling");
	}

	public void transform() {
		log.info("Starting data transformation");
		boolean result = openMrs.transform();
		if (!result) {
			log.warning("OpenMRS DB transformation completed with warnings.");
		}
		result = fm.transform();
		if (!result) {
			log.warning("Field Monitoring DB transformation completed with warnings.");
		}
		log.info("Finished data transformation");
	}

	public void createFacts() {
		log.info("Starting fact tables");
		log.info("Finished fact tables");
	}

	public void updateWarehosue(String dataPath, Date dateFrom, Date dateTo) {
		log.info("Starting DW update");
		// openMrs.divideTables();
		boolean result = openMrs.update(dataPath, dateFrom, dateTo);
		if (!result) {
			log.warning("OpenMRS DB transformation completed with warnings.");
		}
		result = fm.update(dataPath, dateFrom, dateTo);

		if (!result) {
			log.warning("Field Monitoring DB transformation completed with warnings.");
		}
		log.info("Finished DW update");
	}
}
