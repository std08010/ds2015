/**
 * 
 */
package com.ds.di.service.user;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.user.UserDAO;
import com.ds.di.model.user.User;
import com.ds.di.service.AbstractServiceImpl;
import com.ds.di.utils.Globals;
import com.ds.di.utils.SecurityUtils;

/**
 * @author Altin Cipi
 *
 */

@Service(value = UserService.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional
public class UserService extends AbstractServiceImpl<User, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.service.user.UserService";

	@Autowired
	@Qualifier(value = UserDAO.SPRING_KEY)
	private UserDAO				dao;

	@Override
	public UserDAO getDAO()
	{
		return this.dao;
	}

	@Override
	public void delete(User user)
	{
		super.delete(user);
	}

	public User getUser(String username)
	{
		return this.dao.getUser(username);
	}

	public User getUser(String username, String password) throws Exception
	{
		return this.dao.getUser(username, password);
	}

	public User getUser(Long userID, String sessionToken)
	{
		return this.dao.getUser(userID, sessionToken);
	}

	public User getUserByUsernameAndToken(String username, String sessionToken)
	{
		return this.dao.getUserByUsernameAndToken(username, sessionToken);
	}

	public String generateSessionToken(User user) throws Exception
	{
		String key = SecurityUtils.getSalt() + "|" + user.getUsername() + "|" + DateTime.now(DateTimeZone.UTC).getMillis();

		return SecurityUtils.getJasyptEncryptor().encrypt(key);
	}

	public int updateCounterColumn(User user, Long number, String columnName, boolean isIncrease)
	{
		return this.getDAO().updateCounterColumn(user, number, columnName, isIncrease);
	}
}
