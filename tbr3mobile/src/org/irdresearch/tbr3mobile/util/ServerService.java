/**
 * This class handles all mobile form requests to the server
 */

package org.irdresearch.tbr3mobile.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.bouncycastle.jce.provider.JDKDSASigner.noneDSA;
import org.irdresearch.tbr3mobile.App;
import org.irdresearch.tbr3mobile.R;
import org.irdresearch.tbr3mobile.model.OpenMrsObject;
import org.irdresearch.tbr3mobile.shared.FormType;
import org.irdresearch.tbr3mobile.shared.Metadata;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public class ServerService
{
	private static final String	TAG	= "ServerService";
	private static String		tbr3Uri;
	private static DatabaseUtil	dbUtil;
	private static String		directory;
	private static FileUtil		fileUtil;
	private HttpRequest			httpClient;
	private HttpsClient			httpsClient;
	private Context				context;

	public ServerService (Context context)
	{
		this.context = context;
		String prefix = "http" + (App.isUseSsl () ? "s" : "") + "://";
		tbr3Uri = prefix + App.getServer () + "/tbreach3web";
		httpClient = new HttpRequest (this.context);
		httpsClient = new HttpsClient (this.context);
		dbUtil = new DatabaseUtil (this.context);
		directory = Environment.getExternalStorageDirectory () + "/tbr3";
		fileUtil = new FileUtil ();
		FileUtil.createDirectoryInStorage (directory);
	}

	/**
	 * Checks to see if the client is connected to any network (GPRS/Wi-Fi)
	 * 
	 * @return status
	 */
	public boolean checkInternetConnection ()
	{
		boolean status = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo ();
		if (netInfo != null && netInfo.isConnectedOrConnecting ())
		{
			status = true;
		}
		return status;
	}

	public String post (String json)
	{
		String response = null;
		try
		{
			if (App.isOfflineMode ())
			{
				JSONObject responseJson = new JSONObject ();
				responseJson.put ("result", "FAIL: Application Offline");
				return responseJson.toString ();
			}
			if (App.isUseSsl ())
				response = httpsClient.clientPost (tbr3Uri + json, null);
			else
				response = httpClient.clientPost (tbr3Uri + json, null);
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
		}
		catch (Exception e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return response;
	}

	public String get (String uri)
	{
		String response = null;
		try
		{
			if (App.isOfflineMode ())
			{
				JSONObject responseJson = new JSONObject ();
				responseJson.put ("result", "FAIL: Application Offline");
				return responseJson.toString ();
			}
			if (App.isUseSsl ())
				response = httpsClient.clientGet (tbr3Uri + uri);
			else
				response = httpClient.clientGet (tbr3Uri + uri);
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
		}
		catch (Exception e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return response;
	}

	/**
	 * Matches the Client version with Server's version
	 * 
	 * @return
	 */
	public String matchVersions ()
	{
		try
		{
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			String response = get ("?content=" + JsonUtil.getEncodedJson (json));
			if (response != null)
			{
				JSONObject error = JsonUtil.getJSONObject (response);
				if (!error.getString ("ERROR").contains ("Version does not match"))
					return "SUCCESS";
			}
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return "";
	}

	/**
	 * Returns true/false after checking if the user in App variable exists in
	 * the local database. If not found, it searches for the user in the Server.
	 * 
	 * @return status
	 */
	public boolean authenticate ()
	{
		return (getUser (App.getUsername ()) != null);
	}

	/**
	 * Returns Id from Metadata table of given type and name
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public String getId (String type, String name)
	{
		return dbUtil.getObject (Metadata.METADATA_TABLE, "id", "type='" + type + "' AND name='" + name + "'");
	}

	/**
	 * Returns single Object from DB by building a query on the basis of type
	 * and name provided. For multiple records, only first one will be returned
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	private OpenMrsObject getOpenMrsObjectFromDb (String type, String name)
	{
		String[] record = dbUtil.getRecord ("select id, type, name from " + Metadata.METADATA_TABLE + " where type='" + type + "' and name like '%" + name + "%'");
		if (record.length == 0)
		{
			return null;
		}
		OpenMrsObject openMrsObject = new OpenMrsObject (record[0], record[1], record[2]);
		return openMrsObject;
	}

	public OpenMrsObject getUser (String name)
	{
		OpenMrsObject user = null;
		// Always fetch from server and save this user
		try
		{
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", FormType.GET_USER);
			json.put ("username", name);
			String response = get ("?content=" + JsonUtil.getEncodedJson (json));
			if (response != null)
			{
				JSONObject userObj = JsonUtil.getJSONObject (response);
				ContentValues values = new ContentValues ();
				String userName = userObj.getString ("name");
				// If user is found, then save it into local DB
				if (userName.equalsIgnoreCase (App.getUsername ()))
				{
					values = new ContentValues ();
					values.put ("id", userObj.getInt ("id"));
					values.put ("type", Metadata.USER);
					values.put ("name", userName);
					// If the user doesn't exist in DB, save it
					String id = dbUtil.getObject (Metadata.METADATA_TABLE, "id", "type='" + Metadata.USER + "' AND name='" + name + "'");
					if (id == null)
					{
						dbUtil.insert (Metadata.METADATA_TABLE, values);
					}
					user = getOpenMrsObjectFromDb (Metadata.USER, name);
				}
			}
			else
			{
				return null;
			}
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return user;
	}

	public OpenMrsObject getLocation (String name)
	{
		String id = dbUtil.getObject (Metadata.METADATA_TABLE, "id", "type='" + Metadata.LOCATION + "' and name like '%" + name + "%'");
		OpenMrsObject location = null;
		// If not found, fetch from server and save this user
		if (id == null)
		{
			try
			{
				JSONObject json = new JSONObject ();
				json.put ("app_ver", App.getVersion ());
				json.put ("form_name", FormType.GET_LOCATION);
				json.put ("location_name", name);
				String response = get ("?content=" + JsonUtil.getEncodedJson (json));
				if (response != null)
				{
					JSONObject locationObj = JsonUtil.getJSONObject (response);
					ContentValues values = new ContentValues ();
					name = locationObj.getString ("name");
					// If location is found, then save it into local DB
					values = new ContentValues ();
					values.put ("id", locationObj.getInt ("id"));
					values.put ("type", Metadata.LOCATION);
					values.put ("name", name);
					dbUtil.insert (Metadata.METADATA_TABLE, values);
				}
			}
			catch (JSONException e)
			{
				Log.e (TAG, e.getMessage ());
			}
			catch (UnsupportedEncodingException e)
			{
				Log.e (TAG, e.getMessage ());
			}
		}
		location = getOpenMrsObjectFromDb (Metadata.LOCATION, name);
		return location;
	}

	public OpenMrsObject[] getLocations ()
	{
		ArrayList<OpenMrsObject> locations = new ArrayList<OpenMrsObject> ();
		String[][] tableData = dbUtil.getTableData ("select id, name from " + Metadata.METADATA_TABLE + " where type='" + Metadata.LOCATION + "'");
		for (int i = 0; i < tableData.length; i++)
		{
			OpenMrsObject location = new OpenMrsObject (tableData[i][0], Metadata.LOCATION, tableData[i][1]);
			locations.add (location);
		}
		return locations.toArray (new OpenMrsObject[] {});
	}

	public void setCurrentUser (String userName)
	{
		String id = dbUtil.getObject (Metadata.METADATA_TABLE, "id", "type='" + Metadata.USER + "' AND name='" + userName + "'");
		if (id != null)
			App.setCurrentUser (getOpenMrsObjectFromDb (Metadata.USER, userName));
	}

	public void setCurrentLocation (String locationName)
	{
		String id = dbUtil.getObject (Metadata.METADATA_TABLE, "id", "type='" + Metadata.LOCATION + "' AND name='" + locationName + "'");
		if (id != null)
			App.setCurrentUser (getOpenMrsObjectFromDb (Metadata.LOCATION, locationName));
	}

	public String[][] getScreeningReport ()
	{
		ArrayList<String[]> data = new ArrayList<String[]> ();
		try
		{
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", FormType.GET_SCREENING_REPORT);
			json.put ("username", App.getUsername ());
			String response = get ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (response != null)
			{
				if (jsonResponse == null)
				{
					return null;
				}
				for (Iterator<?> itr = jsonResponse.keys (); itr.hasNext ();)
				{
					String key = itr.next ().toString ();
					data.add (new String[] {key, jsonResponse.getString (key)});
				}
			}
		}
		catch (Exception e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return data.toArray (new String[][] {});
	}

	public String[][] getDailySummary ()
	{
		ArrayList<String[]> data = new ArrayList<String[]> ();
		try
		{
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", FormType.GET_DAILY_SUMMARY);
			json.put ("username", App.getUsername ());
			String response = get ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (response != null)
			{
				if (jsonResponse == null)
				{
					return null;
				}
				for (Iterator<?> itr = jsonResponse.keys (); itr.hasNext ();)
				{
					String key = itr.next ().toString ();
					data.add (new String[] {key, jsonResponse.getString (key)});
				}
			}
		}
		catch (Exception e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return data.toArray (new String[][] {});
	}

	public String[][] getPerfornamceData ()
	{
		ArrayList<String[]> data = new ArrayList<String[]> ();
		try
		{
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", FormType.GET_PERFORMANCE_DATA);
			json.put ("username", App.getUsername ());
			String response = get ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (response != null)
			{
				if (jsonResponse == null)
				{
					return null;
				}
				for (Iterator<?> itr = jsonResponse.keys (); itr.hasNext ();)
				{
					String key = itr.next ().toString ();
					data.add (new String[] {key, jsonResponse.getString (key)});
				}
			}
		}
		catch (Exception e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return data.toArray (new String[][] {});
	}

	public String[][] getFormsByDate (Date date)
	{
		ArrayList<String[]> data = new ArrayList<String[]> ();
		try
		{
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", FormType.GET_FORMS_BY_DATE);
			json.put ("username", App.getUsername ());
			json.put ("date", DateTimeUtil.getSQLDate (date));
			String response = get ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (response != null)
			{
				if (jsonResponse == null)
				{
					return null;
				}
				for (Iterator<?> itr = jsonResponse.keys (); itr.hasNext ();)
				{
					String key = itr.next ().toString ();
					data.add (new String[] {key, jsonResponse.getString (key)});
				}
			}
		}
		catch (Exception e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return data.toArray (new String[][] {});
	}

	/**
	 * Returns ID of the Patient Identifier Type from local database
	 * 
	 * @return
	 */
	public String getIdentifierTypeId ()
	{
		String id = dbUtil.getObject (Metadata.METADATA_TABLE, "id", "name='OpenMRS Identification Number'");
		return id;
	}

	/**
	 * Returns patient's DB Id using Patient Identifier
	 * 
	 * @param patientId
	 * @return
	 */
	public String getPatientId (String patientId)
	{
		try
		{
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", FormType.GET_PATIENT);
			json.put ("patient_id", patientId);
			String response = get ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (response != null)
			{
				if (jsonResponse == null)
				{
					return null;
				}
				if (jsonResponse.has ("id"))
				{
					return jsonResponse.getString ("id");
				}
				return null;
			}
		}
		catch (Exception e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return null;
	}

	public String[][] getPatientDetail (String patientId)
	{
		String response = "";
		String[][] details = null;
		JSONObject json = new JSONObject ();
		try
		{
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", FormType.GET_PATIENT_DETAIL);
			json.put ("patient_id", patientId);
			response = get ("?content=" + JsonUtil.getEncodedJson (json));
			if (response == null)
			{
				return details;
			}
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			{
				try
				{
					String name = jsonResponse.get ("name").toString ();
					int age = jsonResponse.getInt ("age");
					String gen = jsonResponse.get ("gender").toString ();
					JSONArray encounters = null;
					if(json.has("encounters"))
					{
						encounters = new JSONArray (jsonResponse.get ("encounters").toString ());
						details = new String[encounters.length () + 3][];
					}
					else
					{
						details = new String[3][];
					}
					details[0] = new String[] {"Name", name};
					details[1] = new String[] {"Age", String.valueOf (age)};
					details[2] = new String[] {"Gender", gen};
					if(encounters != null)
					{
						if(encounters.length() > 0)
						{
							for (int i = 0; i < encounters.length (); i++)
							{
								JSONObject obj = new JSONObject (encounters.get (i).toString ());
								details[i + 3] = new String[] {obj.get ("encounter").toString (), obj.get ("date").toString ()};
							}
						}
					}
				}
				catch (JSONException e)
				{
					Log.e (TAG, e.getMessage ());
				}
			}
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return details;
	}

	/**
	 * Returns observation values against given Patient Identifier and concept
	 * name (observation)
	 * 
	 * @param patientId
	 * @param conceptName
	 * @return
	 */
	public String[] getPatientObs (String patientId, String conceptName)
	{
		try
		{
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", FormType.GET_PATIENT_OBS);
			json.put ("patient_id", patientId);
			json.put ("concept", conceptName);
			String response = get ("?content=" + JsonUtil.getEncodedJson (json));
			if (response != null)
			{
				JSONArray jsonResponse = new JSONArray (response);
				ArrayList<String> obs = new ArrayList<String> ();
				for (int i = 0; i < jsonResponse.length (); i++)
				{
					try
					{
						JSONObject obj = jsonResponse.getJSONObject (i);
						String value = obj.get ("value").toString ();
						obs.add (value);
					}
					catch (JSONException e)
					{
						Log.e (TAG, e.getMessage ());
					}
				}
				return obs.toArray (new String[] {});
			}
		}
		catch (Exception e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return null;
	}

	/**
	 * Returns observation values against given Patient Identifier and concept
	 * name (observation)
	 * 
	 * @param patientId
	 * @param conceptName
	 * @return
	 */
	public String[] getPatientObs (String patientId, String conceptName, String encounterName)
	{
		try
		{
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", FormType.GET_PATIENT_OBS);
			json.put ("patient_id", patientId);
			json.put ("concept", conceptName);
			json.put ("encounter", encounterName);
			String response = get ("?content=" + JsonUtil.getEncodedJson (json));
			if (response != null)
			{
				JSONArray jsonResponse = new JSONArray (response);
				ArrayList<String> obs = new ArrayList<String> ();
				for (int i = 0; i < jsonResponse.length (); i++)
				{
					try
					{
						JSONObject obj = jsonResponse.getJSONObject (i);
						String value = obj.get ("value").toString ();
						obs.add (value);
					}
					catch (JSONException e)
					{
						Log.e (TAG, e.getMessage ());
					}
				}
				return obs.toArray (new String[] {});
			}
		}
		catch (Exception e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return null;
	}

	/**
	 * Returns list of file names containing saved forms
	 * 
	 * @return
	 */
	public String[] getSavedFormFiles ()
	{
		return FileUtil.getFiles (directory);
	}

	/**
	 * Returns lines of text in current file
	 * 
	 * @return
	 */
	public String[] getLines (String filePath, boolean decrypt)
	{
		if (decrypt)
		{
			return decryptFile (directory + "/" + filePath).split ("\n");
		}
		return fileUtil.getLines (directory + "/" + filePath);
	}

	public int countSavedForms (String username)
	{
		return Integer.parseInt (dbUtil.getObject ("select count(*) from offline_forms where username='" + username + "'"));
	}

	public String[] getSavedForm (String username, String timestamp)
	{
		String[] forms = dbUtil.getRecord ("select timestamp, form, json from offline_forms where username='" + username + "' and timestamp=" + timestamp);
		return forms;
	}

	public String[][] getSavedForms (String username)
	{
		String[][] forms = dbUtil.getTableData ("select timestamp, form, json from offline_forms where username='" + username + "'");
		return forms;
	}

	public boolean deleteSavedForm (Long timestamp)
	{
		return dbUtil.delete ("offline_forms", "timestamp=?", new String[] {timestamp.toString ()});
	}

	public String decryptFile (String filePath)
	{
		byte[] bytes = fileUtil.readByteStream (filePath);
		String text = SecurityUtil.decrypt (bytes);
		return text;
	}

	public boolean saveFormToDb (String encounterType, ContentValues values, String[][] observations)
	{
		ContentValues data = new ContentValues ();
		Date date = new Date ();
		try
		{
			String formDate = values.getAsString ("formDate");
			data.put ("form_date", formDate);
		}
		catch (Exception e)
		{
			Log.e (TAG, e.getMessage ());
		}
		data.put ("timestamp", date.getTime ());
		if (encounterType != null)
		{
			data.put ("form", encounterType);
		}
		if (values != null)
		{
			StringBuilder sb = new StringBuilder ();
			Set<Entry<String, Object>> valueSet = values.valueSet ();
			Iterator<Entry<String, Object>> iterator = valueSet.iterator ();
			while (iterator.hasNext ())
			{
				Entry<String, Object> entry = iterator.next ();
				sb.append (entry.getKey () + ":" + entry.getValue ());
				sb.append (";");
			}
			data.put ("content", sb.toString ());
		}
		if (observations != null)
		{
			StringBuilder sb = new StringBuilder ();
			for (int i = 0; i < observations.length; i++)
			{
				sb.append (observations[i][0] + ":" + observations[i][1]);
				sb.append (";");
			}
			data.put ("obs", sb.toString ());
		}
		return dbUtil.insert ("forms", data);
	}

	/**
	 * Save a form to local DB filled in offline
	 * 
	 * @param encounterType
	 * @param json
	 *            form content in json text (not encoded)
	 * @return
	 */
	public boolean saveOfflineForm (String encounterType, String json)
	{
		ContentValues data = new ContentValues ();
		Date date = new Date ();
		data.put ("username", App.getUsername ());
		data.put ("timestamp", date.getTime ());
		data.put ("form", encounterType);
		data.put ("json", json);
		return dbUtil.insert ("offline_forms", data);
	}

	/**
	 * Write a form to local file. If any of the parameters aren't available,
	 * pass null
	 * 
	 * @param encounterType
	 * @param values
	 * @param observations
	 */
	public void writeFormToFile (String encounterType, ContentValues values, String[][] observations)
	{
		StringBuilder sb = new StringBuilder ();
		Date date = new Date ();
		sb.append ("Timestamp:" + date.getTime () + ";");
		if (encounterType != null)
		{
			sb.append ("Form:" + encounterType + ";");
		}
		if (values != null)
		{
			Set<Entry<String, Object>> valueSet = values.valueSet ();
			Iterator<Entry<String, Object>> iterator = valueSet.iterator ();
			sb.append ("Content:[");
			while (iterator.hasNext ())
			{
				Entry<String, Object> entry = iterator.next ();
				sb.append (entry.getKey () + "=" + entry.getValue ());
				sb.append (";");
			}
			sb.append ("];");
		}
		if (observations != null)
		{
			sb.append ("Observations:[");
			for (int i = 0; i < observations.length; i++)
			{
				sb.append (observations[i][0] + "=" + observations[i][1]);
				sb.append (";");
			}
			sb.append ("];\n");
		}
		Calendar cal = Calendar.getInstance ();
		try
		{
			String formDateStr = values.getAsString ("formDate");
			Date formDate = DateTimeUtil.getDateFromString (formDateStr, DateTimeUtil.SQL_DATE);
			cal.set (Calendar.MONTH, formDate.getMonth ());
		}
		catch (Exception e)
		{
			Log.e (TAG, e.getMessage ());
		}
		String dataFilePath = directory + "/forms_" + cal.get (Calendar.YEAR) + "-" + cal.get (Calendar.MONTH) + ".dat";
		// fileUtil.writeByteStream (dataFilePath, SecurityUtil.encrypt
		// (sb.toString ()));
		fileUtil.appendText (dataFilePath, sb.toString ());
	}

	/**
	 * Returns list of Patients matching the parameter(s) from the server
	 * 
	 * @param patientId
	 * @param fullName
	 * @param gender
	 * @param ageStart
	 * @param ageEnd
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public HashMap<String, String[]> searchPatients (String patientId, String firstName, String lastName, String gender, int ageStart, int ageEnd) throws UnsupportedEncodingException
	{
		String response = "";
		HashMap<String, String[]> patients = new HashMap<String, String[]> ();
		JSONObject json = new JSONObject ();
		try
		{
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", FormType.SEARCH_PATIENTS);
			json.put ("patient_id", patientId);
			json.put ("first_name", firstName);
			json.put ("last_name", lastName);
			json.put ("gender", gender);
			json.put ("age_start", ageStart);
			json.put ("age_end", ageEnd);
			response = get ("?content=" + JsonUtil.getEncodedJson (json));
			if (response == null)
			{
				return patients;
			}
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (jsonResponse.has ("patients"))
			{
				JSONArray patientList = new JSONArray (jsonResponse.getString ("patients"));
				for (int i = 0; i < patientList.length (); i++)
				{
					try
					{
						JSONObject obj = patientList.getJSONObject (i);
						String id = obj.get ("patient_id").toString ();
						String name = obj.get ("name").toString ();
						int age = Integer.parseInt (obj.get ("age").toString ());
						String gen = obj.get ("gender").toString ();
						if (gen.equals (gender) && age >= ageStart && age <= ageEnd)
						{
							patients.put (id, new String[] {name, String.valueOf (age), gen});
						}
					}
					catch (JSONException e)
					{
						Log.e (TAG, e.getMessage ());
					}
				}
			}
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
		}
		return patients;
	}

	public String saveScreening (String encounterType, ContentValues values, String[][] observations)
	{
		String response = "";
		// Demographics
		String givenName = null;
		String familyName = null;
		if(!encounterType.equals(FormType.SCREENING))
		{
			givenName = TextUtil.capitalizeFirstLetter (values.getAsString ("firstName"));
			familyName = TextUtil.capitalizeFirstLetter (values.getAsString ("lastName"));
		}
		
		int age = values.getAsInteger ("age");
		String gender = values.getAsString ("gender");
		String patientId = values.getAsString ("patientId");
		String location = values.getAsString ("location");
		String formDate = values.getAsString ("formDate");
		try
		{
			if(!App.isOfflineMode())
			{
				String id = getPatientId (patientId);
				if (id == null)
					return context.getResources ().getString (R.string.patients_not_found);
			}
			// Save Patient
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", encounterType);
			json.put ("username", App.getUsername ());
			json.put ("patient_id", patientId);
			if(!encounterType.equals(FormType.SCREENING))
			{
				json.put ("given_name", givenName);
				json.put ("family_name", familyName);
			}
			json.put ("gender", gender);
			json.put ("age", age);
			json.put ("location", location);
			JSONArray obs = new JSONArray ();
			for (int i = 0; i < observations.length; i++)
			{
				if ("".equals (observations[i][0]) || "".equals (observations[i][1]))
					continue;
				JSONObject obsJson = new JSONObject ();
				obsJson.put ("concept", observations[i][0]);
				obsJson.put ("value", observations[i][1]);
				obs.put (obsJson);
			}
			json.put ("encounter_type", encounterType);
			json.put ("form_date", formDate);
			json.put ("encounter_location", location);
			json.put ("provider", App.getUsername ());
			json.put ("obs", obs.toString ());
			// Save form locally if in offline mode
			if (App.isOfflineMode ())
			{
				saveOfflineForm (encounterType, json.toString ());
				return "SUCCESS";
			}
			response = post ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (jsonResponse == null)
			{
				return response;
			}
			if (jsonResponse.has ("result"))
			{
				String result = jsonResponse.getString ("result");
				return result;
			}
			return response;
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.invalid_data);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.unknown_error);
		}
		return response;
	}

	public String saveCustomerInfo (String encounterType, ContentValues values)
	{
		String response = "";
		String patientId = values.getAsString ("patientId");
		String location = values.getAsString ("location");
		String formDate = values.getAsString ("formDate");
		String address1 = values.getAsString ("address1");
		String address2 = values.getAsString ("town");
		String cityVillage = values.getAsString ("city");
		String country = values.getAsString ("country");
		String phone1 = values.getAsString ("phone1");
		String phone2 = values.getAsString ("phone2");
		String phone1Owner = values.getAsString ("phone1Owner");
		String phone2Owner = values.getAsString ("phone2Owner");
		try
		{
			// Check if the patient Id exists
			String id = getPatientId (patientId);
			if (id == null)
				return context.getResources ().getString (R.string.patient_id_missing);
			// Form JSON
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", encounterType);
			json.put ("username", App.getUsername ());
			json.put ("patient_id", patientId);
			json.put ("location", location);
			json.put ("encounter_type", encounterType);
			json.put ("form_date", formDate);
			json.put ("encounter_location", location);
			json.put ("provider", App.getUsername ());
			// Add address
			json.put ("address1", address1);
			json.put ("address2", address2);
			json.put ("cityVillage", cityVillage);
			json.put ("country", country);
			// Add contacts as array of person attributes
			JSONArray attributes = new JSONArray ();
			JSONObject attributeJson = new JSONObject ();
			if (phone1 != null || "".equals (phone1))
			{
				attributeJson.put ("attribute", "Primary Mobile");
				attributeJson.put ("value", phone1);
				attributes.put (attributeJson);
			}
			if (phone2 != null || "".equals (phone2))
			{
				attributeJson = new JSONObject ();
				attributeJson.put ("attribute", "Secondary Mobile");
				attributeJson.put ("value", phone2);
				attributes.put (attributeJson);
			}
			if (phone1Owner != null || "".equals (phone1Owner))
			{
				attributeJson = new JSONObject ();
				attributeJson.put ("attribute", "Primary Mobile Owner");
				attributeJson.put ("value", phone1Owner);
				attributes.put (attributeJson);
			}
			if (phone2Owner != null || "".equals (phone2Owner))
			{
				attributeJson = new JSONObject ();
				attributeJson.put ("attribute", "Secondary Mobile Owner");
				attributeJson.put ("value", phone2Owner);
				attributes.put (attributeJson);
			}
			json.put ("attributes", attributes.toString ());
			response = post ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (jsonResponse == null)
			{
				return response;
			}
			if (jsonResponse.has ("result"))
			{
				String result = jsonResponse.getString ("result");
				return result;
			}
			return response;
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.invalid_data);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.unknown_error);
		}
		return response;
	}

	public String saveTestIndication (String encounterType, ContentValues values, String[][] observations)
	{
		String response = "";
		String patientId = values.getAsString ("patientId");
		String location = values.getAsString ("location");
		String formDate = values.getAsString ("formDate");
		try
		{
			String id = getPatientId (patientId);
			if (id == null)
				return context.getResources ().getString (R.string.patient_id_missing);
			// Save Patient
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", encounterType);
			json.put ("username", App.getUsername ());
			json.put ("patient_id", patientId);
			json.put ("location", location);
			JSONArray obs = new JSONArray ();
			for (int i = 0; i < observations.length; i++)
			{
				if ("".equals (observations[i][0]) || "".equals (observations[i][1]))
					continue;
				JSONObject obsJson = new JSONObject ();
				obsJson.put ("concept", observations[i][0]);
				obsJson.put ("value", observations[i][1]);
				obs.put (obsJson);
			}
			json.put ("encounter_type", encounterType);
			json.put ("form_date", formDate);
			json.put ("encounter_location", location);
			json.put ("provider", App.getUsername ());
			json.put ("obs", obs.toString ());
			response = post ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (jsonResponse == null)
			{
				return response;
			}
			if (jsonResponse.has ("result"))
			{
				String result = jsonResponse.getString ("result");
				return result;
			}
			return response;
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.invalid_data);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.unknown_error);
		}
		return response;
	}

	public String saveBloodSugarTest (String encounterType, ContentValues values, String[][] observations)
	{
		String response = "";
		String patientId = values.getAsString ("patientId");
		String location = values.getAsString ("location");
		String formDate = values.getAsString ("formDate");
		try
		{
			String id = getPatientId (patientId);
			if (id == null)
				return context.getResources ().getString (R.string.patient_id_missing);
			// Save Patient
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", encounterType);
			json.put ("username", App.getUsername ());
			json.put ("patient_id", patientId);
			json.put ("location", location);
			JSONArray obs = new JSONArray ();
			for (int i = 0; i < observations.length; i++)
			{
				if ("".equals (observations[i][0]) || "".equals (observations[i][1]))
					continue;
				String concept = observations[i][0];
				String value = observations[i][1];
				JSONObject obsJson = new JSONObject ();
				obsJson.put ("concept", concept);
				obsJson.put ("value", value);
				obs.put (obsJson);
			}
			json.put ("encounter_type", encounterType);
			json.put ("form_date", formDate);
			json.put ("encounter_location", location);
			json.put ("provider", App.getUsername ());
			json.put ("obs", obs.toString ());
			response = post ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (jsonResponse == null)
			{
				return response;
			}
			if (jsonResponse.has ("result"))
			{
				String result = jsonResponse.getString ("result");
				return result;
			}
			return response;
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.invalid_data);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.unknown_error);
		}
		return response;
	}

	public String saveBloodSugarResults (String encounterType, ContentValues values, String[][] observations)
	{
		String response = "";
		String patientId = values.getAsString ("patientId");
		String location = values.getAsString ("location");
		String formDate = values.getAsString ("formDate");
		try
		{
			String id = getPatientId (patientId);
			if (id == null)
				return context.getResources ().getString (R.string.patient_id_missing);
			// Save Patient
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", encounterType);
			json.put ("username", App.getUsername ());
			json.put ("patient_id", patientId);
			json.put ("location", location);
			JSONArray obs = new JSONArray ();
			for (int i = 0; i < observations.length; i++)
			{
				if ("".equals (observations[i][0]) || "".equals (observations[i][1]))
					continue;
				String concept = observations[i][0];
				String value = observations[i][1];
				JSONObject obsJson = new JSONObject ();
				obsJson.put ("concept", concept);
				obsJson.put ("value", value);
				obs.put (obsJson);
			}
			json.put ("encounter_type", encounterType);
			json.put ("form_date", formDate);
			json.put ("encounter_location", location);
			json.put ("provider", App.getUsername ());
			json.put ("obs", obs.toString ());
			response = post ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (jsonResponse == null)
			{
				return response;
			}
			if (jsonResponse.has ("result"))
			{
				String result = jsonResponse.getString ("result");
				return result;
			}
			return response;
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.invalid_data);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.unknown_error);
		}
		return response;
	}

	public String saveClinicalEvaluation (String encounterType, ContentValues values, String[][] observations)
	{
		String response = "";
		String patientId = values.getAsString ("patientId");
		String location = values.getAsString ("location");
		String formDate = values.getAsString ("formDate");
		try
		{
			String id = getPatientId (patientId);
			if (id == null)
				return context.getResources ().getString (R.string.patient_id_missing);
			// Save Patient
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", encounterType);
			json.put ("username", App.getUsername ());
			json.put ("patient_id", patientId);
			json.put ("location", location);
			JSONArray obs = new JSONArray ();
			for (int i = 0; i < observations.length; i++)
			{
				if ("".equals (observations[i][0]) || "".equals (observations[i][1]))
					continue;
				JSONObject obsJson = new JSONObject ();
				obsJson.put ("concept", observations[i][0]);
				obsJson.put ("value", observations[i][1]);
				obs.put (obsJson);
			}
			json.put ("encounter_type", encounterType);
			json.put ("form_date", formDate);
			json.put ("encounter_location", location);
			json.put ("provider", App.getUsername ());
			json.put ("obs", obs.toString ());
			response = post ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (jsonResponse == null)
			{
				return response;
			}
			if (jsonResponse.has ("result"))
			{
				String result = jsonResponse.getString ("result");
				return result;
			}
			return response;
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.invalid_data);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.unknown_error);
		}
		return response;
	}

	public String saveDrugDispersal (String encounterType, ContentValues values, String[][] observations)
	{
		String response = "";
		String patientId = values.getAsString ("patientId");
		String location = values.getAsString ("location");
		String formDate = values.getAsString ("formDate");
		try
		{
			String id = getPatientId (patientId);
			if (id == null)
				return context.getResources ().getString (R.string.patient_id_missing);
			// Save Patient
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", encounterType);
			json.put ("username", App.getUsername ());
			json.put ("patient_id", patientId);
			json.put ("location", location);
			JSONArray obs = new JSONArray ();
			for (int i = 0; i < observations.length; i++)
			{
				if ("".equals (observations[i][0]) || "".equals (observations[i][1]))
					continue;
				JSONObject obsJson = new JSONObject ();
				obsJson.put ("concept", observations[i][0]);
				obsJson.put ("value", observations[i][1]);
				obs.put (obsJson);
			}
			json.put ("encounter_type", encounterType);
			json.put ("form_date", formDate);
			json.put ("encounter_location", location);
			json.put ("provider", App.getUsername ());
			json.put ("obs", obs.toString ());
			response = post ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (jsonResponse == null)
			{
				return response;
			}
			if (jsonResponse.has ("result"))
			{
				String result = jsonResponse.getString ("result");
				return result;
			}
			return response;
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.invalid_data);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.unknown_error);
		}
		return response;
	}

	public String savePatientGps (String encounterType, ContentValues values)
	{
		String response = "";
		String patientId = values.getAsString ("patientId");
		String location = values.getAsString ("location");
		String formDate = values.getAsString ("formDate");
		String address1 = values.getAsString ("address1");
		String colony = values.getAsString ("colony");
		String town = values.getAsString ("town");
		String latitude = values.getAsString ("latitude");
		String longitude = values.getAsString ("longitude");
		String cityVillage = values.getAsString ("city");
		String country = values.getAsString ("country");
		try
		{
			// Check if the patient Id exists
			String id = getPatientId (patientId);
			if (id == null)
				return context.getResources ().getString (R.string.patient_id_missing);
			// Form JSON
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", encounterType);
			json.put ("username", App.getUsername ());
			json.put ("patient_id", patientId);
			json.put ("location", location);
			json.put ("encounter_type", encounterType);
			json.put ("form_date", formDate);
			json.put ("encounter_location", location);
			json.put ("provider", App.getUsername ());
			// Add address
			json.put ("address1", address1);
			json.put ("address2", colony);
			json.put ("address3", town);
			json.put ("latitude", latitude);
			json.put ("longitude", longitude);
			json.put ("cityVillage", cityVillage);
			json.put ("country", country);
			response = post ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (jsonResponse == null)
			{
				return response;
			}
			if (jsonResponse.has ("result"))
			{
				String result = jsonResponse.getString ("result");
				return result;
			}
			return response;
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.invalid_data);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.unknown_error);
		}
		return response;
	}

	public String saveFeedback (String encounterType, ContentValues values)
	{
		String response = "";
		try
		{
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", encounterType);
			json.put ("username", App.getUsername ());
			json.put ("location", values.getAsString ("location"));
			json.put ("feedback_type", values.getAsString ("feedbackType"));
			json.put ("feedback", values.getAsString ("feedback"));
			response = post ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (jsonResponse.has ("result"))
			{
				String result = jsonResponse.getString ("result");
				return result;
			}
			else
			{
				return response;
			}
		}
		catch (JSONException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.insert_error);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.insert_error);
		}
		return response;
	}

	/**
	 * Saves non-suspect information to the other web service (e.g. tbr3web)
	 * 
	 * @param encounterType
	 * @param values
	 * @return
	 */
	public String saveNonSuspect (String encounterType, ContentValues values)
	{
		String response = "";
		try
		{
			JSONObject json = new JSONObject ();
			json.put ("app_ver", App.getVersion ());
			json.put ("form_name", encounterType);
			json.put ("age", values.getAsString ("age"));
			json.put ("gender", values.getAsString ("gender"));
			json.put ("weight", values.getAsString ("weight"));
			json.put ("height", values.getAsString ("height"));
			json.put ("location", values.getAsString ("location"));
			json.put ("username", App.getUsername ());
			json.put ("formdate", values.getAsString ("formDate"));
			// Save form locally if in offline mode
			if (App.isOfflineMode ())
			{
				saveOfflineForm (encounterType, json.toString ());
				return "SUCCESS";
			}
			response = post ("?content=" + JsonUtil.getEncodedJson (json));
			JSONObject jsonResponse = JsonUtil.getJSONObject (response);
			if (jsonResponse.has ("result"))
			{
				String result = jsonResponse.getString ("result");
				return result;
			}
			else
			{
				return response;
			}
		}
		catch (NotFoundException e)
		{
			Log.d (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.empty_data);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.d (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.unknown_error);
		}
		catch (JSONException e)
		{
			Log.d (TAG, e.getMessage ());
			response = context.getResources ().getString (R.string.invalid_data);
		}
		return response;
	}
}
