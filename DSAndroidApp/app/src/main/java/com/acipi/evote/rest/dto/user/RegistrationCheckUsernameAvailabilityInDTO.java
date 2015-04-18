/**
 * 
 */
package com.acipi.evote.rest.dto.user;

import com.acipi.evote.rest.dto.AbstractInDTO;

/**
 * @author Altin Cipi
 *
 */
public class RegistrationCheckUsernameAvailabilityInDTO extends AbstractInDTO
{
	private String	username;

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
}
