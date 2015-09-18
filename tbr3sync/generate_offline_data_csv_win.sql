-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `generate_offline_data_csv`(no_of_days INT)
BEGIN

DECLARE start_date datetime;
DECLARE end_date datetime;

SET end_date = NOW();
SET start_date = date_sub(NOW(), INTERVAL no_of_days day) ;

 SET @q1 = concat("SELECT * FROM openmrs.concept ", 
 "WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ",
 "INTO OUTFILE '", "C:\Temp\sz\concept.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n'"); 
 prepare s1 from @q1; 
 execute s1; 

 SET @q2 = concat("SELECT * FROM openmrs.cohort WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", "INTO OUTFILE '", "C:\Temp\sz\cohort.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' "); 
 prepare s2 from @q2; 
 execute s2; 

 SET @q3 =concat("SELECT * FROM openmrs.concept_description WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\concept_description.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' "); 
 prepare s3 from @q3; 
 execute s3; 

 SET @q4=concat("SELECT * FROM openmrs.concept_map_type WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\concept_map_type.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' "); 
 prepare s4 from @q4; 
 execute s4; 

 SET @q5=concat("SELECT * FROM openmrs.concept_proposal WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\concept_proposal.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' "); 
 prepare s5 from @q5; 
 execute s5; 

  SET @q6=concat("SELECT * FROM openmrs.concept_reference_map WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\concept_reference_map.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
  prepare s6 from @q6;
  execute s6;

 SET @q7=concat("SELECT * FROM openmrs.concept_reference_term WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\concept_reference_term.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s7 from @q7;
 execute s7;

 SET @q8=concat("SELECT * FROM openmrs.concept_reference_term_map WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\concept_reference_term_map.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s8 from @q8;
 execute s8;

 SET @q9=concat("SELECT * FROM openmrs.drug WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\drug.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s9 from @q9;
 execute s9;

 SET @q10=concat("SELECT * FROM openmrs.encounter WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\encounter.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s10 from @q10;
 execute s10;

 SET @q11=concat("SELECT * FROM openmrs.encounter_provider WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\encounter_provider.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s11 from @q11;
 execute s11;

 SET @q12=concat("SELECT * FROM openmrs.encounter_role WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\encounter_role.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s12 from @q12;
 execute s12;

 SET @q13=concat("SELECT * FROM openmrs.field WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\field.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s13 from @q13;
 execute s13;

 SET @q14=concat("SELECT * FROM openmrs.form WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\form.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s14 from @q14;
 execute s14;

 SET @q15=concat("SELECT * FROM openmrs.form_field WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\form_field.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s15 from @q15;
 execute s15;

 SET @q16=concat("SELECT * FROM openmrs.location_attribute WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\location_attribute.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s16 from @q16;
 execute s16;

 SET @q17=concat("SELECT * FROM openmrs.location_attribute_type WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\location_attribute_type.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s17 from @q17;
 execute s17;

 SET @q18=concat("SELECT * FROM openmrs.note WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\note.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s18 from @q18;
 execute s18;

 SET @q19=concat("SELECT * FROM openmrs.notification_alert WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\notification_alert.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s19 from @q19;
 execute s19;

 SET @q21=concat("SELECT * FROM openmrs.patient WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\patient.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s21 from @q21;
 execute s21;

 SET @q22=concat("SELECT * FROM openmrs.patient_identifier WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\patient_identifier.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s22 from @q22;
 execute s22;

 SET @q23=concat("SELECT * FROM openmrs.patient_program WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\patient_program.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s23 from @q23;
 execute s23;

 SET @q24=concat("SELECT * FROM openmrs.patient_state WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\patient_state.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s24 from @q24;
 execute s24;

 SET @q25=concat("SELECT * FROM openmrs.person WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\person.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s25 from @q25;
 execute s25;

 SET @q26=concat("SELECT * FROM openmrs.person_address WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\person_address.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s26 from @q26;
 execute s26;

 SET @q27=concat("SELECT * FROM openmrs.person_attribute WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\person_attribute.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s27 from @q27;
 execute s27;

 SET @q28=concat("SELECT * FROM openmrs.person_attribute_type WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\person_attribute_type.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s28 from @q28;
 execute s28;

 SET @q29=concat("SELECT * FROM openmrs.person_merge_log WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\person_merge_log.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s29 from @q29;
 execute s29;

 SET @q30=concat("SELECT * FROM openmrs.person_name WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\person_name.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s30 from @q30;
 execute s30;

 SET @q31=concat("SELECT * FROM openmrs.program WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\program.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s31 from @q31;
 execute s31;

 SET @q32=concat("SELECT * FROM openmrs.program_workflow WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\program_workflow.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s32 from @q32;
 execute s32;

 SET @q33=concat("SELECT * FROM openmrs.program_workflow_state WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\program_workflow_state.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s33 from @q33;
 execute s33;

 SET @q34=concat("SELECT * FROM openmrs.provider WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\provider.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s34 from @q34;
 execute s34;

 SET @q35=concat("SELECT * FROM openmrs.provider_attribute WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\provider_attribute.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s35 from @q35;
 execute s35;

 SET @q36=concat("SELECT * FROM openmrs.provider_attribute_type WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\provider_attribute_type.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s36 from @q36;
 execute s36;

 SET @q37=concat("SELECT * FROM openmrs.relationship WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\relationship.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s37 from @q37;
 execute s37;

 SET @q38=concat("SELECT * FROM openmrs.scheduler_task_config WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\scheduler_task_config.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s38 from @q38;
 execute s38;

 SET @q39=concat("SELECT * FROM openmrs.serialized_object WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\serialized_object.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s39 from @q39;
 execute s39;

 SET @q40=concat("SELECT * FROM openmrs.users WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\users.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s40 from @q40;
 execute s40;

 SET @q41=concat("SELECT * FROM openmrs.visit WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\visit.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' "); 
 prepare s41 from @q41;
 execute s41;

 SET @q42=concat("SELECT * FROM openmrs.visit_attribute WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\visit_attribute.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s42 from @q42;
 execute s42;

 SET @q43=concat("SELECT * FROM openmrs.visit_attribute_type WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\visit_attribute_type.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s43 from @q43;
 execute s43;

 SET @q44=concat("SELECT * FROM openmrs.visit_type WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "') OR (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\visit_type.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s44 from @q44;
 execute s44;

 SET @q20=concat("SELECT * FROM openmrs.notification_alert_recipient WHERE (date_changed BETWEEN '", start_date, "' AND '", end_date, "') ", " INTO OUTFILE '", "C:\Temp\sz\notification_alert_recipient.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s20 from @q20;
 execute s20;


 SET @q45=concat("SELECT * FROM openmrs.active_list_type WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\active_list_type.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s45 from @q45;
 execute s45;

 SET @q46=concat("SELECT * FROM openmrs.concept_answer WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\concept_answer.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s46 from @q46;
 execute s46;

 SET @q47=concat("SELECT * FROM openmrs.concept_class WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\concept_class.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s47 from @q47;
 execute s47;

 SET @q48=concat("SELECT * FROM openmrs.concept_datatype WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\concept_datatype.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s48 from @q48;
 execute s48;

 SET @q49=concat("SELECT * FROM openmrs.concept_name WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\concept_name.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s49 from @q49;
 execute s49;

 SET @q50=concat("SELECT * FROM openmrs.concept_name_bkp WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\concept_name_bkp.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s50 from @q50;
 execute s50;

 SET @q51=concat("SELECT * FROM openmrs.concept_name_tag WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\concept_name_tag.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s51 from @q51;
 execute s51;

 SET @q52=concat("SELECT * FROM openmrs.concept_reference_source WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\concept_reference_source.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s52 from @q52;
 execute s52;

 SET @q53=concat("SELECT * FROM openmrs.concept_set WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\concept_set.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s53 from @q53;
 execute s53;

 SET @q54=concat("SELECT * FROM openmrs.encounter_type WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\encounter_type.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s54 from @q54;
 execute s54;

 SET @q55=concat("SELECT * FROM openmrs.field_answer WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\field_answer.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s55 from @q55;
 execute s55;

 SET @q56=concat("SELECT * FROM openmrs.field_type WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\field_type.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s56 from @q56;
 execute s56;

 SET @q57=concat("SELECT * FROM openmrs.location WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\location.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s57 from @q57;
 execute s57;

 SET @q58=concat("SELECT * FROM openmrs.location_tag WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\location_tag.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s58 from @q58;
 execute s58;

 SET @q59=concat("SELECT * FROM openmrs.obs WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\obs.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s59 from @q59;
 execute s59;

 SET @q60=concat("SELECT * FROM openmrs.order_type WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\order_type.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s60 from @q60;
 execute s60;

 SET @q61=concat("SELECT * FROM openmrs.orders WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\orders.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s61 from @q61;
 execute s61;

 SET @q62=concat("SELECT * FROM openmrs.patient_identifier_type WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\patient_identifier_type.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s62 from @q62;
 execute s62;

 SET @q63=concat("SELECT * FROM openmrs.relationship_type WHERE (date_created BETWEEN '", start_date, "' AND '", end_date, "')", " INTO OUTFILE '", "C:\Temp\sz\relationship_type.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s63 from @q63;
 execute s63;

 SET @q64=concat("SELECT * FROM openmrs.cohort_member  INTO OUTFILE '", "C:\Temp\sz\cohort_member.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s64 from @q64;
 execute s64;

 SET @q65=concat("SELECT * FROM openmrs.concept_complex  INTO OUTFILE '", "C:\Temp\sz\concept_complex.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s65 from @q65;
 execute s65;

 SET @q66=concat("SELECT * FROM openmrs.concept_name_tag_map  INTO OUTFILE '", "C:\Temp\sz\concept_name_tag_map.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s66 from @q66;
 execute s66;

 SET @q67=concat("SELECT * FROM openmrs.concept_numeric  INTO OUTFILE '", "C:\Temp\sz\concept_numeric.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s67 from @q67;
 execute s67;

 SET @q68=concat("SELECT * FROM openmrs.concept_proposal_tag_map  INTO OUTFILE '", "C:\Temp\sz\concept_proposal_tag_map.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s68 from @q68;
 execute s68;

 SET @q69=concat("SELECT * FROM openmrs.concept_set_derived  INTO OUTFILE '", "C:\Temp\sz\concept_set_derived.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s69 from @q69;
 execute s69;

 SET @q70=concat("SELECT * FROM openmrs.concept_state_conversion  INTO OUTFILE '", "C:\Temp\sz\concept_state_conversion.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s70 from @q70;
 execute s70;

 SET @q71=concat("SELECT * FROM openmrs.concept_stop_word  INTO OUTFILE '", "C:\Temp\sz\concept_stop_word.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s71 from @q71;
 execute s71;

 SET @q72=concat("SELECT * FROM openmrs.concept_word  INTO OUTFILE '", "C:\Temp\sz\concept_word.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s72 from @q72;
 execute s72;

 SET @q73=concat("SELECT * FROM openmrs.drug_ingredient  INTO OUTFILE '", "C:\Temp\sz\drug_ingredient.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s73 from @q73;
 execute s73;

 SET @q74=concat("SELECT * FROM openmrs.drug_order  INTO OUTFILE '", "C:\Temp\sz\drug_order.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s74 from @q74;
 execute s74;

 SET @q75=concat("SELECT * FROM openmrs.form_resource  INTO OUTFILE '", "C:\Temp\sz\form_resource.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s75 from @q75;
 execute s75;

 SET @q76=concat("SELECT * FROM openmrs.global_property  INTO OUTFILE '", "C:\Temp\sz\global_property.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s76 from @q76;
 execute s76;

 SET @q77=concat("SELECT * FROM openmrs.location_tag_map  INTO OUTFILE '", "C:\Temp\sz\location_tag_map.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s77 from @q77;
 execute s77;

 SET @q78=concat("SELECT * FROM openmrs.notification_template  INTO OUTFILE '", "C:\Temp\sz\notification_template.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s78 from @q78;
 execute s78;

 SET @q79=concat("SELECT * FROM openmrs.privilege  INTO OUTFILE '", "C:\Temp\sz\privilege.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s79 from @q79;
 execute s79;

 SET @q80=concat("SELECT * FROM openmrs.role  INTO OUTFILE '", "C:\Temp\sz\role.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s80 from @q80;
 execute s80;

 SET @q81=concat("SELECT * FROM openmrs.role_privilege  INTO OUTFILE '", "C:\Temp\sz\role_privilege.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s81 from @q81;
 execute s81;

 SET @q82=concat("SELECT * FROM openmrs.role_role  INTO OUTFILE '", "C:\Temp\sz\role_role.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s82 from @q82;
 execute s82;

 SET @q83=concat("SELECT * FROM openmrs.scheduler_task_config_property  INTO OUTFILE '", "C:\Temp\sz\scheduler_task_config_property.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s83 from @q83;
 execute s83;

 SET @q84=concat("SELECT * FROM openmrs.test_order  INTO OUTFILE '", "C:\Temp\sz\test_order.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s84 from @q84;
 execute s84;

 SET @q85=concat("SELECT * FROM openmrs.user_property  INTO OUTFILE '", "C:\Temp\sz\user_property.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s85 from @q85;
 execute s85;

 SET @q86=concat("SELECT * FROM openmrs.user_role  INTO OUTFILE '", "C:\Temp\sz\user_role.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' ");
 prepare s86 from @q86;
 execute s86;

END
