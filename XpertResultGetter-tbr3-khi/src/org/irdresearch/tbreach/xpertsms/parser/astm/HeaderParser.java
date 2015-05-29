package org.irdresearch.tbreach.xpertsms.parser.astm;

import org.irdresearch.tbreach.xpertsms.constants.astm.ASTMMessageConstants;
import org.irdresearch.tbreach.xpertsms.model.astm.XpertASTMResultUploadMessage;
import org.irdresearch.tbreach.xpertsms.net.exception.astm.InvalidASTMMessageFormatException;

public class HeaderParser extends BaseParser {

	//char 
	
	public HeaderParser(XpertASTMResultUploadMessage record,String messageString) {
		super(record,messageString);
	}
	
	public void parse() throws InvalidASTMMessageFormatException {
		
		String[] fields = messageString.split("\\" + record.getFieldDelimiter());
		
		if(fields.length!= ASTMMessageConstants.NUM_HEADER_FIELDS) {
			throw new InvalidASTMMessageFormatException("H001 - Header record must have " + ASTMMessageConstants.NUM_HEADER_FIELDS + " fields");
		}
		
		
		//set delimiters
		//0 is H
		setDelimiters(fields[1]);
		setMessageId(fields[2]);
		//4th field is empty
		setSystemDetails(fields[4]);
		//5,6,7,8,9 empty
		setReceiverId(fields[9]);
		//11 empty
		setProcessingId(fields[11]);
		setVersionNumber(fields[12]);
		setMessageDateTime(fields[13]);
		

	}
	
	private void setDelimiters(String field) throws InvalidASTMMessageFormatException {
		String delimiters = field;
		if(delimiters.length()==0) {
			throw new InvalidASTMMessageFormatException("H002 - Field 2 must not be empty");
		}
		
		record.setRepeatDelimiter(delimiters.charAt(0));
		if(delimiters.length()>1){
			record.setComponentDelimiter(delimiters.charAt(1));
		}
		
		if(delimiters.length()==2) {
			record.setEscapeDelimiter(delimiters.charAt(2));
		}
	}
	
	private void setMessageId(String field) throws InvalidASTMMessageFormatException {
		if(field.length() < 1 || field.length() > ASTMMessageConstants.MAX_MESSAGE_ID_LENGTH) {
			throw new InvalidASTMMessageFormatException("H003 - Message ID must be between 1 and " + ASTMMessageConstants.MAX_MESSAGE_ID_LENGTH + " characters");
		}
		
		record.setMessageId(field);
	}
	
	private void setSystemDetails(String field) throws InvalidASTMMessageFormatException {
		String systemDetails[] = field.split("\\" + record.getComponentDelimiter());
	
	    if(systemDetails.length != 3) {
	    	throw new InvalidASTMMessageFormatException("H004 - Sender Name or ID must contain three components");
	    }
	    
	    String systemId = systemDetails[0];
	    String systemName = systemDetails[1];
	    String softwareVersion = systemDetails[2];
	    
	    if(systemId.length() < 1 || systemId.length() > ASTMMessageConstants.MAX_SYSTEM_ID_LENGTH) {
	    	throw new InvalidASTMMessageFormatException("H005 - System ID must be between 1 and " + ASTMMessageConstants.MAX_SYSTEM_ID_LENGTH + " characters");
	    }
	    
	    if(!systemName.equals(ASTMMessageConstants.SYSTEM_NAME)) {
	    	throw new InvalidASTMMessageFormatException("H006 - System Name must be \"" + ASTMMessageConstants.SYSTEM_NAME + "\"");
	    }
	    
	    if(softwareVersion.length() < 1 || softwareVersion.length() > ASTMMessageConstants.MAX_SOFTWARE_VERSION_LENGTH) {
	    	throw new InvalidASTMMessageFormatException("H007 - Software Version must be between 1 and " + ASTMMessageConstants.MAX_SOFTWARE_VERSION_LENGTH+ " characters");
	    }
	    
	    record.setSystemId(systemId);
	    record.setSystemName(systemName);
	    record.setSoftwareVersion(softwareVersion);
	}
	
	private void setReceiverId(String field) throws InvalidASTMMessageFormatException {
		if(field.length() < 1 || field.length() > ASTMMessageConstants.MAX_RECV_ID_LENGTH) {
			throw new InvalidASTMMessageFormatException("H008 - Receiver ID must be between 1 and " + ASTMMessageConstants.MAX_RECV_ID_LENGTH + " characters");
		}
		
		record.setReceiverId(field);
	}
	
	private void setProcessingId(String field) throws InvalidASTMMessageFormatException {
		if(!field.equals(ASTMMessageConstants.PROC_ID)) {
			throw new InvalidASTMMessageFormatException("H009 - Processing ID must be \"" + ASTMMessageConstants.PROC_ID + "\"");
		}
		
		record.setProcessingId(field);
	}
	
	private void setVersionNumber(String field) throws InvalidASTMMessageFormatException {
		if(!field.equals(ASTMMessageConstants.VERSION_NUMBER)) {
			throw new InvalidASTMMessageFormatException("H010 - Processing ID must be \"" + ASTMMessageConstants.VERSION_NUMBER + "\"");
		}
		
		record.setVersionNumber(field);
	}
	
	private void setMessageDateTime(String field) throws InvalidASTMMessageFormatException {
		if(field.length() < 1 || field.length() > ASTMMessageConstants.MAX_DATE_LENGTH) {
			throw new InvalidASTMMessageFormatException("H011 - Date must be " + ASTMMessageConstants.MAX_DATE_LENGTH + " characters");
		}
		
		record.setMessageDateTime(field);
		
	}
	

}
