/**
 * 
 */
package com.ds.di.dto.rest.user;

/**
 * @author Altin Cipi
 *
 */
public class UserProfileUnsubscribeInDTO
{
	private String	username;
	private String	sessionToken;
	private String	profileUsername;

	/**
	 * @return the profileUsername
	 */
	public String getProfileUsername()
	{
		return profileUsername;
	}

	/**
	 * @param profileUsername the profileUsername to set
	 */
	public void setProfileUsername(String profileUsername)
	{
		this.profileUsername = profileUsername;
	}

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * @return the sessionToken
	 */
	public String getSessionToken()
	{
		return sessionToken;
	}

	/**
	 * @param sessionToken the sessionToken to set
	 */
	public void setSessionToken(String sessionToken)
	{
		this.sessionToken = sessionToken;
	}
}
