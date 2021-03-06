
package com.ihsinformatics.tbr3.fieldmonitoring.shared.model;

// Generated Jun 12, 2012 4:08:49 PM by Hibernate Tools 3.4.0.CR1

/**
 * DefaultsId generated by hbm2java
 */
public class DefaultsId implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1466285806137805900L;
	private String				definitionType;
	private String				defaultDefinitionKey;

	public DefaultsId ()
	{
	}

	public DefaultsId (String definitionType, String defaultDefinitionKey)
	{
		this.definitionType = definitionType;
		this.defaultDefinitionKey = defaultDefinitionKey;
	}

	public String getDefinitionType ()
	{
		return this.definitionType;
	}

	public void setDefinitionType (String definitionType)
	{
		this.definitionType = definitionType;
	}

	public String getDefaultDefinitionKey ()
	{
		return this.defaultDefinitionKey;
	}

	public void setDefaultDefinitionKey (String defaultDefinitionKey)
	{
		this.defaultDefinitionKey = defaultDefinitionKey;
	}

	public boolean equals (Object other)
	{
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DefaultsId))
			return false;
		DefaultsId castOther = (DefaultsId) other;

		return ((this.getDefinitionType () == castOther.getDefinitionType ()) || (this.getDefinitionType () != null && castOther.getDefinitionType () != null && this.getDefinitionType ().equals (
				castOther.getDefinitionType ())))
				&& ((this.getDefaultDefinitionKey () == castOther.getDefaultDefinitionKey ()) || (this.getDefaultDefinitionKey () != null && castOther.getDefaultDefinitionKey () != null && this
						.getDefaultDefinitionKey ().equals (castOther.getDefaultDefinitionKey ())));
	}

	public int hashCode ()
	{
		int result = 17;

		result = 37 * result + (getDefinitionType () == null ? 0 : this.getDefinitionType ().hashCode ());
		result = 37 * result + (getDefaultDefinitionKey () == null ? 0 : this.getDefaultDefinitionKey ().hashCode ());
		return result;
	}

	@Override
	public String toString ()
	{
		return definitionType + ", " + defaultDefinitionKey;
	}

}
