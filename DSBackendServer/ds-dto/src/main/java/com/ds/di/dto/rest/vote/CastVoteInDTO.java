/**
 * 
 */
package com.ds.di.dto.rest.vote;

/**
 * @author Altin Cipi
 *
 */
public class CastVoteInDTO
{
	private String	username;
	private String	sessionToken;
	private Long	questionID;
	private Long	optionID;

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

	/**
	 * @return the questionID
	 */
	public Long getQuestionID()
	{
		return questionID;
	}

	/**
	 * @param questionID
	 *            the questionID to set
	 */
	public void setQuestionID(Long questionID)
	{
		this.questionID = questionID;
	}

	/**
	 * @return the optionID
	 */
	public Long getOptionID()
	{
		return optionID;
	}

	/**
	 * @param optionID
	 *            the optionID to set
	 */
	public void setOptionID(Long optionID)
	{
		this.optionID = optionID;
	}
}
