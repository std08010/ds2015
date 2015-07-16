/**
 * 
 */
package com.ds.di.dto.rest.vote;

/**
 * @author Altin Cipi
 *
 */
public class HomeFeedGetAllInDTO extends HomeFeedInDTO
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
