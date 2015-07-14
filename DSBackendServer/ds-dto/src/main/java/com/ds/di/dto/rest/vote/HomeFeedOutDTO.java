/**
 * 
 */
package com.ds.di.dto.rest.vote;

import java.util.List;

/**
 * @author Altin Cipi
 *
 */
public class HomeFeedOutDTO
{
	private List<QuestionFeedSingleOutDTO>	feeds;

	/**
	 * @return the feeds
	 */
	public List<QuestionFeedSingleOutDTO> getFeeds()
	{
		return feeds;
	}

	/**
	 * @param feeds
	 *            the feeds to set
	 */
	public void setFeeds(List<QuestionFeedSingleOutDTO> feeds)
	{
		this.feeds = feeds;
	}
}
