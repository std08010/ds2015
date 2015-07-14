/**
 * 
 */
package com.ds.di.service.vote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.vote.QuestionDAORead;
import com.ds.di.model.user.User;
import com.ds.di.model.vote.Question;
import com.ds.di.service.AbstractServiceImplRead;
import com.ds.di.utils.Globals;

/**
 * @author Altin Cipi
 *
 */

@Service(value = QuestionServiceRead.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional(value = "transactionManagerRead")
public class QuestionServiceRead extends AbstractServiceImplRead<Question, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.service.vote.QuestionServiceRead";

	@Autowired
	@Qualifier(value = QuestionDAORead.SPRING_KEY)
	private QuestionDAORead		dao;

	@Override
	public QuestionDAORead getDAO()
	{
		return this.dao;
	}

	public List<Question> getUnanswered(User user, String country, String untilDate, Integer limit, String lastDate, Long lastID) throws Exception
	{
		return this.getDAO().getUnanswered(user, country, untilDate, limit, lastDate, lastID);
	}

	public List<Question> getAnswered(User user, String country, String untilDate, Integer limit, String lastDate, Long lastID) throws Exception
	{
		return this.getDAO().getAnswered(user, country, untilDate, limit, lastDate, lastID);
	}

	public List<Question> getAll(String country, String untilDate, Integer limit, String lastDate, Long lastID) throws Exception
	{
		return this.getDAO().getAll(country, untilDate, limit, lastDate, lastID);
	}

	public List<Question> getQuestions(User user)
	{
		return this.getDAO().getQuestions(user);
	}

	public Question getQuestion(User user, Long questionID)
	{
		return this.getDAO().getQuestion(user, questionID);
	}
}
