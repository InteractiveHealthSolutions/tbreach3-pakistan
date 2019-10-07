/* Copyright(C) 2014 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */

package com.ihsinformatics.tbr3.treatmentregister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public final class Tbr3Properties {

	public static Properties props;

	public static final String VERSION = "version";

	public static final String DB_IP_ADDRESS = "db_ip_address";

	public static final String DB_PORT = "db_port";

	public static final String DB_NAME = "db_name";

	public static final String DB_USERNAME = "db_username";

	public static final String DB_PASSWORD = "db_password";

	public static final String DATE_FORMAT = "date_format";

	/**
	 * Read properties from properties file
	 */
	public static void readProperties() {
		props = new Properties();
		if (new File(FileConstants.FILE_PATH).exists()) {
			try {
				props.load(new FileInputStream(FileConstants.FILE_PATH));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			props.setProperty(VERSION, "2.0.0-beta");
			props.setProperty(DB_NAME, "tbr3_treatment");
			props.setProperty(DB_USERNAME, "root");
			props.setProperty(DB_PASSWORD, "");
			props.setProperty(DB_IP_ADDRESS, "127.0.0.1");
			props.setProperty(DB_PORT, "3306");
		}
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
			if (!(new File(FileConstants.TBR3_DIR).exists())) {
				boolean checkDir = new File(FileConstants.TBR3_DIR)
						.mkdir();
				if (!checkDir) {
					JOptionPane
							.showMessageDialog(
									null,
									"Could not create properties file. Please check the permissions of your home folder.",
									"Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
			props.store(new FileOutputStream(FileConstants.FILE_PATH), null);
			props.load(new FileInputStream(FileConstants.FILE_PATH));
			JOptionPane.showMessageDialog(null, "Settings updated!",
					"Success!", JOptionPane.INFORMATION_MESSAGE);
			success = true;
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Could not create properties file. Please check the permissions of your home folder.",
							"Error!", JOptionPane.ERROR_MESSAGE);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Could not create properties file.", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
		return success;
	}

	public static String getProperty(String key) {
		String value = props.getProperty(key);
		if (value == null)
			value = "";
		return value;
	}
}
