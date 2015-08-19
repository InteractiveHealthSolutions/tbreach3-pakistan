package com.ihsinformatics.tbr3fieldmonitoring.shared.model;

// Generated Jun 12, 2012 4:08:49 PM by Hibernate Tools 3.4.0.CR1

/**
 * UserMappingId generated by hbm2java
 */
public class UserMappingId implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1641729526825130994L;
	private String userId;
	private String locationId;

	public UserMappingId()
	{
	}

	public UserMappingId(String userId, String locationId)
	{
		this.userId = userId;
		this.locationId = locationId;
	}

	public String getUserId()
	{
		return this.userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getLocationId()
	{
		return this.locationId;
	}

	public void setLocationId(String locationId)
	{
		this.locationId = locationId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((locationId == null) ? 0 : locationId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserMappingId other = (UserMappingId) obj;
		if (locationId == null)
		{
			if (other.locationId != null)
				return false;
		}
		else if (!locationId.equals(other.locationId))
			return false;
		if (userId == null)
		{
			if (other.userId != null)
				return false;
		}
		else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return userId + ", " + locationId;
	}
}
