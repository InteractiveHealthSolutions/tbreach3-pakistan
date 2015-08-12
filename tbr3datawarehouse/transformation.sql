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
	uuid char(38) DEFAULT NULL,
	PRIMARY KEY surrogate_id (surrogate_id),
	UNIQUE KEY concept_id (concept_id),
	UNIQUE KEY `uuid_UNIQUE` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into dim_concept 
select 0, 1, c.concept_id, n1.name as full_name, n2.name as concept, d.description, c.retired, dt.name as data_type, cl.name as class, c.uuid from om_concept as c
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
  encounter_creator int(11) NOT NULL DEFAULT '0',
  date_start datetime NOT NULL,
  date_end datetime NOT NULL,
  uuid char(38) NOT NULL,
  PRIMARY KEY (surrogate_id),
  UNIQUE KEY encounter_id (encounter_id),
  UNIQUE KEY uuid_UNIQUE (uuid),
  KEY patient_id (patient_id),
  KEY location_id (location_id),
  KEY provider (provider)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into dim_encounter 
select 0, 1, e.encounter_id, e.encounter_type, et.name as encounter_name, et.description, e.patient_id, e.location_id, p.identifier as provider, e.encounter_datetime as date_entered, e.creator as encounter_creator, e.date_created as date_start, e.date_created as date_end, e.uuid from om_encounter as e
inner join om_encounter_type as et on et.encounter_type_id = e.encounter_type 
inner join om_encounter_provider as ep on ep.encounter_id = e.encounter_id
inner join om_provider as p on p.person_id = ep.provider_id;

-- Denormalize observations and create dimension
drop table if exists dim_obs;
create table dim_obs (surrogate_id int(11) not null, system_id int(11) not null default '0', obs_id int(11) not null, concept_id int(11) not null default '0', question varchar(255) default '', obs_datetime datetime not null, location_id int(11) default null, location_name varchar(255) not null default '', answer longtext, value_boolean tinyint(1) default null, value_coded int(11) default null, value_datetime datetime default null, value_numeric double default null, value_text text, creator int(11) not null default '0', date_created datetime not null, voided tinyint(1) not null default '0', uuid char(38) default null, 
primary key (surrogate_id), unique key obs_id_UNIQUE (obs_id), key concept_id (concept_id), key question (question), key location_id (location_id));
set @Row := 0;
insert into dim_obs 
select @Row := @Row + 1 as surrogate_id, 1 as system_id, o.obs_id, o.concept_id, c.concept as question, obs_datetime, o.location_id, l.location_name, 
concat(ifnull(o.value_boolean, ''), ifnull(ifnull(c2.concept, c2.full_name), ''), ifnull(o.value_datetime, ''), ifnull(o.value_numeric, ''), ifnull(o.value_text, '')) as answer, 
o.value_boolean, o.value_coded, o.value_datetime, o.value_numeric, o.value_text, o.creator, o.date_created, o.voided, o.uuid from om_obs as o 
inner join dim_location as l on l.location_id = o.location_id
inner join dim_concept as c on c.concept_id = o.concept_id 
left outer join dim_concept as c2 on c2.concept_id = o.value_coded;

select * from dim_obs;

-- Denormalize Screening form (as sample)
drop table if exists om_enc_screening;
set @Row := 0;
create table om_enc_screening;
select @Row := @Row + 1 as surrogate_id, 1 as system_id, o.concept_id, o.value_boolean, o.value_coded, o.value_datetime, o.value_numeric, o.value_text, o.value_complex from om_obs as o
inner join om_encounter as e on e.encounter_id = o.encounter_id
inner join om_encounter_type as et on et.encounter_type_id = e.encounter_type;

select e.surrogate_id, e.encounter_name, e.description, e.patient_id, e.location_id, from dim_encounter as e
where e.encounter_type = 4;

