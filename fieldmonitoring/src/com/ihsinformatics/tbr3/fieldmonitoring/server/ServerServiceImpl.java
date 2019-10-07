
package com.ihsinformatics.tbr3.fieldmonitoring.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


import com.ihsinformatics.tbr3.fieldmonitoring.client.ServerService;
import com.ihsinformatics.tbr3.fieldmonitoring.server.util.DateTimeUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.server.util.HibernateUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.server.util.MDHashUtil;
//import com.ihsinformatics.tbr3.fieldmonitoring.server.util.ReportUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.server.util.SMSUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.Parameter;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.*;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 * 
 * @author owais.hussain@irdresearch.org
 */
@SuppressWarnings("serial")
public class ServerServiceImpl extends RemoteServiceServlet implements ServerService
{
	private static String	applicationPath		= "";
	private static String	propertiesFilePath	= "";

	public ServerServiceImpl ()
	{
		String currentDirectory = System.getProperty ("user.dir");
		System.out.println ("Current directory:" + currentDirectory);
		if (currentDirectory.startsWith ("/"))
			applicationPath = "/var/lib/tomcat6/webapps/fieldmonitoring/";
		else
			applicationPath = "C:\\workspace2\\tbreach3-pakistan\\fieldmonitoring\\";
		propertiesFilePath = applicationPath + "tbreachkenya.properties";
		setProperties ();
	}

	private String arrangeFilter (String filter)
	{
		if (filter.trim ().equalsIgnoreCase (""))
			return "";
		return (filter.toUpperCase ().contains ("WHERE") ? "" : " where ") + filter;
	}

	/**
	 * Sends a generic SMS
	 * 
	 * @param sms
	 */
	public void sendGenericSMSAlert (Sms[] sms)
	{
		for (Sms s : sms)
			sendGenericSMSAlert (s);
	}

	/**
	 * Sends a generic SMS
	 * 
	 * @param sms
	 */
	public void sendGenericSMSAlert (Sms sms)
	{
		if (!sms.getTargetNumber ().equals (""))
			HibernateUtil.util.save (sms);
	}

	/**
	 * User authentication: Checks whether user exists, then match his password
	 * and finally checks if he has the role defined. If user name is ADMIN and
	 * no Users are defined, then calculates the password using a specific
	 * formula and matches with the input. If role is not defined, then the
	 * currentStatus property is set to 'D' - for deined
	 * 
	 * @return User
	 */
	public String authenticate(String userName, String password)
	{
		String response = "success";
//		try
//		{
//			Context.openSession();
//			Context.authenticate(userName, password);
//		}
//		catch (ContextAuthenticationException e)
//		{
//			// e.printStackTrace();
//			response = CustomMessage
//					.getErrorMessage(ErrorType.AUTHENTICATION_ERROR);
//		}
		return response;
	}

	/**
	 * Checks if a user exists in the database
	 * 
	 * @return Boolean
	 */
	public Boolean authenticateUser (String userName)
	{
		if (!UserAuthentication.userExsists (userName))
			return false;
		return true;
	}

	/**
	 * Verifies secret answer against stored secret question
	 * 
	 * @return Boolean
	 */
	public Boolean verifySecretAnswer (String userName, String secretAnswer)
	{
		if (!UserAuthentication.validateSecretAnswer (userName, secretAnswer))
			return false;
		return true;
	}

	/**
	 * Get number of records in a table, given appropriate filter
	 * 
	 * @return Long
	 */
	public Long count (String tableName, String filter)
	{
		Object obj = HibernateUtil.util.selectObject ("select count(*) from " + tableName + " " + arrangeFilter (filter));
		return Long.parseLong (obj.toString ());
	}

	/**
	 * Checks existence of data by counting number of records in a table, given
	 * appropriate filter
	 * 
	 * @return Boolean
	 */
	public Boolean exists (String tableName, String filter)
	{
		long count = count (tableName, filter);
		return count > 0;
	}

	/**
	 * Generates CSV file from query passed along with the filters
	 * 
	 * @param query
	 * @return
	 */
//	public String generateCSVfromQuery (String database, String query)
//	{
//		return ReportUtil.generateCSVfromQuery (database, query, ',');
//	}

	/**
	 * Generate report on server side and return the path it was created to
	 * 
	 * @param Path
	 *            of report as String Report parameters as Parameter[] Report to
	 *            be exported in csv format as Boolean
	 * @return String
	 */
//	public String generateReport (String fileName, Parameter[] params, boolean export)
//	{
//		return ReportUtil.generateReport (fileName, params, export);
//	}

	/**
	 * Generate report on server side based on the query saved in the Database
	 * against the reportName and return the path it was created to
	 * 
	 * @param reportName
	 * @param params
	 * @param export
	 * @return
	 */
//	public String generateReportFromQuery (String database, String reportName, String query, Boolean export)
//	{
//		return ReportUtil.generateReportFromQuery (database, reportName, query, export);
//	}

	public String[] getColumnData (String tableName, String columnName, String filter)
	{
		Object[] data = HibernateUtil.util.selectObjects ("select distinct cast(" + columnName + " as char) from " + tableName + " " + arrangeFilter (filter));
		String[] columnData = new String[data.length];
		for (int i = 0; i < data.length; i++)
			columnData[i] = data[i].toString ();
		return columnData;
	}

	/**
	 * Sets current user name, this is due to a strange GWT bug/feature that
	 * shared variables, set from Client-side appear to be empty on Server-side
	 * code
	 * 
	 * @return
	 */
	public void setCurrentUser (String userName, String role)
	{
		FM.setCurrentUserName (userName);
		FM.setCurrentRole (role);
	}

	public String getCurrentUserName ()
	{
		return FM.getCurrentUserName ();
	}

	/**
	 * Initializes properties by reading from properties file
	 * 
	 * @return true
	 */
	private Boolean setProperties ()
	{
		ArrayList<String> text = new ArrayList<String> ();
		try
		{
			FileInputStream fis = new FileInputStream (propertiesFilePath);
			DataInputStream dis = new DataInputStream (fis);
			BufferedReader br = new BufferedReader (new InputStreamReader (dis));
			String strLine;
			while ((strLine = br.readLine ()) != null)
				text.add (strLine);
			dis.close ();

			/* Initially set defaults */
			FM.setProjectTitle ("Sehatmand Zindagi - Field Monitoring");
			FM.setDatabaseName ("tbr3_monitoring");
			FM.setReportingDatabase (FM.getDatabaseName () + "_rpt");
			FM.setSessionLimit (15 * 60 * 1000);
			FM.setHashingAlgorithm ("SHA");
			FM.setCurrentVersion ("1.0.0");

			for (String s : text)
			{
				if (s.startsWith ("#"))
					continue;
				String[] parts = s.split ("=");
				if (parts.length < 2)
					continue;
				if (parts[0].equals ("resources_path"))
					FM.setResourcesPath (applicationPath);
				else if (parts[0].equals ("current_version"))
					FM.setCurrentVersion (parts[1]);
				else if (parts[0].equals ("project_title"))
					FM.setProjectTitle (parts[1]);
				else if (parts[0].equals ("database_name"))
					FM.setDatabaseName (parts[1]);
				else if (parts[0].equals ("data_warehouse_name"))
					FM.setReportingDatabase (parts[1]);
				else if (parts[0].equals ("reports_directory_name"))
					FM.setReportsDirectoryName (parts[1]);
				else if (parts[0].equals ("session_limit"))
					FM.setSessionLimit (Integer.parseInt (parts[1]) * 1000);
				else if (parts[0].equals ("hashing_algorithm"))
					FM.setHashingAlgorithm (parts[1]);
			}
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace ();
			System.out.print ("Unable to read properties file. Make sure that <" + propertiesFilePath + "> exists in the Application root directory and is accessible.");
			System.exit (-1);
			return false;
		}
	}

	public String[][] getSchema ()
	{
		Object[][] data = HibernateUtil.util.selectData ("select TABLE_NAME, COLUMN_NAME, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH from information_schema.columns where TABLE_SCHEMA = '"
				+ FM.getDatabaseName () + "'");
		String[][] schema = new String[data.length][];
		for (int i = 0; i < data.length; i++)
		{
			schema[i] = new String[data[i].length];
			for (int j = 0; j < data[i].length; j++)
			{
				if (data[i][j] == null)
					data[i][j] = 0;
				schema[i][j] = data[i][j].toString ();
			}
		}
		return schema;
	}

	/**
	 * Get default values to be used on front-ends
	 */
	public Defaults[] getDefaults ()
	{
		Object[] objs = HibernateUtil.util.findObjects ("from Defaults");
		Defaults[] defaults = new Defaults[objs.length];
		for (int i = 0; i < objs.length; i++)
		{
			Defaults def = (Defaults) objs[i];
			defaults[i] = def;
		}
		return defaults;
	}

	/**
	 * Get all definitions for static data
	 */
	public Definition[] getDefinitions ()
	{
		Object[] objs = HibernateUtil.util.findObjects ("from Definition");
		Definition[] definitions = new Definition[objs.length];
		for (int i = 0; i < objs.length; i++)
		{
			Definition def = (Definition) objs[i];
			definitions[i] = def;
		}
		return definitions;
	}

	public String getObject (String tableName, String columnName, String filter)
	{
		return HibernateUtil.util.selectObject ("select " + columnName + " from " + tableName + arrangeFilter (filter)).toString ();
	}

	public String[] getQueriesResults (String[] queries)
	{
		String[] results = new String[queries.length];
		for (int i = 0; i < results.length; i++)
		{
			try
			{
				results[i] = HibernateUtil.util.selectObject (queries[i]).toString ();
			}
			catch (Exception e)
			{
				results[i] = "";
				e.printStackTrace ();
			}
		}
		return results;
	}

	public String[] getDumpFiles ()
	{
		ArrayList<String> files = new ArrayList<String> ();
		File folder = new File (FM.getResourcesPath ());
		for (File f : folder.listFiles ())
		{
			if (f.isFile ())
			{
				String file = f.getPath ();
				if (file.endsWith (".zip") || file.endsWith (".ZIP"))
					files.add (file);
			}
		}
		Collections.sort (files);
		Collections.reverse (files);
		return files.toArray (new String[] {});
	}

//	public String[][] getReportsList ()
//	{
//		return ReportUtil.getReportList ();
//	}

	public String[] getRowRecord (String tableName, String[] columnNames, String filter)
	{
		return getTableData (tableName, columnNames, filter)[0];
	}

	public String getSecretQuestion (String userName)
	{
		User user = (User) HibernateUtil.util.findObject ("from User where userName='" + userName + "'");
		return user.getSecretQuestion ();
	}

	@SuppressWarnings("deprecation")
	public String getSnapshotTime ()
	{
		Date dt = new Date ();
		Object obj = HibernateUtil.util.selectObject ("select max(date_end) from encounter where date(date_end) < '" + (dt.getYear () + 1900) + "-" + (dt.getMonth () + 1) + "-" + dt.getDate () + "'");
		return obj.toString ();
	}

	public String[][] getTableData (String tableName, String[] columnNames, String filter)
	{
		StringBuilder columnList = new StringBuilder ();
		for (String s : columnNames)
		{
			columnList.append (s);
			columnList.append (",");
		}
		columnList.deleteCharAt (columnList.length () - 1);
		String query = "select " + columnList.toString () + " from " + tableName + " " + arrangeFilter (filter);
		return getTableData (query);
	}

	public String[][] getTableData (String sqlQuery)
	{
		Object[][] data = HibernateUtil.util.selectData (sqlQuery);
		String[][] stringData = new String[data.length][];
		for (int i = 0; i < data.length; i++)
		{
			stringData[i] = new String[data[i].length];
			for (int j = 0; j < stringData[i].length; j++)
			{
				if (data[i][j] == null)
					data[i][j] = "";
				String str = data[i][j].toString ();
				stringData[i][j] = str;
			}
		}
		return stringData;
	}

	public Boolean[] getUserRgihts (String userName, String role, String menuName)
	{
		if (role.equalsIgnoreCase ("ADMIN"))
		{
			Boolean[] rights = {true, true, true, true, true};
			return rights;
		}
		UserRights userRights = (UserRights) HibernateUtil.util.findObject ("from UserRights where id.userRole='" + role + "' and id.menuName='" + menuName + "'");
		Boolean[] rights = {userRights.isSearchAccess (), userRights.isInsertAccess (), userRights.isUpdateAccess (), userRights.isDeleteAccess (), userRights.isPrintAccess ()};
		return rights;
	}

	public void recordLogin (String userName)
	{
		User user = (User) HibernateUtil.util.findObject ("from User where userName='" + userName + "'");
		HibernateUtil.util.recordLog (LogType.LOGIN, user);
		user.setLoggedIn (true);
		HibernateUtil.util.update (user);
	}

	/**
	 * Update User's Login Status to false and save logout date to last login
	 * record
	 */
	public void recordLogout (String userName)
	{
		User user = (User) HibernateUtil.util.findObject ("from User where userName='" + userName + "'");
		String selectQuery = "select max(login_no) from log_login where user_name='" + userName + "'";
		int num = Integer.parseInt (HibernateUtil.util.selectObject (selectQuery).toString ());
		String updateQuery = "update log_login set date_logout = '" + DateTimeUtil.getSQLDate (new Date ()) + "' where login_no = " + num + "";
		HibernateUtil.util.runCommand (updateQuery);
		user.setLoggedIn (false);
		HibernateUtil.util.update (user);
	}

	public int execute (String query)
	{
		return HibernateUtil.util.runCommand (query);
	}

	public Boolean execute (String[] queries)
	{
		for (String s : queries)
		{
			boolean result = execute (s) >= 0;
			if (!result)
				return false;
		}
		return true;
	}

	public Boolean executeProcedure (String procedure)
	{
		return HibernateUtil.util.runProcedure (procedure);
	}

	/* Delete methods */
	public Boolean deleteDefinition (Definition definition)
	{
		return HibernateUtil.util.delete (definition);
	}

	public Boolean deleteDictionary (Dictionary dictionary)
	{
		return HibernateUtil.util.delete (dictionary);
	}

	public Boolean deleteEncounter (Encounter encounter)
	{
		boolean result;
		// Delete Encounter Results
		HibernateUtil.util.findObjects ("from EncounterResults where id.eId='" + encounter.getId ().getEId () + "' and id.pid1='" + encounter.getId ().getPid1 () + "' and id.pid2='"
				+ encounter.getId ().getPid2 () + "'");
		// Delete Encounter
		result = HibernateUtil.util.delete (encounter);
		return result;
	}

	public Boolean deleteEncounterElement (EncounterElement encounterElement)
	{
		Boolean result = false;
		if (!exists ("encounter_value", "encounter_type='" + encounterElement.getId ().getEncounterType () + "' and element='" + encounterElement.getId ().getElement () + "'"))
			result = HibernateUtil.util.delete (encounterElement);
		return result;
	}

	public Boolean deleteEncounterPrerequisite (EncounterPrerequisite encounterPrerequisite)
	{
		return HibernateUtil.util.delete (encounterPrerequisite);
	}

	public Boolean deleteEncounterResults (EncounterResults encounterResults)
	{
		return HibernateUtil.util.delete (encounterResults);
	}

	public Boolean deleteEncounterType (EncounterType encounterType)
	{
		Boolean result = false;
		if (!exists ("encounter_element", "encounter_type='" + encounterType.getEncounterType () + "'"))
			result = HibernateUtil.util.delete (encounterType);
		return result;
	}

	public Boolean deleteEncounterValue (EncounterValue encounterValue)
	{
		Boolean result = false;
		if (!exists ("encounter_results", "encounter_type='" + encounterValue.getId ().getEncounterType () + "' and element='" + encounterValue.getId ().getElement () + "' and value='"
				+ encounterValue.getId ().getValue () + "'"))
			result = HibernateUtil.util.delete (encounterValue);
		return result;
	}

	public Boolean deleteFeedback (Feedback feedback)
	{
		return HibernateUtil.util.delete (feedback);
	}

	public Boolean deleteLabTest (LabTest labTest)
	{
		return HibernateUtil.util.delete (labTest);
	}

	public Boolean deleteLocation (Location location)
	{
		return HibernateUtil.util.delete (location);
	}

	public Boolean deletePatient (Patient patient)
	{
		return HibernateUtil.util.delete (patient);
	}

	public Boolean deletePerson (Person person)
	{
		return HibernateUtil.util.delete (person);
	}

	@Override
	public Boolean deletePersonRole (PersonRole personRole)
	{
		return HibernateUtil.util.delete (personRole);
	}

	public Boolean deleteUser (User user)
	{
		// Delete any roles
		PersonRole[] personRoles = findPersonRoles (user.getPid ());
		for (PersonRole r : personRoles)
			HibernateUtil.util.delete (r);
		// Delete any mappings
		UserMapping[] userMappings = findUserMappingsByUser (user.getPid ());
		for (UserMapping u : userMappings)
			HibernateUtil.util.delete (u);
		return HibernateUtil.util.delete (user);
	}

	public Boolean deleteUserMapping (UserMapping userMapping)
	{
		return HibernateUtil.util.delete (userMapping);
	}

	public Boolean deleteUserRights (UserRights userRights)
	{
		return HibernateUtil.util.delete (userRights);
	}

	/* Find methods */
	public Definition findDefinition (String definitionType, String definitionKey)
	{
		Definition definition = (Definition) HibernateUtil.util.findObject ("from Definition as def where def.id.definitionType='" + definitionType + "' and def.id.definitionKey='" + definitionKey
				+ "'");
		return definition;
	}

	public Dictionary findDictionary (String term)
	{
		Dictionary dictionary = (Dictionary) HibernateUtil.util.findObject ("from Dictionary where term='" + term + "'");
		return dictionary;
	}

	public Encounter findEncounter (EncounterId encounterID)
	{
		Encounter encounter = (Encounter) HibernateUtil.util.findObject ("from Encounter where id.pid1='" + encounterID.getPid1 () + "' and id.pid2='" + encounterID.getPid2 () + "' and id.encounterType='" + encounterID.getEncounterType () + "' and id.EId='"
				+ encounterID.getEId () + "'");
		return encounter;
	}

	public EncounterElement findEncounterElement (String encounterType, String element)
	{
		EncounterElement eElement = (EncounterElement) HibernateUtil.util.findObject ("from EncounterElement where id.encounterType='" + encounterType + "' and id.element='" + element + "'");
		return eElement;
	}

	public EncounterElement[] findEncounterElements (String encounterType)
	{
		Object[] objects = HibernateUtil.util.findObjects ("from EncounterElement where id.encounterType='" + encounterType + "'");
		EncounterElement[] eElements = new EncounterElement[objects.length];
		for (int i = 0; i < eElements.length; i++)
			eElements[i] = (EncounterElement) objects[i];
		return eElements;
	}

	public EncounterPrerequisite findEncounterPrerequisite (EncounterPrerequisiteId prerequisiteId)
	{
		EncounterPrerequisite prereq = (EncounterPrerequisite) HibernateUtil.util.findObject ("from EncounterPrerequisite where id.encounterType='" + prerequisiteId.getEncounterType ()
				+ "' and id.prerequisiteNo=" + prerequisiteId.getPrerequisiteNo () + "");
		return prereq;
	}

	public EncounterPrerequisite[] findEncounterPrerequisites (EncounterType encounterType)
	{
		Object[] objects = HibernateUtil.util.findObjects ("from EncounterPrerequisite where id.encounterType='" + encounterType.getEncounterType () + "'");
		EncounterPrerequisite[] prereqs = new EncounterPrerequisite[objects.length];
		for (int i = 0; i < objects.length; i++)
			prereqs[i] = (EncounterPrerequisite) objects[i];
		return prereqs;
	}

	public EncounterResults[] findEncounterResults (EncounterId encounterId)
	{
		Object[] objects = HibernateUtil.util.findObjects ("from EncounterResults where id.pid1='" + encounterId.getPid1 () + "' and id.pid2='" + encounterId.getPid2 () + "' and id.encounterType='"
				+ encounterId.getEncounterType () + "' and id.EId='" + encounterId.getEId () + "'");
		EncounterResults[] eResults = new EncounterResults[objects.length];
		for (int i = 0; i < eResults.length; i++)
			eResults[i] = (EncounterResults) objects[i];
		return eResults;
	}

	public EncounterResults findEncounterResultsByElement (EncounterResultsId encounterResultsID)
	{
		EncounterResults eResults = (EncounterResults) HibernateUtil.util.findObject ("from EncounterResults where pid1='" + encounterResultsID.getPid1 () + "' and pid2='"
				+ encounterResultsID.getPid2 () + "' and eid='" + encounterResultsID.getEId () + "' and element='" + encounterResultsID.getElement () + "'");
		return eResults;
	}

	public EncounterType findEncounterType (String encounterType)
	{
		EncounterType eType = (EncounterType) HibernateUtil.util.findObject ("from EncounterType where encounterType='" + encounterType + "'");
		return eType;
	}

	public EncounterValue findEncounterValue (String encounterType, String element, String value)
	{
		EncounterValue eValue = (EncounterValue) HibernateUtil.util.findObject ("from EncounterValue where id.encounterType='" + encounterType + "' and id.element='" + element + "' and id.value='"
				+ value + "'");
		return eValue;
	}

	public LabTest[] findLabTests (String patientId)
	{
		Object[] tests = HibernateUtil.util.findObjects ("from LabTest where patientId='" + patientId + "'");
		LabTest[] labTests = new LabTest[tests.length];
		for (int i = 0; i < tests.length; i++)
			labTests[i] = (LabTest) tests[i];
		return labTests;
	}

	public Location findLocation (String locationID)
	{
		Location location = (Location) HibernateUtil.util.findObject ("from Location where locationId='" + locationID + "'");
		return location;
	}

	public Location[] findLocationsByType (String locationType)
	{
		Object[] list = HibernateUtil.util.findObjects ("from Location where locationType='" + locationType + "'");
		Location[] locations = new Location[list.length];
		for (int i = 0; i < list.length; i++)
			locations[i] = (Location) list[i];
		return locations;
	}

	public Patient findPatient (String patientID)
	{
		Patient patient = (Patient) HibernateUtil.util.findObject ("from Patient where patientId='" + patientID + "'");
		return patient;
	}

	public Patient findPatientByMR (String Mrno)
	{
		Patient patient = (Patient) HibernateUtil.util.findObject ("from Patient where mrNo='" + Mrno + "'");
		return patient;
	}

	public Person findPerson (String PID)
	{
		Person person = (Person) HibernateUtil.util.findObject ("from Person where pid='" + PID + "'");
		return person;
	}

	public PersonRole[] findPersonRoles (String pid)
	{
		Object[] objs = HibernateUtil.util.findObjects ("from PersonRole where pid='" + pid + "'");
		PersonRole[] roles = new PersonRole[objs.length];
		for (int i = 0; i < objs.length; i++)
			roles[i] = (PersonRole) objs[i];
		return roles;
	}

	public Person[] findPersonsByName (String firstName, String lastName)
	{
		Object[] objs = HibernateUtil.util.findObjects ("from Person where firstName like '" + firstName + "%' and lastName like '" + lastName + "%'");
		Person[] persons = new Person[objs.length];
		for (int i = 0; i < objs.length; i++)
			persons[i] = (Person) objs[i];
		return persons;
	}

	public Person findPersonsByNIC (String NIC)
	{
		Person person = (Person) HibernateUtil.util.findObject ("from Person where NIC='" + NIC + "'");
		return person;
	}

	public User findUser (String userName)
	{
		User user = (User) HibernateUtil.util.findObject ("from User where userName='" + userName + "'");
		return user;
	}

	public UserMapping findUserMapping (UserMappingId userMappingId)
	{
		UserMapping mapping = (UserMapping) HibernateUtil.util.findObject ("from UserMapping where id.userId='" + userMappingId.getUserId () + "' and id.locationId='"
				+ userMappingId.getLocationId () + "'");
		return mapping;
	}

	public UserMapping[] findUserMappingsByUser (String userId)
	{
		Object[] objs = HibernateUtil.util.findObjects ("from UserMapping where id.userId='" + userId + "'");
		UserMapping[] userMappings = new UserMapping[objs.length];
		for (int i = 0; i < objs.length; i++)
			userMappings[i] = (UserMapping) objs[i];
		return userMappings;
	}

	public UserRights findUserRights (String roleName, String tableName)
	{
		return UserAuthentication.getRights (roleName, tableName);
	}

	/* Save methods */
	public Boolean saveDefinition (Definition definition)
	{
		return HibernateUtil.util.save (definition);
	}

	public Boolean saveDictionary (Dictionary dictionary)
	{
		return HibernateUtil.util.save (dictionary);
	}

	public Boolean saveEncounter (Encounter encounter)
	{
		// Get the max encounter ID and add 1
		EncounterId currentID = encounter.getId ();
		Object[] max = HibernateUtil.util.selectObjects ("select max(e_id) from encounter where pid1='" + currentID.getPid1 () + "' and pid2='" + currentID.getPid2 () + "' and encounter_type='"
				+ currentID.getEncounterType () + "'");

		Integer maxInt = (Integer) max[0];
		if (maxInt == null)
			currentID.setEId (1);
		else
			currentID.setEId ((maxInt.intValue () + 1));
		encounter.setId (currentID);
		return HibernateUtil.util.save (encounter);
	}

	public Boolean saveEncounterElement (EncounterElement element)
	{
		return HibernateUtil.util.save (element);
	}

	public Boolean saveEncounterPrerequisite (EncounterPrerequisite encounterPrerequisite)
	{
		// Get next Pre-requisite no. for person
		Long prerequisiteNo = Long.parseLong (getObject ("encounter_prerequisite", "ifnull(max(prerequisite_no), 0)", "encounter_type='" + encounterPrerequisite.getId ().getEncounterType () + "'")
				.toString ());
		prerequisiteNo++;
		encounterPrerequisite.getId ().setPrerequisiteNo (prerequisiteNo.intValue ());
		return HibernateUtil.util.save (encounterPrerequisite);
	}

	public Boolean saveEncounterResults (EncounterResults encounterResults)
	{
		// Validate value if bounded by a validation
		EncounterElement elem = findEncounterElement (encounterResults.getId ().getEncounterType (), encounterResults.getId ().getElement ());
		String regex = elem.getValidator ();
		if (!regex.equals ("") && !encounterResults.getValue ().matches (regex))
			return null;
		return HibernateUtil.util.save (encounterResults);
	}

	public Boolean saveEncounterType (EncounterType encounterType)
	{
		return HibernateUtil.util.save (encounterType);
	}

	public Boolean saveEncounterValue (EncounterValue encounterValue)
	{
		return HibernateUtil.util.save (encounterValue);
	}

	/**
	 * Saves Encounter with Results. If the Pre-requisites are not satisfied or
	 * encounter values are not valid then nothing will be saved.
	 */
	public String saveEncounterWithResults (Encounter encounter, EncounterResults[] encounterResults)
	{
		String result = "";
		// Check for Pre-Requisites
		EncounterType encounterType = findEncounterType (encounter.getId ().getEncounterType ());
		EncounterPrerequisite[] prereqs = findEncounterPrerequisites (encounterType);
		for (EncounterPrerequisite pr : prereqs)
		{
			EncounterResults prereqResult = (EncounterResults) HibernateUtil.util.findObject ("from EncounterResults where id.pid1='" + encounter.getId ().getPid1 () + "' and id.encounterType='"
					+ pr.getPrerequisiteEncounter () + "' and id.element='" + pr.getConditionElement () + "'");
			if (prereqResult == null)
			{
				result = "Pre-requisite form " + pr.getPrerequisiteEncounter () + " was not found.";
				return result;
			}
			else if (!prereqResult.getValue ().matches (pr.getPossibleValueRegex ()))
			{
				result = "Pre-requisite was not satisfied. To submit this form, there must be a(n) " + pr.getPrerequisiteEncounter () + " form filled with value of question "
						+ pr.getConditionElement () + " like " + pr.getPossibleValueRegex () + "\n";
				return result;
			}
		}
		// Get the max encounter ID and add 1
		EncounterId currentID = encounter.getId ();
		Object[] max = HibernateUtil.util.selectObjects ("select max(e_id) from encounter where pid1='" + currentID.getPid1 () + "' and pid2='" + currentID.getPid2 () + "' and encounter_type='"
				+ currentID.getEncounterType () + "'");
		Integer maxInt = (Integer) max[0];
		if (maxInt == null)
			currentID.setEId (1);
		else
			currentID.setEId ((maxInt.intValue () + 1));
		encounter.setId (currentID);
		// Validate values of results if bounded by a validation
		for (EncounterResults er : encounterResults)
		{
			er.getId ().setEId (encounter.getId ().getEId ());
			EncounterElement elem = findEncounterElement (er.getId ().getEncounterType (), er.getId ().getElement ());
			String regex = elem.getValidator ();
			if (regex == null)
				continue;
			if (!regex.equals ("") && !er.getValue ().matches (regex))
			{
				result = "Invalid value provided for question: " + elem.getId ().getElement () + " (" + elem.getDescription () + ")\n";
				return result;
			}
		}
		// Save Encounter
		if (saveEncounter (encounter))
		{
			// Save Encounter Results
			for (EncounterResults er : encounterResults) //must check whether the data already exist (primary key issue)
				saveEncounterResults (er);
			result = "SUCCESS";
		}
		else
			result = "Could not save Form.";
		return result;
	}

	public Boolean saveFeedback (Feedback feedback)
	{
		SMSUtil.util.sendAlertsOnFeedback (feedback);
		return HibernateUtil.util.save (feedback);
	}

	public Boolean saveLabTest (LabTest labTest)
	{
		return HibernateUtil.util.save (labTest);
	}

	public Boolean saveLocation (Location location)
	{
		if (exists ("location", "location_name='" + location.getLocationName () + "'"))
			return null;
		return HibernateUtil.util.save (location);
	}

	public Boolean savePatient (Patient patient)
	{
		return HibernateUtil.util.save (patient);
	}

	public Boolean savePerson (Person person)
	{
		return HibernateUtil.util.save (person);
	}

	public Boolean savePersonRole (PersonRole personRole)
	{
		return HibernateUtil.util.save (personRole);
	}

	public Boolean saveUser (User user, String[] roles)
	{
		Boolean result;
		user.setPassword (MDHashUtil.getHashString (user.getPassword ()));
		user.setSecretAnswer (MDHashUtil.getHashString (user.getSecretAnswer ()));
		if (exists ("user", "user_name='" + user.getUserName () + "'"))
			result = null;
		else
		{
			// Create a person first
			Person person = new Person (user.getPid (), user.getUserName ());
			result = HibernateUtil.util.save (person);
			if (result)
			{
				result = HibernateUtil.util.save (user);
				// Create roles
				for (String s : roles)
				{
					PersonRole role = new PersonRole (new PersonRoleId (user.getPid (), s));
					result = HibernateUtil.util.save (role);
				}
			}
			else
			{
				HibernateUtil.util.delete (person);
				HibernateUtil.util.delete (user);
			}
		}
		return result;
	}

	public Boolean saveUserMapping (UserMapping userMapping)
	{
		return HibernateUtil.util.save (userMapping);
	}

	public Boolean saveUserRights (UserRights userRights)
	{
		return HibernateUtil.util.save (userRights);
	}

	/* Update methods */
	public Boolean updateDefaults (Defaults[] defaults)
	{
		Boolean result = true;
		for (Defaults def : defaults)
		{
			if (def != null)
			{
				result = HibernateUtil.util.delete (def);
				result = HibernateUtil.util.save (def);
			}
		}
		return result;
	}

	public Boolean updateDefinition (Definition definition)
	{
		return HibernateUtil.util.update (definition);
	}

	public Boolean updateDictionary (Dictionary dictionary)
	{
		return HibernateUtil.util.update (dictionary);
	}

	public Boolean updateEncounter (Encounter encounter)
	{
		return HibernateUtil.util.update (encounter);
	}

	public Boolean updateEncounterElement (EncounterElement element)
	{
		return HibernateUtil.util.update (element);
	}

	public Boolean updateEncounterPrerequisite (EncounterPrerequisite prerequisite)
	{
		return HibernateUtil.util.update (prerequisite);
	}

	public Boolean updateEncounterResults (EncounterResults encounterResults)
	{
		return HibernateUtil.util.update (encounterResults);
	}

	public Boolean updateEncounterType (EncounterType encounterType)
	{
		return HibernateUtil.util.update (encounterType);
	}

	public Boolean updateEncounterValue (EncounterValue encounterValue)
	{
		return HibernateUtil.util.update (encounterValue);
	}

	public Boolean updateEncounterWithResults (Encounter encounter, EncounterResults[] encounterResults) throws Exception
	{
		Boolean result = null;
		// Validate values of results if bounded by a validation
		for (EncounterResults er : encounterResults)
		{
			er.getId ().setEId (encounter.getId ().getEId ());
			EncounterElement elem = findEncounterElement (er.getId ().getEncounterType (), er.getId ().getElement ());
			String regex = elem.getValidator ();
			if (regex == null)
				continue;
			if (!regex.equals ("") && !er.getValue ().matches (regex))
			{
				return null;
			}
		}
		// Save Encounter
		updateEncounter (encounter);
		// Save Encounter Results
		for (EncounterResults er : encounterResults)
		{
			deleteEncounterResults (er);
			result = saveEncounterResults (er);
		}
		return result;
	}

	public Boolean updateFeedback (Feedback feedback)
	{
		return HibernateUtil.util.update (feedback);
	}

	public Boolean updateLabTest (LabTest labTest)
	{
		return HibernateUtil.util.update (labTest);
	}

	public Boolean updateLocation (Location location)
	{
		return HibernateUtil.util.update (location);
	}

	public Boolean updatePassword (String userName, String newPassword)
	{
		User user = (User) HibernateUtil.util.findObject ("from User where userName = '" + userName + "'");
		user.setPassword (MDHashUtil.getHashString (newPassword));
		return HibernateUtil.util.update (user);
	}

	public Boolean updatePatient (Patient patient)
	{
		return HibernateUtil.util.update (patient);
	}

	public Boolean updatePerson (Person person)
	{
		return HibernateUtil.util.update (person);
	}

	public Boolean updatePersonRole (PersonRole personRole)
	{
		return HibernateUtil.util.update (personRole);
	}

	public Boolean updateUser (User user, String[] roles)
	{
		// Delete existing roles
		PersonRole[] existingRoles = findPersonRoles (user.getPid ());
		for (PersonRole r : existingRoles)
			HibernateUtil.util.delete (r);
		// Create new roles
		existingRoles = new PersonRole[roles.length];
		for (int i = 0; i < roles.length; i++)
		{
			PersonRole personRole = new PersonRole (new PersonRoleId (user.getPid (), roles[i]));
			HibernateUtil.util.save (personRole);
		}
		// Update User
		return HibernateUtil.util.update (user);
	}

	public Boolean updateUserMapping (UserMapping userMapping)
	{
		return HibernateUtil.util.update (userMapping);
	}

	public Boolean updateUserRights (UserRights userRights)
	{
		return HibernateUtil.util.update (userRights);
	}

	public Boolean undoFormRequest (String userName, String encounterType)
	{
		String maxDate = HibernateUtil.util.selectObject (
				"select max(date_end) from encounter where encounter_type = '" + encounterType + "' and pid2 = (select pid from user where user_name = '" + userName + "')").toString ();
		if (maxDate == null)
			return false;
		String updateQuery = "update encounter set description = 'Marked to Undo on " + DateTimeUtil.getSQLDate (new Date ()) + " by " + userName + "' where encounter_type = '" + encounterType
				+ "' and pid2 = (select pid from user where user_name = '" + userName + "') and date_end = '" + maxDate + "'";
		return HibernateUtil.util.runCommand (updateQuery) == 1;
	}
}