
package com.ihsinformatics.tbr3.fieldmonitoring.shared.model;

// Generated Jun 12, 2012 4:08:49 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * Sms generated by hbm2java
 */
public class Sms implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4867821577165595631L;
	private Integer				smsId;
	private String				targetNumber;
	private String				messageText;
	private Date				dateDue;
	private Date				dateSent;
	private String				status;
	private String				errorMessage;
	private String				failureCause;

	public Sms ()
	{
	}

	public Sms (String targetNumber, Date dateDue)
	{
		this.targetNumber = targetNumber;
		this.dateDue = dateDue;
	}

	public Sms (String targetNumber, String messageText, Date dateDue, Date dateSent, String status, String errorMessage, String failureCause)
	{
		this.targetNumber = targetNumber;
		this.messageText = messageText;
		this.dateDue = dateDue;
		this.dateSent = dateSent;
		this.status = status;
		this.errorMessage = errorMessage;
		this.failureCause = failureCause;
	}

	public Integer getSmsId ()
	{
		return this.smsId;
	}

	public void setSmsId (Integer smsId)
	{
		this.smsId = smsId;
	}

	public String getTargetNumber ()
	{
		return this.targetNumber;
	}

	public void setTargetNumber (String targetNumber)
	{
		this.targetNumber = targetNumber;
	}

	public String getMessageText ()
	{
		return this.messageText;
	}

	public void setMessageText (String messageText)
	{
		this.messageText = messageText;
	}

	public Date getDateDue ()
	{
		return this.dateDue;
	}

	public void setDateDue (Date dateDue)
	{
		this.dateDue = dateDue;
	}

	public Date getDateSent ()
	{
		return this.dateSent;
	}

	public void setDateSent (Date dateSent)
	{
		this.dateSent = dateSent;
	}

	public String getStatus ()
	{
		return this.status;
	}

	public void setStatus (String status)
	{
		this.status = status;
	}

	public String getErrorMessage ()
	{
		return this.errorMessage;
	}

	public void setErrorMessage (String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getFailureCause ()
	{
		return this.failureCause;
	}

	public void setFailureCause (String failureCause)
	{
		this.failureCause = failureCause;
	}

	@Override
	public String toString ()
	{
		return smsId + ", " + targetNumber + ", " + messageText + ", " + dateDue + ", " + dateSent + ", " + status + ", " + errorMessage + ", " + failureCause;
	}

}