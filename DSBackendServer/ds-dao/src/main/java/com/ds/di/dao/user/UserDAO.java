/**
 * 
 */
package com.ds.di.dao.user;

import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.AbstractDAOImpl;
import com.ds.di.model.user.User;
import com.ds.di.utils.Globals;
import com.ds.di.utils.SecurityUtils;

/**
 * @author Altin Cipi
 *
 */
@Repository(value = UserDAO.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional(value = "transactionManager")
public class UserDAO extends AbstractDAOImpl<User, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.dao.user.UserDAO";

	public UserDAO()
	{
		super(User.class);
	}

	public User getUser(String username)
	{
		return this.findByAttribute("username", username);
	}

	public User getUser(String username, String password) throws Exception
	{
		User user = this.findByAttribute("username", username);

		if (user != null)
		{
			if (SecurityUtils.validatePassword(password, user.getPassword()))
			{
				return user;
			}
		}

		return null;
	}

	public User getUser(Long userID, String sessionToken)
	{
		User user = this.find(userID);

		if (user != null)
		{
			if (user.getSessionToken() != null && user.getSessionToken().equals(sessionToken))
			{
				return user;
			}
		}

		return null;
	}

	public User getUserByUsernameAndToken(String username, String sessionToken)
	{
		User user = this.getUser(username);

		if (user != null)
		{
			if (user.getSessionToken() != null && user.getSessionToken().equals(sessionToken))
			{
				return user;
			}
		}

		return null;
	}
	
	public int updateCounterColumn(User user, Long number, String columnName, boolean isIncrease)
	{
		if(user == null)
		{
			return -1;
		}
		
		Query query = this.getSession().createQuery(
				"UPDATE User SET " + columnName + " = " + columnName + " " + (isIncrease ? "+" : "-") + " :number " 
						+ " WHERE ID = :userID " );
		query.setParameter("number", number);
		query.setParameter("userID", user.getID());
		
		return query.executeUpdate();
	}
}
