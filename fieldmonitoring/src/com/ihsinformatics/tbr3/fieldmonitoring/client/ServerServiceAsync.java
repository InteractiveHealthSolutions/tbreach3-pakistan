
package com.ihsinformatics.tbr3.fieldmonitoring.client;

import com.ihsinformatics.tbr3.fieldmonitoring.shared.Parameter;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Async counterpart of <code>Service</code>.
 * 
 * @author owais.hussain@irdresearch.org
 * 
 */
public interface ServerServiceAsync
{
	/* Authentication methods */
	void authenticate(String userName, String password,
			AsyncCallback<String> asyncCallback);

	void authenticateUser (String text, AsyncCallback<Boolean> callback) throws Exception;

	void verifySecretAnswer (String userName, String secretAnswer, AsyncCallback<Boolean> callback) throws Exception;

	/* Delete methods */
	void deleteDefinition (Definition definition, AsyncCallback<Boolean> callback) throws Exception;

	void deleteDictionary (Dictionary dictionary, AsyncCallback<Boolean> callback) throws Exception;

	void deleteEncounter (Encounter encounter, AsyncCallback<Boolean> callback) throws Exception;

	void deleteEncounterElement (EncounterElement currentElement, AsyncCallback<Boolean> callback) throws Exception;

	void deleteEncounterPrerequisite (EncounterPrerequisite encounterPrerequisite, AsyncCallback<Boolean> callback) throws Exception;

	void deleteEncounterResults (EncounterResults encounterResults, AsyncCallback<Boolean> callback) throws Exception;

	void deleteEncounterType (EncounterType encounterType, AsyncCallback<Boolean> callback) throws Exception;

	void deleteEncounterValue (EncounterValue encounterValue, AsyncCallback<Boolean> callback) throws Exception;

	void deleteFeedback (Feedback feedback, AsyncCallback<Boolean> callback) throws Exception;

	void deleteLabTest (LabTest labTest, AsyncCallback<Boolean> callback);

	void deleteLocation (Location location, AsyncCallback<Boolean> callback) throws Exception;

	void deletePatient (Patient patient, AsyncCallback<Boolean> callback) throws Exception;

	void deletePerson (Person person, AsyncCallback<Boolean> callback) throws Exception;

	void deletePersonRole (PersonRole personRole, AsyncCallback<Boolean> callback) throws Exception;

	void deleteUser (User user, AsyncCallback<Boolean> callback) throws Exception;

	void deleteUserMapping (UserMapping userMapping, AsyncCallback<Boolean> callback) throws Exception;

	void deleteUserRights (UserRights userRights, AsyncCallback<Boolean> callback) throws Exception;

	/* Find methods */
	void findDefinition (String definitionType, String definitionKey, AsyncCallback<Definition> callback) throws Exception;

	void findDictionary (String term, AsyncCallback<Dictionary> callback) throws Exception;

	void findEncounter (EncounterId encounterID, AsyncCallback<Encounter> callback) throws Exception;

	void findEncounterElement (String encounterType, String element, AsyncCallback<EncounterElement> callback) throws Exception;

	void findEncounterElements (String encounterType, AsyncCallback<EncounterElement[]> asyncCallback)throws Exception;

	void findEncounterPrerequisite (EncounterPrerequisiteId encounterPrerequisiteId, AsyncCallback<EncounterPrerequisite> asyncCallback) throws Exception;

	void findEncounterPrerequisites (EncounterType encounterType, AsyncCallback<EncounterPrerequisite[]> callback) throws Exception;

	void findEncounterResults (EncounterId encounterId, AsyncCallback<EncounterResults[]> callback);

	void findEncounterResultsByElement (EncounterResultsId encounterResultsID, AsyncCallback<EncounterResults> callback) throws Exception;

	void findEncounterType (String encounterType, AsyncCallback<EncounterType> callback) throws Exception;

	void findEncounterValue (String encounterType, String element, String value, AsyncCallback<EncounterValue> callback) throws Exception;

	void findLabTests (String patientId, AsyncCallback<LabTest[]> callback);

	void findLocation (String locationID, AsyncCallback<Location> callback) throws Exception;

	void findLocationsByType (String locationType, AsyncCallback<Location[]> asyncCallback) throws Exception;

	void findPatient (String patientID, AsyncCallback<Patient> callback) throws Exception;

	void findPatientByMR (String Mrno, AsyncCallback<Patient> callback) throws Exception;

	void findPerson (String pid, AsyncCallback<Person> callback) throws Exception;

	void findPersonRoles (String pid, AsyncCallback<PersonRole[]> callback) throws Exception;

	void findPersonsByName (String firstName, String lastName, AsyncCallback<Person[]> callback) throws Exception;

	void findPersonsByNIC (String NIC, AsyncCallback<Person> callback) throws Exception;

	void findUser (String currentUserName, AsyncCallback<User> callback) throws Exception;

	void findUserMapping (UserMappingId userMappingId, AsyncCallback<UserMapping> asyncCallback) throws Exception;

	void findUserMappingsByUser (String userId, AsyncCallback<UserMapping[]> callback) throws Exception;

	void findUserRights (String roleName, String menuName, AsyncCallback<UserRights> callback) throws Exception;

	/* Save methods */
	void saveDefinition (Definition definition, AsyncCallback<Boolean> callback) throws Exception;

	void saveDictionary (Dictionary dictionary, AsyncCallback<Boolean> callback) throws Exception;

	void saveEncounter (Encounter encounter, AsyncCallback<Boolean> callback) throws Exception;

	void saveEncounterElement (EncounterElement currentElement, AsyncCallback<Boolean> callback) throws Exception;

	void saveEncounterPrerequisite (EncounterPrerequisite encounterPrerequisite, AsyncCallback<Boolean> callback) throws Exception;

	void saveEncounterResults (EncounterResults encounterResults, AsyncCallback<Boolean> callback) throws Exception;

	void saveEncounterType (EncounterType encounterType, AsyncCallback<Boolean> callback) throws Exception;

	void saveEncounterValue (EncounterValue encounterValue, AsyncCallback<Boolean> callback) throws Exception;

	void saveEncounterWithResults (Encounter encounter, EncounterResults[] encounterResults, AsyncCallback<String> asyncCallback) throws Exception;

	void saveFeedback (Feedback feedback, AsyncCallback<Boolean> callback) throws Exception;

	void saveLabTest (LabTest labTest, AsyncCallback<Boolean> callback);

	void saveLocation (Location location, AsyncCallback<Boolean> callback) throws Exception;

	void savePatient (Patient patient, AsyncCallback<Boolean> callback) throws Exception;

	void savePerson (Person person, AsyncCallback<Boolean> callback) throws Exception;

	void savePersonRole (PersonRole personRole, AsyncCallback<Boolean> callback) throws Exception;

	void saveUser (User user, String[] currentRoles, AsyncCallback<Boolean> callback);

	void saveUserMapping (UserMapping userMapping, AsyncCallback<Boolean> callback) throws Exception;

	void saveUserRights (UserRights userRights, AsyncCallback<Boolean> callback) throws Exception;

	/* Update methods */
	void updateDefaults (Defaults[] defaults, AsyncCallback<Boolean> callback) throws Exception;

	void updateDefinition (Definition definition, AsyncCallback<Boolean> callback) throws Exception;

	void updateDictionary (Dictionary dictionary, AsyncCallback<Boolean> callback) throws Exception;

	void updateEncounter (Encounter encounter, AsyncCallback<Boolean> callback) throws Exception;

	void updateEncounterElement (EncounterElement element, AsyncCallback<Boolean> callback) throws Exception;

	void updateEncounterPrerequisite (EncounterPrerequisite prerequisite, AsyncCallback<Boolean> callback) throws Exception;

	void updateEncounterResults (EncounterResults encounterResults, AsyncCallback<Boolean> callback) throws Exception;

	void updateEncounterType (EncounterType encounterType, AsyncCallback<Boolean> callback) throws Exception;

	void updateEncounterValue (EncounterValue encounterValue, AsyncCallback<Boolean> callback) throws Exception;

	void updateEncounterWithResults (Encounter encounter, EncounterResults[] encounterResults, AsyncCallback<Boolean> asyncCallback) throws Exception;

	void updateFeedback (Feedback feedback, AsyncCallback<Boolean> callback) throws Exception;

	void updateLabTest (LabTest labTest, AsyncCallback<Boolean> callback);

	void updateLocation (Location location, AsyncCallback<Boolean> callback) throws Exception;

	void updatePassword (String userName, String newPassword, AsyncCallback<Boolean> callback) throws Exception;

	void updatePatient (Patient patient, AsyncCallback<Boolean> callback) throws Exception;

	void updatePerson (Person person, AsyncCallback<Boolean> callback) throws Exception;

	void updatePersonRole (PersonRole personRole, AsyncCallback<Boolean> callback) throws Exception;

	void updateUser (User user, String[] currentRoles, AsyncCallback<Boolean> callback) throws Exception;

	void updateUserMapping (UserMapping userMapping, AsyncCallback<Boolean> callback) throws Exception;

	void updateUserRights (UserRights userRights, AsyncCallback<Boolean> callback) throws Exception;

	/* Other methods */
	void count (String tableName, String filter, AsyncCallback<Long> callback) throws Exception;

	void exists (String tableName, String filer, AsyncCallback<Boolean> callback) throws Exception;

//	void generateCSVfromQuery (String database, String query, AsyncCallback<String> callback) throws Exception;

//	void generateReport (String fileName, Parameter[] params, boolean export, AsyncCallback<String> callback) throws Exception;

//	void generateReportFromQuery (String database, String reportName, String query, Boolean export,
//			AsyncCallback<String> callback) throws Exception;

	void getColumnData (String tableName, String columnName, String filter, AsyncCallback<String[]> callback) throws Exception;

	void getCurrentUserName (AsyncCallback<String> callback) throws Exception;

	void getSchema (AsyncCallback<String[][]> callback) throws Exception;

	void getDefaults (AsyncCallback<Defaults[]> callback) throws Exception;

	void getDefinitions (AsyncCallback<Definition[]> callback) throws Exception;

	void getDumpFiles (AsyncCallback<String[]> callback);

//	void getReportsList (AsyncCallback<String[][]> callback) throws Exception;

	void getRowRecord (String tableName, String[] columnNames, String filter, AsyncCallback<String[]> callback) throws Exception;

	void getObject (String tableName, String columnName, String filter, AsyncCallback<String> callback) throws Exception;

	void getQueriesResults (String[] queries, AsyncCallback<String[]> callback) throws Exception;

	void getSecretQuestion (String userName, AsyncCallback<String> callback) throws Exception;

	void getSnapshotTime (AsyncCallback<String> callback) throws Exception;

	void getTableData (String tableName, String[] columnNames, String filter, AsyncCallback<String[][]> callback) throws Exception;

	void getTableData (String sqlQuery, AsyncCallback<String[][]> callback) throws Exception;

	void getUserRgihts (String userName, String role, String menuName, AsyncCallback<Boolean[]> callback) throws Exception;

	void execute (String query, AsyncCallback<Integer> callback) throws Exception;

	void execute (String[] queries, AsyncCallback<Boolean> callback) throws Exception;

	void executeProcedure (String procedure, AsyncCallback<Boolean> callback) throws Exception;

	void recordLogin (String userName, AsyncCallback<Void> callback) throws Exception;

	void recordLogout (String userName, AsyncCallback<Void> callback) throws Exception;

	void sendGenericSMSAlert (Sms sms, AsyncCallback<Void> callback) throws Exception;

	void sendGenericSMSAlert (Sms[] sms, AsyncCallback<Void> callback) throws Exception;

	void setCurrentUser (String userName, String role, AsyncCallback<Void> callback) throws Exception;
}