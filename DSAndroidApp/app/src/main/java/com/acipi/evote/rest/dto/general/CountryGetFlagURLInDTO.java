/**
 * 
 */
package com.acipi.evote.rest.dto.general;

import com.acipi.evote.rest.dto.AbstractInDTO;

/**
 * @author Altin Cipi
 *
 */
public class CountryGetFlagURLInDTO extends AbstractInDTO
{
	private String	countryName;

	/**
	 * @return the countryName
	 */
	public String getCountryName()
	{
		return countryName;
	}

	/**
	 * @param countryName
	 *            the countryName to set
	 */
	public void setCountryName(String countryName)
	{
		this.countryName = countryName;
	}
}
