package org.irdresearch.tbreach.xpertsms.constants.astm;

public class ASTMMessageConstants {

	public static final char HEADER_RECORD = 'H';
	public static final char ORDER_RECORD = 'O';
	public static final char PATIENT_RECORD = 'P';
	public static final char RESULT_RECORD = 'R';
	public static final char TERMINATOR_RECORD = 'L';
	public static final char COMMENT_RECORD = 'C';
	
	public static final int MAX_DATE_LENGTH = 14;
	
	//HEADER CONSTANTS
	public static final int NUM_HEADER_FIELDS = 14;
	public static final int MAX_MESSAGE_ID_LENGTH = 32;
	public static final int MAX_SYSTEM_ID_LENGTH = 50;
	public static final int MAX_SOFTWARE_VERSION_LENGTH = 16;
	public static final String SYSTEM_NAME = "GeneXpert";
	public static final int MAX_RECV_ID_LENGTH = 20;
	public static final String PROC_ID = "P";
	public static final String VERSION_NUMBER = "1394-97";
	
	
	//PATIENT RECORD
	public static final int MAX_NUM_PATIENT_RECORD_FIELDS = 5; 
	public static final int MAX_PATIENT_ID_LENGTH = 32;

	
	//ORDER RECORD
	public static final int NUM_ORDER_FIELDS = 26; 
	public static final int MAX_SPECIMEN_ID_LENGTH = 25;
	public static final int MAX_INSTRUMENT_SPECIMEN_ID_LENGTH = 32;
	public static final int MAX_UNIVERSAL_TEST_ID_LENGTH = 15;
	public static final String STAT_ORDER_PRIORITY = "S";
	public static final String NORMAL_ORDER_PRIORITY = "R";
	public static final String ORDER_ACTION_CODE_QUALITY_CONTROL = "Q";
	public static final String ORDER_ACTION_CODE_CANCELED = "C";
	public static final String ORDER_ACTION_CODE_PENDING = "P";
	public static final String SPECIMEN_TYPE = "ORH";
	public static final String REPORT_TYPE_FINAL = "F";
	public static final String REPORT_TYPE_CANCELED = "X";
	public static final String REPORT_TYPE_PENDING = "I";
	
	//RESULT RECORD
	public static final int MAX_NUM_RESULT_FIELDS = 14; 
	public static final int MIN_NUM_RESULT_FIELDS = 4;
	public static final int NUM_PROBE_RESULT_FIELDS = 4;
	public static final int MAX_SYSTEM_DEFINED_TEST_ID_LENGTH = 15;
	public static final int MAX_SYSTEM_DEFINED_TEST_NAME_LENGTH = 50;
	public static final int MAX_SYSTEM_DEFINED_TEST_VERSION_LENGTH = 4;
	public static final int MAX_ANALYTE_NAME_LENGTH = 20;
	public static final int MAX_COMPLEMENTARY_RESULT_LENGTH = 10;
	public static final int MAX_QUAL_VALUE_LENGTH = 256;
	public static final int MAX_QUANT_VALUE_LENGTH = 20;
	public static final int MAX_UNIT_LENGTH = 10;
	public static final int MAX_ABNORMAL_FLAG_LENGTH = 2;
	public static final int MAX_RESULT_STATUS_LENGTH = 1;
	public static final int MAX_OPERATOR_ID_LENGTH = 32;
	public static final int MAX_PC_IDENTIFIER_LENGTH = 20;
	public static final int MAX_REAGENT_LOT_ID_LENGTH = 10;
	public static final char FINAL_RESULT = 'F';
	public static final char PENDING_RESULT = 'P';
	public static final char ERROR_RESULT = 'X';
	public static final char CORRECTED_RESULT = 'C';
	
	//COMMENT RECORD
	public static final int NUM_COMMENT_FIELDS = 5;
	public static final int MAX_COMMENT_SOURCE_COMPONENTS = 5;
	public static final int MAX_COMMENT_SOURCE_LENGTH = 1;
	public static final char COMMENT_SOURCE = 'I';
	public static final int MAX_COMMENT_CODE_LENGTH = 50;
	public static final int MAX_COMMENT_DESCRIPTION_LENGTH = 500;
	public static final int MAX_COMMENT_TYPE_LENGTH = 1;
	public static final char COMMENT_TYPE_ERROR = 'N';
	public static final char COMMENT_TYPE_NOTES = 'I';
	
	//PROBE DATA
	public static final String PROBE_A = "PROBE A";
	public static final String PROBE_B = "PROBE B";
	public static final String PROBE_C = "PROBE C";
	public static final String PROBE_D = "PROBE D";
	public static final String PROBE_E = "PROBE E";
	public static final String SPC = "SPC";
	public static final String CT = "Ct";
	public static final String ENDPT = "EndPt";
	
	public static final String TRUE = "true";
	public static final String FALSE = "false";
}
