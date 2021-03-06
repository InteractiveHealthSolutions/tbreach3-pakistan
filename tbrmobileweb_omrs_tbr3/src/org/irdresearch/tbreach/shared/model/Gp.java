package org.irdresearch.tbreach.shared.model;

// default package
// Generated Dec 21, 2010 3:45:59 PM by Hibernate Tools 3.4.0.Beta1

/**
 * Gp generated by hbm2java
 */
public class Gp implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4984315432840869187L;
	private String				gpid;
	private String				qualification1;
	private String				qualification2;
	private String				qualification3;
	private String				speciality1;
	private String				speciality2;
	private String				speciality3;
	private String				workplaceId1;
	private String				scheduleId1;
	private String				workplaceId2;
	private String				scheduleId2;
	private String				workplaceId3;
	private String				scheduleId3;

	public Gp()
	{
		// Not implemented
	}

	public Gp(String gpid)
	{
		this.gpid = gpid;
	}

	public Gp(String gpid, String qualification1, String qualification2, String qualification3, String speciality1, String speciality2,
			String speciality3, String workplaceId1, String scheduleId1, String workplaceId2, String scheduleId2, String workplaceId3,
			String scheduleId3)
	{
		this.gpid = gpid;
		this.qualification1 = qualification1;
		this.qualification2 = qualification2;
		this.qualification3 = qualification3;
		this.speciality1 = speciality1;
		this.speciality2 = speciality2;
		this.speciality3 = speciality3;
		this.workplaceId1 = workplaceId1;
		this.scheduleId1 = scheduleId1;
		this.workplaceId2 = workplaceId2;
		this.scheduleId2 = scheduleId2;
		this.workplaceId3 = workplaceId3;
		this.scheduleId3 = scheduleId3;
	}

	public String getGpid()
	{
		return this.gpid;
	}

	public void setGpid(String gpid)
	{
		this.gpid = gpid;
	}

	public String getQualification1()
	{
		return this.qualification1;
	}

	public void setQualification1(String qualification1)
	{
		this.qualification1 = qualification1;
	}

	public String getQualification2()
	{
		return this.qualification2;
	}

	public void setQualification2(String qualification2)
	{
		this.qualification2 = qualification2;
	}

	public String getQualification3()
	{
		return this.qualification3;
	}

	public void setQualification3(String qualification3)
	{
		this.qualification3 = qualification3;
	}

	public String getSpeciality1()
	{
		return this.speciality1;
	}

	public void setSpeciality1(String speciality1)
	{
		this.speciality1 = speciality1;
	}

	public String getSpeciality2()
	{
		return this.speciality2;
	}

	public void setSpeciality2(String speciality2)
	{
		this.speciality2 = speciality2;
	}

	public String getSpeciality3()
	{
		return this.speciality3;
	}

	public void setSpeciality3(String speciality3)
	{
		this.speciality3 = speciality3;
	}

	public String getWorkplaceId1()
	{
		return this.workplaceId1;
	}

	public void setWorkplaceId1(String workplaceId1)
	{
		this.workplaceId1 = workplaceId1;
	}

	public String getScheduleId1()
	{
		return this.scheduleId1;
	}

	public void setScheduleId1(String scheduleId1)
	{
		this.scheduleId1 = scheduleId1;
	}

	public String getWorkplaceId2()
	{
		return this.workplaceId2;
	}

	public void setWorkplaceId2(String workplaceId2)
	{
		this.workplaceId2 = workplaceId2;
	}

	public String getScheduleId2()
	{
		return this.scheduleId2;
	}

	public void setScheduleId2(String scheduleId2)
	{
		this.scheduleId2 = scheduleId2;
	}

	public String getWorkplaceId3()
	{
		return this.workplaceId3;
	}

	public void setWorkplaceId3(String workplaceId3)
	{
		this.workplaceId3 = workplaceId3;
	}

	public String getScheduleId3()
	{
		return this.scheduleId3;
	}

	public void setScheduleId3(String scheduleId3)
	{
		this.scheduleId3 = scheduleId3;
	}

	@Override
	public String toString()
	{
		return gpid + ", " + qualification1 + ", " + qualification2 + ", " + qualification3 + ", " + speciality1 + ", " + speciality2 + ", "
				+ speciality3 + ", " + workplaceId1 + ", " + scheduleId1 + ", " + workplaceId2 + ", " + scheduleId2 + ", " + workplaceId3 + ", "
				+ scheduleId3;
	}

}
