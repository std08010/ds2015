/**
 * 
 */
package com.ds.di.dto.rest.vote;

import java.util.List;

/**
 * @author Altin Cipi
 *
 */
public class QuestionFeedSingleOutDTO
{
	private QuestionSingleOutDTO		question;
	private Boolean						answered;
	private List<OptionSingleOutDTO>	options;
	private String						username;
	private String						avatarURL;

	/**
	 * @return the question
	 */
	public QuestionSingleOutDTO getQuestion()
	{
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(QuestionSingleOutDTO question)
	{
		this.question = question;
	}

	/**
	 * @return the options
	 */
	public List<OptionSingleOutDTO> getOptions()
	{
		return options;
	}

	/**
	 * @param options
	 *            the options to set
	 */
	public void setOptions(List<OptionSingleOutDTO> options)
	{
		this.options = options;
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

	/**
	 * @return the answered
	 */
	public Boolean getAnswered()
	{
		return answered;
	}

	/**
	 * @param answered the answered to set
	 */
	public void setAnswered(Boolean answered)
	{
		this.answered = answered;
	}
}
