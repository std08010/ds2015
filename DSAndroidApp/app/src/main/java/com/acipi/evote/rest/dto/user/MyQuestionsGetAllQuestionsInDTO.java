/**
 * 
 */
package com.acipi.evote.rest.dto.user;

import com.acipi.evote.rest.dto.AbstractInDTO;

/**
 * @author Altin Cipi
 *
 */
public class MyQuestionsGetAllQuestionsInDTO extends AbstractInDTO
{
	private String	username;
	private String	sessionToken;

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
}
