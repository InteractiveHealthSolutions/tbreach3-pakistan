/**
 * 
 */
package org.irdresearch.tbreach.shared.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author owais.hussain@irdresearch.org
 *
 */
public class ContactSputumResults implements Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 391735773233108197L;
	private int					sputumTestId;
	private String				patientId;
	private String				laboratoryId;
	private int					month;
	private int					irs;
	private Date				dateSubmitted;
	private Date				dateTested;
	private String				testedBy;
	private Boolean				isTestPending;
	private String				smearResult;
	private String				remarks;
	private String				otherRemarks;

	public ContactSputumResults()
	{
		// Not implemented
	}

	public ContactSputumResults(int sputumTestId, String patientId, int month)
	{
		this.sputumTestId = sputumTestId;
		this.patientId = patientId;
		this.month = month;
	}

	public ContactSputumResults(int sputumTestId, String patientId, String laboratoryId, int irs, int month, Date dateSubmitted, Date dateTested,
			String testedBy, Boolean isTestPending, String smearResult, String remarks, String otherRemarks)
	{
		this.sputumTestId = sputumTestId;
		this.patientId = patientId;
		this.laboratoryId = laboratoryId;
		this.irs = irs;
		this.month = month;
		this.dateSubmitted = dateSubmitted;
		this.dateTested = dateTested;
		this.testedBy = testedBy;
		this.isTestPending = isTestPending;
		this.smearResult = smearResult;
		this.remarks = remarks;
		this.otherRemarks = otherRemarks;
	}

	public int getSputumTestId()
	{
		return this.sputumTestId;
	}

	public void setSputumTestId(int sputumTestId)
	{
		this.sputumTestId = sputumTestId;
	}

	public String getPatientId()
	{
		return this.patientId;
	}

	public void setPatientId(String patientId)
	{
		this.patientId = patientId;
	}

	public String getLaboratoryId()
	{
		return this.laboratoryId;
	}

	public void setLaboratoryId(String laboratoryId)
	{
		this.laboratoryId = laboratoryId;
	}

	public int getIrs()
	{
		return irs;
	}

	public void setIrs(int irs)
	{
		this.irs = irs;
	}

	public int getMonth()
	{
		return this.month;
	}

	public void setMonth(int month)
	{
		this.month = month;
	}

	public Date getDateSubmitted()
	{
		return this.dateSubmitted;
	}

	public void setDateSubmitted(Date dateSubmitted)
	{
		this.dateSubmitted = dateSubmitted;
	}

	public Date getDateTested()
	{
		return this.dateTested;
	}

	public void setDateTested(Date dateTested)
	{
		this.dateTested = dateTested;
	}

	public String getTestedBy()
	{
		return this.testedBy;
	}

	public void setTestedBy(String testedBy)
	{
		this.testedBy = testedBy;
	}

	public Boolean getIsTestPending()
	{
		return this.isTestPending;
	}

	public void setIsTestPending(Boolean isTestPending)
	{
		this.isTestPending = isTestPending;
	}

	public String getSmearResult()
	{
		return this.smearResult;
	}

	public void setSmearResult(String smearResult)
	{
		this.smearResult = smearResult;
	}

	public String getRemarks()
	{
		return this.remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}

	public String getOtherRemarks()
	{
		return this.otherRemarks;
	}

	public void setOtherRemarks(String otherRemarks)
	{
		this.otherRemarks = otherRemarks;
	}

	@Override
	public String toString()
	{
		return sputumTestId + ", " + patientId + ", " + laboratoryId + ", " + irs + ", " + month + ", " + dateSubmitted + ", " + dateTested + ", "
		+ testedBy + ", " + isTestPending + ", " + smearResult + ", " + remarks + ", " + otherRemarks;
	}
}
