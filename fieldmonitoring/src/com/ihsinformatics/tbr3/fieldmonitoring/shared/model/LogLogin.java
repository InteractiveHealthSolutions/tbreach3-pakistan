
package com.ihsinformatics.tbr3.fieldmonitoring.shared.model;

// Generated Jun 12, 2012 4:08:49 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * LogLogin generated by hbm2java
 */
public class LogLogin implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5277266461090323509L;
	private Integer				loginNo;
	private String				userName;
	private Date				dateLogin;
	private Date				dateLogout;
	private String				description;

	public LogLogin ()
	{
	}

	public LogLogin (String userName)
	{
		this.userName = userName;
	}

	public LogLogin (String userName, Date dateLogin, Date dateLogout, String description)
	{
		this.userName = userName;
		this.dateLogin = dateLogin;
		this.dateLogout = dateLogout;
		this.description = description;
	}

	public Integer getLoginNo ()
	{
		return this.loginNo;
	}

	public void setLoginNo (Integer loginNo)
	{
		this.loginNo = loginNo;
	}

	public String getUserName ()
	{
		return this.userName;
	}

	public void setUserName (String userName)
	{
		this.userName = userName;
	}

	public Date getDateLogin ()
	{
		return this.dateLogin;
	}

	public void setDateLogin (Date dateLogin)
	{
		this.dateLogin = dateLogin;
	}

	public Date getDateLogout ()
	{
		return this.dateLogout;
	}

	public void setDateLogout (Date dateLogout)
	{
		this.dateLogout = dateLogout;
	}

	public String getDescription ()
	{
		return this.description;
	}

	public void setDescription (String description)
	{
		this.description = description;
	}

	@Override
	public String toString ()
	{
		return loginNo + ", " + userName + ", " + dateLogin + ", " + dateLogout + ", " + description;
	}

}
