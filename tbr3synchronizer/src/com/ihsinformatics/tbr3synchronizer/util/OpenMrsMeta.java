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

import java.util.ArrayList;

public class OpenMrsMeta {
	/**
	 * this class contains list of all the table names, which are required in
	 * openmrs data transfer from local server to main server
	 * 
	 */

	public static ArrayList<String[]> TABLE_NAME_LIST = new ArrayList<String[]>();

	/**
	 * list of table in which data created or changed 'n' days ago
	 */
	public static final String[] SELECT_DATA_CREATED_CHANGED = { "concept",
			"cohort", "concept_description", "concept_map_type",
			"concept_proposal", "concept_reference_map",
			"concept_reference_term", "concept_reference_term_map", "drug",
			"encounter", "encounter_provider", "encounter_role", "field",
			"form", "form_field", "location_attribute",
			"location_attribute_type", "note", "notification_alert", "patient",
			"patient_identifier", "patient_program", "patient_state", "person",
			"person_address", "person_attribute", "person_attribute_type",
			"person_merge_log", "person_name", "program", "program_workflow",
			"program_workflow_state", "provider", "provider_attribute",
			"provider_attribute_type", "relationship", "scheduler_task_config",
			"serialized_object", "users ", "visit ", "visit_attribute",
			"visit_attribute_type", "visit_type" };

	/**
	 * list of table in which data created 'n' days ago
	 */
	public static final String[] SELECT_DATA_CREATED = { "active_list_type",
			"concept_answer", "concept_class", "concept_datatype",
			"concept_name", "concept_name_tag", "concept_reference_source",
			"concept_set", "encounter_type", "field_answer", "field_type",
			"location", "location_tag", "obs", "order_type", "orders",
			"patient_identifier_type", "relationship_type" };
	// "concept_name_bkp",

	/**
	 * list of table to select data
	 */
	public static final String[] SELECT_DATA = { "cohort_member",
			"concept_complex", "concept_name_tag_map", "concept_numeric",
			"concept_proposal_tag_map", "concept_set_derived",
			"concept_state_conversion", "concept_stop_word", "concept_word",
			"drug_ingredient", "drug_order", "form_resource",
			"global_property", "location_tag_map", "notification_template",
			"privilege", "role", "role_privilege", "role_role",
			"scheduler_task_config_property", "user_property", "user_role" };
	// "test_order",

	/**
	 * list of table in which data changed 'n' days ago
	 */
	public static final String[] SELECT_DATA_CHANGED = { "notification_alert_recipient" };

	static {
		TABLE_NAME_LIST.add(SELECT_DATA_CREATED_CHANGED);
		TABLE_NAME_LIST.add(SELECT_DATA_CHANGED);
		TABLE_NAME_LIST.add(SELECT_DATA_CREATED);
		TABLE_NAME_LIST.add(SELECT_DATA);
	}
}
