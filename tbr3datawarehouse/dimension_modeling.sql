-- -- ## TRANSFORMATION SCRIPT FOR SEHATMAND ZINDAGI DATA WAREHOUSE ## -- --

-- Create a table to record systems
drop table if exists dim_systems;
create table dim_systems 
select 1 as system_id, 'OpenMRS' as system_name union
select 2 as system_id, 'Field Monitoring' as system_name union
select 3 as system_id, 'ILMS' as system_name;

-- Denormalize concept tables and create a dimension
drop table if exists dim_concept;
CREATE TABLE dim_concept (
 surrogate_id bigint(21) auto_increment,
 system_id int(1) NOT NULL DEFAULT '0',
 concept_id int(11) NOT NULL,
 full_name varchar(255) DEFAULT '',
 concept varchar(255) DEFAULT '',
 description text,
 retired tinyint(1) NOT NULL DEFAULT '0',
 data_type varchar(255) NOT NULL DEFAULT '',
 class varchar(255) NOT NULL DEFAULT '',
 creator int(11) NOT NULL DEFAULT '0',
 date_created datetime NOT NULL,
 version int(11) DEFAULT NULL,
 changed_by int(11),
 date_changed datetime,
 uuid char(38) DEFAULT NULL,
 PRIMARY KEY surrogate_id (surrogate_id),
 UNIQUE KEY concept_id (concept_id),
 UNIQUE KEY `uuid_UNIQUE` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into dim_concept 
select 0, 1, c.concept_id, n1.name as full_name, n2.name as concept, d.description, c.retired, dt.name as data_type, cl.name as class, c.creator, c.date_created, c.version, c.changed_by, c.date_changed, c.uuid from om_concept as c
inner join om_concept_datatype as dt on dt.concept_datatype_id = c.datatype_id
inner join om_concept_class as cl on cl.concept_class_id = c.class_id
left outer join om_concept_name as n1 on n1.concept_id = c.concept_id and n1.locale = 'en' and n1.voided = 0 and n1.concept_name_type = 'FULLY_SPECIFIED'
left outer join om_concept_name as n2 on n2.concept_id = c.concept_id and n2.locale = 'en' and n2.voided = 0 and n2.concept_name_type <> 'FULLY_SPECIFIED'
left outer join om_concept_description as d on d.concept_id = c.concept_id;
-- Optionally change True/False to Yes/No
update dim_concept set full_name = 'Yes', concept = 'Yes' where concept_id = 1;
update dim_concept set full_name = 'No', concept = 'No' where concept_id = 2;

-- Denormalize location tables and create a dimension
drop table if exists dim_location;
CREATE TABLE dim_location (
  surrogate_id bigint(21) AUTO_INCREMENT,
  system_id int(1) NOT NULL DEFAULT '0',
  location_id int(11) NOT NULL,
  location_name varchar(255) NOT NULL DEFAULT '',
  description varchar(255) DEFAULT NULL,
  address1 varchar(255) DEFAULT NULL,
  address2 varchar(255) DEFAULT NULL,
  city_village varchar(255) DEFAULT NULL,
  state_province varchar(255) DEFAULT NULL,
  postal_code varchar(50) DEFAULT NULL,
  country varchar(50) DEFAULT NULL,
  latitude varchar(50) DEFAULT NULL,
  longitude varchar(50) DEFAULT NULL,
  creator int(11) NOT NULL DEFAULT '0',
  date_created datetime NOT NULL,
  retired tinyint(1) NOT NULL DEFAULT '0',
  parent_location int(11) DEFAULT NULL,
  uuid char(38) DEFAULT NULL,
  PRIMARY KEY surrogate_id (surrogate_id),
  UNIQUE KEY location_id (location_id),
  UNIQUE KEY uuid_UNIQUE (uuid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into dim_location 
select 0, 1, l.location_id, l.name as location_name, l.description, l.address1, l.address2, l.city_village, l.state_province, l.postal_code, l.country, l.latitude, l.longitude, l.creator, l.date_created, l.retired, l.parent_location, l.uuid from om_location as l;


-- Denormalize patient tables and create dimension
-- Set preferred to 1 for people with single entries
update om_patient_identifier 
set preferred = 1 where patient_id not in 
	(select t.patient_id from (select patient_id, count(*) as total from om_patient_identifier group by patient_id having total > 1) as t);
-- Pick only the latest records to avoid cases where there are multiple 
drop table if exists om_patient_latest_identifier;
create table om_patient_latest_identifier 
select * from om_patient_identifier as a 
having a.patient_id = (select max(patient_id) from om_patient_identifier where patient_id = a.patient_id and preferred = 1 and voided = 0);
alter table om_patient_latest_identifier add primary key patient_id (patient_id);

-- Set preferred to 1 for people with single entries
update om_person_name 
set preferred = 1 where person_id not in 
	(select t.person_id from (select person_id, count(*) as total from om_person_name group by person_id having total > 1) as t);
-- Pick only the latest records to avoid cases where there are multiple preferred
drop table if exists om_person_latest_name;
create table om_person_latest_name
select * from om_person_name as a 
having a.person_name_id = (select max(person_name_id) from om_person_name where person_id = a.person_id and preferred = 1);
alter table om_person_latest_name add primary key person_name_id (person_name_id);
alter table om_person_latest_name add unique key person_id (person_id);

-- Set preferred to 1 for people with single entries
update om_person_address 
set preferred = 1 where person_id not in 
	(select t.person_id from (select person_id, count(*) as total from om_person_address group by person_id having total > 1) as t);
-- Pick only the latest records to avoid cases where there are multiple preferred
drop table if exists om_person_latest_address;
create table om_person_latest_address
select * from om_person_address as a 
having a.person_address_id = (select max(person_address_id) from om_person_address where person_id = a.person_id and preferred = 1);
alter table om_person_latest_address add primary key person_address_id (person_address_id);
alter table om_person_latest_address add unique key person_id (person_id);

-- Transpose attributes
drop table if exists om_person_attribute_merged;
create table om_person_attribute_merged
select a.person_id, 
group_concat(if(a.person_attribute_type_id = 1, a.value, NULL)) as race, 
group_concat(if(a.person_attribute_type_id = 2, a.value, NULL)) as birthplace, 
group_concat(if(a.person_attribute_type_id = 3, a.value, NULL)) as citizenship, 
group_concat(if(a.person_attribute_type_id = 4, a.value, NULL)) as mother_name, 
group_concat(if(a.person_attribute_type_id = 5, a.value, NULL)) as civil_status, 
group_concat(if(a.person_attribute_type_id = 6, a.value, NULL)) as health_district, 
group_concat(if(a.person_attribute_type_id = 7, a.value, NULL)) as health_center, 
group_concat(if(a.person_attribute_type_id = 8, a.value, NULL)) as primary_mobile, 
group_concat(if(a.person_attribute_type_id = 9, a.value, NULL)) as secondary_mobile, 
group_concat(if(a.person_attribute_type_id = 10, a.value, NULL)) as primary_phone, 
group_concat(if(a.person_attribute_type_id = 11, a.value, NULL)) as secondary_phone, 
group_concat(if(a.person_attribute_type_id = 12, a.value, NULL)) as primary_mobile_owner, 
group_concat(if(a.person_attribute_type_id = 13, a.value, NULL)) as secondary_mobile_owner, 
group_concat(if(a.person_attribute_type_id = 14, a.value, NULL)) as primary_phone_owner, 
group_concat(if(a.person_attribute_type_id = 15, a.value, NULL)) as secondary_phone_owner from om_person_attribute as a where a.voided = 0 group by a.person_id;
alter table om_person_attribute_merged add primary key person_id (person_id);

drop table if exists dim_patient;
CREATE TABLE dim_patient (
 surrogate_id bigint(20) NOT NULL AUTO_INCREMENT,
 system_id int(11) NOT NULL,
 patient_id int(11) NOT NULL,
 identifier varchar(50) NOT NULL DEFAULT '',
 gender varchar(50) DEFAULT '',
 birthdate date DEFAULT NULL,
 birthdate_estimated tinyint(1) NOT NULL DEFAULT '0',
 dead tinyint(1) NOT NULL DEFAULT '0',
 first_name varchar(50) DEFAULT NULL,
 middle_name varchar(50) DEFAULT NULL,
 last_name varchar(50) DEFAULT NULL,
 race text,
 birthplace text,
 citizenship text,
 mother_name text,
 civil_status text,
 health_district text,
 health_center text,
 primary_mobile text,
 secondary_mobile text,
 primary_phone text,
 secondary_phone text,
 primary_mobile_owner text,
 secondary_mobile_owner text,
 primary_phone_owner text,
 secondary_phone_owner text,
 creator int(11) NOT NULL DEFAULT '0',
 date_created datetime NOT NULL,
 changed_by int(11) DEFAULT NULL,
 date_changed datetime DEFAULT NULL,
 voided tinyint(1) NOT NULL DEFAULT '0',
 uuid char(38) DEFAULT NULL,
  PRIMARY KEY (surrogate_id),
  UNIQUE KEYpatient_id_UNIQUE (patient_id),
  UNIQUE KEYuuid_UNIQUE (uuid),
  KEY identifier (identifier)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into dim_patient 
select 0, 1, p.patient_id, i.identifier, pr.gender, pr.birthdate, pr.birthdate_estimated, pr.dead, n.given_name as first_name, n.middle_name, n.family_name as last_name, 
pa.race, pa.birthplace, pa.citizenship, pa.mother_name, pa.civil_status, pa.health_district, pa.health_center, pa.primary_mobile, pa.secondary_mobile, pa.primary_phone, pa.secondary_phone, pa.primary_mobile_owner, pa.secondary_mobile_owner, pa.primary_phone_owner, pa.secondary_phone_owner, 
p.creator, p.date_created, p.changed_by, p.date_changed, p.voided, pr.uuid from om_patient as p
inner join om_patient_identifier as i on i.patient_id = p.patient_id and i.identifier_type = 1
inner join om_person as pr on pr.person_id = p.patient_id
left outer join om_person_latest_name as n on n.person_id = pr.person_id and n.preferred = 1
left outer join om_person_latest_address as a on a.person_id = p.patient_id
left outer join om_person_attribute_merged as pa on pa.person_id = p.patient_id
where p.voided = 0;

select * from om_users;

drop table if exists dim_user;
CREATE TABLE dim_user (
  surrogate_id int(1) NOT NULL AUTO_INCREMENT,
  system_id int(1) NOT NULL,
  user_id int(11) NOT NULL,
  username varchar(50) NOT NULL,
  person_id int(11) NOT NULL,
  identifier varchar(255) DEFAULT NULL,
  secret_question varchar(255) DEFAULT NULL,
  secret_answer varchar(255) DEFAULT NULL,
  creator int(11) NOT NULL DEFAULT '0',
  date_created datetime NOT NULL,
  changed_by int(11) DEFAULT NULL,
  date_changed datetime DEFAULT NULL,
  retired tinyint(1) NOT NULL DEFAULT '0',
  retire_reason varchar(255) DEFAULT NULL,
  uuid char(38) NOT NULL,
  PRIMARY KEY (surrogate_id),
  UNIQUE KEY user_id_UNIQUE (user_id),
  UNIQUE KEY username_UNIQUE (username),
  UNIQUE KEY person_id_UNIQUE (person_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert ignore into dim_user 
select 0, 1, u.user_id, u.username, u.person_id, p.identifier, u.secret_question, u.secret_answer, u.creator, u.date_created, u.changed_by, u.date_changed, u.retired, u.retire_reason, u.uuid from om_users as u
left outer join om_provider as p on p.person_id = u.person_id and p.retired = 0;

-- Denormalize encounter tables and create dimension
drop table if exists dim_encounter;
CREATE TABLE dim_encounter (
  surrogate_id bigint(21) NOT NULL AUTO_INCREMENT,
  system_id int(1) NOT NULL DEFAULT '0',
  encounter_id int(11) NOT NULL,
  encounter_type int(11) NOT NULL,
  encounter_name varchar(50) NOT NULL DEFAULT '',
  description text,
  patient_id int(11) NOT NULL DEFAULT '0',
  location_id int(11) DEFAULT NULL,
  provider varchar(255) DEFAULT NULL,
  date_entered datetime NOT NULL,
  changed_by int(11) DEFAULT NULL,
  date_changed datetime DEFAULT NULL,
  creator int(11) DEFAULT NULL,
  date_start datetime,
  date_end datetime,
  uuid char(38) NOT NULL,
  PRIMARY KEY (surrogate_id),
  UNIQUE KEY encounter_id (encounter_id),
  UNIQUE KEY uuid_UNIQUE (uuid),
  KEY patient_id (patient_id),
  KEY location_id (location_id),
  KEY provider (provider)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert ignore into dim_encounter 
select 0, 1, e.encounter_id, e.encounter_type, et.name as encounter_name, et.description, e.patient_id, e.location_id, p.identifier as provider, e.encounter_datetime as date_entered, e.creator, e.date_created as date_start, e.changed_by, e.date_changed, e.date_created as date_end, e.uuid from om_encounter as e 
inner join om_encounter_type as et on et.encounter_type_id = e.encounter_type 
left outer join om_encounter_provider as ep on ep.encounter_id = e.encounter_id
left outer join om_provider as p on p.person_id = ep.provider_id;

-- Denormalize observations and create dimension
drop table if exists dim_obs;
CREATE TABLE dim_obs (
  surrogate_id int(11) NOT NULL AUTO_INCREMENT,
  system_id int(11) NOT NULL DEFAULT '0',
  encounter_id int(11) DEFAULT NULL,
  encounter_type int(11) DEFAULT NULL,
  patient_id int(11) DEFAULT NULL,
  identifier varchar(50) DEFAULT NULL,
  provider varchar(50) DEFAULT NULL,
  obs_id int(11) NOT NULL,
  concept_id int(11) NOT NULL DEFAULT '0',
  question varchar(255) DEFAULT '',
  obs_datetime datetime NOT NULL,
  location_id int(11) DEFAULT NULL,
  answer longtext,
  value_boolean tinyint(1) DEFAULT NULL,
  value_coded int(11) DEFAULT NULL,
  value_datetime datetime DEFAULT NULL,
  value_numeric double DEFAULT NULL,
  value_text text,
  creator int(11) NOT NULL DEFAULT '0',
  date_created datetime NOT NULL,
  voided tinyint(1) NOT NULL DEFAULT '0',
  uuid char(38) DEFAULT NULL,
  PRIMARY KEY (surrogate_id),
  UNIQUE KEY obs_id_UNIQUE (obs_id),
  KEY identifier (identifier),
  KEY encounter_type (encounter_type),
  KEY concept_id (concept_id),
  KEY question (question),
  KEY location_id (location_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into dim_obs 
select 0, 1, e.encounter_id, e.encounter_type, e.patient_id, p.identifier, e.provider, o.obs_id, o.concept_id, c.concept as question, obs_datetime, o.location_id, 
concat(ifnull(o.value_boolean, ''), ifnull(ifnull(c2.concept, c2.full_name), ''), ifnull(o.value_datetime, ''), ifnull(o.value_numeric, ''), ifnull(o.value_text, '')) as answer, 
o.value_boolean, o.value_coded, o.value_datetime, o.value_numeric, o.value_text, o.creator, o.date_created, o.voided, o.uuid from om_obs as o 
inner join dim_concept as c on c.concept_id = o.concept_id 
inner join dim_encounter as e on e.encounter_id = o.encounter_id
inner join dim_patient as p on p.patient_id = e.patient_id 
left outer join dim_concept as c2 on c2.concept_id = o.value_coded;

select e.surrogate_id, e.system_id, e.encounter_id, e.patient_id, e.provider, e.location_id, l.location_name, e.patient_id, e.date_entered, 
from dim_encounter as e
inner join dim_obs as o on o.encounter_id = e.encounter_id
inner join dim_location as l on l.location_id = e.location_id
where e.provider is not null and e.encounter_type = 1
group by e.surrogate_id, e.system_id, e.encounter_id, e.patient_id, e.provider, e.location_id, l.location_name, e.patient_id, e.date_entered;


