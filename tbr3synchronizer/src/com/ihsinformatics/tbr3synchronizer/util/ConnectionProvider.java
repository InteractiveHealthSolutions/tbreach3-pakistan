/**
 * Copyright(C) 2016 Interactive Health Solutions, Pvt. Ltd.
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. 
 * 
 */

package com.ihsinformatics.tbr3synchronizer.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * ConnectionProvider class for connection with database
 * 
 * @author Hera Rafique
 * 
 */
public class ConnectionProvider {

	public Properties properties;

	public ConnectionProvider() {
		try {
			/**
			 * read data from .properties file
			 */
			properties = new Properties();
			InputStream propFile = ConnectionProvider.class
					.getResourceAsStream("/tbr3synchronizer.properties");
			properties.load(propFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getOpenMrsLocalConnection() {
		try {
			Class.forName(properties.getProperty(PropertyName.JDBC_DRIVER));
			Connection connection = DriverManager.getConnection(properties
					.getProperty(PropertyName.OPENMRSL_CONNECTION_URL),
					properties.getProperty(PropertyName.OPENMRSL_USER),
					properties.getProperty(PropertyName.OPENMRSL_PASSWORD));
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Connection getOpenMrsMainConnection() {
		try {
			Class.forName(properties.getProperty(PropertyName.JDBC_DRIVER));
			Connection connection = DriverManager.getConnection(properties
					.getProperty(PropertyName.OPENMRSM_CONNECTION_URL),
					properties.getProperty(PropertyName.OPENMRSM_USER),
					properties.getProperty(PropertyName.OPENMRSM_PASSWORD));

			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getDays() {
		return Integer.parseInt(properties.getProperty(PropertyName.DAYS));
	}
}
