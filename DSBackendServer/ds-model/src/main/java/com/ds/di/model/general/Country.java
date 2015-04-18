/**
 * 
 */
package com.ds.di.model.general;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ds.di.model.BaseEntity;

/**
 * @author Altin Cipi
 *
 */

@Entity
@Table(name = "country")
public class Country extends BaseEntity
{
	private static final long	serialVersionUID	= 1042280461583903217L;

	@Column(name = "name", nullable = false, unique = true)
	private String				name;

	@Column(name = "flag_url")
	private String				flagURL;

	@ManyToOne
	@JoinColumn(name = "continent_fk")
	private Continent			continent;

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

	/**
	 * @return the flagURL
	 */
	public String getFlagURL()
	{
		return flagURL;
	}

	/**
	 * @param flagURL
	 *            the flagURL to set
	 */
	public void setFlagURL(String flagURL)
	{
		this.flagURL = flagURL;
	}

	/**
	 * @return the continent
	 */
	public Continent getContinent()
	{
		return continent;
	}

	/**
	 * @param continent
	 *            the continent to set
	 */
	public void setContinent(Continent continent)
	{
		this.continent = continent;
	}
}
