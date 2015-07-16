/**
 * 
 */
package com.acipi.evote.rest.dto.vote;

import com.acipi.evote.rest.dto.AbstractOutDTO;

import java.util.List;

/**
 * @author Altin Cipi
 *
 */
public class HomeFeedOutDTO extends AbstractOutDTO
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
