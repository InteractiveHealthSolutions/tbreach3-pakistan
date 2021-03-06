package org.irdresearch.tbreach.shared.model;

// default package
// Generated Dec 21, 2010 3:45:59 PM by Hibernate Tools 3.4.0.Beta1

/**
 * SetupCityId generated by hbm2java
 */
public class SetupCityId implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5103672041648070763L;
	private int					countryId;
	private int					cityId;

	public SetupCityId()
	{
		// Not implemented
	}

	public SetupCityId(int countryId, int cityId)
	{
		this.countryId = countryId;
		this.cityId = cityId;
	}

	public int getCountryId()
	{
		return this.countryId;
	}

	public void setCountryId(int countryId)
	{
		this.countryId = countryId;
	}

	public int getCityId()
	{
		return this.cityId;
	}

	public void setCityId(int cityId)
	{
		this.cityId = cityId;
	}

	@Override
	public String toString()
	{
		return countryId + ", " + cityId;
	}

}
