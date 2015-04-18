/**
 * 
 */
package com.ds.di.model.general;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ds.di.model.BaseEntity;

/**
 * @author Altin Cipi
 *
 */
@Entity
@Table(name = "continent")
public class Continent extends BaseEntity
{
	private static final long	serialVersionUID	= 8358341152412062382L;

	@Column(name = "name", nullable = false, unique = true)
	private String				name;

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}
