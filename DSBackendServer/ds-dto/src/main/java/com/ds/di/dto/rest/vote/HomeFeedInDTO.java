/**
 * 
 */
package com.ds.di.dto.rest.vote;

/**
 * @author Altin Cipi
 *
 */
public class HomeFeedInDTO
{
	private String	username;
	private String	sessionToken;
	private String	country;
	private String	untilDate;
	private Integer	limit;
	private Long	lastID;

	/**
	 * @return the limit
	 */
	public Integer getLimit()
	{
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(Integer limit)
	{
		this.limit = limit;
	}

	/**
	 * @return the lastID
	 */
	public Long getLastID()
	{
		return lastID;
	}

	/**
	 * @param lastID
	 *            the lastID to set
	 */
	public void setLastID(Long lastID)
	{
		this.lastID = lastID;
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
	 * @return the untilDate
	 */
	public String getUntilDate()
	{
		return untilDate;
	}

	/**
	 * @param untilDate
	 *            the untilDate to set
	 */
	public void setUntilDate(String untilDate)
	{
		this.untilDate = untilDate;
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
