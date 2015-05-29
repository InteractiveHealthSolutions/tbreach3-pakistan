package org.irdresearch.tbreach.xpertsms.model;

public class XpertResultUploadMessage {

	protected String patientId;
	protected String sampleId;
	protected String mtbResult;
	protected String rifResult;
	//protected String mtbBurden;

	protected String probeResultA;
	protected String probeResultB;
	protected String probeResultC;
	protected String probeResultD;
	protected String probeResultE;
	protected String probeResultSPC;
	
	protected String probeCtA;
	protected String probeCtB;
	protected String probeCtC;
	protected String probeCtD;
	protected String probeCtE;
	protected String probeCtSPC;
	
	protected String probeEndptA;
	protected String probeEndptB;
	protected String probeEndptC;
	protected String probeEndptD;
	protected String probeEndptE;
	protected String probeEndptSPC;
	
	protected int retries;
	
	

	public XpertResultUploadMessage() {
		patientId = null;
		sampleId = null;
		mtbResult = null;
		rifResult = null;
		
		 probeResultA = null;
		 probeResultB = null;
		 probeResultC = null;
		 probeResultD = null;
		 probeResultE = null;
		 probeResultSPC = null;
		
		 probeCtA = null;
		 probeCtB = null;
		 probeCtC = null;
		 probeCtD = null;
		 probeCtE = null;
		 probeCtSPC = null;
		
		 probeEndptA = null;
		 probeEndptB = null;
		 probeEndptC = null;
		 probeEndptD = null;
		 probeEndptE = null;
		 probeEndptSPC = null;
		
	}


	/**
	 * @return the patientId
	 */
	public String getPatientId() {
		return patientId;
	}


	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}


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


	public String toPostParams() {
		return "";
	}


	/**
	 * @return the probeResultA
	 */
	public String getProbeResultA() {
		return probeResultA;
	}


	/**
	 * @param probeResultA the probeResultA to set
	 */
	public void setProbeResultA(String probeResultA) {
		this.probeResultA = probeResultA;
	}


	/**
	 * @return the probeResultB
	 */
	public String getProbeResultB() {
		return probeResultB;
	}


	/**
	 * @param probeResultB the probeResultB to set
	 */
	public void setProbeResultB(String probeResultB) {
		this.probeResultB = probeResultB;
	}


	/**
	 * @return the probeResultC
	 */
	public String getProbeResultC() {
		return probeResultC;
	}


	/**
	 * @param probeResultC the probeResultC to set
	 */
	public void setProbeResultC(String probeResultC) {
		this.probeResultC = probeResultC;
	}


	/**
	 * @return the probeResultD
	 */
	public String getProbeResultD() {
		return probeResultD;
	}


	/**
	 * @param probeResultD the probeResultD to set
	 */
	public void setProbeResultD(String probeResultD) {
		this.probeResultD = probeResultD;
	}


	/**
	 * @return the probeResultE
	 */
	public String getProbeResultE() {
		return probeResultE;
	}


	/**
	 * @param probeResultE the probeResultE to set
	 */
	public void setProbeResultE(String probeResultE) {
		this.probeResultE = probeResultE;
	}


	/**
	 * @return the probeResultSPC
	 */
	public String getProbeResultSPC() {
		return probeResultSPC;
	}


	/**
	 * @param probeResultSPC the probeResultSPC to set
	 */
	public void setProbeResultSPC(String probeResultSPC) {
		this.probeResultSPC = probeResultSPC;
	}


	/**
	 * @return the probeCtA
	 */
	public String getProbeCtA() {
		return probeCtA;
	}


	/**
	 * @param probeCtA the probeCtA to set
	 */
	public void setProbeCtA(String probeCtA) {
		this.probeCtA = probeCtA;
	}


	/**
	 * @return the probeCtB
	 */
	public String getProbeCtB() {
		return probeCtB;
	}


	/**
	 * @param probeCtB the probeCtB to set
	 */
	public void setProbeCtB(String probeCtB) {
		this.probeCtB = probeCtB;
	}


	/**
	 * @return the probeCtC
	 */
	public String getProbeCtC() {
		return probeCtC;
	}


	/**
	 * @param probeCtC the probeCtC to set
	 */
	public void setProbeCtC(String probeCtC) {
		this.probeCtC = probeCtC;
	}


	/**
	 * @return the probeCtD
	 */
	public String getProbeCtD() {
		return probeCtD;
	}


	/**
	 * @param probeCtD the probeCtD to set
	 */
	public void setProbeCtD(String probeCtD) {
		this.probeCtD = probeCtD;
	}


	/**
	 * @return the probeCtE
	 */
	public String getProbeCtE() {
		return probeCtE;
	}


	/**
	 * @param probeCtE the probeCtE to set
	 */
	public void setProbeCtE(String probeCtE) {
		this.probeCtE = probeCtE;
	}


	/**
	 * @return the probeCtSPC
	 */
	public String getProbeCtSPC() {
		return probeCtSPC;
	}


	/**
	 * @param probeCtSPC the probeCtSPC to set
	 */
	public void setProbeCtSPC(String probeCtSPC) {
		this.probeCtSPC = probeCtSPC;
	}


	/**
	 * @return the probeEndptA
	 */
	public String getProbeEndptA() {
		return probeEndptA;
	}


	/**
	 * @param probeEndptA the probeEndptA to set
	 */
	public void setProbeEndptA(String probeEndptA) {
		this.probeEndptA = probeEndptA;
	}


	/**
	 * @return the probeEndptB
	 */
	public String getProbeEndptB() {
		return probeEndptB;
	}


	/**
	 * @param probeEndptB the probeEndptB to set
	 */
	public void setProbeEndptB(String probeEndptB) {
		this.probeEndptB = probeEndptB;
	}


	/**
	 * @return the probeEndptC
	 */
	public String getProbeEndptC() {
		return probeEndptC;
	}


	/**
	 * @param probeEndptC the probeEndptC to set
	 */
	public void setProbeEndptC(String probeEndptC) {
		this.probeEndptC = probeEndptC;
	}


	/**
	 * @return the probeEndptD
	 */
	public String getProbeEndptD() {
		return probeEndptD;
	}


	/**
	 * @param probeEndptD the probeEndptD to set
	 */
	public void setProbeEndptD(String probeEndptD) {
		this.probeEndptD = probeEndptD;
	}


	/**
	 * @return the probeEndptE
	 */
	public String getProbeEndptE() {
		return probeEndptE;
	}


	/**
	 * @param probeEndptE the probeEndptE to set
	 */
	public void setProbeEndptE(String probeEndptE) {
		this.probeEndptE = probeEndptE;
	}


	/**
	 * @return the probeEndptSPC
	 */
	public String getProbeEndptSPC() {
		return probeEndptSPC;
	}


	/**
	 * @param probeEndptSPC the probeEndptSPC to set
	 */
	public void setProbeEndptSPC(String probeEndptSPC) {
		this.probeEndptSPC = probeEndptSPC;
	}
	
	/**
	 * @return the retries
	 */
	public int getRetries() {
		return retries;
	}

	/**
	 * @param retries the retries to set
	 */
	public void setRetries(int retries) {
		this.retries = retries;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	/**
	 * @return the mtbBurden
	 *//*
	public String getMtbBurden() {
		return mtbBurden;
	}


	*//**
	 * @param mtbBurden the mtbBurden to set
	 *//*
	public void setMtbBurden(String mtbBurden) {
		this.mtbBurden = mtbBurden;
	}*/


	public String toString() {
		return "XpertResultUploadMessage [mtbResult=" + mtbResult
				+ ", patientId=" + patientId + ", probeCtA=" + probeCtA
				+ ", probeCtB=" + probeCtB + ", probeCtC=" + probeCtC
				+ ", probeCtD=" + probeCtD + ", probeCtE=" + probeCtE
				+ ", probeCtSPC=" + probeCtSPC + ", probeEndptA=" + probeEndptA
				+ ", probeEndptB=" + probeEndptB + ", probeEndptC="
				+ probeEndptC + ", probeEndptD=" + probeEndptD
				+ ", probeEndptE=" + probeEndptE + ", probeEndptSPC="
				+ probeEndptSPC + ", probeResultA=" + probeResultA
				+ ", probeResultB=" + probeResultB + ", probeResultC="
				+ probeResultC + ", probeResultD=" + probeResultD
				+ ", probeResultE=" + probeResultE + ", probeResultSPC="
				+ probeResultSPC + ", retries=" + retries + ", rifResult="
				+ rifResult + ", sampleId=" + sampleId + "]";
	}
	
	public String toSMS() {
		String smsText = "";
		smsText += spaceIfNull(sampleId) + "^" + spaceIfNull(mtbResult) + "^" + spaceIfNull(rifResult) + "^" + spaceIfNull(probeResultA) +
			"^" + spaceIfNull(probeResultB) + "^" + spaceIfNull(probeResultC) + "^" + spaceIfNull(probeResultD) + "^" + spaceIfNull(probeResultE) +
			"^" + spaceIfNull(probeResultSPC) + "^" + spaceIfNull(probeCtA) + "^" + spaceIfNull(probeCtB) + "^" + spaceIfNull(probeCtC) +
			"^" + spaceIfNull(probeCtD) + "^" + spaceIfNull(probeCtE) + "^" + spaceIfNull(probeCtSPC) + "^" + spaceIfNull(probeEndptA) + 
			"^" + spaceIfNull(probeEndptB) + "^" + spaceIfNull(probeEndptB) + "^" + spaceIfNull(probeEndptC) + "^" + spaceIfNull(probeEndptD) +
			"^" + spaceIfNull(probeEndptE) + "^" + spaceIfNull(probeEndptSPC);
		
		
		return smsText;
	}
	
	
	
	public String spaceIfNull(String text) {
		 if(text==null)
			 return "";
		 
		 return text;
	}
	
	public String spaceIfNull(Boolean text) {
		 if(text==null)
			 return "";
		 
		 return text.toString();
	}
	
	public String toCSV() {
		String csv = null;
		return csv;
	}
	
	public String toSQLQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
