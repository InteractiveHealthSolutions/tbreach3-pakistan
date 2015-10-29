-- Copyright(C) YEAR Interactive Health Solutions, Pvt. Ltd.

-- This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
-- published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

-- See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
-- You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html\n\nInteractive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
-- Contrinutors: Tahira Niazi, Owais Hussain


-- ---------------------------------WIPE OFF INVALID DATA----------------------------------------
ALTER TABLE `openmrs`.`patient_identifier` ADD CONSTRAINT `patient_identifier_for_patient` FOREIGN KEY (`patient_id` )  REFERENCES `openmrs`.`person` (`person_id` )  ON DELETE RESTRICT  ON UPDATE CASCADE, ADD INDEX `patient_identifier_for_patient` (`patient_id` ASC);
ALTER TABLE `openmrs`.`encounter_provider` ADD CONSTRAINT `encounter_creator_fk`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;

-- ---------------------------------MOVE OLD OBS DATA--------------------------------------------
drop table if exists obs_pre_2015;
create table obs_pre_2015 select * from obs where year(date_created) < 2015;
update obs set previous_version = null where previous_version is not null;
delete from obs where year(date_created) <= 2013;
delete from obs where year(date_created) = 2014 and month(date_created) = 1;
delete from obs where year(date_created) = 2014 and month(date_created) = 2;
delete from obs where year(date_created) = 2014 and month(date_created) = 3;
delete from obs where year(date_created) = 2014 and month(date_created) = 4;
delete from obs where year(date_created) = 2014 and month(date_created) = 5;
delete from obs where year(date_created) = 2014 and month(date_created) = 6;
delete from obs where year(date_created) = 2014 and month(date_created) = 7;
delete from obs where year(date_created) = 2014 and month(date_created) = 8;
delete from obs where year(date_created) = 2014 and month(date_created) = 9;
delete from obs where year(date_created) = 2014 and month(date_created) = 10;
delete from obs where year(date_created) = 2014 and month(date_created) = 11;
delete from obs where year(date_created) = 2014 and month(date_created) = 12;

-- ---------------------------------WIPE OFF INVALID DATA----------------------------------------
delete from encounter_provider where encounter_id not in (select encounter_id from encounter);

-- ---------------------------------ENABLING CASCADE---------------------------------------------
ALTER TABLE `openmrs`.`active_list_type` DROP FOREIGN KEY `user_who_created_active_list_type`,DROP FOREIGN KEY `user_who_retired_active_list_type`;
ALTER TABLE `openmrs`.`active_list_type` ADD CONSTRAINT `user_who_created_active_list_type`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_active_list_type`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`cohort` DROP FOREIGN KEY `cohort_creator`,DROP FOREIGN KEY `user_who_changed_cohort`,DROP FOREIGN KEY `user_who_voided_cohort`;
ALTER TABLE `openmrs`.`cohort` ADD CONSTRAINT `cohort_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_changed_cohort`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_voided_cohort`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`cohort_member` DROP FOREIGN KEY `parent_cohort`;
ALTER TABLE `openmrs`.`cohort_member` ADD CONSTRAINT `parent_cohort`  FOREIGN KEY (`cohort_id`)  REFERENCES `openmrs`.`cohort` (`cohort_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept` DROP FOREIGN KEY `concept_classes`,DROP FOREIGN KEY `concept_creator`,DROP FOREIGN KEY `concept_datatypes`,DROP FOREIGN KEY `user_who_changed_concept`,DROP FOREIGN KEY `user_who_retired_concept`;
ALTER TABLE `openmrs`.`concept` ADD CONSTRAINT `concept_classes`  FOREIGN KEY (`class_id`)  REFERENCES `openmrs`.`concept_class` (`concept_class_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `concept_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `concept_datatypes`  FOREIGN KEY (`datatype_id`)  REFERENCES `openmrs`.`concept_datatype` (`concept_datatype_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_changed_concept`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_concept`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_answer` DROP FOREIGN KEY `answer`,DROP FOREIGN KEY `answer_answer_drug_fk`,DROP FOREIGN KEY `answer_creator`,DROP FOREIGN KEY `answers_for_concept`;
ALTER TABLE `openmrs`.`concept_answer` ADD CONSTRAINT `answer`  FOREIGN KEY (`answer_concept`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `answer_answer_drug_fk`  FOREIGN KEY (`answer_drug`)  REFERENCES `openmrs`.`drug` (`drug_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `answer_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `answers_for_concept`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_class` DROP FOREIGN KEY `concept_class_creator`,DROP FOREIGN KEY `user_who_retired_concept_class`;
ALTER TABLE `openmrs`.`concept_class` ADD CONSTRAINT `concept_class_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_concept_class`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_complex` DROP FOREIGN KEY `concept_attributes`;
ALTER TABLE `openmrs`.`concept_complex` ADD CONSTRAINT `concept_attributes`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_datatype` DROP FOREIGN KEY `concept_datatype_creator`,DROP FOREIGN KEY `user_who_retired_concept_datatype`;
ALTER TABLE `openmrs`.`concept_datatype` ADD CONSTRAINT `concept_datatype_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_concept_datatype`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_description` DROP FOREIGN KEY `description_for_concept`,DROP FOREIGN KEY `user_who_changed_description`,DROP FOREIGN KEY `user_who_created_description`;
ALTER TABLE `openmrs`.`concept_description` ADD CONSTRAINT `description_for_concept`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_changed_description`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_created_description`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_map_type` DROP FOREIGN KEY `mapped_user_changed_concept_map_type`,DROP FOREIGN KEY `mapped_user_creator_concept_map_type`,DROP FOREIGN KEY `mapped_user_retired_concept_map_type`;
ALTER TABLE `openmrs`.`concept_map_type` ADD CONSTRAINT `mapped_user_changed_concept_map_type`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_user_creator_concept_map_type`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_user_retired_concept_map_type`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_name` DROP FOREIGN KEY `name_for_concept`,DROP FOREIGN KEY `user_who_created_name`,DROP FOREIGN KEY `user_who_voided_this_name`;
ALTER TABLE `openmrs`.`concept_name` ADD CONSTRAINT `name_for_concept`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_created_name`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_voided_this_name`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_name_tag_map` DROP FOREIGN KEY `mapped_concept_name`,DROP FOREIGN KEY `mapped_concept_name_tag`;
ALTER TABLE `openmrs`.`concept_name_tag_map` ADD CONSTRAINT `mapped_concept_name`  FOREIGN KEY (`concept_name_id`)  REFERENCES `openmrs`.`concept_name` (`concept_name_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_concept_name_tag`  FOREIGN KEY (`concept_name_tag_id`)  REFERENCES `openmrs`.`concept_name_tag` (`concept_name_tag_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_numeric` DROP FOREIGN KEY `numeric_attributes`;
ALTER TABLE `openmrs`.`concept_numeric` ADD CONSTRAINT `numeric_attributes`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_proposal` DROP FOREIGN KEY `concept_for_proposal`,DROP FOREIGN KEY `encounter_for_proposal`,DROP FOREIGN KEY `proposal_obs_concept_id`,DROP FOREIGN KEY `proposal_obs_id`,DROP FOREIGN KEY `user_who_changed_proposal`,DROP FOREIGN KEY `user_who_created_proposal`;
ALTER TABLE `openmrs`.`concept_proposal` ADD CONSTRAINT `concept_for_proposal`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `encounter_for_proposal`  FOREIGN KEY (`encounter_id`)  REFERENCES `openmrs`.`encounter` (`encounter_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `proposal_obs_concept_id`  FOREIGN KEY (`obs_concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `proposal_obs_id`  FOREIGN KEY (`obs_id`)  REFERENCES `openmrs`.`obs` (`obs_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_changed_proposal`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_created_proposal`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_proposal_tag_map` DROP FOREIGN KEY `mapped_concept_proposal`,DROP FOREIGN KEY `mapped_concept_proposal_tag`;
ALTER TABLE `openmrs`.`concept_proposal_tag_map` ADD CONSTRAINT `mapped_concept_proposal`  FOREIGN KEY (`concept_proposal_id`)  REFERENCES `openmrs`.`concept_proposal` (`concept_proposal_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_concept_proposal_tag`  FOREIGN KEY (`concept_name_tag_id`)  REFERENCES `openmrs`.`concept_name_tag` (`concept_name_tag_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_reference_map` DROP FOREIGN KEY `map_creator`,DROP FOREIGN KEY `map_for_concept`,DROP FOREIGN KEY `mapped_concept_map_type`,DROP FOREIGN KEY `mapped_concept_reference_term`,DROP FOREIGN KEY `mapped_user_changed_ref_term`;
ALTER TABLE `openmrs`.`concept_reference_map` ADD CONSTRAINT `map_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `map_for_concept`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_concept_map_type`  FOREIGN KEY (`concept_map_type_id`)  REFERENCES `openmrs`.`concept_map_type` (`concept_map_type_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_concept_reference_term`  FOREIGN KEY (`concept_reference_term_id`)  REFERENCES `openmrs`.`concept_reference_term` (`concept_reference_term_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_user_changed_ref_term`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_reference_source` DROP FOREIGN KEY `concept_source_creator`,DROP FOREIGN KEY `user_who_retired_concept_source`;
ALTER TABLE `openmrs`.`concept_reference_source` ADD CONSTRAINT `concept_source_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_concept_source`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_reference_term` DROP FOREIGN KEY `mapped_concept_source`,DROP FOREIGN KEY `mapped_user_changed`,DROP FOREIGN KEY `mapped_user_creator`,DROP FOREIGN KEY `mapped_user_retired`;
ALTER TABLE `openmrs`.`concept_reference_term` ADD CONSTRAINT `mapped_concept_source`  FOREIGN KEY (`concept_source_id`)  REFERENCES `openmrs`.`concept_reference_source` (`concept_source_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_user_changed`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_user_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_user_retired`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_reference_term_map` DROP FOREIGN KEY `mapped_concept_map_type_ref_term_map`,DROP FOREIGN KEY `mapped_term_a`,DROP FOREIGN KEY `mapped_term_b`,DROP FOREIGN KEY `mapped_user_changed_ref_term_map`,DROP FOREIGN KEY `mapped_user_creator_ref_term_map`;
ALTER TABLE `openmrs`.`concept_reference_term_map` ADD CONSTRAINT `mapped_concept_map_type_ref_term_map`  FOREIGN KEY (`a_is_to_b_id`)  REFERENCES `openmrs`.`concept_map_type` (`concept_map_type_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_term_a`  FOREIGN KEY (`term_a_id`)  REFERENCES `openmrs`.`concept_reference_term` (`concept_reference_term_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_term_b`  FOREIGN KEY (`term_b_id`)  REFERENCES `openmrs`.`concept_reference_term` (`concept_reference_term_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_user_changed_ref_term_map`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `mapped_user_creator_ref_term_map`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_set` DROP FOREIGN KEY `has_a`,DROP FOREIGN KEY `user_who_created`;
ALTER TABLE `openmrs`.`concept_set` ADD CONSTRAINT `has_a`  FOREIGN KEY (`concept_set`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_created`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_state_conversion` DROP FOREIGN KEY `concept_triggers_conversion`,DROP FOREIGN KEY `conversion_involves_workflow`,DROP FOREIGN KEY `conversion_to_state`;
ALTER TABLE `openmrs`.`concept_state_conversion` ADD CONSTRAINT `concept_triggers_conversion`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `conversion_involves_workflow`  FOREIGN KEY (`program_workflow_id`)  REFERENCES `openmrs`.`program_workflow` (`program_workflow_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `conversion_to_state`  FOREIGN KEY (`program_workflow_state_id`)  REFERENCES `openmrs`.`program_workflow_state` (`program_workflow_state_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`concept_word` DROP FOREIGN KEY `word_for`,DROP FOREIGN KEY `word_for_name`;
ALTER TABLE `openmrs`.`concept_word` ADD CONSTRAINT `word_for`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `word_for_name`  FOREIGN KEY (`concept_name_id`)  REFERENCES `openmrs`.`concept_name` (`concept_name_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`drug` DROP FOREIGN KEY `dosage_form_concept`,DROP FOREIGN KEY `drug_changed_by`,DROP FOREIGN KEY `drug_creator`,DROP FOREIGN KEY `drug_retired_by`,DROP FOREIGN KEY `primary_drug_concept`,DROP FOREIGN KEY `route_concept`;
ALTER TABLE `openmrs`.`drug` ADD CONSTRAINT `dosage_form_concept`  FOREIGN KEY (`dosage_form`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `drug_changed_by`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `drug_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `drug_retired_by`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `primary_drug_concept`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `route_concept`  FOREIGN KEY (`route`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`drug_ingredient` DROP FOREIGN KEY `combination_drug`,DROP FOREIGN KEY `ingredient`;
ALTER TABLE `openmrs`.`drug_ingredient` ADD CONSTRAINT `combination_drug`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `ingredient`  FOREIGN KEY (`ingredient_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`drug_order` DROP FOREIGN KEY `extends_order`,DROP FOREIGN KEY `inventory_item`;
ALTER TABLE `openmrs`.`drug_order` ADD CONSTRAINT `extends_order`  FOREIGN KEY (`order_id`)  REFERENCES `openmrs`.`orders` (`order_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `inventory_item`  FOREIGN KEY (`drug_inventory_id`)  REFERENCES `openmrs`.`drug` (`drug_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`encounter` DROP FOREIGN KEY `encounter_changed_by`,DROP FOREIGN KEY `encounter_form`,DROP FOREIGN KEY `encounter_ibfk_1`,DROP FOREIGN KEY `encounter_location`,DROP FOREIGN KEY `encounter_type_id`,DROP FOREIGN KEY `encounter_visit_id_fk`,DROP FOREIGN KEY `user_who_voided_encounter`;
ALTER TABLE `openmrs`.`encounter` ADD CONSTRAINT `encounter_changed_by`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `encounter_form`  FOREIGN KEY (`form_id`)  REFERENCES `openmrs`.`form` (`form_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `encounter_ibfk_1`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `encounter_location`  FOREIGN KEY (`location_id`)  REFERENCES `openmrs`.`location` (`location_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `encounter_type_id`  FOREIGN KEY (`encounter_type`)  REFERENCES `openmrs`.`encounter_type` (`encounter_type_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `encounter_visit_id_fk`  FOREIGN KEY (`visit_id`)  REFERENCES `openmrs`.`visit` (`visit_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_voided_encounter`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`encounter_provider` DROP FOREIGN KEY `encounter_id_fk`,DROP FOREIGN KEY `encounter_role_id_fk`,DROP FOREIGN KEY `provider_id_fk`;
ALTER TABLE `openmrs`.`encounter_provider` ADD CONSTRAINT `encounter_id_fk`  FOREIGN KEY (`encounter_id`)  REFERENCES `openmrs`.`encounter` (`encounter_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `encounter_role_id_fk`  FOREIGN KEY (`encounter_role_id`)  REFERENCES `openmrs`.`encounter_role` (`encounter_role_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `provider_id_fk`  FOREIGN KEY (`provider_id`)  REFERENCES `openmrs`.`provider` (`provider_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`encounter_role` DROP FOREIGN KEY `encounter_role_changed_by_fk`,DROP FOREIGN KEY `encounter_role_creator_fk`,DROP FOREIGN KEY `encounter_role_retired_by_fk`;
ALTER TABLE `openmrs`.`encounter_role` ADD CONSTRAINT `encounter_role_changed_by_fk`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `encounter_role_creator_fk`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `encounter_role_retired_by_fk`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`encounter_type` DROP FOREIGN KEY `user_who_created_type`,DROP FOREIGN KEY `user_who_retired_encounter_type`;
ALTER TABLE `openmrs`.`encounter_type` ADD CONSTRAINT `user_who_created_type`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_encounter_type`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`field` DROP FOREIGN KEY `concept_for_field`,DROP FOREIGN KEY `type_of_field`,DROP FOREIGN KEY `user_who_changed_field`,DROP FOREIGN KEY `user_who_created_field`,DROP FOREIGN KEY `user_who_retired_field`;
ALTER TABLE `openmrs`.`field` ADD CONSTRAINT `concept_for_field`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `type_of_field`  FOREIGN KEY (`field_type`)  REFERENCES `openmrs`.`field_type` (`field_type_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_changed_field`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_created_field`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_field`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`field_answer` DROP FOREIGN KEY `answers_for_field`,DROP FOREIGN KEY `field_answer_concept`,DROP FOREIGN KEY `user_who_created_field_answer`;
ALTER TABLE `openmrs`.`field_answer` ADD CONSTRAINT `answers_for_field`  FOREIGN KEY (`field_id`)  REFERENCES `openmrs`.`field` (`field_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `field_answer_concept`  FOREIGN KEY (`answer_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_created_field_answer`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`field_type` DROP FOREIGN KEY `user_who_created_field_type`;
ALTER TABLE `openmrs`.`field_type` ADD CONSTRAINT `user_who_created_field_type`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`form` DROP FOREIGN KEY `form_encounter_type`,DROP FOREIGN KEY `user_who_created_form`,DROP FOREIGN KEY `user_who_last_changed_form`,DROP FOREIGN KEY `user_who_retired_form`;
ALTER TABLE `openmrs`.`form` ADD CONSTRAINT `form_encounter_type`  FOREIGN KEY (`encounter_type`)  REFERENCES `openmrs`.`encounter_type` (`encounter_type_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_created_form`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_last_changed_form`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_form`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`form_field` DROP FOREIGN KEY `field_within_form`,DROP FOREIGN KEY `form_containing_field`,DROP FOREIGN KEY `form_field_hierarchy`,DROP FOREIGN KEY `user_who_created_form_field`,DROP FOREIGN KEY `user_who_last_changed_form_field`;
ALTER TABLE `openmrs`.`form_field` ADD CONSTRAINT `field_within_form`  FOREIGN KEY (`field_id`)  REFERENCES `openmrs`.`field` (`field_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `form_containing_field`  FOREIGN KEY (`form_id`)  REFERENCES `openmrs`.`form` (`form_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `form_field_hierarchy`  FOREIGN KEY (`parent_form_field`)  REFERENCES `openmrs`.`form_field` (`form_field_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_created_form_field`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_last_changed_form_field`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`form_resource` DROP FOREIGN KEY `form_resource_form_fk`;
ALTER TABLE `openmrs`.`form_resource` ADD CONSTRAINT `form_resource_form_fk`  FOREIGN KEY (`form_id`)  REFERENCES `openmrs`.`form` (`form_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`location` DROP FOREIGN KEY `parent_location`,DROP FOREIGN KEY `user_who_created_location`,DROP FOREIGN KEY `user_who_retired_location`;
ALTER TABLE `openmrs`.`location` ADD CONSTRAINT `parent_location`  FOREIGN KEY (`parent_location`)  REFERENCES `openmrs`.`location` (`location_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_created_location`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_location`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`location_attribute` DROP FOREIGN KEY `location_attribute_attribute_type_id_fk`,DROP FOREIGN KEY `location_attribute_changed_by_fk`,DROP FOREIGN KEY `location_attribute_creator_fk`,DROP FOREIGN KEY `location_attribute_location_fk`,DROP FOREIGN KEY `location_attribute_voided_by_fk`;
ALTER TABLE `openmrs`.`location_attribute` ADD CONSTRAINT `location_attribute_attribute_type_id_fk`  FOREIGN KEY (`attribute_type_id`)  REFERENCES `openmrs`.`location_attribute_type` (`location_attribute_type_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `location_attribute_changed_by_fk`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `location_attribute_creator_fk`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `location_attribute_location_fk`  FOREIGN KEY (`location_id`)  REFERENCES `openmrs`.`location` (`location_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `location_attribute_voided_by_fk`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`location_attribute_type` DROP FOREIGN KEY `location_attribute_type_changed_by_fk`,DROP FOREIGN KEY `location_attribute_type_creator_fk`,DROP FOREIGN KEY `location_attribute_type_retired_by_fk`;
ALTER TABLE `openmrs`.`location_attribute_type` ADD CONSTRAINT `location_attribute_type_changed_by_fk`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `location_attribute_type_creator_fk`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `location_attribute_type_retired_by_fk`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`location_tag` DROP FOREIGN KEY `location_tag_creator`,DROP FOREIGN KEY `location_tag_retired_by`;
ALTER TABLE `openmrs`.`location_tag` ADD CONSTRAINT `location_tag_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `location_tag_retired_by`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`location_tag_map` DROP FOREIGN KEY `location_tag_map_location`,DROP FOREIGN KEY `location_tag_map_tag`;
ALTER TABLE `openmrs`.`location_tag_map` ADD CONSTRAINT `location_tag_map_location`  FOREIGN KEY (`location_id`)  REFERENCES `openmrs`.`location` (`location_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `location_tag_map_tag`  FOREIGN KEY (`location_tag_id`)  REFERENCES `openmrs`.`location_tag` (`location_tag_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`note` DROP FOREIGN KEY `encounter_note`,DROP FOREIGN KEY `note_hierarchy`,DROP FOREIGN KEY `obs_note`,DROP FOREIGN KEY `user_who_changed_note`,DROP FOREIGN KEY `user_who_created_note`;
ALTER TABLE `openmrs`.`note` ADD CONSTRAINT `encounter_note`  FOREIGN KEY (`encounter_id`)  REFERENCES `openmrs`.`encounter` (`encounter_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `note_hierarchy`  FOREIGN KEY (`parent`)  REFERENCES `openmrs`.`note` (`note_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `obs_note`  FOREIGN KEY (`obs_id`)  REFERENCES `openmrs`.`obs` (`obs_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_changed_note`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_created_note`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`notification_alert` DROP FOREIGN KEY `alert_creator`,DROP FOREIGN KEY `user_who_changed_alert`;
ALTER TABLE `openmrs`.`notification_alert` ADD CONSTRAINT `alert_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_changed_alert`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`notification_alert_recipient` DROP FOREIGN KEY `alert_read_by_user`,DROP FOREIGN KEY `id_of_alert`;
ALTER TABLE `openmrs`.`notification_alert_recipient` ADD CONSTRAINT `alert_read_by_user`  FOREIGN KEY (`user_id`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `id_of_alert`  FOREIGN KEY (`alert_id`)  REFERENCES `openmrs`.`notification_alert` (`alert_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`obs` DROP FOREIGN KEY `answer_concept`,DROP FOREIGN KEY `answer_concept_drug`,DROP FOREIGN KEY `encounter_observations`,DROP FOREIGN KEY `obs_concept`,DROP FOREIGN KEY `obs_enterer`,DROP FOREIGN KEY `obs_grouping_id`,DROP FOREIGN KEY `obs_location`,DROP FOREIGN KEY `obs_name_of_coded_value`,DROP FOREIGN KEY `obs_order`,DROP FOREIGN KEY `previous_version`,DROP FOREIGN KEY `user_who_voided_obs`;
ALTER TABLE `openmrs`.`obs` ADD CONSTRAINT `answer_concept`  FOREIGN KEY (`value_coded`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `answer_concept_drug` FOREIGN KEY (`value_drug`)  REFERENCES `openmrs`.`drug` (`drug_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `encounter_observations`  FOREIGN KEY (`encounter_id`)  REFERENCES `openmrs`.`encounter` (`encounter_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `obs_concept`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `obs_enterer`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `obs_grouping_id`  FOREIGN KEY (`obs_group_id`)  REFERENCES `openmrs`.`obs` (`obs_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `obs_location`  FOREIGN KEY (`location_id`)  REFERENCES `openmrs`.`location` (`location_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `obs_name_of_coded_value`  FOREIGN KEY (`value_coded_name_id`)  REFERENCES `openmrs`.`concept_name` (`concept_name_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `obs_order`  FOREIGN KEY (`order_id`)  REFERENCES `openmrs`.`orders` (`order_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `previous_version`  FOREIGN KEY (`previous_version`)  REFERENCES `openmrs`.`obs` (`obs_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_voided_obs`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`order_type` DROP FOREIGN KEY `type_created_by`,DROP FOREIGN KEY `user_who_retired_order_type`;
ALTER TABLE `openmrs`.`order_type` ADD CONSTRAINT `type_created_by`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_order_type`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`orders` DROP FOREIGN KEY `discontinued_because`,DROP FOREIGN KEY `order_creator`,DROP FOREIGN KEY `orderer_not_drug`,DROP FOREIGN KEY `orders_in_encounter`,DROP FOREIGN KEY `type_of_order`,DROP FOREIGN KEY `user_who_discontinued_order`,DROP FOREIGN KEY `user_who_voided_order`;
ALTER TABLE `openmrs`.`orders` ADD CONSTRAINT `discontinued_because`  FOREIGN KEY (`discontinued_reason`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `order_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `orderer_not_drug`  FOREIGN KEY (`orderer`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `orders_in_encounter`  FOREIGN KEY (`encounter_id`)  REFERENCES `openmrs`.`encounter` (`encounter_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `type_of_order`  FOREIGN KEY (`order_type_id`)  REFERENCES `openmrs`.`order_type` (`order_type_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_discontinued_order`  FOREIGN KEY (`discontinued_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_voided_order`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`patient` DROP FOREIGN KEY `user_who_changed_pat`,DROP FOREIGN KEY `user_who_created_patient`,DROP FOREIGN KEY `user_who_voided_patient`;
ALTER TABLE `openmrs`.`patient` ADD CONSTRAINT `user_who_changed_pat`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_created_patient`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_voided_patient`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`patient_identifier` DROP FOREIGN KEY `defines_identifier_type`,DROP FOREIGN KEY `identifier_creator`,DROP FOREIGN KEY `identifier_voider`,DROP FOREIGN KEY `patient_identifier_changed_by`,DROP FOREIGN KEY `patient_identifier_ibfk_2`;
ALTER TABLE `openmrs`.`patient_identifier` ADD CONSTRAINT `defines_identifier_type`  FOREIGN KEY (`identifier_type`)  REFERENCES `openmrs`.`patient_identifier_type` (`patient_identifier_type_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `identifier_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `identifier_voider`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `patient_identifier_changed_by`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `patient_identifier_ibfk_2`  FOREIGN KEY (`location_id`)  REFERENCES `openmrs`.`location` (`location_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`patient_identifier_type` DROP FOREIGN KEY `type_creator`,DROP FOREIGN KEY `user_who_retired_patient_identifier_type`;
ALTER TABLE `openmrs`.`patient_identifier_type` ADD CONSTRAINT `type_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_patient_identifier_type`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`patient_program` DROP FOREIGN KEY `patient_program_creator`,DROP FOREIGN KEY `patient_program_location_id`,DROP FOREIGN KEY `patient_program_outcome_concept_id_fk`,DROP FOREIGN KEY `program_for_patient`,DROP FOREIGN KEY `user_who_changed`,DROP FOREIGN KEY `user_who_voided_patient_program`;
ALTER TABLE `openmrs`.`patient_program` ADD CONSTRAINT `patient_program_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `patient_program_location_id`  FOREIGN KEY (`location_id`)  REFERENCES `openmrs`.`location` (`location_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `patient_program_outcome_concept_id_fk`  FOREIGN KEY (`outcome_concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `program_for_patient`  FOREIGN KEY (`program_id`)  REFERENCES `openmrs`.`program` (`program_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_changed`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_voided_patient_program`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`patient_state` DROP FOREIGN KEY `patient_program_for_state`,DROP FOREIGN KEY `patient_state_changer`,DROP FOREIGN KEY `patient_state_creator`,DROP FOREIGN KEY `patient_state_voider`,DROP FOREIGN KEY `state_for_patient`;
ALTER TABLE `openmrs`.`patient_state` ADD CONSTRAINT `patient_program_for_state`  FOREIGN KEY (`patient_program_id`)  REFERENCES `openmrs`.`patient_program` (`patient_program_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `patient_state_changer`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `patient_state_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `patient_state_voider`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `state_for_patient`  FOREIGN KEY (`state`)  REFERENCES `openmrs`.`program_workflow_state` (`program_workflow_state_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`person` DROP FOREIGN KEY `person_died_because`,DROP FOREIGN KEY `user_who_changed_person`,DROP FOREIGN KEY `user_who_created_person`,DROP FOREIGN KEY `user_who_voided_person`;
ALTER TABLE `openmrs`.`person` ADD CONSTRAINT `person_died_because`  FOREIGN KEY (`cause_of_death`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_changed_person`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_created_person`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_voided_person`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`person_address` DROP FOREIGN KEY `patient_address_creator`,DROP FOREIGN KEY `patient_address_void`,DROP FOREIGN KEY `person_address_changed_by`;
ALTER TABLE `openmrs`.`person_address` ADD CONSTRAINT `patient_address_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `patient_address_void`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `person_address_changed_by`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`person_attribute` DROP FOREIGN KEY `attribute_changer`,DROP FOREIGN KEY `attribute_creator`,DROP FOREIGN KEY `attribute_voider`,DROP FOREIGN KEY `defines_attribute_type`,DROP FOREIGN KEY `identifies_person`;
ALTER TABLE `openmrs`.`person_attribute` ADD CONSTRAINT `attribute_changer`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `attribute_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `attribute_voider`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `defines_attribute_type`  FOREIGN KEY (`person_attribute_type_id`)  REFERENCES `openmrs`.`person_attribute_type` (`person_attribute_type_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `identifies_person`  FOREIGN KEY (`person_id`)  REFERENCES `openmrs`.`person` (`person_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`person_attribute_type` DROP FOREIGN KEY `attribute_type_changer`,DROP FOREIGN KEY `attribute_type_creator`,DROP FOREIGN KEY `privilege_which_can_edit`,DROP FOREIGN KEY `user_who_retired_person_attribute_type`;
ALTER TABLE `openmrs`.`person_attribute_type` ADD CONSTRAINT `attribute_type_changer`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `attribute_type_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `privilege_which_can_edit`  FOREIGN KEY (`edit_privilege`)  REFERENCES `openmrs`.`privilege` (`privilege`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_person_attribute_type`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`person_merge_log` DROP FOREIGN KEY `person_merge_log_changed_by_fk`,DROP FOREIGN KEY `person_merge_log_creator`,DROP FOREIGN KEY `person_merge_log_loser`,DROP FOREIGN KEY `person_merge_log_voided_by_fk`,DROP FOREIGN KEY `person_merge_log_winner`;
ALTER TABLE `openmrs`.`person_merge_log` ADD CONSTRAINT `person_merge_log_changed_by_fk`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `person_merge_log_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `person_merge_log_loser`  FOREIGN KEY (`loser_person_id`)  REFERENCES `openmrs`.`person` (`person_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `person_merge_log_voided_by_fk`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `person_merge_log_winner`  FOREIGN KEY (`winner_person_id`)  REFERENCES `openmrs`.`person` (`person_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`person_name` DROP FOREIGN KEY `user_who_made_name`,DROP FOREIGN KEY `user_who_voided_name`;
ALTER TABLE `openmrs`.`person_name` ADD CONSTRAINT `user_who_made_name`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_voided_name`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`program` DROP FOREIGN KEY `program_concept`,DROP FOREIGN KEY `program_creator`,DROP FOREIGN KEY `program_outcomes_concept_id_fk`,DROP FOREIGN KEY `user_who_changed_program`;
ALTER TABLE `openmrs`.`program` ADD CONSTRAINT `program_concept`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `program_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `program_outcomes_concept_id_fk`  FOREIGN KEY (`outcomes_concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_changed_program`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`program_workflow` DROP FOREIGN KEY `program_for_workflow`,DROP FOREIGN KEY `workflow_changed_by`,DROP FOREIGN KEY `workflow_concept`,DROP FOREIGN KEY `workflow_creator`;
ALTER TABLE `openmrs`.`program_workflow` ADD CONSTRAINT `program_for_workflow`  FOREIGN KEY (`program_id`)  REFERENCES `openmrs`.`program` (`program_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `workflow_changed_by`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `workflow_concept`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `workflow_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`program_workflow_state` DROP FOREIGN KEY `state_changed_by`,DROP FOREIGN KEY `state_concept`,DROP FOREIGN KEY `state_creator`,DROP FOREIGN KEY `workflow_for_state`;
ALTER TABLE `openmrs`.`program_workflow_state` ADD CONSTRAINT `state_changed_by`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `state_concept`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `state_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `workflow_for_state`  FOREIGN KEY (`program_workflow_id`)  REFERENCES `openmrs`.`program_workflow` (`program_workflow_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`provider` DROP FOREIGN KEY `provider_changed_by_fk`,DROP FOREIGN KEY `provider_creator_fk`,DROP FOREIGN KEY `provider_person_id_fk`,DROP FOREIGN KEY `provider_retired_by_fk`;
ALTER TABLE `openmrs`.`provider` ADD CONSTRAINT `provider_changed_by_fk`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `provider_creator_fk`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `provider_person_id_fk`  FOREIGN KEY (`person_id`)  REFERENCES `openmrs`.`person` (`person_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `provider_retired_by_fk`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`provider_attribute` DROP FOREIGN KEY `provider_attribute_attribute_type_id_fk`,DROP FOREIGN KEY `provider_attribute_changed_by_fk`,DROP FOREIGN KEY `provider_attribute_creator_fk`,DROP FOREIGN KEY `provider_attribute_provider_fk`,DROP FOREIGN KEY `provider_attribute_voided_by_fk`;
ALTER TABLE `openmrs`.`provider_attribute` ADD CONSTRAINT `provider_attribute_attribute_type_id_fk`  FOREIGN KEY (`attribute_type_id`)  REFERENCES `openmrs`.`provider_attribute_type` (`provider_attribute_type_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `provider_attribute_changed_by_fk`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `provider_attribute_creator_fk`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `provider_attribute_provider_fk`  FOREIGN KEY (`provider_id`)  REFERENCES `openmrs`.`provider` (`provider_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `provider_attribute_voided_by_fk`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`provider_attribute_type` DROP FOREIGN KEY `provider_attribute_type_changed_by_fk`,DROP FOREIGN KEY `provider_attribute_type_creator_fk`,DROP FOREIGN KEY `provider_attribute_type_retired_by_fk`;
ALTER TABLE `openmrs`.`provider_attribute_type` ADD CONSTRAINT `provider_attribute_type_changed_by_fk`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `provider_attribute_type_creator_fk`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `provider_attribute_type_retired_by_fk`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`relationship` DROP FOREIGN KEY `person_a_is_person`,DROP FOREIGN KEY `person_b_is_person`,DROP FOREIGN KEY `relation_creator`,DROP FOREIGN KEY `relation_voider`,DROP FOREIGN KEY `relationship_changed_by`,DROP FOREIGN KEY `relationship_type_id`;
ALTER TABLE `openmrs`.`relationship` ADD CONSTRAINT `person_a_is_person`  FOREIGN KEY (`person_a`)  REFERENCES `openmrs`.`person` (`person_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `person_b_is_person`  FOREIGN KEY (`person_b`)  REFERENCES `openmrs`.`person` (`person_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `relation_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `relation_voider`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `relationship_changed_by`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `relationship_type_id`  FOREIGN KEY (`relationship`)  REFERENCES `openmrs`.`relationship_type` (`relationship_type_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`relationship_type` DROP FOREIGN KEY `user_who_created_rel`,DROP FOREIGN KEY `user_who_retired_relationship_type`;
ALTER TABLE `openmrs`.`relationship_type` ADD CONSTRAINT `user_who_created_rel`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_relationship_type`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`reporting_report_design` DROP FOREIGN KEY `changed_by?for?reporting_report_design`,DROP FOREIGN KEY `creator?for?reporting_report_design`,DROP FOREIGN KEY `report_definition_id?for?reporting_report_design`,DROP FOREIGN KEY `retired_by?for?reporting_report_design`;
ALTER TABLE `openmrs`.`reporting_report_design` ADD CONSTRAINT `changed_by?for?reporting_report_design`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `creator?for?reporting_report_design`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `report_definition_id?for?reporting_report_design`  FOREIGN KEY (`report_definition_id`)  REFERENCES `openmrs`.`serialized_object` (`serialized_object_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `retired_by?for?reporting_report_design`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`reporting_report_request` DROP FOREIGN KEY `requested_by?for?reporting_report_request`;
ALTER TABLE `openmrs`.`reporting_report_request` ADD CONSTRAINT `requested_by?for?reporting_report_request`  FOREIGN KEY (`requested_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`role_privilege` DROP FOREIGN KEY `privilege_definitons`,DROP FOREIGN KEY `role_privilege_to_role`;
ALTER TABLE `openmrs`.`role_privilege` ADD CONSTRAINT `privilege_definitons`  FOREIGN KEY (`privilege`)  REFERENCES `openmrs`.`privilege` (`privilege`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `role_privilege_to_role`  FOREIGN KEY (`role`)  REFERENCES `openmrs`.`role` (`role`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`role_role` DROP FOREIGN KEY `inherited_role`,DROP FOREIGN KEY `parent_role`;
ALTER TABLE `openmrs`.`role_role` ADD CONSTRAINT `inherited_role`  FOREIGN KEY (`child_role`)  REFERENCES `openmrs`.`role` (`role`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `parent_role`  FOREIGN KEY (`parent_role`)  REFERENCES `openmrs`.`role` (`role`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`scheduler_task_config` DROP FOREIGN KEY `scheduler_changer`,DROP FOREIGN KEY `scheduler_creator`;
ALTER TABLE `openmrs`.`scheduler_task_config` ADD CONSTRAINT `scheduler_changer`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `scheduler_creator`  FOREIGN KEY (`created_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`scheduler_task_config_property` DROP FOREIGN KEY `task_config_for_property`;
ALTER TABLE `openmrs`.`scheduler_task_config_property` ADD CONSTRAINT `task_config_for_property`  FOREIGN KEY (`task_config_id`)  REFERENCES `openmrs`.`scheduler_task_config` (`task_config_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`scheduler_task_config_property` DROP FOREIGN KEY `task_config_for_property`;
ALTER TABLE `openmrs`.`scheduler_task_config_property` ADD CONSTRAINT `task_config_for_property`  FOREIGN KEY (`task_config_id`)  REFERENCES `openmrs`.`scheduler_task_config` (`task_config_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`serialized_object` DROP FOREIGN KEY `serialized_object_changed_by`,DROP FOREIGN KEY `serialized_object_creator`,DROP FOREIGN KEY `serialized_object_retired_by`;
ALTER TABLE `openmrs`.`serialized_object` ADD CONSTRAINT `serialized_object_changed_by`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `serialized_object_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `serialized_object_retired_by`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`test_order` DROP FOREIGN KEY `test_order_order_id_fk`,DROP FOREIGN KEY `test_order_specimen_source_fk`;
ALTER TABLE `openmrs`.`test_order` ADD CONSTRAINT `test_order_order_id_fk`  FOREIGN KEY (`order_id`)  REFERENCES `openmrs`.`orders` (`order_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `test_order_specimen_source_fk`  FOREIGN KEY (`specimen_source`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`user_property` DROP FOREIGN KEY `user_property_to_users`;
ALTER TABLE `openmrs`.`user_property` ADD CONSTRAINT `user_property_to_users`  FOREIGN KEY (`user_id`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`user_role` DROP FOREIGN KEY `role_definitions`,DROP FOREIGN KEY `user_role_to_users`;
ALTER TABLE `openmrs`.`user_role` ADD CONSTRAINT `role_definitions`  FOREIGN KEY (`role`)  REFERENCES `openmrs`.`role` (`role`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_role_to_users`  FOREIGN KEY (`user_id`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`users` DROP FOREIGN KEY `person_id_for_user`,DROP FOREIGN KEY `user_creator`,DROP FOREIGN KEY `user_who_changed_user`,DROP FOREIGN KEY `user_who_retired_this_user`;
ALTER TABLE `openmrs`.`users` ADD CONSTRAINT `person_id_for_user`  FOREIGN KEY (`person_id`)  REFERENCES `openmrs`.`person` (`person_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `user_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_changed_user`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `user_who_retired_this_user`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`visit` DROP FOREIGN KEY `visit_changed_by_fk`,DROP FOREIGN KEY `visit_creator_fk`,DROP FOREIGN KEY `visit_indication_concept_fk`,DROP FOREIGN KEY `visit_location_fk`,DROP FOREIGN KEY `visit_patient_fk`,DROP FOREIGN KEY `visit_type_fk`,DROP FOREIGN KEY `visit_voided_by_fk`;
ALTER TABLE `openmrs`.`visit` ADD CONSTRAINT `visit_changed_by_fk`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `visit_creator_fk`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `visit_indication_concept_fk`  FOREIGN KEY (`indication_concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `visit_location_fk`  FOREIGN KEY (`location_id`)  REFERENCES `openmrs`.`location` (`location_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `visit_patient_fk`  FOREIGN KEY (`patient_id`)  REFERENCES `openmrs`.`patient` (`patient_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `visit_type_fk`  FOREIGN KEY (`visit_type_id`)  REFERENCES `openmrs`.`visit_type` (`visit_type_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `visit_voided_by_fk`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`visit_attribute` DROP FOREIGN KEY `visit_attribute_attribute_type_id_fk`,DROP FOREIGN KEY `visit_attribute_changed_by_fk`,DROP FOREIGN KEY `visit_attribute_creator_fk`,DROP FOREIGN KEY `visit_attribute_visit_fk`,DROP FOREIGN KEY `visit_attribute_voided_by_fk`;
ALTER TABLE `openmrs`.`visit_attribute` ADD CONSTRAINT `visit_attribute_attribute_type_id_fk`  FOREIGN KEY (`attribute_type_id`)  REFERENCES `openmrs`.`visit_attribute_type` (`visit_attribute_type_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `visit_attribute_changed_by_fk`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `visit_attribute_creator_fk`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `visit_attribute_visit_fk`  FOREIGN KEY (`visit_id`)  REFERENCES `openmrs`.`visit` (`visit_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `visit_attribute_voided_by_fk`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`visit_attribute_type` DROP FOREIGN KEY `visit_attribute_type_changed_by_fk`,DROP FOREIGN KEY `visit_attribute_type_creator_fk`,DROP FOREIGN KEY `visit_attribute_type_retired_by_fk`;
ALTER TABLE `openmrs`.`visit_attribute_type` ADD CONSTRAINT `visit_attribute_type_changed_by_fk`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `visit_attribute_type_creator_fk`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `visit_attribute_type_retired_by_fk`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;
ALTER TABLE `openmrs`.`visit_type` DROP FOREIGN KEY `visit_type_changed_by`,DROP FOREIGN KEY `visit_type_creator`,DROP FOREIGN KEY `visit_type_retired_by`;
ALTER TABLE `openmrs`.`visit_type` ADD CONSTRAINT `visit_type_changed_by`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE CASCADE,ADD CONSTRAINT `visit_type_creator`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE,ADD CONSTRAINT `visit_type_retired_by`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE CASCADE;

-- ----------------------------------------------------------------------------------------------
-- UPDATE ID'S CREATED IN THE MIDDLE
set @end_date = NOW();
set @start_date = date_sub(NOW(), INTERVAL 27 day) ;
set @count = 10000000;
set @count_obs = 100000000;
set @count_user = 10000;

Update openmrs.active_list_type SET active_list_type.active_list_type_id = (active_list_type.active_list_type_id + @count) where (date_created BETWEEN @start_date AND @end_date);
Update openmrs.cohort SET cohort.cohort_id = (cohort.cohort_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.concept SET concept.concept_id = (concept.concept_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.concept_answer SET concept_answer.concept_answer_id = (concept_answer.concept_answer_id + @count) where (date_created BETWEEN @start_date AND @end_date);
Update openmrs.concept_class SET concept_class.concept_class_id = (concept_class.concept_class_id + @count) where (date_created BETWEEN @start_date AND @end_date);
-- concept_complex
Update openmrs.concept_datatype SET concept_datatype.concept_datatype_id = (concept_datatype.concept_datatype_id + @count) where (date_created BETWEEN @start_date AND @end_date);
Update openmrs.concept_description SET concept_description.concept_description_id = (concept_description.concept_description_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.concept_map_type SET concept_map_type.concept_map_type_id = (concept_map_type.concept_map_type_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.concept_name SET concept_name.concept_name_id = (concept_name.concept_name_id + @count) where (date_created BETWEEN @start_date AND @end_date);
Update openmrs.concept_name_tag SET concept_name_tag.concept_name_tag_id = (concept_name_tag.concept_name_tag_id + @count) where (date_created BETWEEN @start_date AND @end_date);
-- concept_name_tag_map
-- concept_numeric
Update openmrs.concept_proposal SET concept_proposal.concept_proposal_id = (concept_proposal.concept_proposal_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.concept_reference_map SET concept_reference_map.concept_map_id = (concept_reference_map.concept_map_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.concept_reference_source set concept_source_id = (concept_source_id + @count) where (date_created BETWEEN @start_date AND @end_date);
Update openmrs.concept_reference_term set concept_reference_term_id = (concept_reference_term_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.concept_reference_term_map set concept_reference_term_map_id = (concept_reference_term_map_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.concept_set set concept_set_id = (concept_set_id + @count) where (date_created BETWEEN @start_date AND @end_date);
-- openmrs.concept_set_derived
Update openmrs.concept_state_conversion set concept_state_conversion_id = (concept_state_conversion_id + @count);
Update openmrs.concept_stop_word set concept_stop_word_id = (concept_stop_word_id + @count);
Update openmrs.concept_word set concept_word_id = (concept_word_id + @count);
Update openmrs.drug set drug_id = (drug_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
-- drug_ingredient
Update openmrs.orders set order_id = (order_id + @count) where (date_created BETWEEN @start_date AND @end_date);
-- Update openmrs.drug_order set order_id = (order_id + @count);
Update openmrs.encounter set encounter_id= (encounter_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.encounter_provider set encounter_provider_id= (encounter_provider_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.encounter_role set encounter_role_id= (encounter_role_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.encounter_type set encounter_type_id= (encounter_type_id + @count) where (date_created BETWEEN @start_date AND @end_date);
Update openmrs.field set field_id = (field_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
-- field_answer
Update openmrs.field_type set field_type_id = (field_type_id + @count) where (date_created BETWEEN @start_date AND @end_date);
Update openmrs.form set form_id = (form_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.form_field set form_field_id = (form_field_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.form_resource set form_resource_id = (form_resource_id + @count);
-- openmrs.global_property;
Update openmrs.location set location_id = (location_id + @count) where (date_created BETWEEN @start_date AND @end_date);
Update openmrs.location_attribute set location_attribute_id = (location_attribute_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.location_attribute_type set location_attribute_type_id = (location_attribute_type_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.location_tag set location_tag_id = (location_tag_id + @count) where (date_created BETWEEN @start_date AND @end_date);
-- location_tag_map
Update openmrs.note set note_id = (note_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.notification_alert set alert_id = (alert_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.notification_template set template_id = (template_id + @count);
Update openmrs.obs set obs_id = (obs_id + @count_obs) where (date_created BETWEEN @start_date AND @end_date);
Update openmrs.order_type set order_type_id = (order_type_id + @count) where (date_created BETWEEN @start_date AND @end_date);
Update openmrs.person set person_id = (person_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
-- Update openmrs.patient set patient_id = (patient_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.patient_identifier set patient_identifier_id = (patient_identifier_id  + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.patient_identifier_type set patient_identifier_type_id = (patient_identifier_type_id + @count) where (date_created BETWEEN @start_date AND @end_date);
Update openmrs.patient_program set patient_program_id = (patient_program_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.patient_state set patient_state_id = (patient_state_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.person_address set person_address_id = (person_address_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.person_attribute set person_attribute_id = (person_attribute_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.person_attribute_type set person_attribute_type_id = (person_attribute_type_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.person_merge_log set person_merge_log_id = (person_merge_log_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.person_name set person_name_id = (person_name_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
-- privilege
Update openmrs.program set program_id = (program_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.program_workflow set program_workflow_id = (program_workflow_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.program_workflow_state set program_workflow_state_id = (program_workflow_state_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.provider set provider_id = (provider_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.provider_attribute set provider_attribute_id = (provider_attribute_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.provider_attribute_type set provider_attribute_type_id = (provider_attribute_type_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.relationship set relationship_id = (relationship_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.relationship_type set relationship_type_id = (relationship_type_id + @count) where (date_created BETWEEN @start_date AND @end_date);
-- openmrs.role PK varchar
-- openmrs.role_privilege
-- openmrs.role_role
Update openmrs.scheduler_task_config set task_config_id = (task_config_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.scheduler_task_config_property set task_config_property_id = (task_config_property_id + @count);
Update openmrs.serialized_object set serialized_object_id = (serialized_object_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.users set user_id = (user_id + @count_user) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.visit set visit_id = (visit_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.visit_attribute set visit_attribute_id = (visit_attribute_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.visit_attribute_type set visit_attribute_type_id = (visit_attribute_type_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);
Update openmrs.visit_type set visit_type_id = (visit_type_id + @count) where (date_created BETWEEN @start_date AND @end_date) OR (date_changed BETWEEN @start_date AND @end_date);

-- ------------------------------ RESETTING AUTO INCREMENTS--------------------------------------
Select @max1 := Max(active_list_type.active_list_type_id)+ 1 from openmrs.active_list_type;
set @q1 = concat('ALTER TABLE openmrs.active_list_type AUTO_INCREMENT = ', @max1);
PREPARE s1 FROM @q1;
EXECUTE s1;

Select @max2 := Max(cohort.cohort_id)+ 1 from openmrs.cohort;
set @q2 = concat('ALTER TABLE openmrs.cohort AUTO_INCREMENT = ', @max2);
PREPARE s2 FROM @q2;
EXECUTE s2;

Select @max3 := Max(concept.concept_id)+ 1 from openmrs.concept;
set @q3 = concat('ALTER TABLE openmrs.concept AUTO_INCREMENT = ', @max3);
PREPARE s3 FROM @q3;
EXECUTE s3;

Select @max4 := Max(concept_answer.concept_answer_id)+ 1 from openmrs.concept_answer;
set @q4 = concat('ALTER TABLE openmrs.concept_answer AUTO_INCREMENT = ', @max4);
PREPARE s4 FROM @q4;
EXECUTE s4;

Select @max5 := Max(concept_class.concept_class_id)+ 1 from openmrs.concept_class;
set @q5 = concat('ALTER TABLE openmrs.concept_class AUTO_INCREMENT = ', @max5);
PREPARE s5 FROM @q5;
EXECUTE s5;

Select @max6 := Max(concept_datatype.concept_datatype_id)+ 1 from openmrs.concept_datatype;
set @q6 = concat('ALTER TABLE openmrs.concept_datatype AUTO_INCREMENT = ', @max6);
PREPARE s6 FROM @q6;
EXECUTE s6;

Select @max7 := Max(concept_description.concept_description_id)+ 1 from openmrs.concept_description;
set @q7 = concat('ALTER TABLE openmrs.concept_description AUTO_INCREMENT = ', @max7);
PREPARE s7 FROM @q7;
EXECUTE s7;

Select @max8 := Max(concept_map_type.concept_map_type_id)+ 1 from openmrs.concept_map_type;
set @q8 = concat('ALTER TABLE openmrs.concept_map_type AUTO_INCREMENT = ', @max8);
PREPARE s8 FROM @q8;
EXECUTE s8;

Select @max9 := Max(concept_name.concept_name_id)+ 1 from openmrs.concept_name;
set @q9 = concat('ALTER TABLE openmrs.concept_name AUTO_INCREMENT = ', @max9);
PREPARE s9 FROM @q9;
EXECUTE s9;

-- Select @max10 := Max(concept_name_tag.concept_name_tag_id)+ 1 from openmrs.concept_name_tag;
-- set @q10 = concat('ALTER TABLE openmrs.concept_name_tag AUTO_INCREMENT = ', @max10);
-- PREPARE s10 FROM @q10;
-- EXECUTE s10;

-- Select @max11 := Max(concept_proposal.concept_proposal_id)+ 1 from openmrs.concept_proposal;
-- set @q11 = concat('ALTER TABLE openmrs.concept_proposal AUTO_INCREMENT = ', @max11);
-- PREPARE s11 FROM @q11;
-- EXECUTE s11;

Select @max12 := Max(concept_reference_map.concept_map_id)+ 1 from openmrs.concept_reference_map;
set @q12 = concat('ALTER TABLE openmrs.concept_proposal AUTO_INCREMENT = ', @max12);
PREPARE s12 FROM @q12;
EXECUTE s12;

Select @max13 := Max(concept_reference_source.concept_source_id)+ 1 from openmrs.concept_reference_source;
set @q13 = concat('ALTER TABLE openmrs.concept_reference_source AUTO_INCREMENT = ', @max13);
PREPARE s13 FROM @q13;
EXECUTE s13;

Select @max14 := Max(concept_reference_term.concept_reference_term_id)+ 1 from openmrs.concept_reference_term;
set @q14 = concat('ALTER TABLE openmrs.concept_reference_term AUTO_INCREMENT = ', @max14);
PREPARE s14 FROM @q14;
EXECUTE s14;

-- Select @max15 := Max(concept_reference_term_map.concept_reference_term_map_id)+ 1 from openmrs.concept_reference_term_map;
-- set @q15 = concat('ALTER TABLE openmrs.concept_reference_term_map AUTO_INCREMENT = ', @max15);
-- PREPARE s15 FROM @q15;
-- EXECUTE s15;

-- Select @max16 := Max(concept_set.concept_set_id)+ 1 from openmrs.concept_set;
-- set @q16 = concat('ALTER TABLE openmrs.concept_set AUTO_INCREMENT = ', @max16);
-- PREPARE s16 FROM @q16;
-- EXECUTE s16;

-- concept_set_derived

-- Select @max17 := Max(concept_state_conversion.concept_state_conversion_id)+ 1 from openmrs.concept_state_conversion;
-- set @q17 = concat('ALTER TABLE openmrs.concept_state_conversion AUTO_INCREMENT = ', @max17);
-- PREPARE s17 FROM @q17;
-- EXECUTE s17;

Select @max18 := Max(concept_stop_word.concept_stop_word_id)+ 1 from openmrs.concept_stop_word;
set @q18 = concat('ALTER TABLE openmrs.concept_stop_word AUTO_INCREMENT = ', @max18);
PREPARE s18 FROM @q18;
EXECUTE s18;

Select @max19 := Max(concept_word.concept_word_id)+ 1 from openmrs.concept_word;
set @q19 = concat('ALTER TABLE openmrs.concept_word AUTO_INCREMENT = ', @max19);
PREPARE s19 FROM @q19;
EXECUTE s19;

Select @max20 := Max(drug.drug_id)+ 1 from openmrs.drug;
set @q20 = concat('ALTER TABLE openmrs.drug AUTO_INCREMENT = ', @max20);
PREPARE s20 FROM @q20;
EXECUTE s20;

Select @max21 := Max(drug_order.order_id)+ 1 from openmrs.drug_order;
set @q21 = concat('ALTER TABLE openmrs.drug_order AUTO_INCREMENT = ', @max21);
PREPARE s21 FROM @q21;
EXECUTE s21;

Select @max22 := Max(encounter.encounter_id)+ 1 from openmrs.encounter;
set @q22 = concat('ALTER TABLE openmrs.encounter AUTO_INCREMENT = ', @max22);
PREPARE s22 FROM @q22;
EXECUTE s22;

Select @max23 := Max(encounter_provider.encounter_provider_id)+ 1 from openmrs.encounter_provider;
set @q23 = concat('ALTER TABLE openmrs.encounter_provider AUTO_INCREMENT = ', @max23);
PREPARE s23 FROM @q23;
EXECUTE s23;

Select @max24 := Max(encounter_role.encounter_role_id)+ 1 from openmrs.encounter_role;
set @q24 = concat('ALTER TABLE openmrs.encounter_role AUTO_INCREMENT = ', @max24);
PREPARE s24 FROM @q24;
EXECUTE s24;

Select @max25 := Max(encounter_type.encounter_type_id)+ 1 from openmrs.encounter_type;
set @q25 = concat('ALTER TABLE openmrs.encounter_type AUTO_INCREMENT = ', @max25);
PREPARE s25 FROM @q25;
EXECUTE s25;

Select @max26 := Max(field.field_id)+ 1 from openmrs.field;
set @q26 = concat('ALTER TABLE openmrs.field AUTO_INCREMENT = ', @max26);
PREPARE s26 FROM @q26;
EXECUTE s26;

Select @max27 := Max(field_type.field_type_id)+ 1 from openmrs.field_type;
set @q27 = concat('ALTER TABLE openmrs.field_type AUTO_INCREMENT = ', @max27);
PREPARE s27 FROM @q27;
EXECUTE s27;

Select @max28 := Max(form.form_id)+ 1 from openmrs.form;
set @q28 = concat('ALTER TABLE openmrs.form AUTO_INCREMENT = ', @max28);
PREPARE s28 FROM @q28;
EXECUTE s28;

-- Select @max29 := Max(form_field.form_field_id)+ 1 from openmrs.form_field;
-- set @q29 = concat('ALTER TABLE openmrs.form_field AUTO_INCREMENT = ', @max29);
-- PREPARE s29 FROM @q29;
-- EXECUTE s29;

-- Select @max30 := Max(form_resource.form_resource_id)+ 1 from openmrs.form_resource;
-- set @q30 = concat('ALTER TABLE openmrs.form_resource AUTO_INCREMENT = ', @max30);
-- PREPARE s30 FROM @q30;
-- EXECUTE s30;

Select @max31 := Max(location.location_id)+ 1 from openmrs.location;
set @q31 = concat('ALTER TABLE openmrs.location AUTO_INCREMENT = ', @max31);
PREPARE s31 FROM @q31;
EXECUTE s31;

Select @max32 := Max(location_attribute.location_attribute_id)+ 1 from openmrs.location_attribute;
set @q32 = concat('ALTER TABLE openmrs.location_attribute AUTO_INCREMENT = ', @max32);
PREPARE s32 FROM @q32;
EXECUTE s32;

Select @max33 := Max(location_attribute_type.location_attribute_type_id)+ 1 from openmrs.location_attribute_type;
set @q33 = concat('ALTER TABLE openmrs.location_attribute_type AUTO_INCREMENT = ', @max33);
PREPARE s33 FROM @q33;
EXECUTE s33;

-- Select @max34 := Max(location_tag.location_tag_id)+ 1 from openmrs.location_tag;
-- set @q34 = concat('ALTER TABLE openmrs.location_tag AUTO_INCREMENT = ', @max34);
-- PREPARE s34 FROM @q34;
-- EXECUTE s34;

-- Select @max35 := Max(note.note_id)+ 1 from openmrs.note;
-- set @q35 = concat('ALTER TABLE openmrs.note AUTO_INCREMENT = ', @max35);
-- PREPARE s35 FROM @q35;
-- EXECUTE s35;

Select @max36 := Max(notification_alert.alert_id)+ 1 from openmrs.notification_alert;
set @q36 = concat('ALTER TABLE openmrs.notification_alert AUTO_INCREMENT = ', @max36);
PREPARE s36 FROM @q36;
EXECUTE s36;

-- Select @max37 := Max(notification_template.template_id)+ 1 from openmrs.notification_template;
-- set @q37 = concat('ALTER TABLE openmrs.notification_template AUTO_INCREMENT = ', @max37);
-- PREPARE s37 FROM @q37;
-- EXECUTE s37;

Select @max38 := Max(obs.obs_id)+ 1 from openmrs.obs;
set @q38 = concat('ALTER TABLE openmrs.obs AUTO_INCREMENT = ', @max38);
PREPARE s38 FROM @q38;
EXECUTE s38;

Select @max39 := Max(order_type.order_type_id)+ 1 from openmrs.order_type;
set @q39 = concat('ALTER TABLE openmrs.order_type AUTO_INCREMENT = ', @max39);
PREPARE s39 FROM @q39;
EXECUTE s39;

Select @max40 := Max(orders.order_id)+ 1 from openmrs.orders;
set @q40 = concat('ALTER TABLE openmrs.orders AUTO_INCREMENT = ', @max40);
PREPARE s40 FROM @q40;
EXECUTE s40;

Select @max41 := Max(patient.patient_id)+ 1 from openmrs.patient;
set @q41 = concat('ALTER TABLE openmrs.patient AUTO_INCREMENT = ', @max41);
PREPARE s41 FROM @q41;
EXECUTE s41;

Select @max42 := Max(patient_identifier.patient_identifier_id)+ 1 from openmrs.patient_identifier;
set @q42 = concat('ALTER TABLE openmrs.patient_identifier AUTO_INCREMENT = ', @max42);
PREPARE s42 FROM @q42;
EXECUTE s42;

Select @max43 := Max(patient_identifier_type.patient_identifier_type_id)+ 1 from openmrs.patient_identifier_type;
set @q43 = concat('ALTER TABLE openmrs.patient_identifier_type AUTO_INCREMENT = ', @max43);
PREPARE s43 FROM @q43;
EXECUTE s43;

-- Select @max44 := Max(patient_program.patient_program_id)+ 1 from openmrs.patient_program;
-- set @q44 = concat('ALTER TABLE openmrs.patient_program AUTO_INCREMENT = ', @max44);
-- PREPARE s44 FROM @q44;
-- EXECUTE s44;

-- Select @max45 := Max(patient_state.patient_state_id)+ 1 from openmrs.patient_state;
-- set @q45 = concat('ALTER TABLE openmrs.patient_state AUTO_INCREMENT = ', @max45);
-- PREPARE s45 FROM @q45;
-- EXECUTE s45;

Select @max46 := Max(person.person_id)+ 1 from openmrs.person;
set @q46 = concat('ALTER TABLE openmrs.person AUTO_INCREMENT = ', @max46);
PREPARE s46 FROM @q46;
EXECUTE s46;

Select @max47 := Max(person_address.person_address_id)+ 1 from openmrs.person_address;
set @q47 = concat('ALTER TABLE openmrs.person_address AUTO_INCREMENT = ', @max47);
PREPARE s47 FROM @q47;
EXECUTE s47;

Select @max48 := Max(person_attribute.person_attribute_id)+ 1 from openmrs.person_attribute;
set @q48 = concat('ALTER TABLE openmrs.person_attribute AUTO_INCREMENT = ', @max48);
PREPARE s48 FROM @q48;
EXECUTE s48;

Select @max49 := Max(person_attribute_type.person_attribute_type_id)+ 1 from openmrs.person_attribute_type;
set @q49 = concat('ALTER TABLE openmrs.person_attribute_type AUTO_INCREMENT = ', @max49);
PREPARE s49 FROM @q49;
EXECUTE s49;

Select @max50 := Max(person_merge_log.person_merge_log_id)+ 1 from openmrs.person_merge_log;
set @q50 = concat('ALTER TABLE openmrs.person_merge_log AUTO_INCREMENT = ', @max50);
PREPARE s50 FROM @q50;
EXECUTE s50;

Select @max51 := Max(person_name.person_name_id)+ 1 from openmrs.person_name;
set @q51 = concat('ALTER TABLE openmrs.person_name AUTO_INCREMENT = ', @max51);
PREPARE s51 FROM @q51;
EXECUTE s51;

-- Select @max52 := Max(program.program_id)+ 1 from openmrs.program;
-- set @q52 = concat('ALTER TABLE openmrs.program AUTO_INCREMENT = ', @max52);
-- PREPARE s52 FROM @q52;
-- EXECUTE s52;

-- Select @max53 := Max(program_workflow.program_workflow_id)+ 1 from openmrs.program_workflow;
-- set @q53 = concat('ALTER TABLE openmrs.program_workflow AUTO_INCREMENT = ', @max53);
-- PREPARE s53 FROM @q53;
-- EXECUTE s53;

-- Select @max54 := Max(program_workflow_state.program_workflow_state_id)+ 1 from openmrs.program_workflow_state;
-- set @q52 = concat('ALTER TABLE openmrs.program_workflow_state AUTO_INCREMENT = ', @max54);
-- PREPARE s54 FROM @q54;
-- EXECUTE s54;

Select @max55 := Max(provider.provider_id)+ 1 from openmrs.provider;
set @q55 = concat('ALTER TABLE openmrs.provider AUTO_INCREMENT = ', @max55);
PREPARE s55 FROM @q55;
EXECUTE s55;

Select @max56 := Max(provider_attribute.provider_attribute_id)+ 1 from openmrs.provider_attribute;
set @q56 = concat('ALTER TABLE openmrs.provider_attribute AUTO_INCREMENT = ', @max56);
PREPARE s56 FROM @q56;
EXECUTE s56;

Select @max57 := Max(provider_attribute_type.provider_attribute_type_id)+ 1 from openmrs.provider_attribute_type;
set @q57 = concat('ALTER TABLE openmrs.provider_attribute_type AUTO_INCREMENT = ', @max57);
PREPARE s57 FROM @q57;
EXECUTE s57;

-- Select @max58 := Max(relationship.relationship_id)+ 1 from openmrs.relationship;
-- set @q56 = concat('ALTER TABLE openmrs.relationship AUTO_INCREMENT = ', @max58);
-- PREPARE s58 FROM @q58;
-- EXECUTE s58;

Select @max59 := Max(relationship_type.relationship_type_id)+ 1 from openmrs.relationship_type;
set @q59 = concat('ALTER TABLE openmrs.relationship_type AUTO_INCREMENT = ', @max59);
PREPARE s59 FROM @q59;
EXECUTE s59;

Select @max60 := Max(scheduler_task_config.task_config_id)+ 1 from openmrs.scheduler_task_config;
set @q60 = concat('ALTER TABLE openmrs.scheduler_task_config AUTO_INCREMENT = ', @max60);
PREPARE s60 FROM @q60;
EXECUTE s60;

-- Select @max61 := Max(scheduler_task_config_property.task_config_property_id)+ 1 from openmrs.scheduler_task_config_property;
-- set @q61 = concat('ALTER TABLE openmrs.scheduler_task_config_property AUTO_INCREMENT = ', @max61);
-- PREPARE s61 FROM @q61;
-- EXECUTE s61;

Select @max62 := Max(serialized_object.serialized_object_id)+ 1 from openmrs.serialized_object;
set @q62 = concat('ALTER TABLE openmrs.serialized_object AUTO_INCREMENT = ', @max62);
select @q62;
PREPARE s62 FROM @q62;
EXECUTE s62;

Select @max63 := Max(users.user_id)+ 1 from openmrs.users;
set @q63 = concat('ALTER TABLE openmrs.users AUTO_INCREMENT = ', @max63);
PREPARE s63 FROM @q63;
EXECUTE s63;

-- Select @max64 := Max(visit.visit_id)+ 1 from openmrs.visit;
-- set @q64 = concat('ALTER TABLE openmrs.visit AUTO_INCREMENT = ', @max64);
-- PREPARE s64 FROM @q64;
-- EXECUTE s64;

-- Select @max65 := Max(visit_attribute.visit_attribute_id)+ 1 from openmrs.visit_attribute;
-- set @q65 = concat('ALTER TABLE openmrs.visit_attribute AUTO_INCREMENT = ', @max65);
-- PREPARE s65 FROM @q65;
-- EXECUTE s65;

-- Select @max66 := Max(visit_attribute_type.visit_attribute_type_id)+ 1 from openmrs.visit_attribute_type;
-- set @q66 = concat('ALTER TABLE openmrs.visit_attribute_type AUTO_INCREMENT = ', @max66);
-- PREPARE s66 FROM @q66;
-- EXECUTE s66;

-- Select @max67 := Max(visit_type.visit_type_id)+ 1 from openmrs.visit_type;
-- set @q67 = concat('ALTER TABLE openmrs.visit_type AUTO_INCREMENT = ', @max67);
-- PREPARE s67 FROM @q67;
-- EXECUTE s67;

-- ---------------------------------DISABLING CASCADE--------------------------------------------
ALTER TABLE `openmrs`.`active_list_type` DROP FOREIGN KEY `user_who_created_active_list_type`, DROP FOREIGN KEY `user_who_retired_active_list_type`; 
ALTER TABLE `openmrs`.`active_list_type` ADD CONSTRAINT `user_who_created_active_list_type`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_active_list_type`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`cohort` DROP FOREIGN KEY `cohort_creator`, DROP FOREIGN KEY `user_who_changed_cohort`, DROP FOREIGN KEY `user_who_voided_cohort`; 
ALTER TABLE `openmrs`.`cohort` ADD CONSTRAINT `cohort_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_changed_cohort`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_voided_cohort`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`cohort_member` DROP FOREIGN KEY `parent_cohort`; 
ALTER TABLE `openmrs`.`cohort_member` ADD CONSTRAINT `parent_cohort`   FOREIGN KEY (`cohort_id`)   REFERENCES `openmrs`.`cohort` (`cohort_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept` DROP FOREIGN KEY `concept_classes`, DROP FOREIGN KEY `concept_creator`, DROP FOREIGN KEY `concept_datatypes`, DROP FOREIGN KEY `user_who_changed_concept`, DROP FOREIGN KEY `user_who_retired_concept`; 
ALTER TABLE `openmrs`.`concept` ADD CONSTRAINT `concept_classes`   FOREIGN KEY (`class_id`)   REFERENCES `openmrs`.`concept_class` (`concept_class_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `concept_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `concept_datatypes`   FOREIGN KEY (`datatype_id`)   REFERENCES `openmrs`.`concept_datatype` (`concept_datatype_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_changed_concept`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_concept`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_answer` DROP FOREIGN KEY `answer`, DROP FOREIGN KEY `answer_answer_drug_fk`, DROP FOREIGN KEY `answer_creator`, DROP FOREIGN KEY `answers_for_concept`; 
ALTER TABLE `openmrs`.`concept_answer` ADD CONSTRAINT `answer`   FOREIGN KEY (`answer_concept`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `answer_answer_drug_fk`   FOREIGN KEY (`answer_drug`)   REFERENCES `openmrs`.`drug` (`drug_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `answer_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `answers_for_concept`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_class` DROP FOREIGN KEY `concept_class_creator`, DROP FOREIGN KEY `user_who_retired_concept_class`; 
ALTER TABLE `openmrs`.`concept_class` ADD CONSTRAINT `concept_class_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_concept_class`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_complex` DROP FOREIGN KEY `concept_attributes`; 
ALTER TABLE `openmrs`.`concept_complex` ADD CONSTRAINT `concept_attributes`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_datatype` DROP FOREIGN KEY `concept_datatype_creator`, DROP FOREIGN KEY `user_who_retired_concept_datatype`; 
ALTER TABLE `openmrs`.`concept_datatype` ADD CONSTRAINT `concept_datatype_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_concept_datatype`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_description` DROP FOREIGN KEY `description_for_concept`, DROP FOREIGN KEY `user_who_changed_description`, DROP FOREIGN KEY `user_who_created_description`; 
ALTER TABLE `openmrs`.`concept_description` ADD CONSTRAINT `description_for_concept`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_changed_description`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_created_description`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_map_type` DROP FOREIGN KEY `mapped_user_changed_concept_map_type`, DROP FOREIGN KEY `mapped_user_creator_concept_map_type`, DROP FOREIGN KEY `mapped_user_retired_concept_map_type`; 
ALTER TABLE `openmrs`.`concept_map_type` ADD CONSTRAINT `mapped_user_changed_concept_map_type`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_user_creator_concept_map_type`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_user_retired_concept_map_type`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_name` DROP FOREIGN KEY `name_for_concept`, DROP FOREIGN KEY `user_who_created_name`, DROP FOREIGN KEY `user_who_voided_this_name`; 
ALTER TABLE `openmrs`.`concept_name` ADD CONSTRAINT `name_for_concept`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_created_name`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_voided_this_name`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_name_tag_map` DROP FOREIGN KEY `mapped_concept_name`, DROP FOREIGN KEY `mapped_concept_name_tag`; 
ALTER TABLE `openmrs`.`concept_name_tag_map` ADD CONSTRAINT `mapped_concept_name`   FOREIGN KEY (`concept_name_id`)   REFERENCES `openmrs`.`concept_name` (`concept_name_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_concept_name_tag`   FOREIGN KEY (`concept_name_tag_id`)   REFERENCES `openmrs`.`concept_name_tag` (`concept_name_tag_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_numeric` DROP FOREIGN KEY `numeric_attributes`; 
ALTER TABLE `openmrs`.`concept_numeric` ADD CONSTRAINT `numeric_attributes`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_proposal` DROP FOREIGN KEY `concept_for_proposal`, DROP FOREIGN KEY `encounter_for_proposal`, DROP FOREIGN KEY `proposal_obs_concept_id`, DROP FOREIGN KEY `proposal_obs_id`, DROP FOREIGN KEY `user_who_changed_proposal`, DROP FOREIGN KEY `user_who_created_proposal`; 
ALTER TABLE `openmrs`.`concept_proposal` ADD CONSTRAINT `concept_for_proposal`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `encounter_for_proposal`   FOREIGN KEY (`encounter_id`)   REFERENCES `openmrs`.`encounter` (`encounter_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `proposal_obs_concept_id`   FOREIGN KEY (`obs_concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `proposal_obs_id`   FOREIGN KEY (`obs_id`)   REFERENCES `openmrs`.`obs` (`obs_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_changed_proposal`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_created_proposal`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_proposal_tag_map` DROP FOREIGN KEY `mapped_concept_proposal`, DROP FOREIGN KEY `mapped_concept_proposal_tag`; 
ALTER TABLE `openmrs`.`concept_proposal_tag_map` ADD CONSTRAINT `mapped_concept_proposal`   FOREIGN KEY (`concept_proposal_id`)   REFERENCES `openmrs`.`concept_proposal` (`concept_proposal_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_concept_proposal_tag`   FOREIGN KEY (`concept_name_tag_id`)   REFERENCES `openmrs`.`concept_name_tag` (`concept_name_tag_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_reference_map` DROP FOREIGN KEY `map_creator`, DROP FOREIGN KEY `map_for_concept`, DROP FOREIGN KEY `mapped_concept_map_type`, DROP FOREIGN KEY `mapped_concept_reference_term`, DROP FOREIGN KEY `mapped_user_changed_ref_term`; 
ALTER TABLE `openmrs`.`concept_reference_map` ADD CONSTRAINT `map_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `map_for_concept`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_concept_map_type`   FOREIGN KEY (`concept_map_type_id`)   REFERENCES `openmrs`.`concept_map_type` (`concept_map_type_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_concept_reference_term`   FOREIGN KEY (`concept_reference_term_id`)   REFERENCES `openmrs`.`concept_reference_term` (`concept_reference_term_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_user_changed_ref_term`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_reference_source` DROP FOREIGN KEY `concept_source_creator`, DROP FOREIGN KEY `user_who_retired_concept_source`; 
ALTER TABLE `openmrs`.`concept_reference_source` ADD CONSTRAINT `concept_source_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_concept_source`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_reference_term` DROP FOREIGN KEY `mapped_concept_source`, DROP FOREIGN KEY `mapped_user_changed`, DROP FOREIGN KEY `mapped_user_creator`, DROP FOREIGN KEY `mapped_user_retired`; 
ALTER TABLE `openmrs`.`concept_reference_term` ADD CONSTRAINT `mapped_concept_source`   FOREIGN KEY (`concept_source_id`)   REFERENCES `openmrs`.`concept_reference_source` (`concept_source_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_user_changed`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_user_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_user_retired`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_reference_term_map` DROP FOREIGN KEY `mapped_concept_map_type_ref_term_map`, DROP FOREIGN KEY `mapped_term_a`, DROP FOREIGN KEY `mapped_term_b`, DROP FOREIGN KEY `mapped_user_changed_ref_term_map`, DROP FOREIGN KEY `mapped_user_creator_ref_term_map`; 
ALTER TABLE `openmrs`.`concept_reference_term_map` ADD CONSTRAINT `mapped_concept_map_type_ref_term_map`   FOREIGN KEY (`a_is_to_b_id`)   REFERENCES `openmrs`.`concept_map_type` (`concept_map_type_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_term_a`   FOREIGN KEY (`term_a_id`)   REFERENCES `openmrs`.`concept_reference_term` (`concept_reference_term_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_term_b`   FOREIGN KEY (`term_b_id`)   REFERENCES `openmrs`.`concept_reference_term` (`concept_reference_term_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_user_changed_ref_term_map`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `mapped_user_creator_ref_term_map`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_set` DROP FOREIGN KEY `has_a`, DROP FOREIGN KEY `user_who_created`; 
ALTER TABLE `openmrs`.`concept_set` ADD CONSTRAINT `has_a`   FOREIGN KEY (`concept_set`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_created`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_state_conversion` DROP FOREIGN KEY `concept_triggers_conversion`, DROP FOREIGN KEY `conversion_involves_workflow`, DROP FOREIGN KEY `conversion_to_state`; 
ALTER TABLE `openmrs`.`concept_state_conversion` ADD CONSTRAINT `concept_triggers_conversion`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `conversion_involves_workflow`   FOREIGN KEY (`program_workflow_id`)   REFERENCES `openmrs`.`program_workflow` (`program_workflow_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `conversion_to_state`   FOREIGN KEY (`program_workflow_state_id`)   REFERENCES `openmrs`.`program_workflow_state` (`program_workflow_state_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`concept_word` DROP FOREIGN KEY `word_for`, DROP FOREIGN KEY `word_for_name`; 
ALTER TABLE `openmrs`.`concept_word` ADD CONSTRAINT `word_for`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `word_for_name`   FOREIGN KEY (`concept_name_id`)   REFERENCES `openmrs`.`concept_name` (`concept_name_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`drug` DROP FOREIGN KEY `dosage_form_concept`, DROP FOREIGN KEY `drug_changed_by`, DROP FOREIGN KEY `drug_creator`, DROP FOREIGN KEY `drug_retired_by`, DROP FOREIGN KEY `primary_drug_concept`, DROP FOREIGN KEY `route_concept`; 
ALTER TABLE `openmrs`.`drug` ADD CONSTRAINT `dosage_form_concept`   FOREIGN KEY (`dosage_form`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `drug_changed_by`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `drug_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `drug_retired_by`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `primary_drug_concept`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `route_concept`   FOREIGN KEY (`route`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`drug_ingredient` DROP FOREIGN KEY `combination_drug`, DROP FOREIGN KEY `ingredient`; 
ALTER TABLE `openmrs`.`drug_ingredient` ADD CONSTRAINT `combination_drug`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `ingredient`   FOREIGN KEY (`ingredient_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`drug_order` DROP FOREIGN KEY `extends_order`, DROP FOREIGN KEY `inventory_item`; 
ALTER TABLE `openmrs`.`drug_order` ADD CONSTRAINT `extends_order`   FOREIGN KEY (`order_id`)   REFERENCES `openmrs`.`orders` (`order_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `inventory_item`   FOREIGN KEY (`drug_inventory_id`)   REFERENCES `openmrs`.`drug` (`drug_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`encounter` DROP FOREIGN KEY `encounter_changed_by`, DROP FOREIGN KEY `encounter_form`, DROP FOREIGN KEY `encounter_ibfk_1`, DROP FOREIGN KEY `encounter_location`, DROP FOREIGN KEY `encounter_type_id`, DROP FOREIGN KEY `encounter_visit_id_fk`, DROP FOREIGN KEY `user_who_voided_encounter`; 
ALTER TABLE `openmrs`.`encounter` ADD CONSTRAINT `encounter_changed_by`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `encounter_form`   FOREIGN KEY (`form_id`)   REFERENCES `openmrs`.`form` (`form_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `encounter_ibfk_1`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `encounter_location`   FOREIGN KEY (`location_id`)   REFERENCES `openmrs`.`location` (`location_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `encounter_type_id`   FOREIGN KEY (`encounter_type`)   REFERENCES `openmrs`.`encounter_type` (`encounter_type_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `encounter_visit_id_fk`   FOREIGN KEY (`visit_id`)   REFERENCES `openmrs`.`visit` (`visit_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_voided_encounter`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`encounter_provider` DROP FOREIGN KEY `encounter_id_fk`, DROP FOREIGN KEY `encounter_role_id_fk`, DROP FOREIGN KEY `provider_id_fk`; 
ALTER TABLE `openmrs`.`encounter_provider` ADD CONSTRAINT `encounter_id_fk`   FOREIGN KEY (`encounter_id`)   REFERENCES `openmrs`.`encounter` (`encounter_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `encounter_role_id_fk`   FOREIGN KEY (`encounter_role_id`)   REFERENCES `openmrs`.`encounter_role` (`encounter_role_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `provider_id_fk`   FOREIGN KEY (`provider_id`)   REFERENCES `openmrs`.`provider` (`provider_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`encounter_role` DROP FOREIGN KEY `encounter_role_changed_by_fk`, DROP FOREIGN KEY `encounter_role_creator_fk`, DROP FOREIGN KEY `encounter_role_retired_by_fk`; 
ALTER TABLE `openmrs`.`encounter_role` ADD CONSTRAINT `encounter_role_changed_by_fk`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `encounter_role_creator_fk`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `encounter_role_retired_by_fk`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`encounter_type` DROP FOREIGN KEY `user_who_created_type`, DROP FOREIGN KEY `user_who_retired_encounter_type`; 
ALTER TABLE `openmrs`.`encounter_type` ADD CONSTRAINT `user_who_created_type`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_encounter_type`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`field` DROP FOREIGN KEY `concept_for_field`, DROP FOREIGN KEY `type_of_field`, DROP FOREIGN KEY `user_who_changed_field`, DROP FOREIGN KEY `user_who_created_field`, DROP FOREIGN KEY `user_who_retired_field`; 
ALTER TABLE `openmrs`.`field` ADD CONSTRAINT `concept_for_field`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `type_of_field`   FOREIGN KEY (`field_type`)   REFERENCES `openmrs`.`field_type` (`field_type_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_changed_field`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_created_field`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_field`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`field_answer` DROP FOREIGN KEY `answers_for_field`, DROP FOREIGN KEY `field_answer_concept`, DROP FOREIGN KEY `user_who_created_field_answer`; 
ALTER TABLE `openmrs`.`field_answer` ADD CONSTRAINT `answers_for_field`   FOREIGN KEY (`field_id`)   REFERENCES `openmrs`.`field` (`field_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `field_answer_concept`   FOREIGN KEY (`answer_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_created_field_answer`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`field_type` DROP FOREIGN KEY `user_who_created_field_type`; 
ALTER TABLE `openmrs`.`field_type` ADD CONSTRAINT `user_who_created_field_type`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`form` DROP FOREIGN KEY `form_encounter_type`, DROP FOREIGN KEY `user_who_created_form`, DROP FOREIGN KEY `user_who_last_changed_form`, DROP FOREIGN KEY `user_who_retired_form`; 
ALTER TABLE `openmrs`.`form` ADD CONSTRAINT `form_encounter_type`   FOREIGN KEY (`encounter_type`)   REFERENCES `openmrs`.`encounter_type` (`encounter_type_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_created_form`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_last_changed_form`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_form`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`form_field` DROP FOREIGN KEY `field_within_form`, DROP FOREIGN KEY `form_containing_field`, DROP FOREIGN KEY `form_field_hierarchy`, DROP FOREIGN KEY `user_who_created_form_field`, DROP FOREIGN KEY `user_who_last_changed_form_field`; 
ALTER TABLE `openmrs`.`form_field` ADD CONSTRAINT `field_within_form`   FOREIGN KEY (`field_id`)   REFERENCES `openmrs`.`field` (`field_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `form_containing_field`   FOREIGN KEY (`form_id`)   REFERENCES `openmrs`.`form` (`form_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `form_field_hierarchy`   FOREIGN KEY (`parent_form_field`)   REFERENCES `openmrs`.`form_field` (`form_field_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_created_form_field`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_last_changed_form_field`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`form_resource` DROP FOREIGN KEY `form_resource_form_fk`; 
ALTER TABLE `openmrs`.`form_resource` ADD CONSTRAINT `form_resource_form_fk`   FOREIGN KEY (`form_id`)   REFERENCES `openmrs`.`form` (`form_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`location` DROP FOREIGN KEY `parent_location`, DROP FOREIGN KEY `user_who_created_location`, DROP FOREIGN KEY `user_who_retired_location`; 
ALTER TABLE `openmrs`.`location` ADD CONSTRAINT `parent_location`   FOREIGN KEY (`parent_location`)   REFERENCES `openmrs`.`location` (`location_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_created_location`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_location`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`location_attribute` DROP FOREIGN KEY `location_attribute_attribute_type_id_fk`, DROP FOREIGN KEY `location_attribute_changed_by_fk`, DROP FOREIGN KEY `location_attribute_creator_fk`, DROP FOREIGN KEY `location_attribute_location_fk`, DROP FOREIGN KEY `location_attribute_voided_by_fk`; 
ALTER TABLE `openmrs`.`location_attribute` ADD CONSTRAINT `location_attribute_attribute_type_id_fk`   FOREIGN KEY (`attribute_type_id`)   REFERENCES `openmrs`.`location_attribute_type` (`location_attribute_type_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `location_attribute_changed_by_fk`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `location_attribute_creator_fk`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `location_attribute_location_fk`   FOREIGN KEY (`location_id`)   REFERENCES `openmrs`.`location` (`location_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `location_attribute_voided_by_fk`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`location_attribute_type` DROP FOREIGN KEY `location_attribute_type_changed_by_fk`, DROP FOREIGN KEY `location_attribute_type_creator_fk`, DROP FOREIGN KEY `location_attribute_type_retired_by_fk`; 
ALTER TABLE `openmrs`.`location_attribute_type` ADD CONSTRAINT `location_attribute_type_changed_by_fk`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `location_attribute_type_creator_fk`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `location_attribute_type_retired_by_fk`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`location_tag` DROP FOREIGN KEY `location_tag_creator`, DROP FOREIGN KEY `location_tag_retired_by`; 
ALTER TABLE `openmrs`.`location_tag` ADD CONSTRAINT `location_tag_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `location_tag_retired_by`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`location_tag_map` DROP FOREIGN KEY `location_tag_map_location`, DROP FOREIGN KEY `location_tag_map_tag`; 
ALTER TABLE `openmrs`.`location_tag_map` ADD CONSTRAINT `location_tag_map_location`   FOREIGN KEY (`location_id`)   REFERENCES `openmrs`.`location` (`location_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `location_tag_map_tag`   FOREIGN KEY (`location_tag_id`)   REFERENCES `openmrs`.`location_tag` (`location_tag_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`note` DROP FOREIGN KEY `encounter_note`, DROP FOREIGN KEY `note_hierarchy`, DROP FOREIGN KEY `obs_note`, DROP FOREIGN KEY `user_who_changed_note`, DROP FOREIGN KEY `user_who_created_note`; 
ALTER TABLE `openmrs`.`note` ADD CONSTRAINT `encounter_note`   FOREIGN KEY (`encounter_id`)   REFERENCES `openmrs`.`encounter` (`encounter_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `note_hierarchy`   FOREIGN KEY (`parent`)   REFERENCES `openmrs`.`note` (`note_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `obs_note`   FOREIGN KEY (`obs_id`)   REFERENCES `openmrs`.`obs` (`obs_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_changed_note`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_created_note`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`notification_alert` DROP FOREIGN KEY `alert_creator`, DROP FOREIGN KEY `user_who_changed_alert`; 
ALTER TABLE `openmrs`.`notification_alert` ADD CONSTRAINT `alert_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_changed_alert`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`notification_alert_recipient` DROP FOREIGN KEY `alert_read_by_user`, DROP FOREIGN KEY `id_of_alert`; 
ALTER TABLE `openmrs`.`notification_alert_recipient` ADD CONSTRAINT `alert_read_by_user`   FOREIGN KEY (`user_id`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `id_of_alert`   FOREIGN KEY (`alert_id`)   REFERENCES `openmrs`.`notification_alert` (`alert_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`obs` DROP FOREIGN KEY `answer_concept`,DROP FOREIGN KEY `answer_concept_drug`,DROP FOREIGN KEY `encounter_observations`,DROP FOREIGN KEY `obs_concept`,DROP FOREIGN KEY `obs_enterer`,DROP FOREIGN KEY `obs_grouping_id`,DROP FOREIGN KEY `obs_location`,DROP FOREIGN KEY `obs_name_of_coded_value`,DROP FOREIGN KEY `obs_order`,DROP FOREIGN KEY `previous_version`,DROP FOREIGN KEY `user_who_voided_obs`;
ALTER TABLE `openmrs`.`obs` ADD CONSTRAINT `answer_concept`  FOREIGN KEY (`value_coded`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON DELETE RESTRICT  ON UPDATE RESTRICT,ADD CONSTRAINT `answer_concept_drug` FOREIGN KEY (`value_drug`)  REFERENCES `openmrs`.`drug` (`drug_id`)  ON UPDATE RESTRICT,ADD CONSTRAINT `encounter_observations`  FOREIGN KEY (`encounter_id`)  REFERENCES `openmrs`.`encounter` (`encounter_id`)  ON UPDATE RESTRICT,ADD CONSTRAINT `obs_concept`  FOREIGN KEY (`concept_id`)  REFERENCES `openmrs`.`concept` (`concept_id`)  ON UPDATE RESTRICT,ADD CONSTRAINT `obs_enterer`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE RESTRICT,ADD CONSTRAINT `obs_grouping_id`  FOREIGN KEY (`obs_group_id`)  REFERENCES `openmrs`.`obs` (`obs_id`)  ON UPDATE RESTRICT,ADD CONSTRAINT `obs_location`  FOREIGN KEY (`location_id`)  REFERENCES `openmrs`.`location` (`location_id`)  ON UPDATE RESTRICT,ADD CONSTRAINT `obs_name_of_coded_value`  FOREIGN KEY (`value_coded_name_id`)  REFERENCES `openmrs`.`concept_name` (`concept_name_id`)  ON UPDATE RESTRICT,ADD CONSTRAINT `obs_order`  FOREIGN KEY (`order_id`)  REFERENCES `openmrs`.`orders` (`order_id`)  ON UPDATE RESTRICT,ADD CONSTRAINT `previous_version`  FOREIGN KEY (`previous_version`)  REFERENCES `openmrs`.`obs` (`obs_id`)  ON UPDATE RESTRICT,ADD CONSTRAINT `user_who_voided_obs`  FOREIGN KEY (`voided_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`order_type` DROP FOREIGN KEY `type_created_by`, DROP FOREIGN KEY `user_who_retired_order_type`; 
ALTER TABLE `openmrs`.`order_type` ADD CONSTRAINT `type_created_by`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_order_type`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`orders` DROP FOREIGN KEY `discontinued_because`, DROP FOREIGN KEY `order_creator`, DROP FOREIGN KEY `orderer_not_drug`, DROP FOREIGN KEY `orders_in_encounter`, DROP FOREIGN KEY `type_of_order`, DROP FOREIGN KEY `user_who_discontinued_order`, DROP FOREIGN KEY `user_who_voided_order`; 
ALTER TABLE `openmrs`.`orders` ADD CONSTRAINT `discontinued_because`   FOREIGN KEY (`discontinued_reason`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `order_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `orderer_not_drug`   FOREIGN KEY (`orderer`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `orders_in_encounter`   FOREIGN KEY (`encounter_id`)   REFERENCES `openmrs`.`encounter` (`encounter_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `type_of_order`   FOREIGN KEY (`order_type_id`)   REFERENCES `openmrs`.`order_type` (`order_type_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_discontinued_order`   FOREIGN KEY (`discontinued_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_voided_order`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`patient` DROP FOREIGN KEY `user_who_changed_pat`, DROP FOREIGN KEY `user_who_created_patient`, DROP FOREIGN KEY `user_who_voided_patient`; 
ALTER TABLE `openmrs`.`patient` ADD CONSTRAINT `user_who_changed_pat`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_created_patient`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_voided_patient`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`patient_identifier` DROP FOREIGN KEY `defines_identifier_type`, DROP FOREIGN KEY `identifier_creator`, DROP FOREIGN KEY `identifier_voider`, DROP FOREIGN KEY `patient_identifier_changed_by`, DROP FOREIGN KEY `patient_identifier_ibfk_2`; 
ALTER TABLE `openmrs`.`patient_identifier` ADD CONSTRAINT `defines_identifier_type`   FOREIGN KEY (`identifier_type`)   REFERENCES `openmrs`.`patient_identifier_type` (`patient_identifier_type_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `identifier_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `identifier_voider`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `patient_identifier_changed_by`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `patient_identifier_ibfk_2`   FOREIGN KEY (`location_id`)   REFERENCES `openmrs`.`location` (`location_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`patient_identifier_type` DROP FOREIGN KEY `type_creator`, DROP FOREIGN KEY `user_who_retired_patient_identifier_type`; 
ALTER TABLE `openmrs`.`patient_identifier_type` ADD CONSTRAINT `type_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_patient_identifier_type`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`patient_program` DROP FOREIGN KEY `patient_program_creator`, DROP FOREIGN KEY `patient_program_location_id`, DROP FOREIGN KEY `patient_program_outcome_concept_id_fk`, DROP FOREIGN KEY `program_for_patient`, DROP FOREIGN KEY `user_who_changed`, DROP FOREIGN KEY `user_who_voided_patient_program`; 
ALTER TABLE `openmrs`.`patient_program` ADD CONSTRAINT `patient_program_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `patient_program_location_id`   FOREIGN KEY (`location_id`)   REFERENCES `openmrs`.`location` (`location_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `patient_program_outcome_concept_id_fk`   FOREIGN KEY (`outcome_concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `program_for_patient`   FOREIGN KEY (`program_id`)   REFERENCES `openmrs`.`program` (`program_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_changed`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_voided_patient_program`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT; 
ALTER TABLE `openmrs`.`patient_state` DROP FOREIGN KEY `patient_program_for_state`, DROP FOREIGN KEY `patient_state_changer`, DROP FOREIGN KEY `patient_state_creator`, DROP FOREIGN KEY `patient_state_voider`, DROP FOREIGN KEY `state_for_patient`; 
ALTER TABLE `openmrs`.`patient_state` ADD CONSTRAINT `patient_program_for_state`   FOREIGN KEY (`patient_program_id`)   REFERENCES `openmrs`.`patient_program` (`patient_program_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `patient_state_changer`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `patient_state_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `patient_state_voider`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `state_for_patient`   FOREIGN KEY (`state`)   REFERENCES `openmrs`.`program_workflow_state` (`program_workflow_state_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`person` DROP FOREIGN KEY `person_died_because`, DROP FOREIGN KEY `user_who_changed_person`, DROP FOREIGN KEY `user_who_created_person`, DROP FOREIGN KEY `user_who_voided_person`; 
ALTER TABLE `openmrs`.`person` ADD CONSTRAINT `person_died_because`   FOREIGN KEY (`cause_of_death`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_changed_person`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_created_person`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_voided_person`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`person_address` DROP FOREIGN KEY `patient_address_creator`, DROP FOREIGN KEY `patient_address_void`, DROP FOREIGN KEY `person_address_changed_by`; 
ALTER TABLE `openmrs`.`person_address` ADD CONSTRAINT `patient_address_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `patient_address_void`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `person_address_changed_by`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`person_attribute` DROP FOREIGN KEY `attribute_changer`, DROP FOREIGN KEY `attribute_creator`, DROP FOREIGN KEY `attribute_voider`, DROP FOREIGN KEY `defines_attribute_type`, DROP FOREIGN KEY `identifies_person`; 
ALTER TABLE `openmrs`.`person_attribute` ADD CONSTRAINT `attribute_changer`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `attribute_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `attribute_voider`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `defines_attribute_type`   FOREIGN KEY (`person_attribute_type_id`)   REFERENCES `openmrs`.`person_attribute_type` (`person_attribute_type_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `identifies_person`   FOREIGN KEY (`person_id`)   REFERENCES `openmrs`.`person` (`person_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`person_attribute_type` DROP FOREIGN KEY `attribute_type_changer`, DROP FOREIGN KEY `attribute_type_creator`, DROP FOREIGN KEY `privilege_which_can_edit`, DROP FOREIGN KEY `user_who_retired_person_attribute_type`; 
ALTER TABLE `openmrs`.`person_attribute_type` ADD CONSTRAINT `attribute_type_changer`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `attribute_type_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `privilege_which_can_edit`   FOREIGN KEY (`edit_privilege`)   REFERENCES `openmrs`.`privilege` (`privilege`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_person_attribute_type`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`person_merge_log` DROP FOREIGN KEY `person_merge_log_changed_by_fk`, DROP FOREIGN KEY `person_merge_log_creator`, DROP FOREIGN KEY `person_merge_log_loser`, DROP FOREIGN KEY `person_merge_log_voided_by_fk`, DROP FOREIGN KEY `person_merge_log_winner`; 
ALTER TABLE `openmrs`.`person_merge_log` ADD CONSTRAINT `person_merge_log_changed_by_fk`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `person_merge_log_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `person_merge_log_loser`   FOREIGN KEY (`loser_person_id`)   REFERENCES `openmrs`.`person` (`person_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `person_merge_log_voided_by_fk`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `person_merge_log_winner`   FOREIGN KEY (`winner_person_id`)   REFERENCES `openmrs`.`person` (`person_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`person_name` DROP FOREIGN KEY `user_who_made_name`, DROP FOREIGN KEY `user_who_voided_name`; 
ALTER TABLE `openmrs`.`person_name` ADD CONSTRAINT `user_who_made_name`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_voided_name`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`program` DROP FOREIGN KEY `program_concept`, DROP FOREIGN KEY `program_creator`, DROP FOREIGN KEY `program_outcomes_concept_id_fk`, DROP FOREIGN KEY `user_who_changed_program`; 
ALTER TABLE `openmrs`.`program` ADD CONSTRAINT `program_concept`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `program_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `program_outcomes_concept_id_fk`   FOREIGN KEY (`outcomes_concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_changed_program`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`program_workflow` DROP FOREIGN KEY `program_for_workflow`, DROP FOREIGN KEY `workflow_changed_by`, DROP FOREIGN KEY `workflow_concept`, DROP FOREIGN KEY `workflow_creator`; 
ALTER TABLE `openmrs`.`program_workflow` ADD CONSTRAINT `program_for_workflow`   FOREIGN KEY (`program_id`)   REFERENCES `openmrs`.`program` (`program_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `workflow_changed_by`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `workflow_concept`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `workflow_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`program_workflow_state` DROP FOREIGN KEY `state_changed_by`, DROP FOREIGN KEY `state_concept`, DROP FOREIGN KEY `state_creator`, DROP FOREIGN KEY `workflow_for_state`; 
ALTER TABLE `openmrs`.`program_workflow_state` ADD CONSTRAINT `state_changed_by`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `state_concept`   FOREIGN KEY (`concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `state_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `workflow_for_state`   FOREIGN KEY (`program_workflow_id`)   REFERENCES `openmrs`.`program_workflow` (`program_workflow_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`provider` DROP FOREIGN KEY `provider_changed_by_fk`, DROP FOREIGN KEY `provider_creator_fk`, DROP FOREIGN KEY `provider_person_id_fk`, DROP FOREIGN KEY `provider_retired_by_fk`; 
ALTER TABLE `openmrs`.`provider` ADD CONSTRAINT `provider_changed_by_fk`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `provider_creator_fk`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `provider_person_id_fk`   FOREIGN KEY (`person_id`)   REFERENCES `openmrs`.`person` (`person_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `provider_retired_by_fk`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`provider_attribute` DROP FOREIGN KEY `provider_attribute_attribute_type_id_fk`, DROP FOREIGN KEY `provider_attribute_changed_by_fk`, DROP FOREIGN KEY `provider_attribute_creator_fk`, DROP FOREIGN KEY `provider_attribute_provider_fk`, DROP FOREIGN KEY `provider_attribute_voided_by_fk`; 
ALTER TABLE `openmrs`.`provider_attribute` ADD CONSTRAINT `provider_attribute_attribute_type_id_fk`   FOREIGN KEY (`attribute_type_id`)   REFERENCES `openmrs`.`provider_attribute_type` (`provider_attribute_type_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `provider_attribute_changed_by_fk`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `provider_attribute_creator_fk`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `provider_attribute_provider_fk`   FOREIGN KEY (`provider_id`)   REFERENCES `openmrs`.`provider` (`provider_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `provider_attribute_voided_by_fk`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`provider_attribute_type` DROP FOREIGN KEY `provider_attribute_type_changed_by_fk`, DROP FOREIGN KEY `provider_attribute_type_creator_fk`, DROP FOREIGN KEY `provider_attribute_type_retired_by_fk`; 
ALTER TABLE `openmrs`.`provider_attribute_type` ADD CONSTRAINT `provider_attribute_type_changed_by_fk`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `provider_attribute_type_creator_fk`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `provider_attribute_type_retired_by_fk`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`relationship` DROP FOREIGN KEY `person_a_is_person`, DROP FOREIGN KEY `person_b_is_person`, DROP FOREIGN KEY `relation_creator`, DROP FOREIGN KEY `relation_voider`, DROP FOREIGN KEY `relationship_changed_by`, DROP FOREIGN KEY `relationship_type_id`; 
ALTER TABLE `openmrs`.`relationship` ADD CONSTRAINT `person_a_is_person`   FOREIGN KEY (`person_a`)   REFERENCES `openmrs`.`person` (`person_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `person_b_is_person`   FOREIGN KEY (`person_b`)   REFERENCES `openmrs`.`person` (`person_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `relation_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `relation_voider`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `relationship_changed_by`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `relationship_type_id`   FOREIGN KEY (`relationship`)   REFERENCES `openmrs`.`relationship_type` (`relationship_type_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`relationship_type` DROP FOREIGN KEY `user_who_created_rel`, DROP FOREIGN KEY `user_who_retired_relationship_type`; 
ALTER TABLE `openmrs`.`relationship_type` ADD CONSTRAINT `user_who_created_rel`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_relationship_type`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`reporting_report_design` DROP FOREIGN KEY `changed_by?for?reporting_report_design`,DROP FOREIGN KEY `creator?for?reporting_report_design`,DROP FOREIGN KEY `report_definition_id?for?reporting_report_design`,DROP FOREIGN KEY `retired_by?for?reporting_report_design`;
ALTER TABLE `openmrs`.`reporting_report_design` ADD CONSTRAINT `changed_by?for?reporting_report_design`  FOREIGN KEY (`changed_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE RESTRICT,ADD CONSTRAINT `creator?for?reporting_report_design`  FOREIGN KEY (`creator`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE RESTRICT,ADD CONSTRAINT `report_definition_id?for?reporting_report_design`  FOREIGN KEY (`report_definition_id`)  REFERENCES `openmrs`.`serialized_object` (`serialized_object_id`)  ON UPDATE RESTRICT,ADD CONSTRAINT `retired_by?for?reporting_report_design`  FOREIGN KEY (`retired_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`reporting_report_request` DROP FOREIGN KEY `requested_by?for?reporting_report_request`;
ALTER TABLE `openmrs`.`reporting_report_request` ADD CONSTRAINT `requested_by?for?reporting_report_request`  FOREIGN KEY (`requested_by`)  REFERENCES `openmrs`.`users` (`user_id`)  ON DELETE RESTRICT  ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`role_privilege` DROP FOREIGN KEY `privilege_definitons`, DROP FOREIGN KEY `role_privilege_to_role`; 
ALTER TABLE `openmrs`.`role_privilege` ADD CONSTRAINT `privilege_definitons`   FOREIGN KEY (`privilege`)   REFERENCES `openmrs`.`privilege` (`privilege`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `role_privilege_to_role`   FOREIGN KEY (`role`)   REFERENCES `openmrs`.`role` (`role`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`role_role` DROP FOREIGN KEY `inherited_role`, DROP FOREIGN KEY `parent_role`; 
ALTER TABLE `openmrs`.`role_role` ADD CONSTRAINT `inherited_role`   FOREIGN KEY (`child_role`)   REFERENCES `openmrs`.`role` (`role`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `parent_role`   FOREIGN KEY (`parent_role`)   REFERENCES `openmrs`.`role` (`role`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`scheduler_task_config` DROP FOREIGN KEY `scheduler_changer`, DROP FOREIGN KEY `scheduler_creator`; 
ALTER TABLE `openmrs`.`scheduler_task_config` ADD CONSTRAINT `scheduler_changer`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `scheduler_creator`   FOREIGN KEY (`created_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`scheduler_task_config_property` DROP FOREIGN KEY `task_config_for_property`; 
ALTER TABLE `openmrs`.`scheduler_task_config_property` ADD CONSTRAINT `task_config_for_property`   FOREIGN KEY (`task_config_id`)   REFERENCES `openmrs`.`scheduler_task_config` (`task_config_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`scheduler_task_config_property` DROP FOREIGN KEY `task_config_for_property`; 
ALTER TABLE `openmrs`.`scheduler_task_config_property` ADD CONSTRAINT `task_config_for_property`   FOREIGN KEY (`task_config_id`)   REFERENCES `openmrs`.`scheduler_task_config` (`task_config_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`serialized_object` DROP FOREIGN KEY `serialized_object_changed_by`, DROP FOREIGN KEY `serialized_object_creator`, DROP FOREIGN KEY `serialized_object_retired_by`; 
ALTER TABLE `openmrs`.`serialized_object` ADD CONSTRAINT `serialized_object_changed_by`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `serialized_object_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `serialized_object_retired_by`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`test_order` DROP FOREIGN KEY `test_order_order_id_fk`, DROP FOREIGN KEY `test_order_specimen_source_fk`; 
ALTER TABLE `openmrs`.`test_order` ADD CONSTRAINT `test_order_order_id_fk`   FOREIGN KEY (`order_id`)   REFERENCES `openmrs`.`orders` (`order_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `test_order_specimen_source_fk`   FOREIGN KEY (`specimen_source`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`user_property` DROP FOREIGN KEY `user_property_to_users`; 
ALTER TABLE `openmrs`.`user_property` ADD CONSTRAINT `user_property_to_users`   FOREIGN KEY (`user_id`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`user_role` DROP FOREIGN KEY `role_definitions`, DROP FOREIGN KEY `user_role_to_users`; 
ALTER TABLE `openmrs`.`user_role` ADD CONSTRAINT `role_definitions`   FOREIGN KEY (`role`)   REFERENCES `openmrs`.`role` (`role`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_role_to_users`   FOREIGN KEY (`user_id`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`users` DROP FOREIGN KEY `person_id_for_user`, DROP FOREIGN KEY `user_creator`, DROP FOREIGN KEY `user_who_changed_user`, DROP FOREIGN KEY `user_who_retired_this_user`; 
ALTER TABLE `openmrs`.`users` ADD CONSTRAINT `person_id_for_user`   FOREIGN KEY (`person_id`)   REFERENCES `openmrs`.`person` (`person_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `user_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_changed_user`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `user_who_retired_this_user`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`visit` DROP FOREIGN KEY `visit_changed_by_fk`, DROP FOREIGN KEY `visit_creator_fk`, DROP FOREIGN KEY `visit_indication_concept_fk`, DROP FOREIGN KEY `visit_location_fk`, DROP FOREIGN KEY `visit_patient_fk`, DROP FOREIGN KEY `visit_type_fk`, DROP FOREIGN KEY `visit_voided_by_fk`; 
ALTER TABLE `openmrs`.`visit` ADD CONSTRAINT `visit_changed_by_fk`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_creator_fk`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_indication_concept_fk`   FOREIGN KEY (`indication_concept_id`)   REFERENCES `openmrs`.`concept` (`concept_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_location_fk`   FOREIGN KEY (`location_id`)   REFERENCES `openmrs`.`location` (`location_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_patient_fk`   FOREIGN KEY (`patient_id`)   REFERENCES `openmrs`.`patient` (`patient_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_type_fk`   FOREIGN KEY (`visit_type_id`)   REFERENCES `openmrs`.`visit_type` (`visit_type_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_voided_by_fk`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`visit_attribute` DROP FOREIGN KEY `visit_attribute_attribute_type_id_fk`, DROP FOREIGN KEY `visit_attribute_changed_by_fk`, DROP FOREIGN KEY `visit_attribute_creator_fk`, DROP FOREIGN KEY `visit_attribute_visit_fk`, DROP FOREIGN KEY `visit_attribute_voided_by_fk`; 
ALTER TABLE `openmrs`.`visit_attribute` ADD CONSTRAINT `visit_attribute_attribute_type_id_fk`   FOREIGN KEY (`attribute_type_id`)   REFERENCES `openmrs`.`visit_attribute_type` (`visit_attribute_type_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_attribute_changed_by_fk`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_attribute_creator_fk`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_attribute_visit_fk`   FOREIGN KEY (`visit_id`)   REFERENCES `openmrs`.`visit` (`visit_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_attribute_voided_by_fk`   FOREIGN KEY (`voided_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`visit_attribute_type` DROP FOREIGN KEY `visit_attribute_type_changed_by_fk`, DROP FOREIGN KEY `visit_attribute_type_creator_fk`, DROP FOREIGN KEY `visit_attribute_type_retired_by_fk`; 
ALTER TABLE `openmrs`.`visit_attribute_type` ADD CONSTRAINT `visit_attribute_type_changed_by_fk`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_attribute_type_creator_fk`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_attribute_type_retired_by_fk`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;
ALTER TABLE `openmrs`.`visit_type` DROP FOREIGN KEY `visit_type_changed_by`, DROP FOREIGN KEY `visit_type_creator`, DROP FOREIGN KEY `visit_type_retired_by`; 
ALTER TABLE `openmrs`.`visit_type` ADD CONSTRAINT `visit_type_changed_by`   FOREIGN KEY (`changed_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON DELETE RESTRICT   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_type_creator`   FOREIGN KEY (`creator`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT, ADD CONSTRAINT `visit_type_retired_by`   FOREIGN KEY (`retired_by`)   REFERENCES `openmrs`.`users` (`user_id`)   ON UPDATE RESTRICT;

-- ---------------------------------RESTORE OLD OBS DATA-----------------------------------------
-- Fixes unusual data due to missing cascade
update obs_pre_2015 set creator = (creator + 10000) where creator not in (select user_id from users);
update obs_pre_2015 set voided_by = (voided_by + 10000) where voided_by not in (select user_id from users);
SET FOREIGN_KEY_CHECKS=0;
insert into obs select * from obs_pre_2015 where year(date_created) <= 2013;
insert into obs select * from obs_pre_2015 where year(date_created) = 2014 and month(date_created) = 1;
insert into obs select * from obs_pre_2015 where year(date_created) = 2014 and month(date_created) = 2;
insert into obs select * from obs_pre_2015 where year(date_created) = 2014 and month(date_created) = 3;
insert into obs select * from obs_pre_2015 where year(date_created) = 2014 and month(date_created) = 4;
insert into obs select * from obs_pre_2015 where year(date_created) = 2014 and month(date_created) = 5;
insert into obs select * from obs_pre_2015 where year(date_created) = 2014 and month(date_created) = 6;
insert into obs select * from obs_pre_2015 where year(date_created) = 2014 and month(date_created) = 7;
insert into obs select * from obs_pre_2015 where year(date_created) = 2014 and month(date_created) = 8;
insert into obs select * from obs_pre_2015 where year(date_created) = 2014 and month(date_created) = 9;
insert into obs select * from obs_pre_2015 where year(date_created) = 2014 and month(date_created) = 10;
insert into obs select * from obs_pre_2015 where year(date_created) = 2014 and month(date_created) = 11;
insert into obs select * from obs_pre_2015 where year(date_created) = 2014 and month(date_created) = 12;
SET FOREIGN_KEY_CHECKS=1;

drop table obs_pre_2015;