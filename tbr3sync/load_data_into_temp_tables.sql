DROP TABLE IF EXISTS openmrs.temp_concept;
CREATE TABLE openmrs.temp_concept LIKE openmrs.concept;
LOAD DATA LOCAL INFILE '/tmp/sz/concept.csv' INTO TABLE openmrs.temp_concept FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_cohort;
CREATE TABLE openmrs.temp_cohort LIKE openmrs.cohort;
LOAD DATA LOCAL INFILE '/tmp/sz/cohort.csv' INTO TABLE openmrs.temp_cohort FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_description;
CREATE TABLE openmrs.temp_concept_description LIKE openmrs.concept_description;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_description.csv' INTO TABLE openmrs.temp_concept_description FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_map_type;
CREATE TABLE openmrs.temp_concept_map_type LIKE openmrs.concept_map_type;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_map_type.csv' INTO TABLE openmrs.temp_concept_map_type FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_proposal;
CREATE TABLE openmrs.temp_concept_proposal LIKE openmrs.concept_proposal;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_proposal.csv' INTO TABLE openmrs.temp_concept_proposal FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_reference_map;
CREATE TABLE openmrs.temp_concept_reference_map LIKE openmrs.concept_reference_map;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_reference_map.csv' INTO TABLE openmrs.temp_concept_reference_map FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_reference_term;
CREATE TABLE openmrs.temp_concept_reference_term LIKE openmrs.concept_reference_term;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_reference_term.csv' INTO TABLE openmrs.temp_concept_reference_term FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_reference_term_map;
CREATE TABLE openmrs.temp_concept_reference_term_map LIKE openmrs.concept_reference_term_map;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_reference_term_map.csv' INTO TABLE openmrs.temp_concept_reference_term_map FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_drug;
CREATE TABLE openmrs.temp_drug LIKE openmrs.drug;
LOAD DATA LOCAL INFILE '/tmp/sz/drug.csv' INTO TABLE openmrs.temp_drug FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_encounter;
CREATE TABLE openmrs.temp_encounter LIKE openmrs.encounter;
LOAD DATA LOCAL INFILE '/tmp/sz/encounter.csv' INTO TABLE openmrs.temp_encounter FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_encounter_provider;
CREATE TABLE openmrs.temp_encounter_provider LIKE openmrs.encounter_provider;
LOAD DATA LOCAL INFILE '/tmp/sz/encounter_provider.csv' INTO TABLE openmrs.temp_encounter_provider FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_encounter_role;
CREATE TABLE openmrs.temp_encounter_role LIKE openmrs.encounter_role;
LOAD DATA LOCAL INFILE '/tmp/sz/encounter_role.csv' INTO TABLE openmrs.temp_encounter_role FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_field;
CREATE TABLE openmrs.temp_field LIKE openmrs.field;
LOAD DATA LOCAL INFILE '/tmp/sz/field.csv' INTO TABLE openmrs.temp_field FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_form;
CREATE TABLE openmrs.temp_form LIKE openmrs.form;
LOAD DATA LOCAL INFILE '/tmp/sz/form.csv' INTO TABLE openmrs.temp_form FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_form_field;
CREATE TABLE openmrs.temp_form_field LIKE openmrs.form_field;
LOAD DATA LOCAL INFILE '/tmp/sz/form_field.csv' INTO TABLE openmrs.temp_form_field FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_location_attribute;
CREATE TABLE openmrs.temp_location_attribute LIKE openmrs.location_attribute;
LOAD DATA LOCAL INFILE '/tmp/sz/location_attribute.csv' INTO TABLE openmrs.temp_location_attribute FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_location_attribute_type;
CREATE TABLE openmrs.temp_location_attribute_type LIKE openmrs.location_attribute_type;
LOAD DATA LOCAL INFILE '/tmp/sz/location_attribute_type.csv' INTO TABLE openmrs.temp_location_attribute_type FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_note;
CREATE TABLE openmrs.temp_note LIKE openmrs.note;
LOAD DATA LOCAL INFILE '/tmp/sz/note.csv' INTO TABLE openmrs.temp_note FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_notification_alert;
CREATE TABLE openmrs.temp_notification_alert LIKE openmrs.notification_alert;
LOAD DATA LOCAL INFILE '/tmp/sz/notification_alert.csv' INTO TABLE openmrs.temp_notification_alert FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_patient;
CREATE TABLE openmrs.temp_patient LIKE openmrs.patient;
LOAD DATA LOCAL INFILE '/tmp/sz/patient.csv' INTO TABLE openmrs.temp_patient FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_patient_identifier;
CREATE TABLE openmrs.temp_patient_identifier LIKE openmrs.patient_identifier;
LOAD DATA LOCAL INFILE '/tmp/sz/patient_identifier.csv' INTO TABLE openmrs.temp_patient_identifier FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_patient_program;
CREATE TABLE openmrs.temp_patient_program LIKE openmrs.patient_program;
LOAD DATA LOCAL INFILE '/tmp/sz/patient_program.csv' INTO TABLE openmrs.temp_patient_program FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_patient_state;
CREATE TABLE openmrs.temp_patient_state LIKE openmrs.patient_state;
LOAD DATA LOCAL INFILE '/tmp/sz/patient_state.csv' INTO TABLE openmrs.temp_patient_state FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_person;
CREATE TABLE openmrs.temp_person LIKE openmrs.person;
LOAD DATA LOCAL INFILE '/tmp/sz/person.csv' INTO TABLE openmrs.temp_person FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_person_address;
CREATE TABLE openmrs.temp_person_address LIKE openmrs.person_address;
LOAD DATA LOCAL INFILE '/tmp/sz/person_address.csv' INTO TABLE openmrs.temp_person_address FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_person_attribute;
CREATE TABLE openmrs.temp_person_attribute LIKE openmrs.person_attribute;
LOAD DATA LOCAL INFILE '/tmp/sz/person_attribute.csv' INTO TABLE openmrs.temp_person_attribute FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_person_attribute_type;
CREATE TABLE openmrs.temp_person_attribute_type LIKE openmrs.person_attribute_type;
LOAD DATA LOCAL INFILE '/tmp/sz/person_attribute_type.csv' INTO TABLE openmrs.temp_person_attribute_type FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_person_merge_log;
CREATE TABLE openmrs.temp_person_merge_log LIKE openmrs.person_merge_log;
LOAD DATA LOCAL INFILE '/tmp/sz/person_merge_log.csv' INTO TABLE openmrs.temp_person_merge_log FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_person_name;
CREATE TABLE openmrs.temp_person_name LIKE openmrs.person_name;
LOAD DATA LOCAL INFILE '/tmp/sz/person_name.csv' INTO TABLE openmrs.temp_person_name FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_program;
CREATE TABLE openmrs.temp_program LIKE openmrs.program;
LOAD DATA LOCAL INFILE '/tmp/sz/program.csv' INTO TABLE openmrs.temp_program FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_program_workflow;
CREATE TABLE openmrs.temp_program_workflow LIKE openmrs.program_workflow;
LOAD DATA LOCAL INFILE '/tmp/sz/program_workflow.csv' INTO TABLE openmrs.temp_program_workflow FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_program_workflow_state;
CREATE TABLE openmrs.temp_program_workflow_state LIKE openmrs.program_workflow_state;
LOAD DATA LOCAL INFILE '/tmp/sz/program_workflow_state.csv' INTO TABLE openmrs.temp_program_workflow_state FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_provider;
CREATE TABLE openmrs.temp_provider LIKE openmrs.provider;
LOAD DATA LOCAL INFILE '/tmp/sz/provider.csv' INTO TABLE openmrs.temp_provider FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_provider_attribute;
CREATE TABLE openmrs.temp_provider_attribute LIKE openmrs.provider_attribute;
LOAD DATA LOCAL INFILE '/tmp/sz/provider_attribute.csv' INTO TABLE openmrs.temp_provider_attribute FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_provider_attribute_type;
CREATE TABLE openmrs.temp_provider_attribute_type LIKE openmrs.provider_attribute_type;
LOAD DATA LOCAL INFILE '/tmp/sz/provider_attribute_type.csv' INTO TABLE openmrs.temp_provider_attribute_type FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_relationship;
CREATE TABLE openmrs.temp_relationship LIKE openmrs.relationship;
LOAD DATA LOCAL INFILE '/tmp/sz/relationship.csv' INTO TABLE openmrs.temp_relationship FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_scheduler_task_config;
CREATE TABLE openmrs.temp_scheduler_task_config LIKE openmrs.scheduler_task_config;
LOAD DATA LOCAL INFILE '/tmp/sz/scheduler_task_config.csv' INTO TABLE openmrs.temp_scheduler_task_config FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_serialized_object;
CREATE TABLE openmrs.temp_serialized_object LIKE openmrs.serialized_object;
LOAD DATA LOCAL INFILE '/tmp/sz/serialized_object.csv' INTO TABLE openmrs.temp_serialized_object FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_users;
CREATE TABLE openmrs.temp_users LIKE openmrs.users;
LOAD DATA LOCAL INFILE '/tmp/sz/users.csv' INTO TABLE openmrs.temp_users FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_visit;
CREATE TABLE openmrs.temp_visit LIKE openmrs.visit;
LOAD DATA LOCAL INFILE '/tmp/sz/visit.csv' INTO TABLE openmrs.temp_visit FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_visit_attribute;
CREATE TABLE openmrs.temp_visit_attribute LIKE openmrs.visit_attribute;
LOAD DATA LOCAL INFILE '/tmp/sz/visit_attribute.csv' INTO TABLE openmrs.temp_visit_attribute FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_visit_attribute_type;
CREATE TABLE openmrs.temp_visit_attribute_type LIKE openmrs.visit_attribute_type;
LOAD DATA LOCAL INFILE '/tmp/sz/visit_attribute_type.csv' INTO TABLE openmrs.temp_visit_attribute_type FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_visit_type;
CREATE TABLE openmrs.temp_visit_type LIKE openmrs.visit_type;
LOAD DATA LOCAL INFILE '/tmp/sz/visit_type.csv' INTO TABLE openmrs.temp_visit_type FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_notification_alert_recipient;
CREATE TABLE openmrs.temp_notification_alert_recipient LIKE openmrs.notification_alert_recipient;
LOAD DATA LOCAL INFILE '/tmp/sz/notification_alert_recipient.csv' INTO TABLE openmrs.temp_notification_alert_recipient FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_active_list_type;
CREATE TABLE openmrs.temp_active_list_type LIKE openmrs.active_list_type;
LOAD DATA LOCAL INFILE '/tmp/sz/active_list_type.csv' INTO TABLE openmrs.temp_active_list_type FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_answer;
CREATE TABLE openmrs.temp_concept_answer LIKE openmrs.concept_answer;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_answer.csv' INTO TABLE openmrs.temp_concept_answer FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_class;
CREATE TABLE openmrs.temp_concept_class LIKE openmrs.concept_class;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_class.csv' INTO TABLE openmrs.temp_concept_class FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_datatype;
CREATE TABLE openmrs.temp_concept_datatype LIKE openmrs.concept_datatype;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_datatype.csv' INTO TABLE openmrs.temp_concept_datatype FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_name;
CREATE TABLE openmrs.temp_concept_name LIKE openmrs.concept_name;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_name.csv' INTO TABLE openmrs.temp_concept_name FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

-- DROP TABLE IF EXISTS openmrs.temp_concept_name_bkp;
-- CREATE TABLE openmrs.temp_concept_name_bkp LIKE openmrs.concept_name_bkp;
-- LOAD DATA LOCAL INFILE '/tmp/sz/concept_name_bkp.csv' INTO TABLE openmrs.temp_concept_name_bkp FIELDS TERMINATED BY ',' ENCLOSED BY '\"' -- -- LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_name_tag;
CREATE TABLE openmrs.temp_concept_name_tag LIKE openmrs.concept_name_tag;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_name_tag.csv' INTO TABLE openmrs.temp_concept_name_tag FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_reference_source;
CREATE TABLE openmrs.temp_concept_reference_source LIKE openmrs.concept_reference_source;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_reference_source.csv' INTO TABLE openmrs.temp_concept_reference_source FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_set;
CREATE TABLE openmrs.temp_concept_set LIKE openmrs.concept_set;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_set.csv' INTO TABLE openmrs.temp_concept_set FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_encounter_type;
CREATE TABLE openmrs.temp_encounter_type LIKE openmrs.encounter_type;
LOAD DATA LOCAL INFILE '/tmp/sz/encounter_type.csv' INTO TABLE openmrs.temp_encounter_type FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_field_answer;
CREATE TABLE openmrs.temp_field_answer LIKE openmrs.field_answer;
LOAD DATA LOCAL INFILE '/tmp/sz/field_answer.csv' INTO TABLE openmrs.temp_field_answer FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_field_type;
CREATE TABLE openmrs.temp_field_type LIKE openmrs.field_type;
LOAD DATA LOCAL INFILE '/tmp/sz/field_type.csv' INTO TABLE openmrs.temp_field_type FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_location;
CREATE TABLE openmrs.temp_location LIKE openmrs.location;
LOAD DATA LOCAL INFILE '/tmp/sz/location.csv' INTO TABLE openmrs.temp_location FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_location_tag;
CREATE TABLE openmrs.temp_location_tag LIKE openmrs.location_tag;
LOAD DATA LOCAL INFILE '/tmp/sz/location_tag.csv' INTO TABLE openmrs.temp_location_tag FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_obs;
CREATE TABLE openmrs.temp_obs LIKE openmrs.obs;
LOAD DATA LOCAL INFILE '/tmp/sz/obs.csv' INTO TABLE openmrs.temp_obs FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_order_type;
CREATE TABLE openmrs.temp_order_type LIKE openmrs.order_type;
LOAD DATA LOCAL INFILE '/tmp/sz/order_type.csv' INTO TABLE openmrs.temp_order_type FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_orders;
CREATE TABLE openmrs.temp_orders LIKE openmrs.orders;
LOAD DATA LOCAL INFILE '/tmp/sz/orders.csv' INTO TABLE openmrs.temp_orders FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_patient_identifier_type;
CREATE TABLE openmrs.temp_patient_identifier_type LIKE openmrs.patient_identifier_type;
LOAD DATA LOCAL INFILE '/tmp/sz/patient_identifier_type.csv' INTO TABLE openmrs.temp_patient_identifier_type FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_relationship_type;
CREATE TABLE openmrs.temp_relationship_type LIKE openmrs.relationship_type;
LOAD DATA LOCAL INFILE '/tmp/sz/relationship_type.csv' INTO TABLE openmrs.temp_relationship_type FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_cohort_member;
CREATE TABLE openmrs.temp_cohort_member LIKE openmrs.cohort_member;
LOAD DATA LOCAL INFILE '/tmp/sz/cohort_member.csv' INTO TABLE openmrs.temp_cohort_member FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_complex;
CREATE TABLE openmrs.temp_concept_complex LIKE openmrs.concept_complex;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_complex.csv' INTO TABLE openmrs.temp_concept_complex FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_name_tag_map;
CREATE TABLE openmrs.temp_concept_name_tag_map LIKE openmrs.concept_name_tag_map;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_name_tag_map.csv' INTO TABLE openmrs.temp_concept_name_tag_map FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_numeric;
CREATE TABLE openmrs.temp_concept_numeric LIKE openmrs.concept_numeric;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_numeric.csv' INTO TABLE openmrs.temp_concept_numeric FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_proposal_tag_map;
CREATE TABLE openmrs.temp_concept_proposal_tag_map LIKE openmrs.concept_proposal_tag_map;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_proposal_tag_map.csv' INTO TABLE openmrs.temp_concept_proposal_tag_map FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_set_derived;
CREATE TABLE openmrs.temp_concept_set_derived LIKE openmrs.concept_set_derived;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_set_derived.csv' INTO TABLE openmrs.temp_concept_set_derived FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_state_conversion;
CREATE TABLE openmrs.temp_concept_state_conversion LIKE openmrs.concept_state_conversion;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_state_conversion.csv' INTO TABLE openmrs.temp_concept_state_conversion FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_stop_word;
CREATE TABLE openmrs.temp_concept_stop_word LIKE openmrs.concept_stop_word;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_stop_word.csv' INTO TABLE openmrs.temp_concept_stop_word FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_concept_word;
CREATE TABLE openmrs.temp_concept_word LIKE openmrs.concept_word;
LOAD DATA LOCAL INFILE '/tmp/sz/concept_word.csv' INTO TABLE openmrs.temp_concept_word FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_drug_ingredient;
CREATE TABLE openmrs.temp_drug_ingredient LIKE openmrs.drug_ingredient;
LOAD DATA LOCAL INFILE '/tmp/sz/drug_ingredient.csv' INTO TABLE openmrs.temp_drug_ingredient FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_drug_order;
CREATE TABLE openmrs.temp_drug_order LIKE openmrs.drug_order;
LOAD DATA LOCAL INFILE '/tmp/sz/drug_order.csv' INTO TABLE openmrs.temp_drug_order FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_form_resource;
CREATE TABLE openmrs.temp_form_resource LIKE openmrs.form_resource;
LOAD DATA LOCAL INFILE '/tmp/sz/form_resource.csv' INTO TABLE openmrs.temp_form_resource FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_global_property;
CREATE TABLE openmrs.temp_global_property LIKE openmrs.global_property;
LOAD DATA LOCAL INFILE '/tmp/sz/global_property.csv' INTO TABLE openmrs.temp_global_property FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_location_tag_map;
CREATE TABLE openmrs.temp_location_tag_map LIKE openmrs.location_tag_map;
LOAD DATA LOCAL INFILE '/tmp/sz/location_tag_map.csv' INTO TABLE openmrs.temp_location_tag_map FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_notification_template;
CREATE TABLE openmrs.temp_notification_template LIKE openmrs.notification_template;
LOAD DATA LOCAL INFILE '/tmp/sz/notification_template.csv' INTO TABLE openmrs.temp_notification_template FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_privilege;
CREATE TABLE openmrs.temp_privilege LIKE openmrs.privilege;
LOAD DATA LOCAL INFILE '/tmp/sz/privilege.csv' INTO TABLE openmrs.temp_privilege FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_role;
CREATE TABLE openmrs.temp_role LIKE openmrs.role;
LOAD DATA LOCAL INFILE '/tmp/sz/role.csv' INTO TABLE openmrs.temp_role FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_role_privilege;
CREATE TABLE openmrs.temp_role_privilege LIKE openmrs.role_privilege;
LOAD DATA LOCAL INFILE '/tmp/sz/role_privilege.csv' INTO TABLE openmrs.temp_role_privilege FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_role_role;
CREATE TABLE openmrs.temp_role_role LIKE openmrs.role_role;
LOAD DATA LOCAL INFILE '/tmp/sz/role_role.csv' INTO TABLE openmrs.temp_role_role FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_scheduler_task_config_property;
CREATE TABLE openmrs.temp_scheduler_task_config_property LIKE openmrs.scheduler_task_config_property;
LOAD DATA LOCAL INFILE '/tmp/sz/scheduler_task_config_property.csv' INTO TABLE openmrs.temp_scheduler_task_config_property FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

-- DROP TABLE IF EXISTS openmrs.temp_test_order;
-- CREATE TABLE openmrs.temp_test_order LIKE openmrs.test_order;
-- LOAD DATA LOCAL INFILE '/tmp/sz/test_order.csv' INTO TABLE openmrs.temp_test_order FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES 
-- TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_user_property;
CREATE TABLE openmrs.temp_user_property LIKE openmrs.user_property;
LOAD DATA LOCAL INFILE '/tmp/sz/user_property.csv' INTO TABLE openmrs.temp_user_property FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';

DROP TABLE IF EXISTS openmrs.temp_user_role;
CREATE TABLE openmrs.temp_user_role LIKE openmrs.user_role;
LOAD DATA LOCAL INFILE '/tmp/sz/user_role.csv' INTO TABLE openmrs.temp_user_role FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n';