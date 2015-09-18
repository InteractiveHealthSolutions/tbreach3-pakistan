-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`%` PROCEDURE `sync_openmrs`()
BEGIN

SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept where concept.uuid IN (select uuid from openmrs.temp_concept);
insert ignore into openmrs.concept select * from openmrs.temp_concept;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.cohort where cohort.uuid IN (select uuid from openmrs.temp_cohort);
insert ignore into openmrs.cohort select * from openmrs.temp_cohort;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_description where concept_description.uuid IN (select uuid from openmrs.temp_concept_description);
insert ignore into openmrs.concept_description select * from openmrs.temp_concept_description;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_map_type where concept_map_type.uuid IN (select uuid from openmrs.temp_concept_map_type);
insert ignore into openmrs.concept_map_type select * from openmrs.temp_concept_map_type;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_proposal where concept_proposal.uuid IN (select uuid from openmrs.temp_concept_proposal);
insert ignore into openmrs.concept_proposal select * from openmrs.temp_concept_proposal;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_reference_map where concept_reference_map.uuid IN (select uuid from openmrs.temp_concept_reference_map);
insert ignore into openmrs.concept_reference_map select * from openmrs.temp_concept_reference_map;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_reference_term where concept_reference_term.uuid IN (select uuid from openmrs.temp_concept_reference_term);
insert ignore into openmrs.concept_reference_term select * from openmrs.temp_concept_reference_term;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_reference_term_map where concept_reference_term_map.uuid IN (select uuid from openmrs.temp_concept_reference_term_map);
insert ignore into openmrs.concept_reference_term_map select * from openmrs.temp_concept_reference_term_map;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.drug where drug.uuid IN (select uuid from openmrs.temp_drug);
insert ignore into openmrs.drug select * from openmrs.temp_drug;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.encounter where encounter.uuid IN (select uuid from openmrs.temp_encounter);
insert ignore into openmrs.encounter select * from openmrs.temp_encounter;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.encounter_provider where encounter_provider.uuid IN (select uuid from openmrs.temp_encounter_provider);
insert ignore into openmrs.encounter_provider select * from openmrs.temp_encounter_provider;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.encounter_role where encounter_role.uuid IN (select uuid from openmrs.temp_encounter_role);
insert ignore into openmrs.encounter_role select * from openmrs.temp_encounter_role;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.field where field.uuid IN (select uuid from openmrs.temp_field);
insert ignore into openmrs.field select * from openmrs.temp_field;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.form where form.uuid IN (select uuid from openmrs.temp_form);
insert ignore into openmrs.form select * from openmrs.temp_form;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.form_field where form_field.uuid IN (select uuid from openmrs.temp_form_field);
insert ignore into openmrs.form_field select * from openmrs.temp_form_field;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.location_attribute where location_attribute.uuid IN (select uuid from openmrs.temp_location_attribute);
insert ignore into openmrs.location_attribute select * from openmrs.temp_location_attribute;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.note where note.uuid IN (select uuid from openmrs.temp_note);
insert ignore into openmrs.note select * from openmrs.temp_note;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.notification_alert where notification_alert.uuid IN (select uuid from openmrs.temp_notification_alert);
insert ignore into openmrs.notification_alert select * from openmrs.temp_notification_alert;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.patient_identifier where patient_identifier.uuid IN (select uuid from openmrs.temp_patient_identifier);
insert ignore into openmrs.patient_identifier select * from openmrs.temp_patient_identifier;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.patient_program where patient_program.uuid IN (select uuid from openmrs.temp_patient_program);
insert ignore into openmrs.patient_program select * from openmrs.temp_patient_program;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.patient_state where patient_state.uuid IN (select uuid from openmrs.temp_patient_state);
insert ignore into openmrs.patient_state select * from openmrs.temp_patient_state;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.person where person.uuid IN (select uuid from openmrs.temp_person);
insert ignore into openmrs.person select * from openmrs.temp_person;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.person_address where person_address.uuid IN (select uuid from openmrs.temp_person_address);
insert ignore into openmrs.person_address select * from openmrs.temp_person_address;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.person_attribute where person_attribute.uuid IN (select uuid from openmrs.temp_person_attribute);
insert ignore into openmrs.person_attribute select * from openmrs.temp_person_attribute;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.person_attribute_type where person_attribute_type.uuid IN (select uuid from openmrs.temp_person_attribute_type);
insert ignore into openmrs.person_attribute_type select * from openmrs.temp_person_attribute_type;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.person_merge_log where person_merge_log.uuid IN (select uuid from openmrs.temp_person_merge_log);
insert ignore into openmrs.person_merge_log select * from openmrs.temp_person_merge_log;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.person_name where person_name.uuid IN (select uuid from openmrs.temp_person_name);
insert ignore into openmrs.person_name select * from openmrs.temp_person_name;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.program where program.uuid IN (select uuid from openmrs.temp_program);
insert ignore into openmrs.program select * from openmrs.temp_program;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.program_workflow where program_workflow.uuid IN (select uuid from openmrs.temp_program_workflow);
insert ignore into openmrs.program_workflow select * from openmrs.temp_program_workflow;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.program_workflow_state where program_workflow_state.uuid IN (select uuid from openmrs.temp_program_workflow_state);
insert ignore into openmrs.program_workflow_state select * from openmrs.temp_program_workflow_state;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.provider_attribute where provider_attribute.uuid IN (select uuid from openmrs.temp_provider_attribute);
insert ignore into openmrs.provider_attribute select * from openmrs.temp_provider_attribute;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.provider_attribute_type where provider_attribute_type.uuid IN (select uuid from openmrs.temp_provider_attribute_type);
insert ignore into openmrs.provider_attribute_type select * from openmrs.temp_provider_attribute_type;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.relationship where relationship.uuid IN (select uuid from openmrs.temp_relationship);
insert ignore into openmrs.relationship select * from openmrs.temp_relationship;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.scheduler_task_config where scheduler_task_config.uuid IN (select uuid from openmrs.temp_scheduler_task_config);
insert ignore into openmrs.scheduler_task_config select * from openmrs.temp_scheduler_task_config;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.serialized_object where serialized_object.uuid IN (select uuid from openmrs.temp_serialized_object);
insert ignore into openmrs.serialized_object select * from openmrs.temp_serialized_object;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.users where users.uuid IN (select uuid from openmrs.temp_users);
insert ignore into openmrs.users select * from openmrs.temp_users;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.visit where visit.uuid IN (select uuid from openmrs.temp_visit);
insert ignore into openmrs.visit select * from openmrs.temp_visit;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.visit_attribute where visit_attribute.uuid IN (select uuid from openmrs.temp_visit_attribute);
insert ignore into openmrs.visit_attribute select * from openmrs.temp_visit_attribute;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.visit_attribute_type where visit_attribute_type.uuid IN (select uuid from openmrs.temp_visit_attribute_type);
insert ignore into openmrs.visit_attribute_type select * from openmrs.temp_visit_attribute_type;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.visit_type where visit_type.uuid IN (select uuid from openmrs.temp_visit_type);
insert ignore into openmrs.visit_type select * from openmrs.temp_visit_type;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.notification_alert_recipient where notification_alert_recipient.uuid IN (select uuid from openmrs.temp_notification_alert_recipient);
insert ignore into openmrs.notification_alert_recipient select * from openmrs.temp_notification_alert_recipient;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.active_list_type where active_list_type.uuid IN (select uuid from openmrs.temp_active_list_type);
insert ignore into openmrs.active_list_type select * from openmrs.temp_active_list_type;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_answer where concept_answer.uuid IN (select uuid from openmrs.temp_concept_answer);
insert ignore into openmrs.concept_answer select * from openmrs.temp_concept_answer;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_class where concept_class.uuid IN (select uuid from openmrs.temp_concept_class);
insert ignore into openmrs.concept_class select * from openmrs.temp_concept_class;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_datatype where concept_datatype.uuid IN (select uuid from openmrs.temp_concept_datatype);
insert ignore into openmrs.concept_datatype select * from openmrs.temp_concept_datatype;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_name where concept_name.uuid IN (select uuid from openmrs.temp_concept_name);
insert ignore into openmrs.concept_name select * from openmrs.temp_concept_name;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_name_bkp where concept_name_bkp.uuid IN (select uuid from openmrs.temp_concept_name_bkp);
insert ignore into openmrs.concept_name_bkp select * from openmrs.temp_concept_name_bkp;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_name_tag where concept_name_tag.uuid IN (select uuid from openmrs.temp_concept_name_tag);
insert ignore into openmrs.concept_name_tag select * from openmrs.temp_concept_name_tag;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_reference_source where concept_reference_source.uuid IN (select uuid from openmrs.temp_concept_reference_source);
insert ignore into openmrs.concept_reference_source select * from openmrs.temp_concept_reference_source;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_set where concept_set.uuid IN (select uuid from openmrs.temp_concept_set);
insert ignore into openmrs.concept_set select * from openmrs.temp_concept_set;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.encounter_type where encounter_type.uuid IN (select uuid from openmrs.temp_encounter_type);
insert ignore into openmrs.encounter_type select * from openmrs.temp_encounter_type;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.field_answer where field_answer.uuid IN (select uuid from openmrs.temp_field_answer);
insert ignore into openmrs.field_answer select * from openmrs.temp_field_answer;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.field_type where field_type.uuid IN (select uuid from openmrs.temp_field_type);
insert ignore into openmrs.field_type select * from openmrs.temp_field_type;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.location where location.uuid IN (select uuid from openmrs.temp_location);
insert ignore into openmrs.location select * from openmrs.temp_location; 
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.location_tag where location_tag.uuid IN (select uuid from openmrs.temp_location_tag);
insert ignore into openmrs.location_tag select * from openmrs.temp_location_tag;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.obs where obs.uuid IN (select uuid from openmrs.temp_obs);
insert ignore into openmrs.obs select * from openmrs.temp_obs;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.order_type where order_type.uuid IN (select uuid from openmrs.temp_order_type);
insert ignore into openmrs.order_type select * from openmrs.temp_order_type;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.orders where orders.uuid IN (select uuid from openmrs.temp_orders);
insert ignore into openmrs.orders select * from openmrs.temp_orders;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.patient_identifier_type where patient_identifier_type.uuid IN (select uuid from openmrs.temp_patient_identifier_type);
insert ignore into openmrs.patient_identifier_type select * from openmrs.temp_patient_identifier_type;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.relationship where relationship.uuid IN (select uuid from openmrs.temp_relationship);
insert ignore into openmrs.relationship select * from openmrs.temp_relationship;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.relationship_type where relationship_type.uuid IN (select uuid from openmrs.temp_relationship_type);
insert ignore into openmrs.relationship_type select * from openmrs.temp_relationship_type;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.concept_stop_word where concept_stop_word.uuid IN (select uuid from openmrs.temp_concept_stop_word);
insert ignore into openmrs.concept_stop_word select * from openmrs.temp_concept_stop_word;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.form_resource where form_resource.uuid IN (select uuid from openmrs.temp_form_resource);
insert ignore into openmrs.form_resource select * from openmrs.temp_form_resource;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.global_property where global_property.uuid IN (select uuid from openmrs.temp_global_property);
insert ignore into openmrs.global_property select * from openmrs.temp_global_property;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.notification_template where notification_template.uuid IN (select uuid from openmrs.temp_notification_template);
insert ignore into openmrs.notification_template select * from openmrs.temp_notification_template;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.privilege where privilege.uuid IN (select uuid from openmrs.temp_privilege);
insert ignore into openmrs.privilege select * from openmrs.temp_privilege;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.role where role.uuid IN (select uuid from openmrs.temp_role);
insert ignore into openmrs.role select * from openmrs.temp_role;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from openmrs.role where role.uuid IN (select uuid from openmrs.temp_role);
insert ignore into openmrs.role select * from openmrs.temp_role;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from cohort_member where (cohort_member.cohort_id, cohort_member.patient_id) IN (select cohort_id, patient_id from openmrs.temp_cohort_member);
insert ignore into openmrs.cohort_member select * from openmrs.temp_cohort_member;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from concept_complex where (concept_complex.concept_id) IN (select concept_id from openmrs.temp_concept_complex);
insert ignore into openmrs.concept_complex select * from openmrs.temp_concept_complex;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from concept_name_tag_map where (concept_name_tag_map.concept_name_id, concept_name_tag_map.concept_name_tag_id) IN (select concept_name_id, concept_name_tag_id from openmrs.temp_concept_name_tag_map);
insert ignore into openmrs.concept_name_tag_map select * from openmrs.temp_concept_name_tag_map;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from concept_numeric where (concept_numeric.concept_id) IN (select concept_id from openmrs.temp_concept_numeric);
insert ignore into openmrs.concept_numeric select * from openmrs.temp_concept_numeric;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from concept_proposal_tag_map where (concept_proposal_tag_map.concept_proposal_id, concept_proposal_tag_map.concept_name_tag_id) IN (select concept_proposal_id, concept_name_tag_id from openmrs.temp_concept_proposal_tag_map);
insert ignore into openmrs.concept_proposal_tag_map select * from openmrs.temp_concept_proposal_tag_map;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from concept_set_derived where (concept_set_derived.concept_id, concept_set_derived.concept_set) IN (select concept_id,concept_set from openmrs.temp_concept_set_derived);
insert ignore into openmrs.concept_set_derived select * from openmrs.temp_concept_set_derived;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from concept_state_conversion where (concept_state_conversion.concept_state_conversion_id) IN (select concept_state_conversion_id from openmrs.temp_concept_state_conversion);
insert ignore into openmrs.concept_state_conversion select * from openmrs.temp_concept_state_conversion;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from concept_word where (concept_word.concept_word_id) IN (select concept_word_id from openmrs.temp_concept_word);
insert ignore into openmrs.concept_word select * from openmrs.temp_concept_word;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from drug_ingredient where (drug_ingredient.concept_id, drug_ingredient.ingredient_id) IN (select concept_id, ingredient_id from openmrs.temp_drug_ingredient);
insert ignore into openmrs.drug_ingredient select * from openmrs.temp_drug_ingredient;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from drug_order where (drug_order.order_id) IN (select order_id from openmrs.temp_drug_order);
insert ignore into openmrs.drug_order select * from openmrs.temp_drug_order;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from location_tag_map where (location_tag_map.location_id, location_tag_map.location_tag_id) IN (select location_id, location_tag_id from openmrs.temp_location_tag_map);
insert ignore into openmrs.location_tag_map select * from openmrs.temp_location_tag_map;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from patient where (patient.patient_id) IN (select patient_id from openmrs.temp_patient);
insert ignore into openmrs.patient select * from openmrs.temp_patient;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from role_privilege where (role_privilege.role, role_privilege.privilege) IN (select role, privilege from openmrs.temp_role_privilege);
insert ignore into openmrs.role_privilege select * from openmrs.temp_role_privilege;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from role_role where (role_role.parent_role, role_role.child_role) IN (select parent_role, child_role from openmrs.temp_role_role);
insert ignore into openmrs.role_role select * from openmrs.temp_role_role;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from scheduler_task_config_property where (scheduler_task_config_property.task_config_property_id) IN (select task_config_property_id from openmrs.temp_scheduler_task_config_property);
insert ignore into openmrs.scheduler_task_config_property select * from openmrs.temp_scheduler_task_config_property;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from test_order where (test_order.order_id) IN (select order_id from openmrs.temp_test_order);
insert ignore into openmrs.test_order select * from openmrs.temp_test_order;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from user_property where (user_property.user_id, user_property.property) IN (select user_id, property from openmrs.temp_user_property);
insert ignore into openmrs.user_property select * from openmrs.temp_user_property;
SET SQL_SAFE_UPDATES = 0;
set FOREIGN_KEY_CHECKS = 0;

delete from user_role where (user_role.user_id, user_role.role) IN (select user_id, role from openmrs.temp_user_role);
insert ignore into openmrs.user_role select * from temp_user_role;

SET SQL_SAFE_UPDATES = 1;
set FOREIGN_KEY_CHECKS = 1;


END