
package com.ihsinformatics.tbr3.fieldmonitoring.shared.model;

// Generated Aug 28, 2012 5:36:51 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * LabTest generated by hbm2java
 */
public class LabTest implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 893429510584311772L;
	private Integer				testNo;
	private String				patientId;
	private String				laboratoryId;
	private String				testType;
	private Date				orderedOn;
	private String				orderedBy;
	private Date				testedOn;
	private String				testedBy;
	private String				testResult;
	private String				otherDetail;
	private String				conclusion;
	private String				description;
	private String				error;

	public LabTest ()
	{
	}

	public LabTest (String patientId, String laboratoryId, String testType)
	{
		this.patientId = patientId;
		this.laboratoryId = laboratoryId;
		this.testType = testType;
	}

	public LabTest (String patientId, String laboratoryId, String testType, Date orderedOn, String orderedBy, Date testedOn, String testedBy, String testResult, String otherDetail, String conclusion,
			String description, String error)
	{
		this.patientId = patientId;
		this.laboratoryId = laboratoryId;
		this.testType = testType;
		this.orderedOn = orderedOn;
		this.orderedBy = orderedBy;
		this.testedOn = testedOn;
		this.testedBy = testedBy;
		this.testResult = testResult;
		this.otherDetail = otherDetail;
		this.conclusion = conclusion;
		this.description = description;
		this.error = error;
	}

	public Integer getTestNo ()
	{
		return this.testNo;
	}

	public void setTestNo (Integer testNo)
	{
		this.testNo = testNo;
	}

	public String getPatientId ()
	{
		return this.patientId;
	}

	public void setPatientId (String patientId)
	{
		this.patientId = patientId;
	}

	public String getLaboratoryId ()
	{
		return this.laboratoryId;
	}

	public void setLaboratoryId (String laboratoryId)
	{
		this.laboratoryId = laboratoryId;
	}

	public String getTestType ()
	{
		return this.testType;
	}

	public void setTestType (String testType)
	{
		this.testType = testType;
	}

	public Date getOrderedOn ()
	{
		return this.orderedOn;
	}

	public void setOrderedOn (Date orderedOn)
	{
		this.orderedOn = orderedOn;
	}

	public String getOrderedBy ()
	{
		return this.orderedBy;
	}

	public void setOrderedBy (String orderedBy)
	{
		this.orderedBy = orderedBy;
	}

	public Date getTestedOn ()
	{
		return this.testedOn;
	}

	public void setTestedOn (Date testedOn)
	{
		this.testedOn = testedOn;
	}

	public String getTestedBy ()
	{
		return this.testedBy;
	}

	public void setTestedBy (String testedBy)
	{
		this.testedBy = testedBy;
	}

	public String getTestResult ()
	{
		return this.testResult;
	}

	public void setTestResult (String testResult)
	{
		this.testResult = testResult;
	}

	public String getOtherDetail ()
	{
		return this.otherDetail;
	}

	public void setOtherDetail (String otherDetail)
	{
		this.otherDetail = otherDetail;
	}

	public String getConclusion ()
	{
		return this.conclusion;
	}

	public void setConclusion (String conclusion)
	{
		this.conclusion = conclusion;
	}

	public String getDescription ()
	{
		return this.description;
	}

	public void setDescription (String description)
	{
		this.description = description;
	}

	public String getError ()
	{
		return this.error;
	}

	public void setError (String error)
	{
		this.error = error;
	}

	public String toString ()
	{
		return testNo + ", " + patientId + ", " + laboratoryId + ", " + testType + ", " + orderedOn + ", " + orderedBy + ", " + testedOn + ", " + testedBy + ", " + testResult + ", " + otherDetail
				+ ", " + conclusion + ", " + description + ", " + error;
	}
}
