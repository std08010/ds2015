/**
 * 
 */
package com.acipi.evote.rest.dto.vote;

import com.acipi.evote.rest.dto.AbstractInDTO;

import java.util.List;

/**
 * @author Altin Cipi
 *
 */
public class AddQuestionInDTO extends AbstractInDTO
{
	private String			username;
	private String			sessionToken;
	private String			questionText;
	private List<String>	options;

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
	 * @return the questionText
	 */
	public String getQuestionText()
	{
		return questionText;
	}

	/**
	 * @param questionText the questionText to set
	 */
	public void setQuestionText(String questionText)
	{
		this.questionText = questionText;
	}

	/**
	 * @return the options
	 */
	public List<String> getOptions()
	{
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(List<String> options)
	{
		this.options = options;
	}
}
