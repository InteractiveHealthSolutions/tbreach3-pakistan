#!/bin/bash
# Script for synchronizing separate openmrs instances to central openmrs
sudo rm -R /tmp/sz
sudo mkdir /tmp/sz
mysql -u root -p openmrs -e "call openmrs.generate_offline_data_csv(4)"
mysql -h 202.141.249.106 -P 8933 --local-infile -u root -p openmrs < /home/openmrs/Documents/TBR3\ Stored\ Procedures/load_data_into_temp_tables.sql
mysql -h 202.141.249.106 -P 8933 -u root -p openmrs -e "call openmrs.sync_openmrs(4)"