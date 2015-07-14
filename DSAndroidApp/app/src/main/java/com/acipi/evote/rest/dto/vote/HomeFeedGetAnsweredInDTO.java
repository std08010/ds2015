/**
 * 
 */
package com.acipi.evote.rest.dto.vote;

/**
 * @author Altin Cipi
 *
 */
public class HomeFeedGetAnsweredInDTO extends HomeFeedInDTO
{
	private String	lastDate;

	/**
	 * @return the lastDate
	 */
	public String getLastDate()
	{
		return lastDate;
	}

	/**
	 * @param lastDate
	 *            the lastDate to set
	 */
	public void setLastDate(String lastDate)
	{
		this.lastDate = lastDate;
	}
}
