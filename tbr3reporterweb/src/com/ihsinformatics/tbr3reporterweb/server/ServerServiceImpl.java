/**
 * Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
 * Contributors: Owais Hussain
 */
package com.ihsinformatics.tbr3reporterweb.server;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.ModuleMustStartException;
import org.openmrs.util.DatabaseUpdateException;
import org.openmrs.util.InputRequiredException;
import org.openmrs.util.OpenmrsUtil;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ihsinformatics.tbr3reporterweb.client.ServerService;
import com.ihsinformatics.tbr3reporterweb.server.util.DateTimeUtil;
import com.ihsinformatics.tbr3reporterweb.server.util.HibernateUtil;
import com.ihsinformatics.tbr3reporterweb.server.util.ReportUtil;
import com.ihsinformatics.tbr3reporterweb.shared.Parameter;
import com.ihsinformatics.tbr3reporterweb.shared.TBR3;

/**
 * @author Owais
 * 
 */
public class ServerServiceImpl extends RemoteServiceServlet
		implements
			ServerService {
	private static final long serialVersionUID = 4123609914879659870L;

	// Form Openmrs properties file
	// static final String resourcePath =
	// "/usr/share/tomcat6/.OpenMRS/openmrs-runtime.properties";
	static final String resourcePath = "C:\\Application Data\\OpenMRS\\openmrs-runtime.properties";

	public ServerServiceImpl() {
		initOpenMrs();
	}

	public Boolean initOpenMrs() {
		String url, username, password;
		File propsFile = new File(resourcePath);
		Properties props = new Properties();
		OpenmrsUtil.loadProperties(props, propsFile);
		url = (String) props.get("connection.url");
		username = (String) props.get("connection.username");
		password = (String) props.get("connection.password");
		try {
			Context.startup(url, username, password, props);
			Context.openSession();
		} catch (ModuleMustStartException e) {
			e.printStackTrace();
		} catch (DatabaseUpdateException e) {
			e.printStackTrace();
		} catch (InputRequiredException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * User authentication: Checks whether user exists, then match his password
	 * 
	 * @return Boolean
	 */
	public Boolean authenticate(String userName, String password)
			throws Exception {
		boolean result = false;
		try {
			Context.openSession();
			// Authenticate
			Context.authenticate(userName, password);
			// Get user object and look for required privileges
			User user = Context.getUserService().getUserByUsername(userName);
			Collection<Privilege> privileges = user.getPrivileges();
			Set<Role> roles = user.getAllRoles();
			for (Iterator<Role> iter = roles.iterator(); iter.hasNext();) {
				Role role = iter.next();
				if(role.getRole().equals("System Developer")) {
					result = true;
				}
			}
		} catch (ContextAuthenticationException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Get number of records in a table, given appropriate filter
	 * 
	 * @return Long
	 */
	public Long count(String tableName, String filter) throws Exception {
		Object obj = HibernateUtil.util.selectObject("select count(*) from "
				+ tableName + " " + arrangeFilter(filter));
		return Long.parseLong(obj.toString());
	}

	/**
	 * Checks existence of data by counting number of records in a table, given
	 * appropriate filter
	 * 
	 * @return Boolean
	 */
	public Boolean exists(String tableName, String filter) throws Exception {
		long count = count(tableName, filter);
		return count > 0;
	}

	private String arrangeFilter(String filter) throws Exception {
		if (filter.trim().equalsIgnoreCase(""))
			return "";
		return (filter.toUpperCase().contains("WHERE") ? "" : " where ")
				+ filter;
	}

	/**
	 * Generates CSV file from query passed along with the filters
	 * 
	 * @param query
	 * @return
	 */
	public String generateCSVfromQuery(String query) throws Exception {
		return new ReportUtil(resourcePath).generateCSVfromQuery(query, ',');
	}

	/**
	 * Generate report on server side and return the path it was created to
	 * 
	 * @param Path
	 *            of report as String Report parameters as Parameter[] Report to
	 *            be exported in csv format as Boolean
	 * @return String
	 */
	public String generateReport(String fileName, Parameter[] params,
			boolean export) throws Exception {
		return new ReportUtil(resourcePath).generateReport(fileName, params,
				export);
	}

	/**
	 * Generate report on server side based on the query saved in the Database
	 * against the reportName and return the path it was created to
	 * 
	 * @param reportName
	 * @param params
	 * @param export
	 * @return
	 */
	public String generateReportFromQuery(String reportName, String query,
			Parameter[] params, boolean export) throws Exception {
		return new ReportUtil(resourcePath).generateReportFromQuery(reportName,
				query, params, export);
	}

	public String[] getColumnData(String tableName, String columnName,
			String filter) throws Exception {
		Object[] data = HibernateUtil.util.selectObjects("select distinct "
				+ columnName + " from " + tableName + " "
				+ arrangeFilter(filter));
		String[] columnData = new String[data.length];
		for (int i = 0; i < data.length; i++)
			columnData[i] = data[i].toString();
		return columnData;
	}

	public String getCurrentUser() throws Exception {
		return TBR3.getCurrentUser();
	}

	public String getObject(String tableName, String columnName, String filter)
			throws Exception {
		return HibernateUtil.util.selectObject(
				"select " + columnName + " from " + tableName
						+ arrangeFilter(filter)).toString();
	}

	public String[][] getReportsList() throws Exception {
		return new ReportUtil(resourcePath).getReportList();
	}

	public String[] getRowRecord(String tableName, String[] columnNames,
			String filter) throws Exception {
		return getTableData(tableName, columnNames, filter)[0];
	}

	public String getSnapshotTime() throws Exception {
		Date dt = new Date();
		return DateTimeUtil.getSQLDate(dt);
	}

	public String[][] getTableData(String tableName, String[] columnNames,
			String filter) throws Exception {
		StringBuilder columnList = new StringBuilder();
		for (String s : columnNames) {
			columnList.append(s);
			columnList.append(",");
		}
		columnList.deleteCharAt(columnList.length() - 1);

		Object[][] data = HibernateUtil.util.selectData("select "
				+ columnList.toString() + " from " + tableName + " "
				+ arrangeFilter(filter));
		String[][] stringData = new String[data.length][columnNames.length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < columnNames.length; j++) {
				if (data[i][j] == null)
					data[i][j] = "";
				String str = data[i][j].toString();
				stringData[i][j] = str;
			}
		}
		return stringData;
	}

	public int execute(String query) throws Exception {
		return HibernateUtil.util.runCommand(query);
	}

	public Boolean execute(String[] queries) throws Exception {
		for (String s : queries) {
			boolean result = execute(s) >= 0;
			if (!result)
				return false;
		}
		return true;
	}

	public Boolean executeProcedure(String procedure) throws Exception {
		return HibernateUtil.util.runProcedure(procedure);
	}

	/**
	 * Sets current user name, this is due to a strange GWT bug/feature that
	 * shared variables, set from Client-side appear to be empty on Server-side
	 * code
	 * 
	 * @return
	 */
	public void setCurrentUser(String userName) {
		TBR3.setCurrentUser(userName);
	}
}
