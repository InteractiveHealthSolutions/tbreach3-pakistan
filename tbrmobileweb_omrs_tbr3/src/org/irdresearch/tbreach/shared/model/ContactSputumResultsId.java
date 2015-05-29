/**
 * 
 */
package org.irdresearch.tbreach.shared.model;

import java.io.Serializable;

/**
 * @author owais.hussain@irdresearch.org
 *
 */
public class ContactSputumResultsId implements Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1076175898574843667L;
	private String				patientId;
	private int					sputumTestId;

	public ContactSputumResultsId()
	{
		// Not implemented
	}

	public ContactSputumResultsId(String patientId, int sputumTestId)
	{
		this.patientId = patientId;
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

	public int getSputumTestId()
	{
		return this.sputumTestId;
	}

	public void setSputumTestId(int sputumTestId)
	{
		this.sputumTestId = sputumTestId;
	}

	@Override
	public String toString()
	{
		return patientId + ", " + sputumTestId;
	}

}
