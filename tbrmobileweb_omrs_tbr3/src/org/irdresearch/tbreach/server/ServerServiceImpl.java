package org.irdresearch.tbreach.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//import org.irdresearch.tbreach.client.ServerService;
import org.irdresearch.tbreach.shared.ListType;
import org.irdresearch.tbreach.shared.Parameter;
import org.irdresearch.tbreach.shared.TBR;
import org.irdresearch.tbreach.shared.model.Contact;
import org.irdresearch.tbreach.shared.model.ContactSputumResults;
import org.irdresearch.tbreach.shared.model.Dictionary;
import org.irdresearch.tbreach.shared.model.DrugHistory;
import org.irdresearch.tbreach.shared.model.DrugHistoryId;
import org.irdresearch.tbreach.shared.model.Encounter;
import org.irdresearch.tbreach.shared.model.EncounterId;
import org.irdresearch.tbreach.shared.model.EncounterResults;
import org.irdresearch.tbreach.shared.model.EncounterResultsId;
import org.irdresearch.tbreach.shared.model.Feedback;
import org.irdresearch.tbreach.shared.model.GeneXpertResults;
import org.irdresearch.tbreach.shared.model.Gp;
import org.irdresearch.tbreach.shared.model.GpMapping;
import org.irdresearch.tbreach.shared.model.GpMappingId;
import org.irdresearch.tbreach.shared.model.Household;
import org.irdresearch.tbreach.shared.model.HouseholdId;
import org.irdresearch.tbreach.shared.model.Incentive;
import org.irdresearch.tbreach.shared.model.IncentiveId;
import org.irdresearch.tbreach.shared.model.Location;
import org.irdresearch.tbreach.shared.model.MessageSettings;
import org.irdresearch.tbreach.shared.model.Monitor;
import org.irdresearch.tbreach.shared.model.NonSuspect;
import org.irdresearch.tbreach.shared.model.Patient;
import org.irdresearch.tbreach.shared.model.PatientDOTS;
import org.irdresearch.tbreach.shared.model.Person;
import org.irdresearch.tbreach.shared.model.Relationship;
import org.irdresearch.tbreach.shared.model.RelationshipId;
import org.irdresearch.tbreach.shared.model.Screening;
import org.irdresearch.tbreach.shared.model.SetupCity;
import org.irdresearch.tbreach.shared.model.SetupCountry;
import org.irdresearch.tbreach.shared.model.SetupIncentive;
import org.irdresearch.tbreach.shared.model.Sms;
import org.irdresearch.tbreach.shared.model.SputumResults;
import org.irdresearch.tbreach.shared.model.Supervisor;
import org.irdresearch.tbreach.shared.model.TreatmentRefusal;
import org.irdresearch.tbreach.shared.model.UserRights;
import org.irdresearch.tbreach.shared.model.Users;
import org.irdresearch.tbreach.shared.model.Worker;
import org.irdresearch.tbreach.shared.model.XrayResults;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 * 
 * @author owais.hussain@irdresearch.org
 */
@SuppressWarnings("serial")
public class ServerServiceImpl //extends RemoteServiceServlet implements ServerService
{
	/**
	 * Get full name (first name + middle name + last name + surname) of any Person
	 * @param Person ID as String
	 * @return full name as String
	 */
	public String getFullName(String PID) throws Exception
	{
		if (PID.equals(""))
			return "";
		return HibernateUtil.util
				.selectObject(
						"select LTRIM(RTRIM(IFNULL(FirstName, '') + ' ' + IFNULL(MiddleName, '') + IFNULL(LastName, '') + IFNULL(Surname, ''))) from Person where PID='"
								+ PID + "'").toString().toUpperCase();
	}

	private String arrangeFilter(String filter) throws Exception
	{
		if (filter.trim().equalsIgnoreCase(""))
			return "";
		return (filter.toUpperCase().contains("WHERE") ? "" : " where ") + filter;
	}

	/**
	 * Get Mobile phone number of any Person
	 * @param Person ID as String
	 * @return Mobile number as String
	 */
	public String getMobileNumber(String PID) throws Exception
	{
		if (PID.equals(""))
			return "";
		return HibernateUtil.util.selectObject("select Mobile from Contact where PID='" + PID + "'").toString();
	}

	/**
	 * Sends a generic SMS
	 * @param sms
	 */
	
	public void sendGenericSMSAlert(Sms[] sms)
	{
		for (Sms s : sms)
			sendGenericSMSAlert(s);
	}

	/**
	 * Sends a generic SMS
	 * @param sms
	 */
	
	public void sendGenericSMSAlert(Sms sms)
	{
		if (!sms.getTargetNumber().equals(""))
			HibernateUtil.util.save(sms);
	}

	/**
	 * User authentication:
	 * Checks whether user exists, then match his password
	 * @return Boolean
	 */
	
	public Boolean authenticate(String userName, String password) throws Exception
	{
		if (!UserAuthentication.userExsists(userName))
			return false;
		else if (!UserAuthentication.validatePassword(userName, password))
			return false;
		TBR.setCurrentUser(userName.toUpperCase());
		return true;
	}

	/**
	 * Checks if a user exists in the database
	 * @return Boolean
	 */
	
	public Boolean authenticateUser(String userName) throws Exception
	{
		if (!UserAuthentication.userExsists(userName))
			return false;
		return true;
	}

	/**
	 * Verifies secret answer against stored secret question
	 * @return Boolean
	 */
	
	public Boolean verifySecretAnswer(String userName, String secretAnswer) throws Exception
	{
		if (!UserAuthentication.validateSecretAnswer(userName, secretAnswer))
			return false;
		return true;
	}

	/**
	 * Get number of records in a table, given appropriate filter
	 * @return Long
	 */
	
	public Long count(String tableName, String filter) throws Exception
	{
		Object obj = HibernateUtil.util.selectObject("select count(*) from " + tableName + " " + arrangeFilter(filter));
		return Long.parseLong(obj.toString());
	}

	/**
	 * Checks existence of data by counting number of records in a table, given appropriate filter
	 * @return Boolean
	 */
	
	public Boolean exists(String tableName, String filter) throws Exception
	{
		long count = count(tableName, filter);
		return count > 0;
	}

	/**
	 * Generates CSV file from query passed along with the filters
	 * @param query
	 * @return
	 */
	
	public String generateCSVfromQuery(String database, String query) throws Exception
	{
		return ReportUtil.generateCSVfromQuery(database, query, ',');
	}

	/**
	 * Generate report on server side and return the path it was created to
	 * @param
	 * 		Path of report as String
	 * 		Report parameters as Parameter[]
	 * 		Report to be exported in csv format as Boolean
	 * @return String
	 */
	
	/*public String generateReport(String fileName, Parameter[] params, boolean export) throws Exception
	{
		return ReportUtil.generateReport(fileName, params, export);
	}

	*//**
	 * Generate report on server side based on the query saved in the Database against the reportName and return the path it was created to
	 * @param reportName
	 * @param params
	 * @param export
	 * @return 
	 *//*
	
	public String generateReportFromQuery(String database, String reportName, String query, Boolean export) throws Exception
	{
		return ReportUtil.generateReportFromQuery(database, reportName, query, export);
	}*/

	
	public String[] getColumnData(String tableName, String columnName, String filter) throws Exception
	{
		Object[] data = HibernateUtil.util.selectObjects("select distinct " + columnName + " from " + tableName + " " + arrangeFilter(filter));
		String[] columnData = new String[data.length];
		for (int i = 0; i < data.length; i++)
			columnData[i] = data[i].toString();
		return columnData;
	}

	
	public String getCurrentUser() throws Exception
	{
		return TBR.getCurrentUser();
	}

	
	public String[] getDumpFiles() throws Exception
	{
		ArrayList<String> files = new ArrayList<String>();
		File folder = new File(TBR.getResourcesPath());
		for (File f : folder.listFiles())
		{
			if (f.isFile())
			{
				String file = f.getPath();
				if (file.endsWith(".zip") || file.endsWith(".ZIP"))
					files.add(file);
			}
		}
		Collections.sort(files);
		Collections.reverse(files);
		return files.toArray(new String[] {});
	}

	
	public String[][] getLists() throws Exception
	{
		String[][] lists = null;
		ArrayList<ArrayList<String>> arrayList = new ArrayList<ArrayList<String>>();
		try
		{
			DocumentBuilderFactory buildFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = buildFactory.newDocumentBuilder();
			File file = new File(TBR.getStaticFilePath());
			Document doc = documentBuilder.parse(file);
			Element docElement = doc.getDocumentElement();
			for (ListType type : ListType.values())
			{
				ArrayList<String> array = new ArrayList<String>();
				NodeList list = docElement.getElementsByTagName(type.toString());
				if (list != null)
				{
					for (int i = 0; i < list.getLength(); i++)
					{
						Node node = list.item(i);
						NodeList children;
						if (node.getNodeType() == Node.ELEMENT_NODE)
						{
							children = node.getChildNodes();
							if (children.getLength() > 0)
							{
								for (int j = 0; j < children.getLength(); j++)
								{
									NodeList items = children.item(j).getChildNodes();
									for (int k = 0; k < items.getLength(); k++)
									{
										String str = items.item(k).getTextContent();
										array.add(str);
									}
								}
							}
						}
					}
				}
				arrayList.add(array);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		lists = new String[arrayList.size()][];
		for (int i = 0; i < arrayList.size(); i++)
		{
			String[] str = new String[0];
			lists[i] = arrayList.get(i).toArray(str);
		}
		return lists;
	}

	
	public String getObject(String tableName, String columnName, String filter) throws Exception
	{
		return HibernateUtil.util.selectObject("select " + columnName + " from " + tableName + arrangeFilter(filter)).toString();
	}

	
	public String[][] getReportsList() throws Exception
	{
		return ReportUtil.getReportList();
	}

	
	public String[] getRowRecord(String tableName, String[] columnNames, String filter) throws Exception
	{
		return getTableData(tableName, columnNames, filter)[0];
	}

	
	public String getSecretQuestion(String userName) throws Exception
	{
		Users user = (Users) HibernateUtil.util.findObject("from Users where UserName = '" + userName + "'");
		return user.getSecretQuestion();
	}

	@SuppressWarnings("deprecation")
	
	public String getSnapshotTime() throws Exception
	{
		Date dt = new Date();
		Object obj = HibernateUtil.util.selectObject("select Max(DateEncounterEnd) from tbreach_rpt.Encounter where DATE(DateEncounterEnd) < '"
				+ (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + "'");
		return obj.toString();
	}

	
	public String[][] getTableData(String tableName, String[] columnNames, String filter) throws Exception
	{
		StringBuilder columnList = new StringBuilder();
		for (String s : columnNames)
		{
			columnList.append(s);
			columnList.append(",");
		}
		columnList.deleteCharAt(columnList.length() - 1);

		Object[][] data = HibernateUtil.util.selectData("select " + columnList.toString() + " from " + tableName + " " + arrangeFilter(filter));
		String[][] stringData = new String[data.length][columnNames.length];
		for (int i = 0; i < data.length; i++)
		{
			for (int j = 0; j < columnNames.length; j++)
			{
				if (data[i][j] == null)
					data[i][j] = "";
				String str = data[i][j].toString();
				stringData[i][j] = str;
			}
		}
		return stringData;
	}

	
	public Boolean[] getUserRgihts(String userName, String menuName) throws Exception
	{
		String role = HibernateUtil.util.selectObject("select Role from Users where UserName='" + userName + "'").toString();
		if (role.equalsIgnoreCase("ADMIN"))
		{
			Boolean[] rights = { true, true, true, true, true };
			return rights;
		}
		UserRights userRights = (UserRights) HibernateUtil.util.findObject("from UserRights where Role='" + role + "' and MenuName='" + menuName
				+ "'");
		Boolean[] rights = { userRights.isSearchAccess(), userRights.isInsertAccess(), userRights.isUpdateAccess(), userRights.isDeleteAccess(),
				userRights.isPrintAccess() };
		return rights;
	}

	
	public void recordLogin(String userName) throws Exception
	{
		Users user = (Users) HibernateUtil.util.findObject("from Users where UserName = '" + userName + "'");
		HibernateUtil.util.recordLog(LogType.LOGIN, user);
	}

	
	public void recordLogout(String userName) throws Exception
	{
		Users user = (Users) HibernateUtil.util.findObject("from Users where UserName = '" + userName + "'");
		HibernateUtil.util.recordLog(LogType.LOGOUT, user);
	}

	
	public int execute(String query) throws Exception
	{
		return HibernateUtil.util.runCommand(query);
	}

	
	public Boolean execute(String[] queries) throws Exception
	{
		for (String s : queries)
		{
			boolean result = execute(s) >= 0;
			if (!result)
				return false;
		}
		return true;
	}

	
	public Boolean executeProcedure(String procedure) throws Exception
	{
		return HibernateUtil.util.runProcedure(procedure);
	}

	/* Delete methods */
	
	public Boolean deleteCity(SetupCity city) throws Exception
	{
		return HibernateUtil.util.delete(city);
	}

	
	public Boolean deleteContact(Contact contact) throws Exception
	{
		return HibernateUtil.util.delete(contact);
	}

	
	public Boolean deleteCountry(SetupCountry country) throws Exception
	{
		return HibernateUtil.util.delete(country);
	}

	
	public Boolean deleteDictionary(Dictionary dictionary) throws Exception
	{
		return HibernateUtil.util.delete(dictionary);
	}

	
	public Boolean deleteDrugHistory(DrugHistory drugHistory) throws Exception
	{
		return HibernateUtil.util.delete(drugHistory);
	}

	
	public Boolean deleteEncounter(Encounter encounter) throws Exception
	{
		return HibernateUtil.util.delete(encounter);
	}

	
	public Boolean deleteEncounterResults(EncounterResults encounterResults) throws Exception
	{
		return HibernateUtil.util.delete(encounterResults);
	}

	
	public Boolean deleteEncounterResults(EncounterResultsId encounterResultsId) throws Exception
	{
		boolean result = false;
		EncounterResults er = (EncounterResults) HibernateUtil.util.findObject("from EncounterResults where EncounterId="
				+ encounterResultsId.getEncounterId() + " and PID1='" + encounterResultsId.getPid1() + "' and PID2='" + encounterResultsId.getPid2()
				+ "' and Element='" + encounterResultsId.getElement() + "'");
		result = deleteEncounterResults(er);
		return result;
	}

	
	public Boolean deleteEncounterWithResults(Encounter encounter) throws Exception
	{
		boolean result = false;
		try
		{
			String[] elements = getColumnData("EncounterResults", "Element", "EncounterID=" + encounter.getId().getEncounterId() + " AND PID1='"
					+ encounter.getId().getPid1() + "' AND PID2='" + encounter.getId().getPid2() + "'");
			// Delete encounter results
			for (String s : elements)
			{
				EncounterResultsId id = new EncounterResultsId(encounter.getId().getEncounterId(), encounter.getId().getPid1(), encounter.getId()
						.getPid2(), s);
				result = deleteEncounterResults(id);
			}
			// Delete encounter
			result = deleteEncounter(encounter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	
	public Boolean deleteFeedback(Feedback feedback) throws Exception
	{
		return HibernateUtil.util.delete(feedback);
	}

	
	public Boolean deleteGeneXpertResults(GeneXpertResults geneXpertResults) throws Exception
	{
		return HibernateUtil.util.delete(geneXpertResults);
	}

	
	public Boolean deleteGP(Gp gp, Person person, Contact contact, Users user) throws Exception
	{
		return HibernateUtil.util.delete(user) && HibernateUtil.util.delete(gp) && HibernateUtil.util.delete(person)
				&& HibernateUtil.util.delete(contact);
	}

	
	public Boolean deleteHousehold(Household household) throws Exception
	{
		return HibernateUtil.util.delete(household);
	}

	
	public Boolean deleteIncentive(Incentive incentive) throws Exception
	{
		return HibernateUtil.util.delete(incentive);
	}

	
	public Boolean deleteIncentiveSetup(SetupIncentive incentive) throws Exception
	{
		return HibernateUtil.util.delete(incentive);
	}

	
	public Boolean deleteLocation(Location location) throws Exception
	{
		return HibernateUtil.util.delete(location);
	}

	
	public Boolean deleteMonitor(Monitor monitor, Person person, Contact contact, Users user) throws Exception
	{
		return HibernateUtil.util.delete(user) && HibernateUtil.util.delete(monitor) && HibernateUtil.util.delete(person)
				&& HibernateUtil.util.delete(contact);
	}

	
	public Boolean deleteNonSuspect(NonSuspect nonSuspect) throws Exception
	{
		return HibernateUtil.util.delete(nonSuspect);
	}

	
	public Boolean deleteScreening(Screening screening) throws Exception
	{
		return HibernateUtil.util.delete(screening);
	}

	
	public Boolean deletePatient(Patient patient) throws Exception
	{
		return HibernateUtil.util.delete(patient);
	}

	
	public Boolean deletePatientDOTS(PatientDOTS patientDOTS) throws Exception
	{
		return HibernateUtil.util.delete(patientDOTS);
	}

	
	public Boolean deletePerson(Person person) throws Exception
	{
		return HibernateUtil.util.delete(person);
	}

	
	public Boolean deleteRelationship(Relationship relationship) throws Exception
	{
		return HibernateUtil.util.delete(relationship);
	}

	
	public Boolean deleteSetupIncentive(SetupIncentive current) throws Exception
	{
		return HibernateUtil.util.delete(current);
	}

	
	public Boolean deleteSputumResults(SputumResults sputumResults) throws Exception
	{
		return HibernateUtil.util.delete(sputumResults);
	}

	
	public Boolean deleteTreatmentRefusal(TreatmentRefusal treatmentRefusal) throws Exception
	{
		return HibernateUtil.util.delete(treatmentRefusal);
	}

	
	public Boolean deleteUser(Users user) throws Exception
	{
		return HibernateUtil.util.delete(user);
	}

	
	public Boolean deleteUserRights(UserRights userRights) throws Exception
	{
		return HibernateUtil.util.delete(userRights);
	}

	
	public Boolean deleteWorker(Worker worker, Person person, Contact contact, Users user) throws Exception
	{
		return HibernateUtil.util.delete(user) && HibernateUtil.util.delete(worker) && HibernateUtil.util.delete(person)
				&& HibernateUtil.util.delete(contact);
	}

	
	public Boolean deleteXrayResults(XrayResults xrayResults) throws Exception
	{
		return HibernateUtil.util.delete(xrayResults);
	}

	/* Find methods */
	
	public Contact findContact(String personID) throws Exception
	{
		return (Contact) HibernateUtil.util.findObject("from Contact where PID='" + personID + "'");
	}

	
	public ContactSputumResults findContactSputumResultsBySputumTestID(String sputumTestID) throws Exception
	{
		return (ContactSputumResults) HibernateUtil.util.findObject("from ContactSputumResults where SputumTestID='" + sputumTestID + "'");
	}

	
	public Dictionary findDictionary(String term) throws Exception
	{
		return (Dictionary) HibernateUtil.util.findObject("from Dictionary where Term='" + term + "'");
	}

	
	public DrugHistory findDrugHistory(String patientID) throws Exception
	{
		return (DrugHistory) HibernateUtil.util.findObject("from DrugHistory where PatientID='" + patientID + "'");
	}

	
	public Encounter findEncounter(EncounterId encounterID) throws Exception
	{
		return (Encounter) HibernateUtil.util.findObject("from Encounter where PID1='" + encounterID.getPid1() + "' and PID2='"
				+ encounterID.getPid2() + "' and EncounterID='" + encounterID.getEncounterId() + "'");
	}

	
	public EncounterResults[] findEncounterResults(EncounterResultsId encounterResultsID) throws Exception
	{
		return (EncounterResults[]) HibernateUtil.util.findObjects("from EncounterResults where PID1='" + encounterResultsID.getPid1()
				+ "' and PID2='" + encounterResultsID.getPid2() + "' and EncounterID='" + encounterResultsID.getEncounterId() + "'");
	}

	
	public EncounterResults findEncounterResultsByElement(EncounterResultsId encounterResultsID) throws Exception
	{
		return (EncounterResults) HibernateUtil.util.findObject("from EncounterResults where PID1='" + encounterResultsID.getPid1() + "' and PID2='"
				+ encounterResultsID.getPid2() + "' and EncounterID='" + encounterResultsID.getEncounterId() + "' and Element='"
				+ encounterResultsID.getElement() + "'");
	}

	public GeneXpertResults findGeneXpertResultsByTestID(Integer testID) throws Exception
	{
		return (GeneXpertResults) HibernateUtil.util.findObject("from GeneXpertResults where TestID=" + testID);
	}
	
	public GeneXpertResults[] findGeneXpertResults(String sputumTestID) throws Exception
	{
		Object[] objects = HibernateUtil.util.findObjects("from GeneXpertResults where SputumTestID='" + sputumTestID + "'");
		
		return Arrays.copyOf(objects,objects.length,GeneXpertResults[].class);
	}
	
	public GeneXpertResults[] findGeneXpertResults(String sputumTestID, String patientID) throws Exception
	{
		Object[] objects = HibernateUtil.util.findObjects("from GeneXpertResults where SputumTestID='" + sputumTestID + "' AND PatientID='" + patientID + "'");
		
		return Arrays.copyOf(objects,objects.length,GeneXpertResults[].class);
	}
	
	public GeneXpertResults findGeneXpertResultsByCartridgeID(String cartridgeId) throws Exception
	{
		return (GeneXpertResults) HibernateUtil.util.findObject("from GeneXpertResults where CartridgeID='" + cartridgeId+"'");
	}
	

	public Gp findGP(String GPID) throws Exception
	{
		return (Gp) HibernateUtil.util.findObject("from Gp where GPID='" + GPID + "'");
	}

	
	public Household findHousehold(HouseholdId householdID) throws Exception
	{
		return (Household) HibernateUtil.util.findObject("from Household where PatientMRNo='" + householdID.getPatientMrno() + "' and HouseholdID='"
				+ householdID.getHouseholdId() + "'");
	}

	
	public Incentive findIncentive(IncentiveId incentiveID) throws Exception
	{
		return (Incentive) HibernateUtil.util.findObject("from Incentive where PID='" + incentiveID.getPid() + "' and IncentiveID='"
				+ incentiveID.getIncentiveId() + "'");
	}

	
	public Location findLocation(String locationID) throws Exception
	{
		return (Location) HibernateUtil.util.findObject("from Location where LocationID='" + locationID + "'");
	}
	
	public MessageSettings findMessageSettings() throws Exception
	{
		return (MessageSettings) HibernateUtil.util.findObject("from MessageSettings");
	}

	
	public Monitor findMonitor(String monitorID) throws Exception
	{
		return (Monitor) HibernateUtil.util.findObject("from Monitor where MonitorID='" + monitorID + "'");
	}

	
	public NonSuspect findNonSuspect(String patientID) throws Exception
	{
		return (NonSuspect) HibernateUtil.util.findObject("from NonSuspect where PatientID='" + patientID + "'");
	}

	
	public Screening findScreening(int screeningId) throws Exception
	{
		return (Screening) HibernateUtil.util.findObject("from Screening where ScreeningID=" + screeningId);
	}

	
	public Patient findPatient(String patientID) throws Exception
	{
		return (Patient) HibernateUtil.util.findObject("from Patient where PatientID='" + patientID + "'");
	}

	
	public Patient findPatientByMR(String Mrno) throws Exception
	{
		return (Patient) HibernateUtil.util.findObject("from Patient where MRNo='" + Mrno + "'");
	}

	
	public PatientDOTS findPatientDOTS(String patientID) throws Exception
	{
		return (PatientDOTS) HibernateUtil.util.findObject("from PatientDOTS where MRNo = (select MRNo from PatientDOTS where PatientID='"
				+ patientID + "')");
	}

	
	public PatientDOTS findPatientDOTSByMR(String Mrno) throws Exception
	{
		return (PatientDOTS) HibernateUtil.util.findObject("from PatientDOTS where MRNo = '" + Mrno + "')");
	}

	
	public Person findPerson(String PID) throws Exception
	{
		return (Person) HibernateUtil.util.findObject("from Person where PID='" + PID + "'");
	}

	
	public Person[] findPersonsByName(String firstName, String lastName) throws Exception
	{
		return (Person[]) HibernateUtil.util.findObjects("from Person where FirstName LIKE '" + firstName + "%' and LastName LIKE '" + lastName
				+ "%'");
	}

	
	public Person findPersonsByNIC(String NIC) throws Exception
	{
		return (Person) HibernateUtil.util.findObject("from Person where NIC='" + NIC + "'");
	}

	
	public Relationship findRelationship(RelationshipId relationshipID) throws Exception
	{
		return (Relationship) HibernateUtil.util.findObject("from Relationship where PID='" + relationshipID.getPid() + "' and RelativePID='"
				+ relationshipID.getRelativePid() + "'");
	}

	
	public SetupIncentive findSetupIncentive(String incentiveID) throws Exception
	{
		return (SetupIncentive) HibernateUtil.util.findObject("from SetupIncentive where IncentiveID='" + incentiveID + "'");
	}

	
	public SputumResults findSputumResults(String patientID, String sputumTestID) throws Exception
	{
		return (SputumResults) HibernateUtil.util.findObject("from SputumResults where PatientID='" + patientID + "' and SputumTestID='"
				+ sputumTestID + "'");
	}

	
	public SputumResults[] findSputumResultsByPatientID(String patientID) throws Exception
	{
		return (SputumResults[]) HibernateUtil.util.findObjects("from SputumResults where PatientID='" + patientID + "'");
	}

	
	public SputumResults findSputumResultsBySputumTestID(String sputumTestID) throws Exception
	{
		return (SputumResults) HibernateUtil.util.findObject("from SputumResults where SputumTestID='" + sputumTestID + "'");
	}

	
	public Supervisor findSupervisor(String supervisorID) throws Exception
	{
		return (Supervisor) HibernateUtil.util.findObject("from Supervisor where SupervisorID='" + supervisorID + "'");
	}

	
	public TreatmentRefusal findTreatmentRefusal(String patientID) throws Exception
	{
		return (TreatmentRefusal) HibernateUtil.util.findObject("from TreatmentRefusal where PatientID='" + patientID + "'");
	}

	
	public Users findUser(String userName) throws Exception
	{
		return (Users) HibernateUtil.util.findObject("from Users where UserName='" + userName + "'");
	}

	
	public UserRights findUserRights(String roleName, String menuName) throws Exception
	{
		return (UserRights) HibernateUtil.util.findObject("from UserRights where Role='" + roleName + "' and MenuName='" + menuName + "'");
	}

	
	public Worker findWorker(String workerID) throws Exception
	{
		return (Worker) HibernateUtil.util.findObject("from Worker where WorkerID='" + workerID + "'");
	}

	
	public XrayResults findXrayResults(String irs) throws Exception
	{
		return (XrayResults) HibernateUtil.util.findObject("from XrayResults where IRS='" + irs + "'");
	}

	/* Save methods */
	
	public Boolean saveCity(SetupCity city) throws Exception
	{
		return HibernateUtil.util.save(city);
	}

	
	public Boolean saveContact(Contact contact) throws Exception
	{
		return HibernateUtil.util.save(contact);
	}

	
	public Boolean saveContactSputumResults(ContactSputumResults sputumResults) throws Exception
	{
		return HibernateUtil.util.save(sputumResults);
	}

	
	public Boolean saveCountry(SetupCountry country) throws Exception
	{
		return HibernateUtil.util.save(country);
	}

	
	public Boolean saveDictionary(Dictionary dictionary) throws Exception
	{
		return HibernateUtil.util.save(dictionary);
	}

	
	public Boolean saveDrugHistory(DrugHistory drugHistory) throws Exception
	{
		DrugHistoryId drugHistoryID = drugHistory.getId();
		Object[] max = HibernateUtil.util.selectObjects("select max(DispensationNo) from DrugHistory where PatientID='"
				+ drugHistoryID.getPatientId() + "'");
		Integer maxInt = (Integer) max[0];
		if (maxInt == null)
		{
			drugHistoryID.setDispensationNo(1);
		}
		else
		{
			drugHistoryID.setDispensationNo((maxInt.intValue() + 1));
		}
		drugHistory.setId(drugHistoryID);
		return HibernateUtil.util.save(drugHistory);
	}

	
	public Boolean saveEncounter(Encounter encounter) throws Exception
	{
		// Get the max encounter ID and add 1
		EncounterId currentID = encounter.getId();
		Object[] max = HibernateUtil.util.selectObjects("select max(encounterID) from Encounter where pid1='" + currentID.getPid1() + "' and pid2='"
				+ currentID.getPid2() + "'");

		Integer maxInt = (Integer) max[0];
		if (maxInt == null)
		{
			currentID.setEncounterId(1);
		}
		else
		{
			currentID.setEncounterId((maxInt.intValue() + 1));
		}
		encounter.setId(currentID);
		return HibernateUtil.util.save(encounter);
	}

	
	public Boolean saveEncounterResults(EncounterResults encounterResults) throws Exception
	{
		return HibernateUtil.util.save(encounterResults);
	}

	
	public Boolean saveEncounterWithResults(Encounter encounter, ArrayList<String> encounterResults) throws Exception
	{
		boolean result = false;
		// Save an encounter
		try
		{
			Long encounterID = HibernateUtil.util.count("select IFNULL(max(EncounterID), 0) + 1 from Encounter where PID1='"
					+ encounter.getId().getPid1() + "' and PID2='" + encounter.getId().getPid2() + "'");
			result = saveEncounter(encounter);

			for (String s : encounterResults)
			{
				String[] split = s.split("=");
				EncounterResults encounterResult = new EncounterResults();
				encounterResult.setId(new EncounterResultsId(encounterID.intValue(), encounter.getId().getPid1(), encounter.getId().getPid2(),
						split[0]));
				if (split.length == 2)
					encounterResult.setValue((split[1]));
				else
					encounterResult.setValue("");
				result = saveEncounterResults(encounterResult);
			}

			// Send SMS according to the Encounter type
			if (encounter.getEncounterType().equals("CDF") || encounter.getEncounterType().equals("PAED_DIAG"))
				SMSUtil.util.sendAlertsOnClinicalDiagnosis(encounter);
			else if (encounter.getEncounterType().equals("BASELINE"))
				SMSUtil.util.sendAlertsOnBaselineSubmission(encounter);
			else if (encounter.getEncounterType().equals("END_FOL"))
				SMSUtil.util.sendAlertsOnEndOfFollowup(encounter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	
	public Boolean saveFeedback(Feedback feedback) throws Exception
	{
		SMSUtil.util.sendAlertsOnFeedback(feedback);
		return HibernateUtil.util.save(feedback);
	}

	
	public Boolean saveGeneXpertResults(GeneXpertResults geneXpertResults) throws Exception
	{
		Boolean result = HibernateUtil.util.save(geneXpertResults);
		System.out.println("calling SMS function");
		SMSUtil.util.sendAlertsOnAutoGXPResults(geneXpertResults);
		System.out.println("called SMS function");
		return result;
	}

	/*public Boolean saveGeneXpertResultsAuto(GeneXpertResultsAuto geneXpertResults) throws Exception
	{
		return HibernateUtil.util.save(geneXpertResults);
	}*/
	
	
	
	public Boolean saveGP(Gp gp, Person person, Contact contact, Users user) throws Exception
	{
		boolean result = false;
		try
		{
			result = saveUser(user);
		}
		catch (Exception e)
		{
			result = false;
		}
		return result && HibernateUtil.util.save(gp) && HibernateUtil.util.save(person) && HibernateUtil.util.save(contact);
	}

	
	public Boolean saveHousehold(Household household) throws Exception
	{
		return HibernateUtil.util.save(household);
	}

	
	public Boolean saveIncentive(Incentive incentive) throws Exception
	{
		return HibernateUtil.util.save(incentive);
	}

	
	public Boolean saveLocation(Location location) throws Exception
	{
		return HibernateUtil.util.save(location);
	}
	
	public Boolean saveMessageSettings(MessageSettings messageSettings) throws Exception
	{
		return HibernateUtil.util.save(messageSettings);
	}

	
	public Boolean saveNewPatient(Patient patient, Person person, Contact contact, String[] encounterResults) throws Exception
	{
		boolean result = false;
		// Save an encounter
		try
		{
			Encounter encounter = new Encounter(new EncounterId(0, patient.getPatientId(), patient.getScreenerId()), "SUSPECT_ID",
					patient.getTreatmentCenter(), new Date(), new Date(), new Date(), "");
			HibernateUtil.util.save(encounter);
			Long encounterID = HibernateUtil.util.count("select count(*) from Encounter where PID1='" + encounter.getId().getPid1() + "' and PID2='"
					+ encounter.getId().getPid2() + "' and EncounterType='" + encounter.getEncounterType() + "'");

			for (String s : encounterResults)
			{
				String[] split = s.split("=");
				EncounterResults encounterResult = new EncounterResults();
				encounterResult.setId(new EncounterResultsId(encounterID.intValue(), encounter.getId().getPid1(), encounter.getId().getPid2(),
						split[0]));
				encounterResult.setValue(split[1]);
				HibernateUtil.util.save(encounterResult);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		result = HibernateUtil.util.save(patient);
		result = HibernateUtil.util.save(person);
		result = HibernateUtil.util.save(contact);
		if (!result) // In case of failure of any save, delete all 3
		{
			HibernateUtil.util.delete(patient);
			HibernateUtil.util.delete(person);
			HibernateUtil.util.delete(contact);
			return false;
		}
		return result;
	}

	
	public Boolean saveMonitor(Monitor monitor, Person person, Contact contact, Users user) throws Exception
	{
		boolean result = false;
		try
		{
			result = saveUser(user);
		}
		catch (Exception e)
		{
			result = false;
		}
		return result && HibernateUtil.util.save(monitor) && HibernateUtil.util.save(person) && HibernateUtil.util.save(contact);
	}

	
	public Boolean saveNonSuspect(NonSuspect nonSuspect) throws Exception
	{
		return HibernateUtil.util.save(nonSuspect);
	}
	
	
	public Boolean savePatient(Patient patient) throws Exception
	{
		return HibernateUtil.util.save(patient);
	}

	
	public Boolean savePatientDOTS(PatientDOTS patientDOTS) throws Exception
	{
		return HibernateUtil.util.save(patientDOTS);
	}

	
	public Boolean savePerson(Person person) throws Exception
	{
		return HibernateUtil.util.save(person);
	}

	
	public Boolean saveRelationship(Relationship relationship) throws Exception
	{
		return HibernateUtil.util.save(relationship);
	}

	
	public Boolean saveScreening(Screening screening) throws Exception
	{
		boolean result = HibernateUtil.util.save(screening);
		SMSUtil.util.sendAlertsOnScreening(screening);
		return result;
	}

	
	public Boolean saveSetupIncentive(SetupIncentive incentive) throws Exception
	{
		return HibernateUtil.util.save(incentive);
	}

	
	public Boolean saveSputumResults(SputumResults sputumResults) throws Exception
	{
		return HibernateUtil.util.save(sputumResults);
	}

	
	public Boolean saveTreatmentRefusal(TreatmentRefusal treatmentRefusal) throws Exception
	{
		return HibernateUtil.util.save(treatmentRefusal);
	}

	
	public Boolean saveUser(Users user) throws Exception
	{
		user.setPassword(MDHashUtil.getHashString(user.getPassword()));
		user.setSecretAnswer(MDHashUtil.getHashString(user.getSecretAnswer()));
		return HibernateUtil.util.save(user);
	}

	
	public Boolean saveUserRights(UserRights userRights) throws Exception
	{
		return HibernateUtil.util.save(userRights);
	}

	
	public Boolean saveWorker(Worker worker, Person person, Contact contact, Users user) throws Exception
	{
		boolean result = false;
		try
		{
			result = saveUser(user);
		}
		catch (Exception e)
		{
			result = false;
		}
		return result && HibernateUtil.util.save(worker) && HibernateUtil.util.save(person) && HibernateUtil.util.save(contact);
	}

	
	public Boolean saveXrayResults(XrayResults xrayResults) throws Exception
	{
		return HibernateUtil.util.save(xrayResults);
	}

	/* Update methods */
	
	public Boolean updateContact(Contact contact) throws Exception
	{
		return HibernateUtil.util.update(contact);
	}

	
	public Boolean updateContactSputumResults(ContactSputumResults sputumResults) throws Exception
	{
		return HibernateUtil.util.update(sputumResults);
	}

	
	public Boolean updateDictionary(Dictionary dictionary) throws Exception
	{
		return HibernateUtil.util.update(dictionary);
	}

	
	public Boolean updateDrugHistory(DrugHistory drugHistory) throws Exception
	{
		return HibernateUtil.util.update(drugHistory);
	}

	
	public Boolean updateEncounter(Encounter encounter) throws Exception
	{
		return HibernateUtil.util.update(encounter);
	}

	
	public Boolean updateEncounterResults(EncounterResults encounterResults) throws Exception
	{
		return HibernateUtil.util.update(encounterResults);
	}

	
	public Boolean updateEncounterResults(EncounterResultsId encounterResultsId, String newValue) throws Exception
	{
		EncounterResults encounterResults = (EncounterResults) HibernateUtil.util.findObject("from EncounterResults where EncounterId="
				+ encounterResultsId.getEncounterId() + " and PID1='" + encounterResultsId.getPid1() + "' and PID2='" + encounterResultsId.getPid2()
				+ "' and Element='" + encounterResultsId.getElement() + "'");
		encounterResults.setValue(newValue);
		return HibernateUtil.util.update(encounterResults);
	}

	
	public Boolean updateFeedback(Feedback feedback) throws Exception
	{
		return HibernateUtil.util.update(feedback);
	}

	
	public Boolean updateGeneXpertResults(GeneXpertResults geneXpertResults, Boolean isTBPositive) throws Exception
	{
		Boolean result = HibernateUtil.util.update(geneXpertResults);
		//if (isTBPositive != null)
		System.out.println("Update calling send");
			SMSUtil.util.sendAlertsOnAutoGXPResults(geneXpertResults);
			System.out.println("Update called send");
		return result;
	}
	
	/*public Boolean updateGeneXpertResultsAuto(GeneXpertResultsAuto geneXpertResults, Boolean isTBPositive,String operatorId,String pcId,String instrumentSerial,String moduleId,String cartridgeId,String reagentLotId) throws Exception
	{
		Boolean result = HibernateUtil.util.update(geneXpertResults);
		if (isTBPositive != null)
			SMSUtil.util.sendAlertsOnAutoGXPResults(geneXpertResults,operatorId,pcId,instrumentSerial,moduleId,cartridgeId,reagentLotId);
		return result;
	}*/
	
	
	
	public Boolean updateGP(Gp gp, Person person, Contact contact, Users user) throws Exception
	{
		return HibernateUtil.util.update(user) && HibernateUtil.util.update(gp) && HibernateUtil.util.update(person)
				&& HibernateUtil.util.update(contact);
	}

	
	public Boolean updateGPMonitorMapping(String gpId, String monitorID, Boolean map) throws Exception
	{
		if (!map)
		{
			GpMapping mapping = (GpMapping) HibernateUtil.util.findObject("from GpMapping where GPID='" + gpId + "' and MonitorID='" + monitorID
					+ "'");
			return HibernateUtil.util.delete(mapping);
		}
		GpMapping mapping = new GpMapping(new GpMappingId(gpId, monitorID));
		return HibernateUtil.util.save(mapping);
	}

	
	public Boolean updateHousehold(Household household) throws Exception
	{
		return HibernateUtil.util.update(household);
	}

	
	public Boolean updateIncentive(Incentive incentive) throws Exception
	{
		return HibernateUtil.util.update(incentive);
	}

	
	public Boolean updateLocation(Location location) throws Exception
	{
		return HibernateUtil.util.update(location);
	}

	public Boolean updateMessageSettings(MessageSettings messageSettings) throws Exception
	{
		return HibernateUtil.util.update(messageSettings);
	}
	
	public Boolean updateMonitor(Monitor monitor, Person person, Contact contact, Users user) throws Exception
	{
		return HibernateUtil.util.update(user) && HibernateUtil.util.update(monitor) && HibernateUtil.util.update(person)
				&& HibernateUtil.util.update(contact);
	}

	
	public Boolean updateNonSuspect(NonSuspect nonSuspect) throws Exception
	{
		return HibernateUtil.util.update(nonSuspect);
	}
	
	
	public Boolean updateScreening(Screening screening) throws Exception
	{
		return HibernateUtil.util.update(screening);
	}

	
	public Boolean updatePassword(String userName, String newPassword) throws Exception
	{
		Users user = (Users) HibernateUtil.util.findObject("from Users where UserName = '" + userName + "'");
		user.setPassword(MDHashUtil.getHashString(newPassword));
		return HibernateUtil.util.update(user);
	}

	
	public Boolean updatePatient(Patient patient) throws Exception
	{
		return HibernateUtil.util.update(patient);
	}

	
	public Boolean updatePatientDOTS(PatientDOTS patientDOTS) throws Exception
	{
		return HibernateUtil.util.update(patientDOTS);
	}

	
	public Boolean updatePerson(Person person) throws Exception
	{
		return HibernateUtil.util.update(person);
	}

	
	public Boolean updateRelationship(Relationship relationship) throws Exception
	{
		return HibernateUtil.util.update(relationship);
	}

	
	public Boolean updateSetupCity(SetupCity city) throws Exception
	{
		return HibernateUtil.util.update(city);
	}

	
	public Boolean updateSetupCountry(SetupCountry country) throws Exception
	{
		return HibernateUtil.util.update(country);
	}

	
	public Boolean updateSetupIncentive(SetupIncentive incentive) throws Exception
	{
		return HibernateUtil.util.update(incentive);
	}

	
	public Boolean updateSputumResults(SputumResults sputumResults, Boolean isTBPositive) throws Exception
	{
		Boolean result = HibernateUtil.util.update(sputumResults);
		SMSUtil.util.sendAlertsOnSmearResults(sputumResults);
		return result;
	}

	
	public Boolean updateTreatmentRefusal(TreatmentRefusal treatmentRefusal) throws Exception
	{
		return HibernateUtil.util.update(treatmentRefusal);
	}

	
	public Boolean updateUser(Users user) throws Exception
	{
		user.setPassword(MDHashUtil.getHashString(user.getPassword()));
		user.setSecretAnswer(MDHashUtil.getHashString(user.getSecretAnswer()));
		return HibernateUtil.util.update(user);
	}

	
	public Boolean updateUserRights(UserRights userRights) throws Exception
	{
		return HibernateUtil.util.update(userRights);
	}

	
	public Boolean updateWorker(Worker worker, Person person, Contact contact, Users user) throws Exception
	{
		return HibernateUtil.util.update(user) && HibernateUtil.util.update(worker) && HibernateUtil.util.update(person)
				&& HibernateUtil.util.update(contact);
	}

	
	public Boolean updateXrayResults(XrayResults xrayResults) throws Exception
	{
		Boolean result = HibernateUtil.util.update(xrayResults);
		SMSUtil.util.sendAlertsOnXRayResults(xrayResults);
		return result;
	}
}