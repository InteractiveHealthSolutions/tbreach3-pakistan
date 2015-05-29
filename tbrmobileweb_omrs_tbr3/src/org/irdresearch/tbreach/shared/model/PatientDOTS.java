package org.irdresearch.tbreach.shared.model;

// Generated Dec 1, 2011 12:41:53 PM by Hibernate Tools 3.4.0.Beta1

/**
 * @author Owais
 * 
 */
public class PatientDOTS implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6520294959037106864L;
	private String				mrNo;
	private String				dotsNo;

	public PatientDOTS()
	{
		// Not implemented
	}

	public PatientDOTS(String mrNo, String dotsNo)
	{
		this.mrNo = mrNo;
		this.dotsNo = dotsNo;
	}

	public String getMrNo()
	{
		return mrNo;
	}

	public void setMrNo(String mrNo)
	{
		this.mrNo = mrNo;
	}

	public String getDotsNo()
	{
		return dotsNo;
	}

	public void setDotsNo(String dotsNo)
	{
		this.dotsNo = dotsNo;
	}

	@Override
	public String toString()
	{
		return mrNo + ", " + dotsNo;
	}

}
