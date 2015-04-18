/**
 * 
 */
package com.acipi.evote.rest.dto.user;

import com.acipi.evote.rest.dto.AbstractOutDTO;

/**
 * @author Altin Cipi
 *
 */
public class UserProfileGetAllInfoOutDTO extends AbstractOutDTO
{
	private String	username;
	private String	avatarURL;
	private String	country;
	private String	countryFlagURL;

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * @return the avatarURL
	 */
	public String getAvatarURL()
	{
		return avatarURL;
	}

	/**
	 * @param avatarURL
	 *            the avatarURL to set
	 */
	public void setAvatarURL(String avatarURL)
	{
		this.avatarURL = avatarURL;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country)
	{
		this.country = country;
	}

	/**
	 * @return the countryFlagURL
	 */
	public String getCountryFlagURL()
	{
		return countryFlagURL;
	}

	/**
	 * @param countryFlagURL
	 *            the countryFlagURL to set
	 */
	public void setCountryFlagURL(String countryFlagURL)
	{
		this.countryFlagURL = countryFlagURL;
	}
}
