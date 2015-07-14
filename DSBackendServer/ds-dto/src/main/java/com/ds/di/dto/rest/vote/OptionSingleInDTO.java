/**
 * 
 */
package com.ds.di.dto.rest.vote;

/**
 * @author Altin Cipi
 *
 */
public class OptionSingleInDTO
{
	private Long	optionID;
	private String	optionText;

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

	/**
	 * @return the optionText
	 */
	public String getOptionText()
	{
		return optionText;
	}

	/**
	 * @param optionText
	 *            the optionText to set
	 */
	public void setOptionText(String optionText)
	{
		this.optionText = optionText;
	}
}
