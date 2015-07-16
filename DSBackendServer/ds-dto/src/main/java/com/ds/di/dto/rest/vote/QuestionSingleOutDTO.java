/**
 * 
 */
package com.ds.di.dto.rest.vote;

/**
 * @author Altin Cipi
 *
 */
public class QuestionSingleOutDTO
{
	private Long	questionID;
	private String	questionText;
	private String	insertedAt;

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
}
