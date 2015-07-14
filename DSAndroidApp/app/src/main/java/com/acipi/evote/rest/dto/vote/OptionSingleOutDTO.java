/**
 * 
 */
package com.acipi.evote.rest.dto.vote;

import com.acipi.evote.rest.dto.AbstractOutDTO;

/**
 * @author Altin Cipi
 *
 */
public class OptionSingleOutDTO extends AbstractOutDTO
{
	private Long	optionID;
	private String	optionText;
	private String	insertedAt;
	private Long	votesNum;

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

	/**
	 * @return the insertedAt
	 */
	public String getInsertedAt()
	{
		return insertedAt;
	}

	/**
	 * @param insertedAt
	 *            the insertedAt to set
	 */
	public void setInsertedAt(String insertedAt)
	{
		this.insertedAt = insertedAt;
	}

	/**
	 * @return the votesNum
	 */
	public Long getVotesNum()
	{
		return votesNum;
	}

	/**
	 * @param votesNum the votesNum to set
	 */
	public void setVotesNum(Long votesNum)
	{
		this.votesNum = votesNum;
	}
}
