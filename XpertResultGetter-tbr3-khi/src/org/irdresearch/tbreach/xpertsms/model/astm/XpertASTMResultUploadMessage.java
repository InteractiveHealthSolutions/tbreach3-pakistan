package org.irdresearch.tbreach.xpertsms.model.astm;

import java.io.IOException;
import java.io.StringReader;

import javax.jws.WebParam.Mode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.irdresearch.tbreach.xpertsms.constants.astm.ASTMMessageConstants;
import org.irdresearch.tbreach.xpertsms.model.XpertResultUploadMessage;
import org.irdresearch.tbreach.xpertsms.ui.ControlPanel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XpertASTMResultUploadMessage extends XpertResultUploadMessage {

	protected char fieldDelimiter;
	protected char repeatDelimiter;
	protected char componentDelimiter;
	protected char escapeDelimiter;
	
	//header fields
	protected String messageId;
	protected String systemId; //e.g. GENEXPERT_PC (as assigned in System Name in system configuration)
	protected String instrumentSpecimenID; //e.g. GeneXpert
	protected String softwareVersion;
	protected String receiverId; //Host ID under system config
	protected String processingId;
	protected String versionNumber; //1384-97
	protected String messageDateTime;
	
	//order fields
	protected String instrumentSpecimenId;
	protected String universalTestId; //Assay Host Test Code e.g. G4v5
	protected String priority;
	protected String orderDateTime;
	protected String actionCode;
	protected String specimenType;
	protected String reportType;
	
	//result fields
	//protected String systemDefinedTestId; //e.g. TBPos or Rif as per Host Configuration
	protected String systemDefinedTestName; // e.g. Xpert MTB-RIF Assay G4
	protected String systemDefinedTestVersion; //e.g 5
	
	protected String resultStatus;// e.g. F
	protected String operatorId;// e.g Karachi Xray
	protected String testStartDate;
	protected String testEndDate;
	protected String pcId;
	protected String instrumentSerial;
	protected String moduleId;
	protected String cartridgeId;
	protected String reagentLotId;
	protected String expDate;
	
	protected Boolean isFinal;
	protected Boolean isPending;
	protected Boolean isError;
	protected Boolean isCorrection;
	
	/*
R|2|^G4v5^^TBPos^^^Probe D^|POS^|||
R|3|^G4v5^^TBPos^^^Probe D^Ct|^32.4|||
R|4|^G4v5^^TBPos^^^Probe D^EndPt|^102.0|||
R|5|^G4v5^^TBPos^^^Probe C^|POS^|||
R|6|^G4v5^^TBPos^^^Probe C^Ct|^31.2|||
R|7|^G4v5^^TBPos^^^Probe C^EndPt|^130.0|||
R|8|^G4v5^^TBPos^^^Probe E^|NEG^|||
R|9|^G4v5^^TBPos^^^Probe E^Ct|^0|||
R|10|^G4v5^^TBPos^^^Probe E^EndPt|^4.0|||
R|11|^G4v5^^TBPos^^^Probe B^|POS^|||
R|12|^G4v5^^TBPos^^^Probe B^Ct|^31.5|||
R|13|^G4v5^^TBPos^^^Probe B^EndPt|^88.0|||
R|14|^G4v5^^TBPos^^^Probe A^|POS^|||
R|15|^G4v5^^TBPos^^^Probe A^Ct|^30.7|||
R|16|^G4v5^^TBPos^^^Probe A^EndPt|^92.0|||
R|17|^G4v5^^TBPos^^^SPC^|NA^|||
R|18|^G4v5^^TBPos^^^SPC^Ct|^28.1|||
R|19|^G4v5^^TBPos^^^SPC^EndPt|^302.0|||
	 */
	
	//comments fields
	protected String errorCode;
    
	
	public XpertASTMResultUploadMessage() {
		super();
		fieldDelimiter = '|';
		repeatDelimiter = '@';
		componentDelimiter = '^';
		escapeDelimiter = '\\';
		messageId = null;
		retries = 0;
	}
	
	@Override
	public String toPostParams() {
		String postParams = "";
		//send test ID, username, password, xpert detail, result,
		
		postParams += "type=astmresult";
		
		if(ControlPanel.props.getProperty("serveruser")!=null)
			postParams += "&username=" + ControlPanel.props.getProperty("serveruser");
		
		if(ControlPanel.props.getProperty("serverpass")!=null)
			postParams += "&password=" + ControlPanel.props.getProperty("serverpass");
		if(patientId!=null)
			postParams += "&pid=" + patientId;
		if(sampleId!=null)
			postParams += "&sampleid=" + sampleId.replaceAll("\\(","").replaceAll("\\)","");
		postParams += "&mtb=" + mtbResult;
		postParams += "&rif=" + rifResult;
		
		if(isFinal()!=null && isFinal())
			postParams += "&final=yes";
		if(isPending()!=null && isPending())
			postParams += "&pending=yes";
		if(isError()!=null && isError()) {
			postParams += "&error=yes";
			postParams += "&errorcode=" + errorCode;
		}
		if(isCorrection()!=null && isCorrection())
			postParams += "&correction=yes";
		
		postParams += "&enddate=" + testEndDate;
		
		postParams += "&operatorid=" + operatorId;// e.g Karachi Xray
		postParams += "&pcid=" + pcId;
		postParams += "&instserial=" + instrumentSerial;
		postParams += "&moduleid=" + moduleId;
		postParams += "&cartrigeid=" + cartridgeId;
		postParams += "&reagentlotid=" + reagentLotId;
		postParams += "&systemid=" + systemId;
		postParams += "&receiverid=" + receiverId;
		postParams += "&expdate=" + expDate;
		
		if(ControlPanel.props.getProperty("exportprobes").equalsIgnoreCase(ASTMMessageConstants.TRUE)) {
			postParams += "&probea=" + probeResultA;
			postParams += "&probeb=" + probeResultB;
			postParams += "&probec=" + probeResultC;
			postParams += "&probed=" + probeResultD;
			postParams += "&probee=" + probeResultE;
			postParams += "&probespc=" + probeResultSPC;
			postParams += "&probeact=" + probeCtA;
			postParams += "&probebct=" + probeCtB;
			postParams += "&probecct=" + probeCtC;
			postParams += "&probedct=" + probeCtD;
			postParams += "&probeect=" + probeCtE;
			postParams += "&probespcct=" + probeCtSPC;
			postParams += "&probeaendpt=" + probeEndptA;
			postParams += "&probebendpt=" + probeEndptB;
			postParams += "&probecendpt=" + probeEndptC;
			postParams += "&probedendpt=" + probeEndptD;
			postParams += "&probeeendpt=" + probeEndptE;
			postParams += "&probespcendpt=" + probeEndptSPC;
			
		}
		
		
		return postParams + "&";
	}
	
	@Override
	public String toSMS() {
		
			String smsText = "";
			smsText += spaceIfNull(patientId) + "^" + spaceIfNull(sampleId) + "^" + spaceIfNull(mtbResult) + "^" + spaceIfNull(rifResult)+ "^" + spaceIfNull(systemId);
			
			smsText += "^" + spaceIfNull(pcId) + "^" + spaceIfNull(operatorId)+ "^" + spaceIfNull(instrumentSerial)+ "^" + spaceIfNull(moduleId) + "^" + spaceIfNull(cartridgeId) +  "^" + spaceIfNull(reagentLotId)+ "^" + spaceIfNull(testEndDate);
			
			if(isFinal()!=null && isFinal())
				smsText += "^" + "yes";
			else
				smsText += "^" + "no";
			if(isPending()!=null && isPending())
				smsText += "^" + "yes";
			else
				smsText += "^" + "no";
			
			if(isError()!=null && isError()) {
				smsText += "^" + "yes";
				smsText += "^" + errorCode;
			}
			else
				smsText += "^" + "no";
			
			if(isCorrection()!=null && isCorrection())
				smsText += "^" + "yes";
			else
				smsText += "^" + "no";
			
			
			if(ControlPanel.props.getProperty("exportprobes").equalsIgnoreCase(ASTMMessageConstants.TRUE)) {
				smsText += "^" + spaceIfNull(probeResultA) +
				"^" + spaceIfNull(probeResultB) + "^" + spaceIfNull(probeResultC) + "^" + spaceIfNull(probeResultD) + "^" + spaceIfNull(probeResultE) +
				"^" + spaceIfNull(probeResultSPC) + "^" + spaceIfNull(probeCtA) + "^" + spaceIfNull(probeCtB) + "^" + spaceIfNull(probeCtC) +
				"^" + spaceIfNull(probeCtD) + "^" + spaceIfNull(probeCtE) + "^" + spaceIfNull(probeCtSPC) + "^" + spaceIfNull(probeEndptA) + 
				"^" + spaceIfNull(probeEndptB) + "^" + spaceIfNull(probeEndptC) + "^" + spaceIfNull(probeEndptD) + "^" + spaceIfNull(probeEndptE) +
				"^" + spaceIfNull(probeEndptSPC);// + "^" + spaceIfNull(probeEndptSPC);
			}
			
			return smsText;
		
	}
	
	@Override
	public String toString() {
		return "XpertASTMResultUploadMessage [fieldDelimiter=" + fieldDelimiter
				+ ", repeatDelimiter=" + repeatDelimiter
				+ ", componentDelimiter=" + componentDelimiter
				+ ", escapeDelimiter=" + escapeDelimiter + ", messageId="
				+ messageId + ", systemId=" + systemId + ", instrumentSpecimenID="
				+ instrumentSpecimenID + ", softwareVersion=" + softwareVersion
				+ ", receiverId=" + receiverId + ", processingId="
				+ processingId + ", versionNumber=" + versionNumber
				+ ", messageDateTime=" + messageDateTime
				+ ", instrumentSpecimenId=" + instrumentSpecimenId
				+ ", universalTestId=" + universalTestId + ", priority="
				+ priority + ", orderDateTime=" + orderDateTime
				+ ", actionCode=" + actionCode + ", specimenType="
				+ specimenType + ", reportType=" + reportType
				+ ", systemDefinedTestName=" + systemDefinedTestName
				+ ", systemDefinedTestVersion=" + systemDefinedTestVersion
				+ ", resultStatus=" + resultStatus + ", operatorId="
				+ operatorId + ", testStartDate=" + testStartDate
				+ ", testEndDate=" + testEndDate + ", pcId=" + pcId
				+ ", instrumentSerial=" + instrumentSerial + ", moduleId="
				+ moduleId + ", cartridgeId=" + cartridgeId + ", reagentLotId="
				+ reagentLotId + ", expDate=" + expDate + ", isFinal="
				+ isFinal + ", isPending=" + isPending + ", isError=" + isError
				+ ", isCorrection=" + isCorrection + ", errorCode=" + errorCode
				+ ", patientId=" + patientId + ", sampleId=" + sampleId
				+ ", mtbResult=" + mtbResult + ", rifResult=" + rifResult
				+ ", probeResultA=" + probeResultA + ", probeResultB="
				+ probeResultB + ", probeResultC=" + probeResultC
				+ ", probeResultD=" + probeResultD + ", probeResultE="
				+ probeResultE + ", probeResultSPC=" + probeResultSPC
				+ ", probeCtA=" + probeCtA + ", probeCtB=" + probeCtB
				+ ", probeCtC=" + probeCtC + ", probeCtD=" + probeCtD
				+ ", probeCtE=" + probeCtE + ", probeCtSPC=" + probeCtSPC
				+ ", probeEndptA=" + probeEndptA + ", probeEndptB="
				+ probeEndptB + ", probeEndptC=" + probeEndptC
				+ ", probeEndptD=" + probeEndptD + ", probeEndptE="
				+ probeEndptE + ", probeEndptSPC=" + probeEndptSPC
				+ ", retries=" + retries + "]";
	}

	/**
	 * @return the fieldDelimiter
	 */
	public char getFieldDelimiter() {
		return fieldDelimiter;
	}

	/**
	 * @param fieldDelimiter the fieldDelimiter to set
	 */
	public void setFieldDelimiter(char fieldDelimiter) {
		this.fieldDelimiter = fieldDelimiter;
	}

	/**
	 * @return the repeatDelimiter
	 */
	public char getRepeatDelimiter() {
		return repeatDelimiter;
	}

	/**
	 * @param repeatDelimiter the repeatDelimiter to set
	 */
	public void setRepeatDelimiter(char repeatDelimiter) {
		this.repeatDelimiter = repeatDelimiter;
	}

	/**
	 * @return the componentDelimiter
	 */
	public char getComponentDelimiter() {
		return componentDelimiter;
	}

	/**
	 * @param componentDelimiter the componentDelimiter to set
	 */
	public void setComponentDelimiter(char componentDelimiter) {
		this.componentDelimiter = componentDelimiter;
	}

	/**
	 * @return the escapeDelimiter
	 */
	public char getEscapeDelimiter() {
		return escapeDelimiter;
	}

	/**
	 * @param escapeDelimiter the escapeDelimiter to set
	 */
	public void setEscapeDelimiter(char escapeDelimiter) {
		this.escapeDelimiter = escapeDelimiter;
	}

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the systemId
	 */
	public String getSystemId() {
		return systemId;
	}

	/**
	 * @param systemId the systemId to set
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	/**
	 * @return the instrumentSpecimenID
	 */
	public String getSystemName() {
		return instrumentSpecimenID;
	}

	/**
	 * @param instrumentSpecimenID the instrumentSpecimenID to set
	 */
	public void setSystemName(String instrumentSpecimenID) {
		this.instrumentSpecimenID = instrumentSpecimenID;
	}

	/**
	 * @return the softwareVersion
	 */
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	/**
	 * @param softwareVersion the softwareVersion to set
	 */
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	/**
	 * @return the receiverId
	 */
	public String getReceiverId() {
		return receiverId;
	}

	/**
	 * @param receiverId the receiverId to set
	 */
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * @return the processingId
	 */
	public String getProcessingId() {
		return processingId;
	}

	/**
	 * @param processingId the processingId to set
	 */
	public void setProcessingId(String processingId) {
		this.processingId = processingId;
	}

	/**
	 * @return the versionNumber
	 */
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @param versionNumber the versionNumber to set
	 */
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * @return the messageDateTime
	 */
	public String getMessageDateTime() {
		return messageDateTime;
	}

	/**
	 * @param messageDateTime the messageDateTime to set
	 */
	public void setMessageDateTime(String messageDateTime) {
		this.messageDateTime = messageDateTime;
	}

	
	/**
	 * @return the intrumentSpecimenId
	 */
	public String getInstrumentSpecimenId() {
		return instrumentSpecimenId;
	}

	/**
	 * @param intrumentSpecimenId the intrumentSpecimenId to set
	 */
	public void setInstrumentSpecimenId(String instrumentSpecimenId) {
		this.instrumentSpecimenId = instrumentSpecimenId;
	}

	/**
	 * @return the universalTestId
	 */
	public String getUniversalTestId() {
		return universalTestId;
	}

	/**
	 * @param universalTestId the universalTestId to set
	 */
	public void setUniversalTestId(String universalTestId) {
		this.universalTestId = universalTestId;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * @return the orderDateTime
	 */
	public String getOrderDateTime() {
		return orderDateTime;
	}

	/**
	 * @param orderDateTime the orderDateTime to set
	 */
	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	/**
	 * @return the actionCode
	 */
	public String getActionCode() {
		return actionCode;
	}

	/**
	 * @param actionCode the actionCode to set
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * @return the specimenType
	 */
	public String getSpecimenType() {
		return specimenType;
	}

	/**
	 * @param specimenType the specimenType to set
	 */
	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * @return the systemDefinedTestId
	 *//*
	public String getSystemDefinedTestId() {
		return systemDefinedTestId;
	}

	*//**
	 * @param systemDefinedTestId the systemDefinedTestId to set
	 *//*
	public void setSystemDefinedTestId(String systemDefinedTestId) {
		this.systemDefinedTestId = systemDefinedTestId;
	}*/

	/**
	 * @return the systemDefinedTestName
	 */
	public String getSystemDefinedTestName() {
		return systemDefinedTestName;
	}

	/**
	 * @param systemDefinedTestName the systemDefinedTestName to set
	 */
	public void setSystemDefinedTestName(String systemDefinedTestName) {
		this.systemDefinedTestName = systemDefinedTestName;
	}

	/**
	 * @return the systemDefinedTestVersion
	 */
	public String getSystemDefinedTestVersion() {
		return systemDefinedTestVersion;
	}

	/**
	 * @param systemDefinedTestVersion the systemDefinedTestVersion to set
	 */
	public void setSystemDefinedTestVersion(String systemDefinedTestVersion) {
		this.systemDefinedTestVersion = systemDefinedTestVersion;
	}

	

	/**
	 * @return the resultStatus
	 */
	public String getResultStatus() {
		return resultStatus;
	}

	/**
	 * @param resultStatus the resultStatus to set
	 */
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	/**
	 * @return the operatorId
	 */
	public String getOperatorId() {
		return operatorId;
	}

	/**
	 * @param operatorId the operatorId to set
	 */
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	/**
	 * @return the testStartDate
	 */
	public String getTestStartDate() {
		return testStartDate;
	}

	/**
	 * @param testStartDate the testStartDate to set
	 */
	public void setTestStartDate(String testStartDate) {
		this.testStartDate = testStartDate;
	}

	/**
	 * @return the testEndDate
	 */
	public String getTestEndDate() {
		return testEndDate;
	}

	/**
	 * @param testEndDate the testEndDate to set
	 */
	public void setTestEndDate(String testEndDate) {
		this.testEndDate = testEndDate;
	}

	/**
	 * @return the pcId
	 */
	public String getPcId() {
		return pcId;
	}

	/**
	 * @param pcId the pcId to set
	 */
	public void setPcId(String pcId) {
		this.pcId = pcId;
	}

	/**
	 * @return the instrumentSerial
	 */
	public String getInstrumentSerial() {
		return instrumentSerial;
	}

	/**
	 * @param instrumentSerial the instrumentSerial to set
	 */
	public void setInstrumentSerial(String instrumentSerial) {
		this.instrumentSerial = instrumentSerial;
	}

	/**
	 * @return the moduleId
	 */
	public String getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return the cartridgeId
	 */
	public String getCartridgeId() {
		return cartridgeId;
	}

	/**
	 * @param cartridgeId the cartridgeId to set
	 */
	public void setCartridgeId(String cartridgeId) {
		this.cartridgeId = cartridgeId;
	}

	/**
	 * @return the reagentLotId
	 */
	public String getReagentLotId() {
		return reagentLotId;
	}

	/**
	 * @param reagentLotId the reagentLotId to set
	 */
	public void setReagentLotId(String reagentLotId) {
		this.reagentLotId = reagentLotId;
	}

	/**
	 * @return the expDate
	 */
	public String getExpDate() {
		return expDate;
	}

	/**
	 * @param expDate the expDate to set
	 */
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	/**
	 * @return the isFinal
	 */
	public  Boolean isFinal() {
		if(isFinal!=null)
			return isFinal.booleanValue();
		return null;
	}

	/**
	 * @param isFinal the isFinal to set
	 */
	public void setFinal(boolean isFinal) {
		if(isFinal()==null)
			this.isFinal = new Boolean(isFinal);
	}

	/**
	 * @return the isPending
	 */
	public Boolean isPending() {
		if(isPending!=null)
			return isPending.booleanValue();
		return null;
	}

	/**
	 * @param isPending the isPending to set
	 */
	public void setPending(boolean isPending) {
		if(isPending()==null)
			this.isPending = new Boolean(isPending);
	}

	/**
	 * @return the isError
	 */
	public Boolean isError() {
		if(isError!=null)
			return isError.booleanValue();
		
		return null;
	}

	/**
	 * @param isError the isError to set
	 */
	public void setError(boolean isError) {
		if(isError()==null)
			this.isError = new Boolean(isError);
	}

	/**
	 * @return the isCorrection
	 */
	public Boolean isCorrection() {
		if(isCorrection!=null)
			return isCorrection.booleanValue();
		return null;
	}

	/**
	 * @param isCorrection the isCorrection to set
	 */
	public void setCorrection(boolean isCorrection) {
		if(isCorrection()==null)
			this.isCorrection = new Boolean(isCorrection);
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	
	@Override
	public String toCSV() {
		String csv = null;
		csv= "\"" + spaceIfNull(messageId) + "\",\"" + spaceIfNull(systemId) + "\",\"" + spaceIfNull(instrumentSpecimenID) + "\",\"" + spaceIfNull(softwareVersion)
				+ "\",\"" + spaceIfNull(receiverId) + "\",\""
				+ spaceIfNull(processingId) + "\",\"" + spaceIfNull(versionNumber)
				+ "\",\"" + spaceIfNull(messageDateTime)
				+ "\",\"" + spaceIfNull(instrumentSpecimenId)
				+ "\",\"" + spaceIfNull(universalTestId) + "\",\""
				+ spaceIfNull(priority) + "\",\"" + spaceIfNull(orderDateTime)
				+ "\",\""  + spaceIfNull(actionCode) + "\",\""
				+ spaceIfNull(specimenType) + "\",\"" + spaceIfNull(reportType)
				+ "\",\"" + spaceIfNull(systemDefinedTestName)
				+ "\",\"" + spaceIfNull(systemDefinedTestVersion)
				+ "\",\"" + spaceIfNull(resultStatus) + "\",\""
				+ spaceIfNull(operatorId) + "\",\"" + spaceIfNull(testStartDate)
				+ "\",\"" + spaceIfNull(testEndDate) + "\",\"" + spaceIfNull(pcId)
				+ "\",\"" + spaceIfNull(instrumentSerial) + "\",\""
				+ spaceIfNull(moduleId) + "\",\"" + spaceIfNull(cartridgeId) + "\",\""
				+ spaceIfNull(reagentLotId) + "\",\"" + spaceIfNull(expDate) + "\",\""
				+ spaceIfNull(isFinal) + "\",\"" + spaceIfNull(isPending) + "\",\"" + spaceIfNull(isError)
				+ "\",\"" + spaceIfNull(isCorrection) + "\",\"" + spaceIfNull(errorCode)
				+ "\",\"" + spaceIfNull(patientId) + "\",\"" + spaceIfNull(sampleId)
				+ "\",\"" + spaceIfNull(mtbResult) + "\",\"" + spaceIfNull(rifResult)
				+ "\",\"" + spaceIfNull(probeResultA) + "\",\""
				+ spaceIfNull(probeResultB) + "\",\"" + spaceIfNull(probeResultC)
				+ "\",\"" + spaceIfNull(probeResultD) + "\",\""
				+ spaceIfNull(probeResultE) + "\",\"" + spaceIfNull(probeResultSPC)
				+ "\",\"" + spaceIfNull(probeCtA) + "\",\"" + spaceIfNull(probeCtB)
				+ "\",\"" + spaceIfNull(probeCtC) + "\",\"" + spaceIfNull(probeCtD)
				+ "\",\"" + spaceIfNull(probeCtE) + "\",\"" + spaceIfNull(probeCtSPC)
				+ "\",\"" + spaceIfNull(probeEndptA) + "\",\""
				+ spaceIfNull(probeEndptB) + "\",\"" + spaceIfNull(probeEndptC)
				+ "\",\"" + spaceIfNull(probeEndptD) + "\",\""
				+ spaceIfNull(probeEndptE) + "\",\"" + spaceIfNull(probeEndptSPC) + "\"";
				
		return csv;
	}
	
	public Element toXMLNode() {
		String xml = "";
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(doc==null) {
			return null;
		}
	
		Element messageNode = doc.createElement("outgoingmessage");
		
		Element patientIdNode = doc.createElement("patientid");
		Text patientIdText = doc.createTextNode(spaceIfNull(patientId));
		patientIdNode.appendChild(patientIdText);
		messageNode.appendChild(patientIdNode);
		
		Element sampleIdNode = doc.createElement("sampleid");
		Text sampleIdText = doc.createTextNode(spaceIfNull(sampleId));
		sampleIdNode.appendChild(sampleIdText);
		messageNode.appendChild(sampleIdNode);
		
		Element mtbNode = doc.createElement("mtb");
		Text mtbText = doc.createTextNode(spaceIfNull(mtbResult));
		mtbNode.appendChild(mtbText);
		messageNode.appendChild(mtbNode);
		
		Element rifNode = doc.createElement("rif");
		Text rifText = doc.createTextNode(spaceIfNull(rifResult));
		rifNode.appendChild(rifText);
		messageNode.appendChild(rifNode);
		
		Element finalNode = doc.createElement("final");
		Text finalText = doc.createTextNode(spaceIfNull(isFinal));
		finalNode.appendChild(finalText);
		messageNode.appendChild(finalNode);
		
		Element errorNode = doc.createElement("error");
		Text errorText = doc.createTextNode(spaceIfNull(isError));
		errorNode.appendChild(errorText);
		messageNode.appendChild(errorNode);
		
		Element errorCodeNode = doc.createElement("errorcode");
		Text errorCodeText = doc.createTextNode(spaceIfNull(errorCode));
		errorCodeNode.appendChild(errorCodeText);
		messageNode.appendChild(errorCodeNode);
		
		Element pendingNode = doc.createElement("pending");
		Text pendingText = doc.createTextNode(spaceIfNull(isPending));
		pendingNode.appendChild(pendingText);
		messageNode.appendChild(pendingNode);
		
		Element correctionNode = doc.createElement("correction");
		Text correctionText = doc.createTextNode(spaceIfNull(isCorrection));
		correctionNode.appendChild(correctionText);
		messageNode.appendChild(correctionNode);
		
		Element instrumentIdNode = doc.createElement("instrumentid");
		Text instrumentIdText = doc.createTextNode(spaceIfNull(instrumentSerial));
		instrumentIdNode.appendChild(instrumentIdText);
		messageNode.appendChild(instrumentIdNode);
		
		Element pcIdNode = doc.createElement("pcid");
		Text pcIdText = doc.createTextNode(spaceIfNull(pcId));
		pcIdNode.appendChild(pcIdText);
		messageNode.appendChild(pcIdNode);
		
		Element cartridgeIdNode = doc.createElement("cartridgeid");
		Text cartridgeIdText = doc.createTextNode(spaceIfNull(cartridgeId));
		cartridgeIdNode.appendChild(cartridgeIdText);
		messageNode.appendChild(cartridgeIdNode);
		
		Element moduleIdNode = doc.createElement("moduleid");
		Text moduleIdText = doc.createTextNode(spaceIfNull(moduleId));
		moduleIdNode.appendChild(moduleIdText);
		messageNode.appendChild(moduleIdNode);
		
		Element reagentLotIdNode = doc.createElement("reagentlotid");
		Text reagentLotIdText = doc.createTextNode(spaceIfNull(reagentLotId));
		reagentLotIdNode.appendChild(reagentLotIdText);
		messageNode.appendChild(reagentLotIdNode);
		
		Element operatorIdNode = doc.createElement("operatorid");
		Text operatorIdText = doc.createTextNode(spaceIfNull(operatorId));
		operatorIdNode.appendChild(operatorIdText);
		messageNode.appendChild(operatorIdNode);
		
		Element resultDateNode = doc.createElement("resultdate");
		Text resultDateText = doc.createTextNode(spaceIfNull(testEndDate));
		resultDateNode.appendChild(resultDateText);
		messageNode.appendChild(resultDateNode);
		
		Element probeResultANode = doc.createElement("proberesulta");
		Text probeResultAText = doc.createTextNode(spaceIfNull(probeResultA));
		probeResultANode.appendChild(probeResultAText);
		messageNode.appendChild(probeResultANode);
		
		Element probeCtANode = doc.createElement("probecta");
		Text probeCtAText = doc.createTextNode(spaceIfNull(probeCtA));
		probeCtANode.appendChild(probeCtAText);
		messageNode.appendChild(probeCtAText);
		
		Element probeEndptANode = doc.createElement("probeendpta");
		Text probeEndptAText = doc.createTextNode(spaceIfNull(probeEndptA));
		probeEndptANode.appendChild(probeEndptAText);
		messageNode.appendChild(probeEndptAText);
		
		Element probeResultBNode = doc.createElement("proberesultb");
		Text probeResultBText = doc.createTextNode(spaceIfNull(probeResultB));
		probeResultBNode.appendChild(probeResultBText);
		messageNode.appendChild(probeResultBNode);
		
		Element probeCtBNode = doc.createElement("probectb");
		Text probeCtBText = doc.createTextNode(spaceIfNull(probeCtB));
		probeCtBNode.appendChild(probeCtBText);
		messageNode.appendChild(probeCtBText);
		
		Element probeEndptBNode = doc.createElement("probeendptb");
		Text probeEndptBText = doc.createTextNode(spaceIfNull(probeEndptB));
		probeEndptBNode.appendChild(probeEndptBText);
		messageNode.appendChild(probeEndptBText);
		
		Element probeResultCNode = doc.createElement("proberesultc");
		Text probeResultCText = doc.createTextNode(spaceIfNull(probeResultC));
		probeResultCNode.appendChild(probeResultCText);
		messageNode.appendChild(probeResultCNode);
		
		Element probeCtCNode = doc.createElement("probectc");
		Text probeCtCText = doc.createTextNode(spaceIfNull(probeCtC));
		probeCtCNode.appendChild(probeCtCText);
		messageNode.appendChild(probeCtCText);
		
		Element probeEndptCNode = doc.createElement("probeendptc");
		Text probeEndptCText = doc.createTextNode(spaceIfNull(probeEndptC));
		probeEndptCNode.appendChild(probeEndptCText);
		messageNode.appendChild(probeEndptCText);
		
		Element probeResultDNode = doc.createElement("proberesultd");
		Text probeResultDText = doc.createTextNode(spaceIfNull(probeResultD));
		probeResultDNode.appendChild(probeResultDText);
		messageNode.appendChild(probeResultDNode);
		
		Element probeCtDNode = doc.createElement("probectd");
		Text probeCtDText = doc.createTextNode(spaceIfNull(probeCtD));
		probeCtDNode.appendChild(probeCtDText);
		messageNode.appendChild(probeCtDText);
		
		Element probeEndptDNode = doc.createElement("probeendptd");
		Text probeEndptDText = doc.createTextNode(spaceIfNull(probeEndptD));
		probeEndptDNode.appendChild(probeEndptDText);
		messageNode.appendChild(probeEndptDText);
		
		Element probeResultENode = doc.createElement("proberesulte");
		Text probeResultEText = doc.createTextNode(spaceIfNull(probeResultE));
		probeResultENode.appendChild(probeResultEText);
		messageNode.appendChild(probeResultENode);
		
		Element probeCtENode = doc.createElement("probecte");
		Text probeCtEText = doc.createTextNode(spaceIfNull(probeCtE));
		probeCtENode.appendChild(probeCtEText);
		messageNode.appendChild(probeCtEText);
		
		Element probeEndptENode = doc.createElement("probeendpte");
		Text probeEndptEText = doc.createTextNode(spaceIfNull(probeEndptE));
		probeEndptENode.appendChild(probeEndptEText);
		messageNode.appendChild(probeEndptEText);
		
		Element probeResultSPCNode = doc.createElement("proberesultspc");
		Text probeResultSPCText = doc.createTextNode(spaceIfNull(probeResultSPC));
		probeResultSPCNode.appendChild(probeResultSPCText);
		messageNode.appendChild(probeResultSPCNode);
		
		Element probeCtSPCNode = doc.createElement("probectspc");
		Text probeCtSPCText = doc.createTextNode(spaceIfNull(probeCtSPC));
		probeCtSPCNode.appendChild(probeCtSPCText);
		messageNode.appendChild(probeCtSPCText);

		Element probeEndptSPCNode = doc.createElement("probeendptspc");
		Text probeEndptSPCText = doc.createTextNode(spaceIfNull(probeEndptSPC));
		probeEndptSPCNode.appendChild(probeEndptSPCText);
		messageNode.appendChild(probeEndptSPCText);
		
		Element messageIdNode = doc.createElement("messageid");
		Text messageIdText = doc.createTextNode(spaceIfNull(messageId));
		messageIdNode.appendChild(messageIdText);
		messageNode.appendChild(messageIdNode);
		
		Element systemIdNode = doc.createElement("systemid");
		Text systemIdText = doc.createTextNode(spaceIfNull(systemId));
		systemIdNode.appendChild(systemIdText);
		messageNode.appendChild(systemIdNode);
		
		Element instrumentSpecimenIDNode = doc.createElement("systemname");
		Text instrumentSpecimenIDText = doc.createTextNode(spaceIfNull(instrumentSpecimenID));
		instrumentSpecimenIDNode.appendChild(instrumentSpecimenIDText);
		messageNode.appendChild(instrumentSpecimenIDNode);
		
		Element softwareVersionNode = doc.createElement("softwareversion");
		Text softwareVersionText = doc.createTextNode(spaceIfNull(softwareVersion));
		softwareVersionNode.appendChild(softwareVersionText);
		messageNode.appendChild(softwareVersionNode);
		
		Element receiverIdNode = doc.createElement("receiverid");
		Text receiverIdText = doc.createTextNode(spaceIfNull(receiverId));
		receiverIdNode.appendChild(receiverIdText);
		messageNode.appendChild(receiverIdNode);
		
		Element processingIdNode = doc.createElement("processingid");
		Text processingIdText = doc.createTextNode(spaceIfNull(processingId));
		processingIdNode.appendChild(processingIdText);
		messageNode.appendChild(processingIdNode);
		
		Element versionNumberNode = doc.createElement("versionNumber");
		Text versionNumberText = doc.createTextNode(spaceIfNull(versionNumber));
		versionNumberNode.appendChild(versionNumberText);
		messageNode.appendChild(versionNumberNode);
		
		Element messageDateTimeNode = doc.createElement("messagedatetime");
		Text messageDateTimeText = doc.createTextNode(spaceIfNull(messageDateTime));
		messageDateTimeNode.appendChild(messageDateTimeText);
		messageNode.appendChild(messageDateTimeNode);
		
		Element instrumentSpecimenIdNode = doc.createElement("instrumentspecimenid");
		Text instrumentSpecimenIdText = doc.createTextNode(spaceIfNull(instrumentSpecimenID));
		instrumentSpecimenIdNode.appendChild(instrumentSpecimenIdText);
		messageNode.appendChild(instrumentSpecimenIdNode);
		
		Element universalTestIdNode = doc.createElement("universaltestid");
		Text universalTestIdText = doc.createTextNode(spaceIfNull(universalTestId));
		universalTestIdNode.appendChild(universalTestIdText);
		messageNode.appendChild(universalTestIdNode);
		
		Element orderDateTimeNode = doc.createElement("orderdatetime");
		Text orderDateTimeText = doc.createTextNode(spaceIfNull(orderDateTime));
		orderDateTimeNode.appendChild(orderDateTimeText);
		messageNode.appendChild(orderDateTimeNode);
		
		Element priorityNode = doc.createElement("priority");
		Text priorityText = doc.createTextNode(spaceIfNull(priority));
		priorityNode.appendChild(priorityText);
		messageNode.appendChild(priorityNode);
		
		Element actionCodeNode = doc.createElement("actioncode");
		Text actionCodeText = doc.createTextNode(spaceIfNull(actionCode));
		actionCodeNode.appendChild(actionCodeText);
		messageNode.appendChild(actionCodeNode);
		
		Element specimenTypeNode = doc.createElement("specimentype");
		Text specimenTypeText = doc.createTextNode(spaceIfNull(specimenType));
		specimenTypeNode.appendChild(specimenTypeText);
		messageNode.appendChild(specimenTypeNode);
		
		Element reportTypeNode = doc.createElement("reporttype");
		Text reportTypeText = doc.createTextNode(spaceIfNull(reportType));
		reportTypeNode.appendChild(reportTypeText);
		messageNode.appendChild(reportTypeNode);
		
		Element systemDefinedTestNameNode = doc.createElement("systemdefinedtestname");
		Text systemDefinedTestNameText = doc.createTextNode(spaceIfNull(systemDefinedTestName));
		systemDefinedTestNameNode.appendChild(systemDefinedTestNameText);
		messageNode.appendChild(systemDefinedTestNameNode);
		
		Element systemDefinedTestVersionNode = doc.createElement("systemdefinedtestversion");
		Text systemDefinedTestVersionText = doc.createTextNode(spaceIfNull(systemDefinedTestVersion));
		systemDefinedTestVersionNode.appendChild(systemDefinedTestVersionText);
		messageNode.appendChild(systemDefinedTestVersionNode);
		
		Element resultStatusNode = doc.createElement("resultstatus");
		Text resultStatusText = doc.createTextNode(spaceIfNull(resultStatus));
		resultStatusNode.appendChild(resultStatusText);
		messageNode.appendChild(resultStatusNode);
		
		doc.appendChild(messageNode);
		
		return messageNode;
	}
	
	public void loadXML(String xml) {
		//File fXmlFile = new File("/Users/mkyong/staff.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputSource is = new InputSource();
	    is.setCharacterStream(new StringReader(xml));

	    Document doc = null;
	    
	    try {
			 doc = dBuilder.parse(is);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    NodeList main = doc.getElementsByTagName("outgoingmessage").item(0).getChildNodes();
	    
	    Node node = main.item(0);
	    setPatientId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(1);
	    setSampleId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(2);
	    setMtbResult(node.getFirstChild().getNodeValue());
		
	    node = main.item(3);
	    setRifResult(node.getFirstChild().getNodeValue());
	    
	    node = main.item(4);
	    setFinal(Boolean.parseBoolean(node.getFirstChild().getNodeValue()));
	    
	    node = main.item(5);
	    setError(Boolean.parseBoolean(node.getFirstChild().getNodeValue()));
	    
	    node = main.item(6);
	    setErrorCode(node.getFirstChild().getNodeValue());
	    
	    node = main.item(7);
	    setError(Boolean.parseBoolean(node.getFirstChild().getNodeValue()));
	    
	    node = main.item(8);
	    setCorrection(Boolean.parseBoolean(node.getFirstChild().getNodeValue()));
	    
	    node = main.item(9);
	    setInstrumentSerial(node.getFirstChild().getNodeValue());
	   
	    node = main.item(10);
	    setPcId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(11);
	    setCartridgeId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(12);
	    setModuleId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(13);
	    setReagentLotId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(14);
	    setOperatorId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(15);
	    setTestEndDate(node.getFirstChild().getNodeValue());
	    
	    node = main.item(16);
	    setProbeResultA(node.getFirstChild().getNodeValue());
	    
	    node = main.item(17);
	    setProbeCtA(node.getFirstChild().getNodeValue());
	    
	    node = main.item(18);
	    setProbeEndptA(node.getFirstChild().getNodeValue());
	    
	    node = main.item(19);
	    setProbeResultB(node.getFirstChild().getNodeValue());
	    
	    node = main.item(20);
	    setProbeCtB(node.getFirstChild().getNodeValue());
	    
	    node = main.item(21);
	    setProbeEndptB(node.getFirstChild().getNodeValue());
	    
	    node = main.item(22);
	    setProbeResultC(node.getFirstChild().getNodeValue());
	    
	    node = main.item(23);
	    setProbeCtC(node.getFirstChild().getNodeValue());
	    
	    node = main.item(24);
	    setProbeEndptC(node.getFirstChild().getNodeValue());
	    
	    node = main.item(25);
	    setProbeResultD(node.getFirstChild().getNodeValue());
	    
	    node = main.item(26);
	    setProbeCtD(node.getFirstChild().getNodeValue());
	    
	    node = main.item(27);
	    setProbeEndptD(node.getFirstChild().getNodeValue());
	    
	    node = main.item(28);
	    setProbeResultE(node.getFirstChild().getNodeValue());
	    
	    node = main.item(29);
	    setProbeCtE(node.getFirstChild().getNodeValue());
	    
	    node = main.item(30);
	    setProbeEndptE(node.getFirstChild().getNodeValue());
	    
	    node = main.item(31);
	    setProbeResultSPC(node.getFirstChild().getNodeValue());
	    
	    node = main.item(32);
	    setProbeCtSPC(node.getFirstChild().getNodeValue());
	    
	    node = main.item(33);
	    setProbeEndptSPC(node.getFirstChild().getNodeValue());
	   
	    node = main.item(34);
	    setMessageId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(35);
	    setSystemId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(36);
	    setInstrumentSpecimenId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(37);
	    setSoftwareVersion(node.getFirstChild().getNodeValue());
	    
	    node = main.item(38);
	    setReceiverId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(39);
	    setProcessingId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(40);
	    setVersionNumber(node.getFirstChild().getNodeValue());
	    
	    node = main.item(41);
	    setMessageDateTime(node.getFirstChild().getNodeValue());
	    
	    node = main.item(42);
	    setInstrumentSpecimenId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(43);
	    setUniversalTestId(node.getFirstChild().getNodeValue());
	    
	    node = main.item(44);
	    setOrderDateTime(node.getFirstChild().getNodeValue());
	    
	    node = main.item(45);
	    setPriority(node.getFirstChild().getNodeValue());
	    
	    node = main.item(46);
	    setActionCode(node.getFirstChild().getNodeValue());
	    
	    node = main.item(47);
	    setSpecimenType(node.getFirstChild().getNodeValue());
	    
	    node = main.item(48);
	    setReportType(node.getFirstChild().getNodeValue());
	    
	    node = main.item(49);
	    setSystemDefinedTestName(node.getFirstChild().getNodeValue());
	    
	    node = main.item(50);
	    setSystemDefinedTestVersion(node.getFirstChild().getNodeValue());
	    
	    node = main.item(51);
	    setResultStatus(node.getFirstChild().getNodeValue());

	}
	
	@Override
	public String toSQLQuery() {
		String query = "";
		String mtbBurden = null;
		if(mtbResult != null) {
			
			int index = mtbResult.indexOf("MTB DETECTED");
			
			if(index!=-1) {
				mtbBurden = mtbResult.substring(index+"MTB DETECTED".length()+1);
				mtbResult = "MTB DETECTED";
				
				
			}
			
			else {
				index = mtbResult.indexOf("MTB NOT DETECTED");
				if(index!=-1) {
					mtbResult = "MTB NOT DETECTED";
					mtbBurden = "";
				}
				
				else {
					
					mtbBurden = "";
				}
			}
		}
		
		 query = "insert into genexpertresults (PatientID,SputumTestID,DateTested,DrugResistance,GeneXpertResult,MTBBurden,ErrorCode,InstrumentID,ModuleID,CartridgeID,ReagentLotID,PcID,ProbeResultA,ProbeResultB,ProbeResultC,ProbeResultD,ProbeResultE,ProbeResultSPC,ProbeCtA,ProbeCtB,ProbeCtC,ProbeCtD,ProbeCtE,ProbeCtSPC,ProbeEndptA,ProbeEndptB,ProbeEndptC,ProbeEndptD,ProbeEndptE,ProbeEndptSPC,OperatorID,CartridgeExpiryDate) VALUES ("   
					+ "'" + this.getPatientId() + "',"
					+ "'" + this.getSampleId() + "',"
					+ "'" + this.getTestEndDate() + "',"
					+ "'" + this.getRifResult() + "',"
					+ "'" + this.getMtbResult() + "',"
					+ "'" + mtbBurden + "',"
					+ this.getErrorCode() + ","
					+ "'" + this.getInstrumentSerial() + "',"
					+ "'" + this.getModuleId() + "',"
					+ "'" + this.getCartridgeId() + "',"
					+ "'" + this.getReagentLotId() + "',"
					+ "'" + this.getPcId() + "',"
					+ "'" + this.getProbeResultA() + "',"
					+ "'" + this.getProbeResultB() + "',"
					+ "'" + this.getProbeResultC() + "',"
					+ "'" + this.getProbeResultD() + "',"
					+ "'" + this.getProbeResultE() + "',"
					+ "'" + this.getProbeResultSPC() + "',"
					+ "'" + this.getProbeCtA() + "',"
					+ "'" + this.getProbeCtB() + "',"
					+ "'" + this.getProbeCtC() + "',"
					+ "'" + this.getProbeCtD() + "',"
					+ "'" + this.getProbeCtE() + "',"
					+ "'" + this.getProbeCtSPC() + "',"
					+ "'" + this.getProbeEndptA() + "',"
					+ "'" + this.getProbeEndptB() + "',"
					+ "'" + this.getProbeEndptC() + "',"
					+ "'" + this.getProbeEndptD() + "',"
					+ "'" + this.getProbeEndptE() + "',"
					+ "'" + this.getProbeEndptSPC() + "',"
					+ "'" + this.getOperatorId() + "',"
					+ "'" + this.getExpDate() + "'"
					+   ")";
		
		return query;
	}
}
