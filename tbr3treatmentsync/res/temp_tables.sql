drop table if exists temp_data_log;
drop table if exists temp_definition;
drop table if exists temp_definition_type;
drop table if exists temp_element;
drop table if exists temp_encounter;
drop table if exists temp_encounter_prerequisite;
drop table if exists temp_encounter_result;
drop table if exists temp_encounter_type;
drop table if exists temp_location;
drop table if exists temp_location_attribute;
drop table if exists temp_location_attribute_type;
drop table if exists temp_patient;
drop table if exists temp_person;
drop table if exists temp_person_attribute;
drop table if exists temp_person_attribute_type;
drop table if exists temp_person_contact;
-- no temp table for Privilege
drop table if exists temp_role;
drop table if exists temp_role_privilege;
drop table if exists temp_user_location;
drop table if exists temp_user_role;
drop table if exists temp_users;
CREATE TABLE temp_data_log ( log_id int(11) NOT NULL, log_type char(6) NOT NULL, entity_name varchar(45) NOT NULL, record text, description text, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (log_id), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_definition ( definition_id int(11) NOT NULL AUTO_INCREMENT, definition_type_id int(11) NOT NULL, definition varchar(45) NOT NULL, description varchar(255) DEFAULT NULL, is_default bit(1) DEFAULT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (definition_id), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_definition_type ( definition_type_id int(11) NOT NULL AUTO_INCREMENT, definition_type varchar(45) NOT NULL, PRIMARY KEY (definition_type_id), UNIQUE KEY `definition_type_UNIQUE` (definition_type));
CREATE TABLE temp_element ( element_id int(11) NOT NULL AUTO_INCREMENT, element_name varchar(45) NOT NULL, validation_regex varchar(255) DEFAULT NULL, data_type varchar(10) DEFAULT NULL, description varchar(255) DEFAULT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL,  PRIMARY KEY (element_id),  UNIQUE KEY `uuid_UNIQUE` (uuid),  UNIQUE KEY `element_name_UNIQUE` (element_name));
CREATE TABLE temp_encounter ( encounter_id int(11) NOT NULL AUTO_INCREMENT, encounter_type_id int(11) DEFAULT NULL, user_id int(11) DEFAULT NULL, person_id int(11) DEFAULT NULL, duration_seconds int(11) DEFAULT NULL, date_entered datetime DEFAULT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (encounter_id), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_encounter_prerequisite ( encounter_prerequisite_id int(11) NOT NULL AUTO_INCREMENT, encounter_type_id int(11) NOT NULL, prerequisite_encounter_type_id int(11) DEFAULT NULL, element_id int(11) DEFAULT NULL, should_be_regex varchar(255) DEFAULT NULL, should_not_be_regex varchar(255) DEFAULT NULL, description varchar(255) DEFAULT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (encounter_prerequisite_id), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_encounter_result ( encounter_result_id int(11) NOT NULL AUTO_INCREMENT, encounter_id int(11) NOT NULL, element_id int(11) NOT NULL, result varchar(255) NOT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (encounter_result_id), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_encounter_type ( encounter_type_id int(11) NOT NULL AUTO_INCREMENT, encounter_type varchar(45) NOT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (encounter_type_id), UNIQUE KEY `uuid_UNIQUE` (uuid), UNIQUE KEY `encounter_type_UNIQUE` (encounter_type));
CREATE TABLE temp_location ( location_id int(11) NOT NULL AUTO_INCREMENT, location_name varchar(45) NOT NULL, category varchar(20) DEFAULT NULL, description varchar(255) DEFAULT NULL, address1 varchar(45) DEFAULT NULL, address2 varchar(45) DEFAULT NULL, address3 varchar(45) DEFAULT NULL, city_village varchar(45) DEFAULT NULL, state_province varchar(45) DEFAULT NULL, country varchar(45) DEFAULT NULL, landmark1 varchar(45) DEFAULT NULL, landmark2 varchar(45) DEFAULT NULL, latitude varchar(45) DEFAULT NULL, longitude varchar(45) DEFAULT NULL, primary_contact varchar(20) DEFAULT NULL, secondary_contact varchar(20) DEFAULT NULL, email varchar(255) DEFAULT NULL, fax varchar(20) DEFAULT NULL, parent_location int(11) DEFAULT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (location_id), UNIQUE KEY `uuid_UNIQUE` (uuid), UNIQUE KEY `location_name_UNIQUE` (location_name));
CREATE TABLE temp_location_attribute ( location_attribute_id int(11) NOT NULL AUTO_INCREMENT, attribute_type_id int(11) NOT NULL, location_id int(11) NOT NULL, attribute_value varchar(255) NOT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (location_attribute_id), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_location_attribute_type (location_attribute_type_id int(11) NOT NULL AUTO_INCREMENT, attribute_name varchar(45) NOT NULL, validation_regex varchar(255) DEFAULT NULL, required bit(1) DEFAULT NULL, description varchar(255) DEFAULT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (location_attribute_type_id), UNIQUE KEY `uuid_UNIQUE` (uuid), UNIQUE KEY `attribute_name_UNIQUE` (attribute_name));
CREATE TABLE temp_patient ( person_id int(11) NOT NULL, patient_id varchar(20) DEFAULT NULL, external_id1 varchar(20) DEFAULT NULL, external_id2 varchar(20) DEFAULT NULL, blood_group char(5) DEFAULT NULL, weight decimal(10,5) DEFAULT NULL, weight_unit char(5) DEFAULT NULL, height decimal(10,5) DEFAULT NULL, height_unit char(5) DEFAULT NULL, date_closed datetime DEFAULT NULL, closed_by int(11) DEFAULT NULL, reason_closed varchar(255) DEFAULT NULL, died bit(1) DEFAULT NULL, date_died datetime DEFAULT NULL, death_cause varchar(255) DEFAULT NULL, comments varchar(255) DEFAULT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (person_id), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_person ( person_id int(11) NOT NULL AUTO_INCREMENT, title varchar(5) DEFAULT NULL, first_name varchar(45) NOT NULL, middle_name varchar(45) DEFAULT NULL, last_name varchar(45) DEFAULT NULL, family_name varchar(45) DEFAULT NULL, gender char(1) NOT NULL, dob datetime DEFAULT NULL, dob_estimated bit(1) DEFAULT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (person_id), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_person_attribute ( person_attribute_id int(11) NOT NULL AUTO_INCREMENT, attribute_type_id int(11) NOT NULL, person_id int(11) NOT NULL, attribute_value varchar(255) NOT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (person_attribute_id), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_person_attribute_type ( person_attribute_type_id int(11) NOT NULL AUTO_INCREMENT, attribute_name varchar(45) NOT NULL, data_type varchar(10) DEFAULT NULL, validation_regex varchar(255) DEFAULT NULL, required bit(1) NOT NULL, description varchar(255) DEFAULT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (person_attribute_type_id), UNIQUE KEY `attribute_name_UNIQUE` (attribute_name), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_person_contact ( address_id int(11) NOT NULL AUTO_INCREMENT, person_id int(11) DEFAULT NULL, address1 varchar(45) DEFAULT NULL, address2 varchar(45) DEFAULT NULL, address3 varchar(45) DEFAULT NULL, city_village varchar(45) DEFAULT NULL, state_province varchar(45) DEFAULT NULL, country varchar(45) DEFAULT NULL, landmark1 varchar(45) DEFAULT NULL, landmark2 varchar(45) DEFAULT NULL, latitude varchar(45) DEFAULT NULL, longitude varchar(45) DEFAULT NULL, primary_contact varchar(20) DEFAULT NULL, secondary_contact varchar(20) DEFAULT NULL, email varchar(255) DEFAULT NULL, fax varchar(20) DEFAULT NULL, preferred bit(1) DEFAULT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL,  PRIMARY KEY (address_id),  UNIQUE KEY `uuid_UNIQUE` (uuid));
-- skipping privilege table
CREATE TABLE temp_role ( role_id int(11) NOT NULL AUTO_INCREMENT, role_name varchar(45) NOT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (role_id), UNIQUE KEY `uuid_UNIQUE` (uuid), UNIQUE KEY `role_name_UNIQUE` (role_name));
CREATE TABLE temp_role_privilege ( role_id int(11) NOT NULL, privilege varchar(45) NOT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (role_id,privilege), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_user_location ( user_id int(11) NOT NULL, location_id int(11) NOT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (user_id,location_id), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_user_role ( user_id int(11) NOT NULL, role_id int(11) NOT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (user_id,role_id), UNIQUE KEY `uuid_UNIQUE` (uuid));
CREATE TABLE temp_users ( user_id int(11) NOT NULL AUTO_INCREMENT, username varchar(20) NOT NULL, full_name varchar(255) DEFAULT NULL, global_data_access bit(1) DEFAULT NULL, disabled bit(1) DEFAULT NULL, reason_disabled varchar(255) DEFAULT NULL, password_hash varchar(255) DEFAULT NULL, secret_question varchar(255) DEFAULT NULL, secret_answer_hash varchar(255) DEFAULT NULL, date_created datetime NOT NULL, created_by int(11) DEFAULT NULL, created_at int(11) DEFAULT NULL, date_changed datetime DEFAULT NULL, changed_by int(11) DEFAULT NULL, changed_at int(11) DEFAULT NULL, uuid char(38) NOT NULL, PRIMARY KEY (user_id), UNIQUE KEY `uuid_UNIQUE` (uuid), UNIQUE KEY `username_UNIQUE` (username));
