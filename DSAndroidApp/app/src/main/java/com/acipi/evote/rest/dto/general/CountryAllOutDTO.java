/**
 * 
 */
package com.acipi.evote.rest.dto.general;

import com.acipi.evote.rest.dto.AbstractOutDTO;

import java.util.List;

/**
 * @author Altin Cipi
 *
 */
public class CountryAllOutDTO extends AbstractOutDTO
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
