/**
 * 
 */
package com.ds.di.model.vote;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ds.di.model.BaseEntity;

/**
 * @author Altin Cipi
 *
 */
@Entity
@Table(name = "option", uniqueConstraints = {})
public class Option extends BaseEntity
{
	private static final long	serialVersionUID	= 1868571107772693725L;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne
	@JoinColumn(name = "question_fk", nullable = false)
	private Question			question;

	/**
	 * @return the question
	 */
	public Question getQuestion()
	{
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(Question question)
	{
		this.question = question;
	}
}
