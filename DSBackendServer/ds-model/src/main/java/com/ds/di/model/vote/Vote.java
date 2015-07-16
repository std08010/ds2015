/**
 * 
 */
package com.ds.di.model.vote;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ds.di.model.BaseEntity;
import com.ds.di.model.user.User;

/**
 * @author Altin Cipi
 *
 */
@Entity
@Table(name = "vote", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_fk", "question_fk" }) })
public class Vote extends BaseEntity
{
	private static final long	serialVersionUID	= 1868501107782693725L;

	@OnDelete(action=OnDeleteAction.CASCADE)
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "question_fk", nullable = false)
	private Question			question;

	@ManyToOne
	@JoinColumn(name = "user_fk", nullable = false)
	private User				user;

	@OnDelete(action=OnDeleteAction.CASCADE)
	@ManyToOne
	@JoinColumn(name = "option_fk", nullable = false)
	private Option				option;

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

	/**
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user)
	{
		this.user = user;
	}

	/**
	 * @return the option
	 */
	public Option getOption()
	{
		return option;
	}

	/**
	 * @param option
	 *            the option to set
	 */
	public void setOption(Option option)
	{
		this.option = option;
	}
}
