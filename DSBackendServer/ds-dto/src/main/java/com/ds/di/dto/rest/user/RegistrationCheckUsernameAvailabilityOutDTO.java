/**
 * 
 */
package com.ds.di.dto.rest.user;

/**
 * @author Altin Cipi
 *
 */
public class RegistrationCheckUsernameAvailabilityOutDTO
{
	private boolean	available;
	private String reason;
	
	/**
	 * @return the available
	 */
	public boolean isAvailable()
	{
		return available;
	}
	/**
	 * @param available the available to set
	 */
	public void setAvailable(boolean available)
	{
		this.available = available;
	}
	/**
	 * @return the reason
	 */
	public String getReason()
	{
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason)
	{
		this.reason = reason;
	}
}
