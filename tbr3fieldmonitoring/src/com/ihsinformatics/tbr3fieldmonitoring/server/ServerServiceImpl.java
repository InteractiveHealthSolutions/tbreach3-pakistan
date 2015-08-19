/**
 * Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
 * Contributors: Tahira Niazi
 */
package com.ihsinformatics.tbr3fieldmonitoring.server;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.openmrs.LocationAttribute;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.ModuleMustStartException;
import org.openmrs.util.DatabaseUpdateException;
import org.openmrs.util.InputRequiredException;
import org.openmrs.util.OpenmrsUtil;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ihsinformatics.tbr3fieldmonitoring.client.ServerService;
import com.ihsinformatics.tbr3fieldmonitoring.server.util.HibernateUtil;
import com.ihsinformatics.tbr3fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3fieldmonitoring.shared.ErrorType;
import com.ihsinformatics.tbr3fieldmonitoring.shared.TBR3;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.Defaults;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.Definition;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.Encounter;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.EncounterElement;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.EncounterId;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.EncounterResults;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.EncounterType;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.Location;

/**
 * @author Tahira
 * 
 */
public class ServerServiceImpl extends RemoteServiceServlet implements
		ServerService
{
	private static final long serialVersionUID = 4123609914579659870L;

	// Form Openmrs properties file
	static final String propFilePath = "C:\\Users\\Tahira\\AppData\\Roaming\\OpenMRS\\openmrs-runtime.properties";
//	static final String propFilePath = "/usr/share/tomcat6/.OpenMRS/openmrs-runtime.properties";
	private static File propsFile;
	private static Properties props;
	private static String url, username, password;

	public ServerServiceImpl()
	{
		initOpenMrs();
	}

	public Boolean initOpenMrs()
	{
		propsFile = new File(propFilePath);
		props = new Properties();

		OpenmrsUtil.loadProperties(props, propsFile);
		url = (String) props.get("connection.url");
		username = (String) props.get("connection.username");
		password = (String) props.get("connection.password");
		try
		{
			Context.startup(url, username, password, props);

			Context.openSession();
		}
		catch (ModuleMustStartException e)
		{
			e.printStackTrace();
		}
		catch (DatabaseUpdateException e)
		{
			e.printStackTrace();
		}
		catch (InputRequiredException e)
		{
			e.printStackTrace();
		}
		// try
		// {
		// Context.openSession();
		// // Context.authenticate("tahira", "TahiraNiazi007");
		// }

		return false;
	}

	public String authenticate(String userName, String password)
	{
		String response = "success";
		try
		{
			Context.openSession();
			Context.authenticate(userName, password);
		}
		catch (ContextAuthenticationException e)
		{
			// e.printStackTrace();
			response = CustomMessage
					.getErrorMessage(ErrorType.AUTHENTICATION_ERROR);
		}
		return response;
	}

	@Override
	public String getLocation(String userName, String password,
			String locationId)
	{
		String result = "";
		Context.openSession();
		Context.authenticate(userName, password);
		try
		{
			java.util.List<org.openmrs.Location> locations = Context
					.getLocationService().getLocations(locationId);

			if (locations.size() != 0)
			{
				result = locations.get(0).getName();
				System.out.println("location found...........................");
			}
			else
			{
				System.out
						.println("location NOT found...........................");
				System.out.println(locations.size());
				result = "FAILED \n Location ID:"
						+ CustomMessage
								.getErrorMessage(ErrorType.ITEM_NOT_FOUND);
				return result;
			}
		}
		catch (APIException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Boolean saveLocation(Location location, String userName,
			String password, String[] locationAttributes)
	{
		Boolean result = true;
		try
		{
			Context.openSession();
			// System.out.println("password is " + TBR3.getPassword());
			Context.authenticate(userName, password);

			org.openmrs.User omrsCreator = Context.getUserService()
					.getUserByUsername(userName);

			org.openmrs.Location omrsLocation = new org.openmrs.Location();
			omrsLocation.setAddress1(location.getAddress1());
			omrsLocation.setName(location.getLocationName());

			System.out.println(location.getLocationName());

			org.openmrs.LocationAttributeType locationAttributeType;

			for (int i = 0; i < locationAttributes.length; i++)
			{
				locationAttributeType = Context.getLocationService()
						.getLocationAttributeType(i + 1);

				org.openmrs.LocationAttribute attribute = new LocationAttribute();
				attribute.setAttributeType(locationAttributeType);
				attribute.setValue(locationAttributes[i]);
				attribute.setCreator(omrsCreator);
				attribute.setDateCreated(new Date());
				omrsLocation.addAttribute(attribute);
			}

			org.openmrs.Location savedLocation = Context.getLocationService()
					.saveLocation(omrsLocation);

			if (savedLocation == null)
				result = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return result;
	}

	/**
	 * Sets current user name, this is due to a strange GWT bug/feature that
	 * shared variables, set from Client-side appear to be empty on Server-side
	 * code
	 * 
	 * @return
	 */
	public void setCurrentUser(String userName)
	{
		TBR3.setCurrentUserName(userName);
		// IRZ.setCurrentRole(role);
	}

	public static void main(String[] args)
	{
		try
		{
			Context.openSession();
			Context.authenticate("tahira", "TahiraNiazi007");
			try
			{
				java.util.List<org.openmrs.Location> locationList = Context
						.getLocationService().getLocations("101000");
				System.out.println(locationList.size());
				System.out.println(locationList.get(0).getName());
				org.openmrs.Location omrsLocation = Context
						.getLocationService().getLocation("ABC Clinic");

				if (omrsLocation != null)
				{
					System.out.println("location found");
				}
				else
				{
					System.out.println("location NOT found");
				}
			}
			catch (APIException e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String[] getUserRoles(String userName, String password)
	{
		String[] roles = null;

		try
		{
			Context.openSession();
			Context.authenticate(userName, password);
			org.openmrs.User omrsUser = Context.getUserService()
					.getUserByUsername(userName);
			Set<org.openmrs.Role> rolesOfUser = omrsUser.getAllRoles();
			// roles = rolesOfUser.toArray(new String[rolesOfUser.size()]);

			roles = new String[rolesOfUser.size()];
			int i = 0;
			for (org.openmrs.Role role : rolesOfUser)
			{
				roles[i++] = role.getName();
			}
		}
		catch (APIException e)
		{
			e.printStackTrace();
		}

		return roles;
	}

	/**
	 * Get default values to be used on front-ends
	 */
	public Defaults[] getDefaults()
	{
		Object[] objs = HibernateUtil.util.findObjects("from Defaults");
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
	public Definition[] getDefinitions()
	{
		Object[] objs = HibernateUtil.util.findObjects("from Definition");
		Definition[] definitions = new Definition[objs.length];
		for (int i = 0; i < objs.length; i++)
		{
			Definition def = (Definition) objs[i];
			definitions[i] = def;
		}
		return definitions;
	}

	public String saveFormData(Encounter encounter,
			EncounterResults[] encounterResults)
	{
		String result = "";
		try
		{
			result = saveEncounterWithResults(encounter, encounterResults);
			// if (!updatePatient(patient))
			// return CustomMessage.getErrorMessage(ErrorType.UPDATE_ERROR);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				deleteEncounter(encounter);
				return CustomMessage.getErrorMessage(ErrorType.INSERT_ERROR);
			}
			catch (Exception e1)
			{
			}
		}
		return result;
	}

	public Boolean deleteEncounter(Encounter encounter)
	{
		boolean result;
		// Delete Encounter Results
		HibernateUtil.util.findObjects("from EncounterResults where id.eId='"
				+ encounter.getId().geteId() + "' and id.pid1='"
				+ encounter.getId().getPid1() + "' and id.pid2='"
				+ encounter.getId().getPid2() + "'");
		// Delete Encounter
		result = HibernateUtil.util.delete(encounter);
		return result;
	}

	/**
	 * Saves Encounter with Results. If the Pre-requisites are not satisfied or
	 * encounter values are not valid then nothing will be saved.
	 */
	public String saveEncounterWithResults(Encounter encounter,
			EncounterResults[] encounterResults)
	{
		String result = "";
		// Check for Pre-Requisites
		// EncounterType encounterType =
		// findEncounterType(encounter.getId().getEncounterType());
		// EncounterPrerequisite[] prereqs =
		// findEncounterPrerequisites(encounterType);
		// for (EncounterPrerequisite pr : prereqs)
		// {
		// EncounterResults prereqResult = (EncounterResults)
		// HibernateUtil.util.findObject("from EncounterResults where id.pid1='"
		// + encounter.getId().getPid1() + "' and id.encounterType='"
		// + pr.getPrerequisiteEncounter() + "' and id.element='" +
		// pr.getConditionElement() + "'");
		// if (prereqResult == null)
		// {
		// result = "Pre-requisite form " + pr.getPrerequisiteEncounter() +
		// " was not found.";
		// return result;
		// }
		// else if
		// (!prereqResult.getValue().matches(pr.getPossibleValueRegex()))
		// {
		// result =
		// "Pre-requisite was not satisfied. To submit this form, there must be a(n) "
		// + pr.getPrerequisiteEncounter() +
		// " form filled with value of question "
		// + pr.getConditionElement() + " like " + pr.getPossibleValueRegex() +
		// "\n";
		// return result;
		// }
		// }
		// Get the max encounter ID and add 1
		EncounterId currentID = encounter.getId();
		Object[] max = HibernateUtil.util
				.selectObjects("select max(e_id) from encounter where pid1='"
						+ currentID.getPid1() + "' and pid2='"
						+ currentID.getPid2() + "' and encounter_type='"
						+ currentID.getEncounterType() + "'");
		Integer maxInt = (Integer) max[0];
		if (maxInt == null)
			currentID.seteId(1);
		else
			currentID.seteId((maxInt.intValue() + 1));
		encounter.setId(currentID);
		// Validate values of results if bounded by a validation

		// remove from here
		for (EncounterResults er : encounterResults)
		{
			System.out.println(er.getValue());
		}
		// till here

		for (EncounterResults er : encounterResults)
		{
			er.getId().seteId(encounter.getId().geteId());
			EncounterElement elem = findEncounterElement(er.getId()
					.getEncounterType(), er.getId().getElement());
			String regex = elem.getValidator();
			if (regex == null)
				continue;
			if (!regex.equals("") && !er.getValue().matches(regex))
			{
				result = "Invalid value provided for question: "
						+ elem.getId().getElement() + " ("
						+ elem.getDescription() + ")\n";
				return result;
			}
		}
		// Save Encounter
		if (saveEncounter(encounter))
		{
			// Save Encounter Results
			for (EncounterResults er : encounterResults)
				saveEncounterResults(er);
			result = "SUCCESS";
			System.out.println("encounter saved ............................");
		}
		else
			result = "Could not save Form.";
		return result;
	}

	public EncounterType findEncounterType(String encounterType)
	{
		EncounterType eType = (EncounterType) HibernateUtil.util
				.findObject("from EncounterType where encounterType='"
						+ encounterType + "'");
		return eType;
	}

	public EncounterElement findEncounterElement(String encounterType,
			String element)
	{
		EncounterElement eElement = (EncounterElement) HibernateUtil.util
				.findObject("from EncounterElement where id.encounterType='"
						+ encounterType + "' and id.element='" + element + "'");
		return eElement;
	}

	private Boolean saveEncounter(Encounter encounter)
	{
		// Get the max encounter ID and add 1
		EncounterId currentID = encounter.getId();
		Object[] max = HibernateUtil.util
				.selectObjects("select max(e_id) from encounter where pid1='"
						+ currentID.getPid1() + "' and pid2='"
						+ currentID.getPid2() + "' and encounter_type='"
						+ currentID.getEncounterType() + "'");

		Integer maxInt = (Integer) max[0];
		if (maxInt == null)
			currentID.seteId(1);
		else
			currentID.seteId((maxInt.intValue() + 1));
		encounter.setId(currentID);
		return HibernateUtil.util.save(encounter);
	}

	private Boolean saveEncounterResults(EncounterResults encounterResults)
	{
		// Validate value if bounded by a validation
		/*
		 * when encounter type is CLIENT EDIT it gets 2 things ATTRIBUTE AND
		 * VALUE ATTRIBUTE contains "person;nic" and VALUE contains "123456/*"
		 * the edited value PROBLEM: We cannot fix the regex in db for the
		 * ATTRIBUTE or the VALUE because it will vary for different fields
		 * SO,in order to validate the CLIENT EDIT I access the
		 * Encounter_elements() in the savePatientEdit
		 */
		EncounterElement elem = findEncounterElement(encounterResults.getId()
				.getEncounterType(), encounterResults.getId().getElement());
		String regex = elem.getValidator();
		if (regex != null)
		{
			if (!regex.equals("")
					&& !encounterResults.getValue().matches(regex))
				return null;
		}
		return HibernateUtil.util.save(encounterResults);
	}

}
