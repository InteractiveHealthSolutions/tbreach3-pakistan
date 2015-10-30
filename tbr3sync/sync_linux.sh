#!/bin/bash
# Script for synchronizing separate openmrs instances to central openmrs
rm -R /tmp/sz/*
service tomcat6 stop
mysql -u root -p openmrs -e "call openmrs.generate_offline_data_csv(4)"
mysql -h 202.141.249.106 -P 8940 --local-infile -u root -p openmrs < /home/openmrs/Documents/TBR3\ Stored\ Procedures/load_data_into_temp_tables.sql
mysql -h 202.141.249.106 -P 8940 -u root -p openmrs -e "call openmrs.sync_openmrs(4)"
service tomcat6 start