package org.irdresearch.tbreach.xpertsms.parser.astm;



import org.irdresearch.tbreach.xpertsms.model.astm.XpertASTMResultUploadMessage;

public class BaseParser {

	protected XpertASTMResultUploadMessage record;
	String messageString;
	
	public BaseParser(XpertASTMResultUploadMessage record, String messageString) {
		this.record = record;
		this.messageString = messageString;
	}
	
	public void parse() throws Exception {
		//System.out.println("Parsing: " + messageString + " <<<>>>");
	}
	
}
