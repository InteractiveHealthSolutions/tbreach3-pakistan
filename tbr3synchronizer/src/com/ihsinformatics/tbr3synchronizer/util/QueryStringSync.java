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

/**
 * Queries for synchronizing data
 * 
 * @author Hera Rafique
 *
 */

public class QueryStringSync {

	public static ArrayList<String> SYNC_QUERIES = new ArrayList<String>();

	// queries for synchronization process

	static {
		/* ---------- INSERT DATA ---------- */
		/* -- People -- */
		SYNC_QUERIES
				.add("INSERT INTO person_attribute_type SELECT * FROM temp_person_attribute_type WHERE uuid NOT IN (SELECT uuid FROM person_attribute_type)");
		SYNC_QUERIES
				.add("UPDATE temp_person SET creator = 1 WHERE creator NOT IN (SELECT user_id FROM users)");
		SYNC_QUERIES
				.add("INSERT INTO person SELECT * FROM temp_person WHERE uuid NOT IN (SELECT uuid FROM person) AND person_id NOT IN (SELECT person_id from person)");
		SYNC_QUERIES
				.add("UPDATE temp_person_address SET creator = 1 WHERE creator NOT IN (SELECT user_id FROM users)");
		SYNC_QUERIES
				.add("INSERT INTO person_address SELECT * FROM temp_person_address WHERE uuid NOT IN (SELECT uuid FROM person_address)");
		SYNC_QUERIES
				.add("INSERT INTO person_attribute SELECT * FROM temp_person_attribute WHERE uuid NOT IN (SELECT uuid FROM person_attribute)");
		SYNC_QUERIES
				.add("INSERT INTO person_name SELECT * FROM temp_person_name WHERE uuid NOT IN (SELECT uuid FROM person_name) AND person_id NOT IN (SELECT person_id from person)");

		/* -- Users & Privileges -- */
		SYNC_QUERIES
				.add("INSERT INTO role SELECT * FROM temp_role WHERE uuid NOT IN (SELECT uuid FROM role)");
		SYNC_QUERIES
				.add("INSERT INTO role_role SELECT * FROM temp_role_role WHERE CONCAT(parent_role, child_role) NOT IN (SELECT CONCAT(parent_role, child_role) FROM role_role)");
		SYNC_QUERIES
				.add("INSERT INTO privilege SELECT * FROM temp_privilege WHERE uuid NOT IN (SELECT uuid FROM privilege)");
		SYNC_QUERIES
				.add("INSERT INTO role_privilege SELECT * FROM temp_role_privilege WHERE CONCAT(role, privilege) NOT IN (SELECT CONCAT(role, privilege) FROM role_privilege)");
		SYNC_QUERIES
				.add("INSERT INTO users SELECT * FROM temp_users WHERE uuid NOT IN (SELECT uuid FROM users)");
		SYNC_QUERIES
				.add("INSERT INTO user_property SELECT * FROM temp_user_property WHERE CONCAT(user_id, property) NOT IN (SELECT CONCAT(user_id, property) FROM user_property) AND user_id IN (SELECT user_id FROM users)");
		SYNC_QUERIES
				.add("INSERT INTO user_role SELECT * FROM temp_user_role WHERE CONCAT(user_id, role) NOT IN (SELECT CONCAT(user_id, role) FROM user_role) AND user_id IN (SELECT user_id FROM users)");
		SYNC_QUERIES
				.add("INSERT INTO provider SELECT * FROM temp_provider WHERE uuid NOT IN (SELECT uuid FROM provider)");

		/* -- Locations -- */
		SYNC_QUERIES
				.add("INSERT INTO location_attribute_type SELECT * FROM temp_location_attribute_type WHERE uuid NOT IN (SELECT uuid FROM location_attribute_type)");
		SYNC_QUERIES
				.add("INSERT INTO location SELECT * FROM temp_location WHERE uuid NOT IN (SELECT uuid FROM location)");
		SYNC_QUERIES
				.add("INSERT INTO location_attribute SELECT * FROM temp_location_attribute WHERE uuid NOT IN (SELECT uuid FROM location_attribute)");
		SYNC_QUERIES
				.add("INSERT INTO location_tag SELECT * FROM temp_location_tag WHERE uuid NOT IN (SELECT uuid FROM location_tag)");
		SYNC_QUERIES
				.add("INSERT INTO location_tag_map SELECT * FROM temp_location_tag_map WHERE CONCAT(location_id, location_tag_id) NOT IN (SELECT CONCAT(location_id, location_tag_id) FROM location_tag)");

		/* -- Concepts -- */
		SYNC_QUERIES
				.add("INSERT INTO concept_class SELECT * FROM temp_concept_class WHERE uuid NOT IN (SELECT uuid FROM concept_class)");
		SYNC_QUERIES
				.add("INSERT INTO concept_set SELECT * FROM temp_concept_set WHERE uuid NOT IN (SELECT uuid FROM concept_set)");
		SYNC_QUERIES
				.add("INSERT INTO concept_datatype SELECT * FROM temp_concept_datatype WHERE uuid NOT IN (SELECT uuid FROM concept_datatype)");
		SYNC_QUERIES
				.add("INSERT INTO concept_map_type SELECT * FROM temp_concept_map_type WHERE uuid NOT IN (SELECT uuid FROM concept_map_type)");
		SYNC_QUERIES
				.add("INSERT INTO concept_stop_word SELECT * FROM temp_concept_stop_word WHERE uuid NOT IN (SELECT uuid FROM concept_stop_word)");
		SYNC_QUERIES
				.add("INSERT INTO concept SELECT * FROM temp_concept WHERE uuid NOT IN (SELECT uuid FROM concept)");
		SYNC_QUERIES
				.add("INSERT INTO concept_name SELECT * FROM temp_concept_name WHERE uuid NOT IN (SELECT uuid FROM concept_name)");
		SYNC_QUERIES
				.add("INSERT INTO concept_numeric SELECT * FROM temp_concept_numeric WHERE concept_id NOT IN (SELECT concept_id FROM concept_numeric)");
		SYNC_QUERIES
				.add("INSERT INTO concept_description SELECT * FROM temp_concept_description WHERE uuid NOT IN (SELECT uuid FROM concept_description)");
		SYNC_QUERIES
				.add("INSERT INTO concept_set_derived SELECT * FROM temp_concept_set_derived WHERE concat(concept_id, concept_set) NOT IN (SELECT concat(concept_id, concept_set) FROM concept_set_derived)");
		SYNC_QUERIES
				.add("INSERT INTO concept_answer SELECT * FROM temp_concept_answer WHERE uuid NOT IN (SELECT uuid FROM concept_answer)");
		SYNC_QUERIES
				.add("INSERT INTO concept_word SELECT * FROM temp_concept_word WHERE concept_word_id NOT IN (SELECT concept_word_id FROM concept_word)");
		SYNC_QUERIES
				.add("INSERT INTO concept_reference_map SELECT * FROM temp_concept_reference_map WHERE uuid NOT IN (SELECT uuid FROM concept_reference_map)");
		SYNC_QUERIES
				.add("INSERT INTO concept_reference_term SELECT * FROM temp_concept_reference_term WHERE uuid NOT IN (SELECT uuid FROM concept_reference_term)");
		SYNC_QUERIES
				.add("INSERT INTO concept_reference_term_map SELECT * FROM temp_concept_reference_term_map WHERE uuid NOT IN (SELECT uuid FROM concept_reference_term_map)");
		SYNC_QUERIES
				.add("INSERT INTO concept_reference_source SELECT * FROM temp_concept_reference_source WHERE uuid NOT IN (SELECT uuid FROM concept_reference_source)");

		/* -- Patients -- */
		SYNC_QUERIES
				.add("INSERT INTO patient_identifier_type SELECT * FROM temp_patient_identifier_type WHERE uuid NOT IN (SELECT uuid FROM patient_identifier_type)");
		/* -- Skip all patients that aren't saved in person table -- */
		SYNC_QUERIES
				.add("DELETE FROM temp_patient WHERE patient_id NOT IN (SELECT person_id FROM person)");
		SYNC_QUERIES
				.add("INSERT INTO patient SELECT * FROM temp_patient WHERE patient_id IN (SELECT person_id FROM person) AND patient_id NOT IN (SELECT patient_id FROM patient)");
		SYNC_QUERIES
				.add("INSERT INTO patient_identifier SELECT * FROM temp_patient_identifier WHERE uuid NOT IN (SELECT uuid FROM patient_identifier)");

		/* -- Encounters & Obs -- */
		SYNC_QUERIES
				.add("INSERT INTO encounter_type SELECT * FROM temp_encounter_type WHERE uuid NOT IN (SELECT uuid FROM encounter_type)");
		SYNC_QUERIES
				.add("INSERT INTO form SELECT * FROM temp_form WHERE uuid NOT IN (SELECT uuid FROM form)");
		SYNC_QUERIES
				.add("INSERT INTO encounter_role SELECT * FROM temp_encounter_role WHERE uuid NOT IN (SELECT uuid FROM encounter_role)");
		SYNC_QUERIES
				.add("INSERT INTO encounter SELECT * FROM temp_encounter WHERE uuid NOT IN (SELECT uuid FROM encounter) AND patient_id IN (SELECT patient_id FROM patient)");
		SYNC_QUERIES
				.add("INSERT INTO encounter_provider SELECT * FROM temp_encounter_provider WHERE uuid NOT IN (SELECT uuid FROM encounter_provider) AND encounter_id IN (SELECT encounter_id FROM encounter)");
		SYNC_QUERIES
				.add("INSERT INTO obs SELECT * FROM temp_obs WHERE uuid NOT IN (SELECT uuid FROM obs) AND encounter_id IN (SELECT encounter_id FROM encounter)");

		/* -- Orders -- */
		SYNC_QUERIES
				.add("INSERT INTO order_type SELECT * FROM temp_order_type WHERE uuid NOT IN (SELECT uuid FROM order_type)");
		SYNC_QUERIES
				.add("INSERT INTO orders SELECT * FROM temp_orders WHERE uuid NOT IN (SELECT uuid FROM orders)");
		SYNC_QUERIES
				.add("INSERT INTO drug_order SELECT * FROM temp_drug_order WHERE order_id NOT IN (SELECT order_id FROM drug_order)");

		/* ---------- UPDATE DATA ---------- */
		/* -- Patients -- */
		SYNC_QUERIES
				.add("UPDATE temp_person SET creator = 1 WHERE creator NOT IN (SELECT user_id FROM users)");

		SYNC_QUERIES
				.add("UPDATE person_attribute_type AS a, temp_person_attribute_type AS t "
						+ "SET a.name = t.name, a.description = t.description, a.format = t.format, a.foreign_key = t.foreign_key, a.searchable = t.searchable, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.retired = t.retired, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retire_reason = t.retire_reason, a.edit_privilege = t.edit_privilege, a.sort_weight = t.sort_weight "
						+ "WHERE a.person_attribute_type_id = t.person_attribute_type_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE person AS a, temp_person AS t "
						+ "SET a.gender = t.gender, a.birthdate = t.birthdate, a.birthdate_estimated = t.birthdate_estimated, a.dead = t.dead, a.death_date = t.death_date, a.cause_of_death = t.cause_of_death, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.voided = t.voided, a.voided_by = t.voided_by, a.date_voided = t.date_voided, a.void_reason = t.void_reason "
						+ "WHERE a.person_id = t.person_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE person_address AS a, temp_person_address AS t "
						+ "SET a.person_id = t.person_id, a.preferred = t.preferred, a.address1 = t.address1, a.address2 = t.address2, a.city_village = t.city_village, a.state_province = t.state_province, a.postal_code = t.postal_code, a.country = t.country, a.latitude = t.latitude, a.longitude = t.longitude, a.start_date = t.start_date, a.end_date = t.end_date, a.creator = t.creator, a.date_created = t.date_created, a.voided = t.voided, a.voided_by = t.voided_by, a.date_voided = t.date_voided, a.void_reason = t.void_reason, a.county_district = t.county_district, a.address3 = t.address3, a.address4 = t.address4, a.address5 = t.address5, a.address6 = t.address6, a.date_changed = t.date_changed, a.changed_by = t.changed_by "
						+ "WHERE a.person_address_id = t.person_address_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE person_attribute AS a, temp_person_attribute AS t "
						+ "SET a.person_id = t.person_id, a.value = t.value, a.person_attribute_type_id = t.person_attribute_type_id, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.voided = t.voided, a.voided_by = t.voided_by, a.date_voided = t.date_voided, a.void_reason = t.void_reason "
						+ "WHERE a.person_attribute_id = t.person_attribute_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE person_name AS a, temp_person_name AS t "
						+ "SET a.preferred = t.preferred, a.person_id = t.person_id, a.prefix = t.prefix, a.given_name = t.given_name, a.middle_name = t.middle_name, a.family_name_prefix = t.family_name_prefix, a.family_name = t.family_name, a.family_name2 = t.family_name2, a.family_name_suffix = t.family_name_suffix, a.degree = t.degree, a.creator = t.creator, a.date_created = t.date_created, a.voided = t.voided, a.voided_by = t.voided_by, a.date_voided = t.date_voided, a.void_reason = t.void_reason, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.uuid = t.uuid "
						+ "WHERE a.person_name_id = t.person_name_id AND a.uuid = t.uuid");

		/* -- Users & Privileges -- */
		SYNC_QUERIES.add("UPDATE role AS a, temp_role AS t "
				+ "SET a.description = t.description "
				+ "WHERE a.role = t.role AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE users AS a, temp_users AS t "
						+ "SET a.system_id = t.system_id, a.username = t.username, a.password = t.password, a.salt = t.salt, a.secret_question = t.secret_question, a.secret_answer = t.secret_answer, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.person_id = t.person_id, a.retired = t.retired, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retire_reason = t.retire_reason "
						+ "WHERE a.user_id = t.user_id AND a.uuid = t.uuid");

		SYNC_QUERIES.add("UPDATE user_property AS a, temp_user_property AS t "
				+ "SET a.property_value = t.property_value "
				+ "WHERE a.user_id = t.user_id AND a.property = t.property");

		SYNC_QUERIES
				.add("UPDATE provider AS a, temp_provider AS t "
						+ "SET a.person_id = t.person_id, a.name = t.name, a.identifier = t.identifier, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.retired = t.retired, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retire_reason = t.retire_reason "
						+ "WHERE a.provider_id = t.provider_id AND a.uuid = t.uuid");

		/* -- Locations -- */
		SYNC_QUERIES
				.add("UPDATE location_attribute_type AS a, temp_location_attribute_type AS t "
						+ "SET a.name = t.name, a.description = t.description, a.datatype = t.datatype, a.datatype_config = t.datatype_config, a.preferred_handler = t.preferred_handler, a.handler_config = t.handler_config, a.min_occurs = t.min_occurs, a.max_occurs = t.max_occurs, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.retired = t.retired, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retire_reason = t.retire_reason "
						+ "WHERE a.location_attribute_type_id = t.location_attribute_type_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE location AS a, temp_location AS t "
						+ "SET a.name = t.name, a.description = t.description, a.address1 = t.address1, a.address2 = t.address2, a.city_village = t.city_village, a.state_province = t.state_province, a.postal_code = t.postal_code, a.country = t.country, a.latitude = t.latitude, a.longitude = t.longitude, a.creator = t.creator, a.date_created = t.date_created, a.county_district = t.county_district, a.address3 = t.address3, a.address4 = t.address4, a.address5 = t.address5, a.address6 = t.address6, a.retired = t.retired, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retire_reason = t.retire_reason, a.parent_location = t.parent_location "
						+ "WHERE a.location_id = t.location_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE location_attribute AS a, temp_location_attribute AS t "
						+ "SET a.attribute_type_id = t.attribute_type_id, a.value_reference = t.value_reference, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.voided = t.voided, a.voided_by = t.voided_by, a.date_voided = t.date_voided, a.void_reason = t.void_reason "
						+ "WHERE a.location_attribute_id = t.location_attribute_id AND a.location_id = t.location_id AND a.uuid = t.uuid");

		/* -- Concepts -- */
		SYNC_QUERIES
				.add("UPDATE concept_class AS a, temp_concept_class AS t "
						+ "SET a.name = t.name, a.description = t.description, a.creator = t.creator, a.date_created = t.date_created, a.retired = t.retired, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retire_reason = t.retire_reason "
						+ "WHERE a.concept_class_id = t.concept_class_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE concept_datatype AS a, temp_concept_datatype AS t "
						+ "SET a.name = t.name, a.hl7_abbreviation = t.hl7_abbreviation, a.description = t.description, a.creator = t.creator, a.date_created = t.date_created, a.retired = t.retired, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retire_reason = t.retire_reason "
						+ "WHERE a.concept_datatype_id = t.concept_datatype_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE concept_map_type AS a, temp_concept_map_type AS t "
						+ "SET a.name = t.name, a.description = t.description, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.is_hidden = t.is_hidden, a.retired = t.retired, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retire_reason = t.retire_reason "
						+ "WHERE a.concept_map_type_id = t.concept_map_type_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE concept_stop_word AS a, temp_concept_stop_word AS t "
						+ "SET a.word = t.word, a.locale = t.locale "
						+ "WHERE a.concept_stop_word_id = t.concept_stop_word_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE concept AS a, temp_concept AS t "
						+ "SET a.retired = t.retired, a.short_name = t.short_name, a.description = t.description, a.form_text = t.form_text, a.datatype_id = t.datatype_id, a.class_id = t.class_id, a.is_set = t.is_set, a.creator = t.creator, a.date_created = t.date_created, a.version = t.version, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retire_reason = t.retire_reason "
						+ "WHERE a.concept_id = t.concept_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE concept_name AS a, temp_concept_name AS t "
						+ "SET a.concept_id = t.concept_id, a.name = t.name, a.locale = t.locale, a.locale_preferred = t.locale_preferred, a.creator = t.creator, a.date_created = t.date_created, a.concept_name_type = t.concept_name_type, a.voided = t.voided, a.voided_by = t.voided_by, a.date_voided = t.date_voided, a.void_reason = t.void_reason "
						+ "WHERE a.concept_name_id = t.concept_name_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE concept_numeric AS a, temp_concept_numeric AS t "
						+ "SET a.hi_absolute = t.hi_absolute, a.hi_critical = t.hi_critical, a.hi_normal = t.hi_normal, a.low_absolute = t.low_absolute, a.low_critical = t.low_critical, a.low_normal = t.low_normal, a.units = t.units, a.precise = t.precise "
						+ "WHERE a.concept_id = t.concept_id");

		SYNC_QUERIES
				.add("UPDATE concept_description AS a, temp_concept_description AS t "
						+ "SET a.concept_id = t.concept_id, a.description = t.description, a.locale = t.locale, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed "
						+ "WHERE a.concept_description_id = t.concept_description_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE concept_answer AS a, temp_concept_answer AS t "
						+ "SET a.concept_id = t.concept_id, a.answer_concept = t.answer_concept, a.answer_drug = t.answer_drug, a.creator = t.creator, a.date_created = t.date_created, a.sort_weight = t.sort_weight "
						+ "WHERE a.concept_answer_id = t.concept_answer_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE concept_word AS a, temp_concept_word AS t "
						+ "SET a.concept_id = t.concept_id, a.word = t.word, a.locale = t.locale, a.concept_name_id = t.concept_name_id, a.weight = t.weight "
						+ "WHERE a.concept_word_id = t.concept_word_id");

		SYNC_QUERIES
				.add("UPDATE concept_reference_map AS a, temp_concept_reference_map AS t "
						+ "SET a.concept_reference_term_id = t.concept_reference_term_id, a.concept_map_type_id = t.concept_map_type_id, a.creator = t.creator, a.date_created = t.date_created, a.concept_id = t.concept_id, a.changed_by = t.changed_by, a.date_changed = t.date_changed "
						+ "WHERE a.concept_map_id = t.concept_map_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE concept_reference_term AS a, temp_concept_reference_term AS t "
						+ "SET a.concept_source_id = t.concept_source_id, a.name = t.name, a.code = t.code, a.version = t.version, a.description = t.description, a.creator = t.creator, a.date_created = t.date_created, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.retired = t.retired, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retire_reason = t.retire_reason "
						+ "WHERE a.concept_reference_term_id = t.concept_reference_term_id AND a.uuid = t.uuid");

		/* -- Patients -- */
		SYNC_QUERIES
				.add("UPDATE patient AS a, temp_patient AS t "
						+ "SET a.tribe = t.tribe, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.voided = t.voided, a.voided_by = t.voided_by, a.date_voided = t.date_voided, a.void_reason = t.void_reason "
						+ "WHERE a.patient_id = t.patient_id");

		SYNC_QUERIES
				.add("UPDATE patient_identifier AS a, temp_patient_identifier AS t "
						+ "SET a.patient_id = t.patient_id, a.identifier = t.identifier, a.identifier_type = t.identifier_type, a.preferred = t.preferred, a.location_id = t.location_id, a.creator = t.creator, a.date_created = t.date_created, a.date_changed = t.date_changed, a.changed_by = t.changed_by, a.voided = t.voided, a.voided_by = t.voided_by, a.date_voided = t.date_voided, a.void_reason = t.void_reason "
						+ "WHERE a.patient_identifier_id = t.patient_identifier_id AND a.uuid = t.uuid");

		/* -- Encounters & Obs -- */
		SYNC_QUERIES
				.add("UPDATE encounter_type AS a, temp_encounter_type AS t "
						+ "SET a.name = t.name, a.description = t.description, a.creator = t.creator, a.date_created = t.date_created, a.retired = t.retired, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retire_reason = t.retire_reason "
						+ "WHERE a.encounter_type_id = t.encounter_type_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE form AS a, temp_form AS t "
						+ "SET a.name = t.name, a.version = t.version, a.build = t.build, a.published = t.published, a.xslt = t.xslt, a.template = t.template, a.description = t.description, a.encounter_type = t.encounter_type, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.retired = t.retired, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retired_reason = t.retired_reason "
						+ "WHERE a.form_id = t.form_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE encounter_role AS a, temp_encounter_role AS t "
						+ "SET a.name = t.name, a.description = t.description, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.retired = t.retired, a.retired_by = t.retired_by, a.date_retired = t.date_retired, a.retire_reason = t.retire_reason "
						+ "WHERE a.encounter_role_id = t.encounter_role_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE encounter AS a, temp_encounter AS t "
						+ "SET a.encounter_type = t.encounter_type, a.patient_id = t.patient_id, a.location_id = t.location_id, a.form_id = t.form_id, a.encounter_datetime = t.encounter_datetime, a.creator = t.creator, a.date_created = t.date_created, a.voided = t.voided, a.voided_by = t.voided_by, a.date_voided = t.date_voided, a.void_reason = t.void_reason, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.visit_id = t.visit_id "
						+ "WHERE a.encounter_id = t.encounter_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE encounter_provider AS a, temp_encounter_provider AS t "
						+ "SET a.encounter_id = t.encounter_id, a.provider_id = t.provider_id, a.encounter_role_id = t.encounter_role_id, a.creator = t.creator, a.date_created = t.date_created, a.changed_by = t.changed_by, a.date_changed = t.date_changed, a.voided = t.voided, a.date_voided = t.date_voided, a.voided_by = t.voided_by, a.void_reason = t.void_reason "
						+ "WHERE a.encounter_provider_id = t.encounter_provider_id AND a.uuid = t.uuid");

		SYNC_QUERIES
				.add("UPDATE obs AS a, temp_obs AS t "
						+ "SET a.person_id = t.person_id, a.concept_id = t.concept_id, a.encounter_id = t.encounter_id, a.order_id = t.order_id, a.obs_datetime = t.obs_datetime, a.location_id = t.location_id, a.obs_group_id = t.obs_group_id, a.accession_number = t.accession_number, a.value_group_id = t.value_group_id, a.value_boolean = t.value_boolean, a.value_coded = t.value_coded, a.value_coded_name_id = t.value_coded_name_id, a.value_drug = t.value_drug, a.value_datetime = t.value_datetime, a.value_numeric = t.value_numeric, a.value_modifier = t.value_modifier, a.value_text = t.value_text, a.value_complex = t.value_complex, a.comments = t.comments, a.creator = t.creator, a.date_created = t.date_created, a.voided = t.voided, a.voided_by = t.voided_by, a.date_voided = t.date_voided, a.void_reason = t.void_reason, a.previous_version = t.previous_version "
						+ "WHERE a.obs_id = t.obs_id AND a.uuid = t.uuid");

	}

}
