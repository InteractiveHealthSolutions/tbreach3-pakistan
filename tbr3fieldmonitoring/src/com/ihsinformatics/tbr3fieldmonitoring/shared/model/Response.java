package com.ihsinformatics.tbr3fieldmonitoring.shared.model;

import java.util.Date;

public class Response implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7119504509243983023L;
	private int responseId;
	private String responderReferenceTable;
	private String responderReferenceColumn;
	private String responderId;
	private String responderCellNumber;
	private Date recievedDate;
	private String text;
	private String type;
	private String referenceNumber;
	private Date dateResponseProcessed;

	public Response()
	{
	}

	public int getResponseId()
	{
		return responseId;
	}

	public void setResponseId(int responseId)
	{
		this.responseId = responseId;
	}

	public String getResponderReferenceTable()
	{
		return responderReferenceTable;
	}

	public void setResponderReferenceTable(String responderReferenceTable)
	{
		this.responderReferenceTable = responderReferenceTable;
	}

	public String getResponderReferenceColumn()
	{
		return responderReferenceColumn;
	}

	public void setResponderReferenceColumn(String responderReferenceColumn)
	{
		this.responderReferenceColumn = responderReferenceColumn;
	}

	public String getResponderId()
	{
		return responderId;
	}

	public void setResponderId(String responderId)
	{
		this.responderId = responderId;
	}

	public String getResponderCellNumber()
	{
		return responderCellNumber;
	}

	public void setResponderCellNumber(String responderCellNumber)
	{
		this.responderCellNumber = responderCellNumber;
	}

	public Date getRecievedDate()
	{
		return recievedDate;
	}

	public void setRecievedDate(Date recievedDate)
	{
		this.recievedDate = recievedDate;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public String getReferenceNumber()
	{
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber)
	{
		this.referenceNumber = referenceNumber;
	}

	public void setDateResponseProcessed(Date dateResponseProcessed)
	{
		this.dateResponseProcessed = dateResponseProcessed;
	}

	public Date getDateResponseProcessed()
	{
		return dateResponseProcessed;
	}
}
