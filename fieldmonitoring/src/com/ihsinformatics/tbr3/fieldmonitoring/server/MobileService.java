/**
 * 
 */

package com.ihsinformatics.tbr3.fieldmonitoring.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import com.ihsinformatics.tbr3.fieldmonitoring.server.util.DateTimeUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.server.util.XmlUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FormType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.Encounter;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.EncounterId;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.EncounterResults;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.EncounterResultsId;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.Feedback;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.LabTest;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.Patient;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.Person;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public class MobileService extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4096512335786671901L;
	private HttpServletRequest request;
	private ServerServiceImpl ssl;

	public MobileService() {
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Doing post on Server..");
		ssl = new ServerServiceImpl();
		System.out.println("SSL initiated");
		PrintWriter out = resp.getWriter();
		String xmlResponse = "xmlResponse";
		try {
			request = req;
			String version = request.getParameter("appver");
			if (version.equals(FM.getCurrentVersion())) {
				String formType = request.getParameter("form");
				System.out.println("----->" + formType);
				if (formType.equals(FormType.LOGIN))
					xmlResponse = doLogin();
				else if (formType.equals(FormType.SCREENING))
					xmlResponse = doScreening(formType);
				else if (formType.equals(FormType.SMEAR_RESULTS))
					xmlResponse = doSmearResults(formType);
				else if (formType.equals(FormType.TREATMENT))
					xmlResponse = doTreatment(formType);
				else if (formType.equals(FormType.SITES_DATA)
						|| formType.equals(FormType.TREATMENT_REVIEW)
						|| formType.equals(FormType.LAB_REVIEW)
						|| formType.equals(FormType.COMPARISON_REVIEW))
					xmlResponse = doReportingDataForm(formType);
				else if (formType.equals(FormType.FEEDBACK))
					xmlResponse = doFeedback(formType);
				else if (formType.equals(FormType.UNDO))
					xmlResponse = undo(formType);
			} else
				xmlResponse = XmlUtil
						.createErrorXml("You are using version "
								+ version
								+ " of Mobile application. You must update your mobile application to version "
								+ FM.getCurrentVersion());
		} catch (NullPointerException e) {
			xmlResponse = XmlUtil.createErrorXml(CustomMessage
					.getErrorMessage(ErrorType.DATA_MISMATCH_ERROR));
		} catch (Exception e) {
			xmlResponse = XmlUtil.createErrorXml(CustomMessage
					.getErrorMessage(ErrorType.DATA_CONNECTION_ERROR));
		}
		out.print(xmlResponse);
		out.flush();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String searchValueInEncounterResults(
			EncounterResults[] encounterResults, String element) {
		for (EncounterResults er : encounterResults)
			if (er.getId().getElement().equals(element))
				return er.getValue();
		return null;
	}

	private String doLogin() {
		String xml = null;
		String userName = request.getParameter("username").toUpperCase();
		String password = request.getParameter("password");
		String userRole = request.getParameter("userrole").toUpperCase();
		String phoneTime = request.getParameter("phonetime").toUpperCase();

		String pid;
		String locationId = "";
		// Check phone time
		long phoneTimeStamp = Long.parseLong(phoneTime);
		Date date = new Date();
		long difference = Math.abs(date.getTime() - phoneTimeStamp);
		System.out.println("Phone Date: " + phoneTimeStamp);
		System.out.println("Server Date: " + date.toString());
		System.out.println("Difference: " + difference);
		final double secDiff = difference / 1000;
		final double hrDiff = secDiff / 3600;
		System.out.println("Difference in hours: " + hrDiff);
		/*
		 * if (hrDiff > 1) { return XmlUtil.createErrorXml (
		 * "Date/time on your phone is incorrect. Please correct the date and time and try again."
		 * ); }
		 */
		// Authenticate and find User Id
//		User user = ssl.authenticate(userName, password, userRole); 
		User user = null;
		if (user == null) {
			return XmlUtil
					.createErrorXml("Invalid Username or Password. Please try again");
		}
		pid = user.getPid();
		// Check for and match existing user roles
		if (!ssl.exists("person_role", "pid='" + pid + "' and role='"
				+ userRole + "'")) {
			return XmlUtil
					.createErrorXml("You cannot login using this user name as "
							+ userRole + ". Please select appropriate role.");
		}
		ssl.recordLogin(userName);

		// Create XML response
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return "";
		}
		Element responseNode = doc.createElement("response");

		Element node = doc.createElement("userrole");
		Text value = doc.createTextNode(userRole.toUpperCase());
		node.appendChild(value);
		responseNode.appendChild(node);

		node = doc.createElement("userid");
		value = doc.createTextNode(String.valueOf(pid));
		node.appendChild(value);
		responseNode.appendChild(node);

		node = doc.createElement("username");
		value = doc.createTextNode(userName);
		node.appendChild(value);
		responseNode.appendChild(node);

		// Get all locations
		node = doc.createElement("locations");
		String[][] locations = ssl
				.getTableData("select location_id, location_name from location where location_type='FACILITY'");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < locations.length; i++)
			sb.append(/* WordUtils.capitalizeFully */(locations[i][1]) + "|"
					+ locations[i][0] + (i != locations.length - 1 ? ";" : ""));
		value = doc.createTextNode(sb.toString());
		node.appendChild(value);
		responseNode.appendChild(node);

		Element labidNode = doc.createElement("locationid");
		Text labidValue = doc.createTextNode(locationId);
		labidNode.appendChild(labidValue);
		responseNode.appendChild(labidNode);

		doc.appendChild(responseNode);
		xml = XmlUtil.docToString(doc);
		return xml;
	}

	private String doScreening(String formType) {
		String xml = null;
		String startTimestamp = request.getParameter("startTimestamp")
				.toUpperCase();
		String endTimestamp = request.getParameter("endTimestamp")
				.toUpperCase();
		String userName = request.getParameter("userName").toUpperCase();
		String dateEntered = request.getParameter("dateEntered").toUpperCase();
		String patientId = request.getParameter("patientId").toUpperCase();
		String locationId = request.getParameter("locationId").toUpperCase();
		String isSuspect = request.getParameter("isSuspect").toUpperCase();

		User user = ssl.findUser(userName);
		if (user == null)
			return XmlUtil.createErrorXml("User not found in the database.");
		EncounterId encounterId = new EncounterId(0, patientId, user.getPid(),
				formType);
		Encounter encounter = new Encounter(encounterId, locationId);
		encounter.setDateEntered(new Date(Long.parseLong(dateEntered)));
		encounter.setDateStart(new Date(Long.parseLong(startTimestamp)));
		encounter.setDateEnd(new Date(Long.parseLong(endTimestamp)));
		String results = request.getParameter("results").toUpperCase()
				.replace("[", "").replaceAll("]", "");
		String[] parts = results.split(";");
		EncounterResults[] encounterResults = new EncounterResults[parts.length];
		for (int i = 0; i < parts.length; i++) {
			String element;
			String value;
			String[] split = parts[i].split("=");
			if (split.length == 0)
				continue;
			element = split[0];
			if (split.length == 1)
				value = "";
			else
				value = split[1];
			EncounterResultsId id = new EncounterResultsId(
					encounterId.getEId(), encounterId.getPid1(),
					encounterId.getPid2(), encounterId.getEncounterType(),
					element);
			encounterResults[i] = new EncounterResults(id, value);
		}
		// Check if the Person already exists
		if (ssl.findPatient(patientId) != null)
			return XmlUtil
					.createErrorXml("A Suspect/Patient with this ID already exists. Please enter a different ID.");
		Person person = new Person(patientId, searchValueInEncounterResults(
				encounterResults, "F_NAME"));
		person.setLastName(searchValueInEncounterResults(encounterResults,
				"L_NAME"));
		person.setMiddleName(searchValueInEncounterResults(encounterResults,
				"M_NAME"));
		person.setAddress1(searchValueInEncounterResults(encounterResults,
				"ADDRESS"));
		person.setAlive(true);
		person.setApproximateAge(Byte.parseByte(searchValueInEncounterResults(
				encounterResults, "AGE")));
		person.setGender(searchValueInEncounterResults(encounterResults,
				"GENDER").charAt(0));
		person.setMobile(searchValueInEncounterResults(encounterResults,
				"CONTACT"));

		Patient patient = new Patient(patientId, encounter.getId().getPid1());
		patient.setDateScreened(encounter.getDateEntered());
		patient.setPatientStatus("SCREENED");
		if (isSuspect.equals("1")) {
			patient.setTreatmentCentre(searchValueInEncounterResults(
					encounterResults, "FACILITY"));
			patient.setDateSuspected(encounter.getDateEntered());
			patient.setDiseaseSuspected(true);
			patient.setDiseaseConfirmed(false);
			patient.setPatientStatus("SUSPECT");
		}
		patient.setSuspectedBy(user.getPid());

		// Save Person object
		if (ssl.savePerson(person)) {
			// Save Patient object if successful
			if (ssl.savePatient(patient)) {
				String status = "SUCCESS";
				try {
					status = ssl.saveEncounterWithResults(encounter,
							encounterResults);
					if (!status.equals("SUCCESS"))
						throw new Exception(status);
				} catch (Exception e) {
					ssl.deletePatient(patient);
					ssl.deletePerson(person);
					return XmlUtil.createErrorXml(e.getMessage());
				}
			} else
				ssl.deletePerson(person);
		}
		// Create XML response
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return "";
		}
		Element responseNode = doc.createElement("response");
		Element node = doc.createElement("status");
		Text value = doc.createTextNode("success");
		node.appendChild(value);
		responseNode.appendChild(node);
		doc.appendChild(responseNode);
		xml = XmlUtil.docToString(doc);
		return xml;
	}

	private String doSmearResults(String formType) {
		String xml = null;
		String startTimestamp = request.getParameter("startTimestamp")
				.toUpperCase();
		String endTimestamp = request.getParameter("endTimestamp")
				.toUpperCase();
		String userName = request.getParameter("userName").toUpperCase();
		String dateEntered = request.getParameter("dateEntered").toUpperCase();
		String patientId = request.getParameter("patientId").toUpperCase();
		String locationId = request.getParameter("locationId").toUpperCase();

		User user = ssl.findUser(userName);
		if (user == null)
			return XmlUtil.createErrorXml("User not found in the database.");

		/*
		 * Location location = ssl.findLocation (locationId); if (location ==
		 * null) return XmlUtil.createErrorXml
		 * ("Laboratory ID not found in the database.");
		 */

		Patient patient = ssl.findPatient(patientId);
		if (patient == null)
			return XmlUtil
					.createErrorXml("Suspect/Patient ID was not found in the database. Please re-enter.");
		EncounterId encounterId = new EncounterId(0, patient.getPid(),
				user.getPid(), formType);
		Encounter encounter = new Encounter(encounterId, locationId);
		encounter.setDateEntered(new Date(Long.parseLong(dateEntered)));
		encounter.setDateStart(new Date(Long.parseLong(startTimestamp)));
		encounter.setDateEnd(new Date(Long.parseLong(endTimestamp)));
		String results = request.getParameter("results").toUpperCase()
				.replace("[", "").replaceAll("]", "");
		String[] parts = results.split(";");
		EncounterResults[] encounterResults = new EncounterResults[parts.length];
		for (int i = 0; i < parts.length; i++) {
			String element;
			String value;
			String[] split = parts[i].split("=");
			if (split.length == 0)
				continue;
			element = split[0];
			if (split.length == 1)
				value = "";
			else
				value = split[1];
			EncounterResultsId id = new EncounterResultsId(
					encounterId.getEId(), encounterId.getPid1(),
					encounterId.getPid2(), encounterId.getEncounterType(),
					element);
			encounterResults[i] = new EncounterResults(id, value);
		}
		// Create laboratory test
		LabTest labTest = new LabTest(patientId, locationId, "MICROSCOPY");
		String collected = searchValueInEncounterResults(encounterResults,
				"COLLECTED");
		if (collected.equals("Y")) {
			try {
				String smearResult = searchValueInEncounterResults(
						encounterResults, "RESULT");
				String otherResult = searchValueInEncounterResults(
						encounterResults, "OTH_RESULT");
				String colTimestamp = searchValueInEncounterResults(
						encounterResults, "COL_DATE");
				if (colTimestamp != null) {
					Date collectionDate = DateTimeUtil.getDateFromString(
							colTimestamp, DateTimeUtil.BE_FORMAT);
					labTest.setOrderedOn(collectionDate);
				}
				labTest.setOrderedBy(userName);
				labTest.setTestedBy(userName);
				labTest.setTestedOn(encounter.getDateEntered());
				labTest.setTestResult(smearResult);
				labTest.setOtherDetail(otherResult);
				ssl.saveLabTest(labTest);
				if (smearResult.contains("+") || smearResult.equals("1-9AFB")) {
					patient.setDiseaseConfirmed(true);
					patient.setPatientStatus("CONFIRMED");
					patient.setDateConfirmed(labTest.getTestedOn());
					patient.setConfirmationSource("MICROSCOPY");
					ssl.updatePatient(patient);
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return XmlUtil.createErrorXml("Unable to save Test Results.");
			}
		}
		String status = ssl.saveEncounterWithResults(encounter,
				encounterResults);
		if (!status.equals("SUCCESS")) {
			ssl.deleteLabTest(labTest);
			return XmlUtil.createErrorXml(status);
		}
		// Create XML response
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return "";
		}
		Element responseNode = doc.createElement("response");
		Element node = doc.createElement("status");
		Text value = doc.createTextNode("success");
		node.appendChild(value);
		responseNode.appendChild(node);
		doc.appendChild(responseNode);
		xml = XmlUtil.docToString(doc);
		return xml;
	}

	private String doTreatment(String formType) {
		String xml = null;
		String startTimestamp = request.getParameter("startTimestamp")
				.toUpperCase();
		String endTimestamp = request.getParameter("endTimestamp")
				.toUpperCase();
		String userName = request.getParameter("userName").toUpperCase();
		String dateEntered = request.getParameter("dateEntered").toUpperCase();
		String patientId = request.getParameter("patientId").toUpperCase();

		User user = ssl.findUser(userName);
		if (user == null)
			return XmlUtil.createErrorXml("User not found in the database.");
		Patient patient = ssl.findPatient(patientId);
		if (patient == null)
			return XmlUtil
					.createErrorXml("Patient ID was not found in the database. Please re-enter.");
		EncounterId encounterId = new EncounterId(0, patient.getPid(),
				user.getPid(), formType);
		Encounter encounter = new Encounter(encounterId, "");
		encounter.setDateEntered(new Date(Long.parseLong(dateEntered)));
		encounter.setDateStart(new Date(Long.parseLong(startTimestamp)));
		encounter.setDateEnd(new Date(Long.parseLong(endTimestamp)));
		String results = request.getParameter("results").toUpperCase()
				.replace("[", "").replaceAll("]", "");
		String[] parts = results.split(";");
		EncounterResults[] encounterResults = new EncounterResults[parts.length];
		for (int i = 0; i < parts.length; i++) {
			String element;
			String value;
			String[] split = parts[i].split("=");
			if (split.length == 0)
				continue;
			element = split[0];
			if (split.length == 1)
				value = "";
			else
				value = split[1];
			EncounterResultsId id = new EncounterResultsId(
					encounterId.getEId(), encounterId.getPid1(),
					encounterId.getPid2(), encounterId.getEncounterType(),
					element);
			encounterResults[i] = new EncounterResults(id, value);
		}
		String status = ssl.saveEncounterWithResults(encounter,
				encounterResults);
		String referred = searchValueInEncounterResults(encounterResults,
				"REFERRED");
		if (referred.equals("Y")) {
			patient.setDateClosed(encounter.getDateEntered());
			patient.setPatientStatus("CLOSED");
			patient.setTreatmentOutcome("REFERRED");
			ssl.updatePatient(patient);
		}
		if (!status.equals("SUCCESS")) {
			return XmlUtil.createErrorXml(status);
		}
		// Create XML response
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return "";
		}
		Element responseNode = doc.createElement("response");
		Element node = doc.createElement("status");
		Text value = doc.createTextNode("success");
		node.appendChild(value);
		responseNode.appendChild(node);
		doc.appendChild(responseNode);
		xml = XmlUtil.docToString(doc);
		return xml;
	}

	private String doReportingDataForm(String formType) {
		String xml = null;
		String startTimestamp = request.getParameter("startTimestamp")
				.toUpperCase();
		String endTimestamp = request.getParameter("endTimestamp")
				.toUpperCase();
		String userName = request.getParameter("userName").toUpperCase();
		String dateEntered = request.getParameter("dateEntered").toUpperCase();

		User user = ssl.findUser(userName);
		if (user == null)
			return XmlUtil.createErrorXml("User not found in the database.");
		EncounterId encounterId = new EncounterId(0, user.getPid(),
				user.getPid(), formType);
		Encounter encounter = new Encounter(encounterId, "");
		encounter.setDateEntered(new Date(Long.parseLong(dateEntered)));
		encounter.setDateStart(new Date(Long.parseLong(startTimestamp)));
		encounter.setDateEnd(new Date(Long.parseLong(endTimestamp)));
		String results = request.getParameter("results").toUpperCase()
				.replace("[", "").replaceAll("]", "");  //correct result
		String[] parts = results.split(";");
		
		
		EncounterResults[] encounterResults = new EncounterResults[parts.length];

		String month = "";
		String year = "";
		String facility = "";
		for (int i = 0; i < parts.length; i++) {
			String element;
			String value;
			String[] split = parts[i].split("=");
			if (split.length == 0)
				continue;
			element = split[0];
			if (split.length == 1)
				value = "";
			else
				value = split[1];

			if (formType.equals(FormType.COMPARISON_REVIEW)
					|| formType.equals(FormType.SITES_DATA)
					|| formType.equals(FormType.LAB_REVIEW)
					|| formType.equals(FormType.TREATMENT_REVIEW)) {
				if (element.equals("MONTH")) {
					month = value;
				}
				else if (element.equals("YEAR")) {
					year = value;
				}
				else if (element.equals("FACILITY") || element.equals("SITE")) {
					facility = value;
				}
			}
			EncounterResultsId id = new EncounterResultsId(
					encounterId.getEId(), encounterId.getPid1(),
					encounterId.getPid2(), encounterId.getEncounterType(),
					element);
			encounterResults[i] = new EncounterResults(id, value);
		}
		if (month.length() != 0 && year.length() != 0) {
			encounter.setDescription(facility + ":" + month + ":" + year);
		}
		Boolean exists = ssl.exists("encounter", "where encounter_type='" + formType + "' AND description='" + facility + ":" + month + ":" + year + "'");
		if (exists) {
			return XmlUtil.createErrorXml("Form already exists for " + facility
					+ "," + month + "," + year);
		}
		String status = ssl.saveEncounterWithResults(encounter,
				encounterResults);
		if (!status.equals("SUCCESS")) {
			return XmlUtil.createErrorXml(status);
		}
		// Create XML response
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return "";
		}
		Element responseNode = doc.createElement("response");
		Element node = doc.createElement("status");
		Text value = doc.createTextNode("success");
		node.appendChild(value);
		responseNode.appendChild(node);
		doc.appendChild(responseNode);
		xml = XmlUtil.docToString(doc);
		return xml;
	}

	private String doFeedback(String formType) {
		String xml = null;
		String dateReported = request.getParameter("dateReported")
				.toUpperCase();
		String userName = request.getParameter("userName").toUpperCase();
		String feedbackType = request.getParameter("feedbackType")
				.toUpperCase();
		String feedbackText = request.getParameter("feedback").toUpperCase();

		User user = ssl.findUser(userName);
		if (user == null)
			return XmlUtil.createErrorXml("User not found in the database.");
		Feedback feedback = new Feedback(userName, feedbackType, feedbackText,
				new Date(Long.parseLong(dateReported)), "P");
		feedback.setAttachment(null);
		Boolean status = ssl.saveFeedback(feedback);
		if (!status) {
			return XmlUtil.createErrorXml("ERROR");
		}
		// Create XML response
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return "";
		}
		Element responseNode = doc.createElement("response");
		Element node = doc.createElement("status");
		Text value = doc.createTextNode("success");
		node.appendChild(value);
		responseNode.appendChild(node);
		doc.appendChild(responseNode);
		xml = XmlUtil.docToString(doc);
		return xml;
	}

	private String undo(String formType) {
		String xml = null;
		String userName = request.getParameter("userName").toUpperCase();
		String encounterType = request.getParameter("undoForm").toUpperCase();

		User user = ssl.findUser(userName);
		if (user == null)
			return XmlUtil.createErrorXml("User not found in the database.");
		Boolean status = ssl.undoFormRequest(userName, encounterType);
		if (!status) {
			return XmlUtil
					.createErrorXml("Form not found or cannot be undone.");
		}
		// Create XML response
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return "";
		}
		Element responseNode = doc.createElement("response");
		Element node = doc.createElement("status");
		Text value = doc.createTextNode("success");
		node.appendChild(value);
		responseNode.appendChild(node);
		doc.appendChild(responseNode);
		xml = XmlUtil.docToString(doc);
		return xml;
	}
}
