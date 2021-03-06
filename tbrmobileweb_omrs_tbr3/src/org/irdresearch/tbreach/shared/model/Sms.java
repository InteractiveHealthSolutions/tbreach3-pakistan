package org.irdresearch.tbreach.shared.model;

import java.util.Date;

/**
 * Sms generated by hbm2java
 */
public class Sms implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3313510261969218335L;
	private Integer				smsid;
	private String				targetNumber;
	private String				messageText;
	private Date				dueDateTime;
	private Date				sentDateTime;
	private String				status;
	private String				errorMessage;
	private String				failureCause;

	public Sms()
	{
		// Not implemented
	}

	public Sms(String targetNumber, Date dueDateTime)
	{
		this.targetNumber = targetNumber;
		this.dueDateTime = dueDateTime;
	}

	public Sms(String targetNumber, String messageText, Date dueDateTime, Date sentDateTime, String status, String errorMessage, String failurCause)
	{
		this.targetNumber = targetNumber;
		this.messageText = messageText;
		this.dueDateTime = dueDateTime;
		this.sentDateTime = sentDateTime;
		this.status = status;
		this.errorMessage = errorMessage;
		this.failureCause = failurCause;
	}

	public Integer getSmsid()
	{
		return this.smsid;
	}

	public void setSmsid(Integer smsid)
	{
		this.smsid = smsid;
	}

	public String getTargetNumber()
	{
		return this.targetNumber;
	}

	public void setTargetNumber(String targetNumber)
	{
		this.targetNumber = targetNumber;
	}

	public String getMessageText()
	{
		return this.messageText;
	}

	public void setMessageText(String messageText)
	{
		this.messageText = messageText;
	}

	public Date getDueDateTime()
	{
		return this.dueDateTime;
	}

	public void setDueDateTime(Date dueDateTime)
	{
		this.dueDateTime = dueDateTime;
	}

	public Date getSentDateTime()
	{
		return this.sentDateTime;
	}

	public void setSentDateTime(Date sentDateTime)
	{
		this.sentDateTime = sentDateTime;
	}

	public String getStatus()
	{
		return this.status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getFailureCause()
	{
		return failureCause;
	}

	public void setFailureCause(String failureCause)
	{
		this.failureCause = failureCause;
	}

	@Override
	public String toString()
	{
		return smsid + ", " + targetNumber + ", " + messageText + ", " + dueDateTime + ", " + sentDateTime + ", " + status;
	}

}
