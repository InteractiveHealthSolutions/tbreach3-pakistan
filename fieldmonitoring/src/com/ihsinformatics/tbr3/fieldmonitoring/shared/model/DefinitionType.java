
package com.ihsinformatics.tbr3.fieldmonitoring.shared.model;

// Generated Jun 12, 2012 4:08:49 PM by Hibernate Tools 3.4.0.CR1

/**
 * DefinitionType generated by hbm2java
 */
public class DefinitionType implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6829128759980205045L;
	private String				definitionType;

	public DefinitionType ()
	{
	}

	public DefinitionType (String definitionType)
	{
		this.definitionType = definitionType;
	}

	public String getDefinitionType ()
	{
		return this.definitionType;
	}

	public void setDefinitionType (String definitionType)
	{
		this.definitionType = definitionType;
	}

	@Override
	public String toString ()
	{
		return definitionType;
	}

}
