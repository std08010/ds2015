/**
 * 
 */
package com.ds.di.dto.rest.user;

import java.util.List;

import com.ds.di.dto.rest.vote.QuestionFeedSingleOutDTO;

/**
 * @author Altin Cipi
 *
 */
public class UserProfileGetAllInfoOutDTO
{
	private String							username;
	private String							avatarURL;
	private String							country;
	private String							countryFlagURL;
	private Long							allQuestions;
	private List<QuestionFeedSingleOutDTO>	questions;

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
	 * @return the countryFlagURL
	 */
	public String getCountryFlagURL()
	{
		return countryFlagURL;
	}

	/**
	 * @param countryFlagURL
	 *            the countryFlagURL to set
	 */
	public void setCountryFlagURL(String countryFlagURL)
	{
		this.countryFlagURL = countryFlagURL;
	}

	/**
	 * @return the allQuestions
	 */
	public Long getAllQuestions()
	{
		return allQuestions;
	}

	/**
	 * @param allQuestions
	 *            the allQuestions to set
	 */
	public void setAllQuestions(Long allQuestions)
	{
		this.allQuestions = allQuestions;
	}

	/**
	 * @return the questions
	 */
	public List<QuestionFeedSingleOutDTO> getQuestions()
	{
		return questions;
	}

	/**
	 * @param questions
	 *            the questions to set
	 */
	public void setQuestions(List<QuestionFeedSingleOutDTO> questions)
	{
		this.questions = questions;
	}
}
