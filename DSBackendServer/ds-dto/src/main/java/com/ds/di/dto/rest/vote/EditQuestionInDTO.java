/**
 * 
 */
package com.ds.di.dto.rest.vote;

import java.util.List;

/**
 * @author Altin Cipi
 *
 */
public class EditQuestionInDTO
{
	private String					username;
	private String					sessionToken;
	private Long					questionID;
	private String					questionText;
	private List<OptionSingleInDTO>	options;

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
	 * @return the questionText
	 */
	public String getQuestionText()
	{
		return questionText;
	}

	/**
	 * @param questionText
	 *            the questionText to set
	 */
	public void setQuestionText(String questionText)
	{
		this.questionText = questionText;
	}

	/**
	 * @return the options
	 */
	public List<OptionSingleInDTO> getOptions()
	{
		return options;
	}

	/**
	 * @param options
	 *            the options to set
	 */
	public void setOptions(List<OptionSingleInDTO> options)
	{
		this.options = options;
	}
}
