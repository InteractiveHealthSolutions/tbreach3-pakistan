package org.irdresearch.tbreach.xpertsms.parser.astm;

import org.irdresearch.tbreach.xpertsms.constants.astm.ASTMMessageConstants;
import org.irdresearch.tbreach.xpertsms.model.astm.XpertASTMResultUploadMessage;
import org.irdresearch.tbreach.xpertsms.net.exception.astm.InvalidASTMMessageFormatException;

public class PatientRecordParser extends BaseParser {
	
	public PatientRecordParser(XpertASTMResultUploadMessage record,String messageString) {
		super(record,messageString);
	}

	public void parse() throws InvalidASTMMessageFormatException {
		
		String[] fields = messageString.split("\\" + record.getFieldDelimiter());

		if (fields.length > ASTMMessageConstants.MAX_NUM_PATIENT_RECORD_FIELDS) {
			throw new InvalidASTMMessageFormatException("P001 - Patient record must have at most " + ASTMMessageConstants.MAX_NUM_PATIENT_RECORD_FIELDS + " fields");
		}
		
		if(fields.length == 5) {
			setPatientId(fields[4]);
		}
	}
	
	public void setPatientId(String field) throws InvalidASTMMessageFormatException {
		if(field.length() < 1 || field.length() > ASTMMessageConstants.MAX_PATIENT_ID_LENGTH) {
			throw new InvalidASTMMessageFormatException("P002 - Patient ID must be between 1 and " + ASTMMessageConstants.MAX_PATIENT_ID_LENGTH + " characters");
		}
		
		record.setPatientId(field.trim());
	}
}
