/**
 * 
 */
package com.ds.di.model.vote;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ds.di.model.BaseEntity;
import com.ds.di.model.user.User;

/**
 * @author Altin Cipi
 *
 */
@Entity
@Table(name = "question", uniqueConstraints = {})
public class Question extends BaseEntity
{
	private static final long	serialVersionUID	= 1868571107782693025L;

	@ManyToOne
	@JoinColumn(name = "user_fk", nullable = false)
	private User				user;

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
}
