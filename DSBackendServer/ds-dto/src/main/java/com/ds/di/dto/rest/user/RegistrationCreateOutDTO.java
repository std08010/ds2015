/**
 * 
 */
package com.ds.di.dto.rest.user;

/**
 * @author Altin Cipi
 *
 */
public class RegistrationCreateOutDTO
{
	private String	sessionToken;
	private String	countryURL;
	private String	avatarURL;

	/**
	 * @return the sessionToken
	 */
	public String getSessionToken()
	{
		return sessionToken;
	}

	/**
	 * @param sessionToken
	 *            the sessionToken to set
	 */
	public void setSessionToken(String sessionToken)
	{
		this.sessionToken = sessionToken;
	}

	/**
	 * @return the countryURL
	 */
	public String getCountryURL()
	{
		return countryURL;
	}

	/**
	 * @param countryURL
	 *            the countryURL to set
	 */
	public void setCountryURL(String countryURL)
	{
		this.countryURL = countryURL;
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
}
