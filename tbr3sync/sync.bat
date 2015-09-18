@echo off
RD /S /Q "C:\Temp\sz"
mkdir "C:\Temp\sz"
cd "C:\Program Files\MySQL\MySQL Server 5.6\bin"
mysql -u root -p openmrs -e "call generate_offline_data_csv(4);"
mysql -h 202.141.249.106 -P 8933 --local-infile -u root -p openmrs < "C:\"
mysql -h 202.141.249.106 -P 8933 --local-infile -u root -p openmrs -e "call sync_openmrs();"

