/**
 * 
 */
package com.ds.di.dto.rest.general;

import java.util.List;

/**
 * @author Altin Cipi
 *
 */
public class CountryAllOutDTO
{
	private List<String> countries;

	/**
	 * @return the countries
	 */
	public List<String> getCountries()
	{
		return countries;
	}

	/**
	 * @param countries the countries to set
	 */
	public void setCountries(List<String> countries)
	{
		this.countries = countries;
	}
}
