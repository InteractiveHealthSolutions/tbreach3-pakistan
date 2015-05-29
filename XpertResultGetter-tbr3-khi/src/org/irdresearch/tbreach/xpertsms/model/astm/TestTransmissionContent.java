package org.irdresearch.tbreach.xpertsms.model.astm;

import java.util.Date;

public class TestTransmissionContent {

	private String sampleId;
	private Date resultDate;
	private String mtbResult;
	private String rifResult;
	private String serverAddres;
	private int serverPort;
	
	/**
	 * @return the sampleId
	 */
	public String getSampleId() {
		return sampleId;
	}
	/**
	 * @param sampleId the sampleId to set
	 */
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	/**
	 * @return the resultDate
	 */
	public Date getResultDate() {
		return resultDate;
	}
	/**
	 * @param resultDate the resultDate to set
	 */
	public void setResultDate(Date resultDate) {
		this.resultDate = resultDate;
	}
	/**
	 * @return the mtbResult
	 */
	public String getMtbResult() {
		return mtbResult;
	}
	/**
	 * @param mtbResult the mtbResult to set
	 */
	public void setMtbResult(String mtbResult) {
		this.mtbResult = mtbResult;
	}
	/**
	 * @return the rifResult
	 */
	public String getRifResult() {
		return rifResult;
	}
	/**
	 * @param rifResult the rifResult to set
	 */
	public void setRifResult(String rifResult) {
		this.rifResult = rifResult;
	}
	/**
	 * @return the serverAddres
	 */
	public String getServerAddres() {
		return serverAddres;
	}
	/**
	 * @param serverAddres the serverAddres to set
	 */
	public void setServerAddres(String serverAddres) {
		this.serverAddres = serverAddres;
	}
	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}
	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	public String getUrl() {
		String url = null;
		
		return url;
	}
	
	public String getPostParams() {
		String postParams = null;
		
		return postParams;
	}
	
}
