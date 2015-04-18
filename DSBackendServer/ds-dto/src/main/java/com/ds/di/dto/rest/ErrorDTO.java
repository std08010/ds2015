/**
 * 
 */
package com.ds.di.dto.rest;

/**
 * @author Altin Cipi
 *
 */
public class ErrorDTO
{
	private int statusCode;
	private String message;

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode()
	{
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}
}
