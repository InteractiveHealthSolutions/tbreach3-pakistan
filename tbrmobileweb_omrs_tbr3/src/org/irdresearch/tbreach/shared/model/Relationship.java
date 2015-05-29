package org.irdresearch.tbreach.shared.model;

// default package
// Generated Dec 21, 2010 3:45:59 PM by Hibernate Tools 3.4.0.Beta1

/**
 * Relationship generated by hbm2java
 */
public class Relationship implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3040127179853881943L;
	private RelationshipId		id;
	private String				relationship;

	public Relationship()
	{
		// Not implemented
	}

	public Relationship(RelationshipId id, String relationship)
	{
		this.id = id;
		this.relationship = relationship;
	}

	public RelationshipId getId()
	{
		return this.id;
	}

	public void setId(RelationshipId id)
	{
		this.id = id;
	}

	public String getRelationship()
	{
		return this.relationship;
	}

	public void setRelationship(String relationship)
	{
		this.relationship = relationship;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.id + ", " + this.relationship;
	}

}
