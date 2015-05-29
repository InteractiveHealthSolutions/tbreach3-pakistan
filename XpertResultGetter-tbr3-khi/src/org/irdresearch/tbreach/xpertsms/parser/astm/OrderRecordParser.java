package org.irdresearch.tbreach.xpertsms.parser.astm;

import org.irdresearch.tbreach.xpertsms.constants.astm.ASTMMessageConstants;
import org.irdresearch.tbreach.xpertsms.model.astm.XpertASTMResultUploadMessage;
import org.irdresearch.tbreach.xpertsms.net.exception.astm.InvalidASTMMessageFormatException;

public class OrderRecordParser extends BaseParser{
	
	public OrderRecordParser(XpertASTMResultUploadMessage record, String messageString) {
		super(record,messageString);
	}

	public void parse() throws InvalidASTMMessageFormatException {
		
		String[] fields = messageString.split("\\" + record.getFieldDelimiter());
		
		if(fields.length!= ASTMMessageConstants.NUM_ORDER_FIELDS) {
			throw new InvalidASTMMessageFormatException("O001 - Order record must have " + ASTMMessageConstants.NUM_ORDER_FIELDS + " fields");
		}
		
		setSpecimenId(fields[2]);
		
		if(fields[3].length()!=0) {
			setInstrumentSpecimenId(fields[3]);
		}
		
		setUniversalTestId(fields[4]);
		setPriority(fields[5]);
		if(fields[6].length()!=0) {
			setOrderDateTime(fields[6]);
		}
		
		setActionCode(fields[11]);
		setSpecimenType(fields[15]);
		setReportType(fields[25]);
		
		
	}
	
	private void setSpecimenId(String field) throws InvalidASTMMessageFormatException {
		if(field.length() < 1 || field.length() > ASTMMessageConstants.MAX_SPECIMEN_ID_LENGTH) {
			throw new InvalidASTMMessageFormatException("O002 - Specimen ID must be between 1 and " + ASTMMessageConstants.MAX_SPECIMEN_ID_LENGTH + " characters");
		}
		
		record.setSampleId(field);
	}
	
	private void setInstrumentSpecimenId(String field) throws InvalidASTMMessageFormatException {
		if(field.length() < 1 || field.length() > ASTMMessageConstants.MAX_INSTRUMENT_SPECIMEN_ID_LENGTH) {
			throw new InvalidASTMMessageFormatException("O003 - Instrument Specimen ID must be between 1 and " + ASTMMessageConstants.MAX_INSTRUMENT_SPECIMEN_ID_LENGTH + " characters");
		}
		
		record.setInstrumentSpecimenId(field);
	}
	
	private void setUniversalTestId(String field) throws InvalidASTMMessageFormatException {
		if(field.length() < 1 || field.length() > ASTMMessageConstants.MAX_UNIVERSAL_TEST_ID_LENGTH) {
			throw new InvalidASTMMessageFormatException("O004 - Universal ID must be between 1 and " + ASTMMessageConstants.MAX_UNIVERSAL_TEST_ID_LENGTH + " characters");
		}
		
		record.setUniversalTestId(field);
	}
	
	private void setPriority(String field) throws InvalidASTMMessageFormatException {
		if(!field.equals(ASTMMessageConstants.STAT_ORDER_PRIORITY) && !field.equals(ASTMMessageConstants.NORMAL_ORDER_PRIORITY)) {
			throw new InvalidASTMMessageFormatException("O005 - Priority must be either \"" + ASTMMessageConstants.STAT_ORDER_PRIORITY + "\" OR \"" + ASTMMessageConstants.NORMAL_ORDER_PRIORITY  + "\"");
		}
		
		record.setPriority(field);
	}
	
	private void setOrderDateTime(String field) throws InvalidASTMMessageFormatException {
		if(field.length() < 1 || field.length() > ASTMMessageConstants.MAX_DATE_LENGTH) {
			throw new InvalidASTMMessageFormatException("O006 - Date must be " + ASTMMessageConstants.MAX_DATE_LENGTH + " characters");
		}
		
		record.setOrderDateTime(field);
	}
	
	private void setActionCode(String field) throws InvalidASTMMessageFormatException {
		if(field.length()!=0 && !field.equals(ASTMMessageConstants.ORDER_ACTION_CODE_QUALITY_CONTROL) && !field.equals(ASTMMessageConstants.ORDER_ACTION_CODE_CANCELED) && !field.equals(ASTMMessageConstants.ORDER_ACTION_CODE_PENDING)) {
			throw new InvalidASTMMessageFormatException("O007 - Invalid order action code " + field);
		}
		
		record.setActionCode(field);
	}
	
	private void setSpecimenType(String field) throws InvalidASTMMessageFormatException {
		if(!field.equals(ASTMMessageConstants.SPECIMEN_TYPE)) {
			throw new InvalidASTMMessageFormatException("O008 - Invalid specimen type");
		}
		
		record.setSpecimenType(field);
	}
	
	private void setReportType(String field) throws InvalidASTMMessageFormatException {
		if(!field.equals(ASTMMessageConstants.REPORT_TYPE_FINAL) && !field.equals(ASTMMessageConstants.REPORT_TYPE_CANCELED) && !field.equals(ASTMMessageConstants.REPORT_TYPE_PENDING)) {
			throw new InvalidASTMMessageFormatException("O009 - Invalid report type: " + field);
		}
		
		record.setReportType(field);
	}

	
}