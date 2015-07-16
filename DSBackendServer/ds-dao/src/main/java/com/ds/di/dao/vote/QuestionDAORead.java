/**
 * 
 */
package com.ds.di.dao.vote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.AbstractDAOImplRead;
import com.ds.di.model.user.User;
import com.ds.di.model.vote.Question;
import com.ds.di.utils.Globals;
import com.ds.di.utils.lists.ListUtils;

/**
 * @author Altin Cipi
 *
 */
@Repository(value = QuestionDAORead.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional(value = "transactionManagerRead")
public class QuestionDAORead extends AbstractDAOImplRead<Question, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.dao.vote.QuestionDAORead";

	public QuestionDAORead()
	{
		super(Question.class);
	}

	@SuppressWarnings("unchecked")
	public List<Question> getUnanswered(User user, String country, String untilDate, Integer limit, String lastDate, Long lastID) throws Exception
	{
		Query query = this.getSession().createQuery(
				"SELECT question FROM Question question " + " WHERE question.comment IS NOT NULL " + (lastDate != null && lastID != null ? " AND (question.insertedAt, question.ID) < (:lastDate, :lastID) " : "") + (untilDate != null ? " AND question.insertedAt >= :untilDate " : "")
						+ (country != null ? " AND question.user.country.name = :country " : "") + (user != null ? " AND (SELECT COUNT(vote) FROM Vote vote WHERE vote.question.ID = question.ID AND vote.user = :user) = 0 " : "") + " ORDER BY question.insertedAt DESC, question.ID DESC");

		query.setMaxResults(limit != null && limit >= 0 ? limit : 50);

		if (user != null)
		{
			query.setParameter("user", (User) user);
		}

		if (lastDate != null && lastID != null)
		{
			query.setParameter("lastDate", (Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(lastDate));
			query.setParameter("lastID", (Long) lastID);
		}

		if (untilDate != null)
		{
			query.setParameter("untilDate", (DateTime) DateTime.parse(untilDate));
		}

		if (country != null)
		{
			query.setParameter("country", (String) country);
		}

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Question> getAnswered(User user, String country, String untilDate, Integer limit, String lastDate, Long lastID) throws Exception
	{
		Query query = this.getSession().createQuery(
				"SELECT question FROM Question question " + " WHERE question.comment IS NOT NULL " + (lastDate != null && lastID != null ? " AND (question.insertedAt, question.ID) < (:lastDate, :lastID) " : "") + (untilDate != null ? " AND question.insertedAt >= :untilDate " : "")
						+ (country != null ? " AND question.user.country.name = :country " : "") + (user != null ? " AND (SELECT COUNT(vote) FROM Vote vote WHERE vote.question.ID = question.ID AND vote.user = :user) = 1 " : "") + " ORDER BY question.insertedAt DESC, question.ID DESC");

		query.setMaxResults(limit != null && limit >= 0 ? limit : 50);

		if (user != null)
		{
			query.setParameter("user", (User) user);
		}

		if (lastDate != null && lastID != null)
		{
			query.setParameter("lastDate", (Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(lastDate));
			query.setParameter("lastID", (Long) lastID);
		}

		if (untilDate != null)
		{
			query.setParameter("untilDate", (DateTime) DateTime.parse(untilDate));
		}

		if (country != null)
		{
			query.setParameter("country", (String) country);
		}

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Question> getAll(String country, String untilDate, Integer limit, String lastDate, Long lastID) throws Exception
	{
		Query query = this.getSession().createQuery(
				"SELECT question FROM Question question " + " WHERE question.comment IS NOT NULL " + (lastDate != null && lastID != null ? " AND (question.insertedAt, question.ID) < (:lastDate, :lastID) " : "") + (untilDate != null ? " AND question.insertedAt >= :untilDate " : "")
						+ (country != null ? " AND question.user.country.name = :country " : "") + " ORDER BY question.insertedAt DESC, question.ID DESC");

		query.setMaxResults(limit != null && limit >= 0 ? limit : 50);

		if (lastDate != null && lastID != null)
		{
			query.setParameter("lastDate", (Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(lastDate));
			query.setParameter("lastID", (Long) lastID);
		}

		if (untilDate != null)
		{
			query.setParameter("untilDate", (DateTime) DateTime.parse(untilDate));
		}

		if (country != null)
		{
			query.setParameter("country", (String) country);
		}

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Question> getQuestions(User user)
	{
		Criteria cr = getCriteria();
		cr.add(Restrictions.eq("user", user));

		return cr.list();
	}

	@SuppressWarnings("unchecked")
	public Question getQuestion(User user, Long questionID)
	{
		Criteria cr = getCriteria();
		cr.add(Restrictions.eq("user", user));
		cr.add(Restrictions.eq("ID", questionID));

		return ListUtils.getFirstElement(cr.list());
	}
}
