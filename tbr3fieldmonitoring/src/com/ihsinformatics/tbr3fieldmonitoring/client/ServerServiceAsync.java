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
package com.ihsinformatics.tbr3fieldmonitoring.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.Defaults;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.Definition;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.Encounter;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.EncounterElement;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.EncounterResults;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.EncounterType;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.Location;

/**
 * @author Tahira
 * 
 */
public interface ServerServiceAsync
{
	void getLocation(String userName, String password, String locationName,
			AsyncCallback<String> asyncCallback);

	void saveLocation(Location location, String userName, String password,
			String[] locationAttributes, AsyncCallback<Boolean> asyncCallback);

	void authenticate(String userName, String password,
			AsyncCallback<String> asyncCallback);

	void setCurrentUser(String userName, AsyncCallback<Void> asyncCallback);

	void getDefaults(AsyncCallback<Defaults[]> asyncCallback) throws Exception;

	void getDefinitions(AsyncCallback<Definition[]> asyncCallback)
			throws Exception;

	void getUserRoles(String userName, String password,
			AsyncCallback<String[]> asyncCallback);

	void findEncounterElement(String encounterType, String element,
			AsyncCallback<EncounterElement> callback) throws Exception;

	void findEncounterType(String encounterType,
			AsyncCallback<EncounterType> callback) throws Exception;

	void saveEncounterWithResults(Encounter encounter,
			EncounterResults[] encounterResults,
			AsyncCallback<String> asyncCallback);

	void deleteEncounter(Encounter encounter, AsyncCallback<Boolean> callback)
			throws Exception;

	void saveFormData(Encounter encounter, EncounterResults[] encounterResults,
			AsyncCallback<String> asyncCallback) throws Exception;

}
