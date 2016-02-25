DROP TABLE IF EXISTS lms_tmp_account;
DROP TABLE IF EXISTS lms_tmp_branch;
DROP TABLE IF EXISTS lms_tmp_department;
DROP TABLE IF EXISTS lms_tmp_designation;
DROP TABLE IF EXISTS lms_tmp_doctor;
DROP TABLE IF EXISTS lms_tmp_doctor_beneficiary;
DROP TABLE IF EXISTS lms_tmp_employee;
DROP TABLE IF EXISTS lms_tmp_expense_type;
DROP TABLE IF EXISTS lms_tmp_expense_voucher;
DROP TABLE IF EXISTS lms_tmp_lab;
DROP TABLE IF EXISTS lms_tmp_location_beneficiary_amount;
DROP TABLE IF EXISTS lms_tmp_other_lab;
DROP TABLE IF EXISTS lms_tmp_patient;
DROP TABLE IF EXISTS lms_tmp_patient_dues;
DROP TABLE IF EXISTS lms_tmp_system;
DROP TABLE IF EXISTS lms_tmp_test;
DROP TABLE IF EXISTS lms_tmp_transaction;
CREATE TABLE lms_tmp_account (  account_id int(11) NOT NULL,  account_name varchar(256) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (account_id));
CREATE TABLE lms_tmp_branch (  location varchar(200) NOT NULL,  address varchar(256) DEFAULT NULL,  phone varchar(45) DEFAULT NULL,  code varchar(5) DEFAULT NULL,  is_registered char(1) DEFAULT NULL,  branch_type varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (location));
CREATE TABLE lms_tmp_department (  department_id int(11) NOT NULL,  department_name varchar(45) DEFAULT NULL,  account_id int(11) DEFAULT NULL,  date_created datetime DEFAULT NULL,  created_by varchar(45) DEFAULT NULL,  date_updated datetime DEFAULT NULL,  updated_by varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (department_id));
CREATE TABLE lms_tmp_designation (  record_id int(11) NOT NULL,  designation varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (record_id));
CREATE TABLE lms_tmp_doctor (  record_no int(11) NOT NULL,  doctor_id int(11) DEFAULT NULL,  doctor_name varchar(45) DEFAULT NULL,  clinic varchar(256) DEFAULT NULL,  address varchar(256) DEFAULT NULL,  contact varchar(45) DEFAULT NULL,  email varchar(256) DEFAULT NULL,  book_number varchar(45) DEFAULT NULL,  is_active char(1) DEFAULT NULL,  branch_code varchar(45) DEFAULT NULL,  medical_representative varchar(256) DEFAULT NULL,  routine_test int(11) DEFAULT NULL,  special_test int(11) DEFAULT NULL,  ultrasound int(11) DEFAULT NULL,  xray int(11) DEFAULT NULL,  ecg int(11) DEFAULT NULL,  no_discount_test int(11) DEFAULT NULL,  date_created datetime DEFAULT NULL,  created_by varchar(45) DEFAULT NULL,  date_updated datetime DEFAULT NULL,  updated_by varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (record_no));
CREATE TABLE lms_tmp_doctor_beneficiary (  record_id int(11) NOT NULL,  doctor_record_no int(11) DEFAULT NULL,  routine_test int(11) DEFAULT NULL,  special_test int(11) DEFAULT NULL,  ultrasound int(11) DEFAULT NULL,  xray int(11) DEFAULT NULL,  ecg int(11) DEFAULT NULL,  no_discount_test int(11) DEFAULT NULL,  date_created datetime DEFAULT NULL,  created_by varchar(45) DEFAULT NULL,  date_updated datetime DEFAULT NULL,  updated_by varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (record_id));
CREATE TABLE lms_tmp_employee (  employee_id int(11) NOT NULL,  employee_name varchar(256) DEFAULT NULL,  designation varchar(45) DEFAULT NULL,  age varchar(5) DEFAULT NULL,  age_unit varchar(10) DEFAULT NULL,  gender varchar(10) DEFAULT NULL,  address varchar(256) DEFAULT NULL,  contact varchar(45) DEFAULT NULL,  education varchar(45) DEFAULT NULL,  is_on_job char(1) DEFAULT NULL,  date_joined datetime DEFAULT NULL,  date_left datetime DEFAULT NULL,  branch_code varchar(45) DEFAULT NULL,  EMP_PWD varchar(256) DEFAULT NULL,  date_created datetime DEFAULT NULL,  created_by varchar(45) DEFAULT NULL,  date_updated datetime DEFAULT NULL,  updated_by varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (employee_id));
CREATE TABLE lms_tmp_expense_type (  expense_type_id int(11) NOT NULL,  expense_type varchar(256) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (expense_type_id));
CREATE TABLE lms_tmp_expense_voucher (  voucher_id int(11) NOT NULL,  voucher_date datetime DEFAULT NULL,  expense_type varchar(45) DEFAULT NULL,  expense_reference varchar(45) DEFAULT NULL,  payment_mode varchar(45) DEFAULT NULL,  cheque_date datetime DEFAULT NULL,  cheque_number varchar(45) DEFAULT NULL,  bank varchar(256) DEFAULT NULL,  narration varchar(45) DEFAULT NULL,  amount decimal(10,0) DEFAULT NULL,  amount_in_words varchar(256) DEFAULT NULL,  username varchar(45) DEFAULT NULL,  working_shift varchar(45) DEFAULT NULL,  exact_time varchar(45) DEFAULT NULL,  computer_id varchar(45) DEFAULT NULL,  is_cancelled char(1) DEFAULT NULL,  reason_cancelled varchar(256) DEFAULT NULL,  branch_code varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (voucher_id));
CREATE TABLE lms_tmp_lab (  lab_name char(45) NOT NULL,  services varchar(256) DEFAULT NULL,  ntn varchar(45) DEFAULT NULL,  lab_code varchar(45) DEFAULT NULL,  is_registered char(1) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (lab_name));
CREATE TABLE lms_tmp_location_beneficiary_amount (  record_id int(11) NOT NULL,  location_id int(11) DEFAULT NULL,  location_name varchar(256) DEFAULT NULL,  patient_lab_no int(11) DEFAULT NULL,  beneficiary_date datetime DEFAULT NULL,  routine_test int(11) DEFAULT NULL,  special_test int(11) DEFAULT NULL,  ultrasound int(11) DEFAULT NULL,  xray int(11) DEFAULT NULL,  ecg int(11) DEFAULT NULL,  no_discount_test int(11) DEFAULT NULL,  date_created datetime DEFAULT NULL,  created_by varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (record_id));
CREATE TABLE lms_tmp_other_lab (  lab_id int(11) NOT NULL,  lab_name varchar(256) DEFAULT NULL,  contact varchar(45) DEFAULT NULL,  address varchar(45) DEFAULT NULL,  lab_code varchar(45) DEFAULT NULL,  date_created datetime DEFAULT NULL,  is_active char(1) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (lab_id));
CREATE TABLE lms_tmp_patient (  patient_lab_no int(11) NOT NULL,  branch_code varchar(45) DEFAULT NULL,  record_date datetime DEFAULT NULL,  patient_name varchar(256) DEFAULT NULL,  age varchar(5) DEFAULT NULL,  age_unit varchar(45) DEFAULT NULL,  gender varchar(10) DEFAULT NULL,  contact_no varchar(45) DEFAULT NULL,  doctor_location varchar(45) DEFAULT NULL,  doctor_id int(11) DEFAULT NULL,  doctor_name varchar(256) DEFAULT NULL,  sample_collector_id int(11) DEFAULT NULL,  sample_collector_name varchar(256) DEFAULT NULL,  total_amount decimal(10,0) DEFAULT NULL,  discount decimal(10,0) DEFAULT NULL,  net_amount decimal(10,0) DEFAULT NULL,  cash_received decimal(10,0) DEFAULT NULL,  dues_remaining decimal(10,0) DEFAULT NULL,  is_referred char(1) DEFAULT NULL,  referred_by varchar(256) DEFAULT NULL,  patient_history varchar(256) DEFAULT NULL,  is_cancelled char(1) DEFAULT NULL,  reason_cancelled varchar(256) DEFAULT NULL,  username varchar(45) DEFAULT NULL,  working_shift varchar(45) DEFAULT NULL,  computer_id varchar(256) DEFAULT NULL,  exact_time varchar(45) DEFAULT NULL,  routine_test_discount decimal(10,0) DEFAULT NULL,  routine_test_discount_amount decimal(10,0) DEFAULT NULL,  routine_test_rate decimal(10,0) DEFAULT NULL,  special_test_discount decimal(10,0) DEFAULT NULL,  special_test_discount_amount decimal(10,0) DEFAULT NULL,  special_test_rate decimal(10,0) DEFAULT NULL,  ultrasound_disc decimal(10,0) DEFAULT NULL,  ultrasound_discount_amount decimal(10,0) DEFAULT NULL,  ultrasound_rate decimal(10,0) DEFAULT NULL,  xray_discount decimal(10,0) DEFAULT NULL,  xray_discount_amount decimal(10,0) DEFAULT NULL,  xray_rate decimal(10,0) DEFAULT NULL,  ecg_discount decimal(10,0) DEFAULT NULL,  ecg_discount_amount decimal(10,0) DEFAULT NULL,  ecg_rate decimal(10,0) DEFAULT NULL,  no_discount_test_discount decimal(10,0) DEFAULT NULL,  no_discount_test_discount_amount decimal(10,0) DEFAULT NULL,  no_discount_test_rate decimal(10,0) DEFAULT NULL,  reference_no int(11) DEFAULT NULL,  reference_relationship varchar(45) DEFAULT NULL,  reference_name varchar(256) DEFAULT NULL,  BS_PIDN varchar(45) DEFAULT NULL,  zakat_applicable char(1) DEFAULT NULL,  date_created datetime DEFAULT NULL,  created_by varchar(45) DEFAULT NULL,  date_updated datetime DEFAULT NULL,  updated_by varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (patient_lab_no));
CREATE TABLE lms_tmp_patient_dues (  voucher_id int(11) NOT NULL,  date_recorded datetime DEFAULT NULL,  amount_received decimal(10,0) DEFAULT NULL,  patient_lab_number varchar(45) DEFAULT NULL,  username varchar(45) DEFAULT NULL,  working_shift varchar(45) DEFAULT NULL,  exact_time varchar(45) DEFAULT NULL,  computer_id varchar(45) DEFAULT NULL,  branch varchar(45) DEFAULT NULL,  date_created datetime DEFAULT NULL,  created_by varchar(45) DEFAULT NULL,  date_updated datetime DEFAULT NULL,  updated_by varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (voucher_id));
CREATE TABLE lms_tmp_system (  system_name char(45) NOT NULL,  system_code varchar(45) DEFAULT NULL,  branch_code varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (system_name));
CREATE TABLE lms_tmp_test (  test_id int(11) NOT NULL,  test_name varchar(256) DEFAULT NULL,  test_fee decimal(10,0) DEFAULT NULL,  department_id int(11) DEFAULT NULL,  department_name varchar(256) DEFAULT NULL,  test_type varchar(45) DEFAULT NULL,  test_day int(11) DEFAULT NULL,  sputum_collection varchar(256) DEFAULT NULL,  sputum_quality varchar(45) DEFAULT NULL,  WLF_TEST char(1) DEFAULT NULL,  INC_TEST char(1) DEFAULT NULL,  INC_AMT decimal(10,0) DEFAULT NULL,  date_created datetime DEFAULT NULL,  created_by varchar(45) DEFAULT NULL,  date_updated datetime DEFAULT NULL,  updated_by varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (test_id));
CREATE TABLE lms_tmp_transaction (  transaction_id int(11) NOT NULL,  patient_lab_no varchar(45) DEFAULT NULL,  branch_code varchar(45) DEFAULT NULL,  test_id int(11) DEFAULT NULL,  test_name varchar(256) DEFAULT NULL,  test_fee decimal(10,0) DEFAULT NULL,  test_department varchar(45) DEFAULT NULL,  test_priority varchar(45) DEFAULT NULL,  reporting_date datetime DEFAULT NULL,  sample_received char(1) DEFAULT NULL,  sample_date datetime DEFAULT NULL,  sample_time varchar(45) DEFAULT NULL,  sample_receiver_computer varchar(45) DEFAULT NULL,  discount decimal(10,0) DEFAULT NULL,  discount_amount decimal(10,0) DEFAULT NULL,  discount_rate decimal(10,0) DEFAULT NULL,  date_created datetime DEFAULT NULL,  created_by varchar(45) DEFAULT NULL,  date_updated datetime DEFAULT NULL,  updated_by varchar(45) DEFAULT NULL,  uuid char(38) DEFAULT NULL,  PRIMARY KEY (transaction_id));
