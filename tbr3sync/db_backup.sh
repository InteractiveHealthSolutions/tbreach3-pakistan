#!/bin/bash
# Script for backing up MySQL databases automatically
user="root"
password="jingle94"
db_names="openmrs xpertsms smstarseel"
backup_path="/media/szc-kng/Elements/database_dumps"
date=`date +%Y-%m-%d`
echo "Backing up databases" $db_names "to" $backup_path"/"$date
# Execute backup script
mysqldump -u $user -p$password --databases $db_names > $backup_path/$date.sql
# Compress backup
cd $backup_path
gzip $date.sql
