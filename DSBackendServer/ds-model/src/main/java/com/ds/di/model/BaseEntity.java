package com.ds.di.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

/**
 * @author Altin Cipi
 *
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable
{
	private static final long	serialVersionUID	= -6023719927445526379L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", insertable = false, updatable = false, nullable = false)
	private Long				ID;

	@Column(name = "code")
	private String				code;

	@Column(name = "descr")
	private String				descr;

	@Column(name = "comment", columnDefinition = "TEXT")
	private String				comment;

	@Version
	@Column(name = "version_number", columnDefinition = "bigint default 0")
	private Long				versionNumber;

	@Column(name = "active")
	private Boolean				active				= Boolean.TRUE;

	@Column(name = "inserted_at", insertable = true, updatable = false, nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime			insertedAt;

	@Column(name = "updated_at", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime			updatedAt;

	public Long getID()
	{
		return ID;
	}

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code != null ? code.trim() : code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

	/**
	 * @return the descr
	 */
	public String getDescr()
	{
		return descr != null ? StringUtils.trimTrailingWhitespace(descr) : descr;
	}

	/**
	 * @param descr
	 *            the descr to set
	 */
	public void setDescr(String descr)
	{
		this.descr = descr;
	}

	/**
	 * @return the comment
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * @return the versionNumber
	 */
	public Long getVersionNumber()
	{
		return versionNumber;
	}

	/**
	 * @param versionNumber
	 *            the versionNumber to set
	 */
	public void setVersionNumber(Long versionNumber)
	{
		this.versionNumber = versionNumber;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive()
	{
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(Boolean active)
	{
		this.active = active;
	}

	/**
	 * @return the insertedAt
	 */
	public DateTime getInsertedAt()
	{
		return insertedAt;
	}

	/**
	 * @param insertedAt
	 *            the insertedAt to set
	 */
	public void setInsertedAt(DateTime insertedAt)
	{
		this.insertedAt = insertedAt;
	}

	/**
	 * @return the updatedAt
	 */
	public DateTime getUpdatedAt()
	{
		return updatedAt;
	}

	/**
	 * @param updatedAt
	 *            the updatedAt to set
	 */
	public void setUpdatedAt(DateTime updatedAt)
	{
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString()
	{
		return getID() + " - " + this.getDescr();
	}
}
