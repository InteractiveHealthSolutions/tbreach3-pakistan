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
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import com.ihsinformatics.tbr3ilmssync.util.DatabaseUtil;

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
							+ "-u to update data for given number of days\n"
							+ "-d number of past days to synchronize data\n");
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
					instance.add(Calendar.DATE, days);
					dateFrom = instance.getTime();
					tbr3.updateData(dateFrom, dateTo);
				} catch (Exception e) {
					System.out
							.println("Please enter the number of days in the argument \n"
									+ "i.e. -u 365");
				}
			}
		}
		System.exit(0);
	}

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

			System.out.println(dwDb.tryConnection());
			System.out.println(ilmsDb.tryConnection());

		} catch (IOException e) {
			log.severe(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param dateFrom
	 * @param dateTo
	 */
	private void updateData(Date dateFrom, Date dateTo) {
	}
}
