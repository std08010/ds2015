/**
 * 
 */
package com.acipi.evote.rest.dto.general;

import com.acipi.evote.rest.dto.AbstractOutDTO;

/**
 * @author Altin Cipi
 *
 */
public class CountryGetFlagURLOutDTO extends AbstractOutDTO
{
	private String	countryFlagURL;

	/**
	 * @return the countryFlagURL
	 */
	public String getCountryFlagURL()
	{
		return countryFlagURL;
	}

	/**
	 * @param countryFlagURL the countryFlagURL to set
	 */
	public void setCountryFlagURL(String countryFlagURL)
	{
		this.countryFlagURL = countryFlagURL;
	}
}
