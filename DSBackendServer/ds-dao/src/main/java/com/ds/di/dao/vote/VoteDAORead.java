/**
 * 
 */
package com.ds.di.dao.vote;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.AbstractDAOImplRead;
import com.ds.di.model.user.User;
import com.ds.di.model.vote.Option;
import com.ds.di.model.vote.Question;
import com.ds.di.model.vote.Vote;
import com.ds.di.utils.Globals;
import com.ds.di.utils.lists.ListUtils;

/**
 * @author Altin Cipi
 *
 */
@Repository(value = VoteDAORead.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional(value = "transactionManagerRead")
public class VoteDAORead extends AbstractDAOImplRead<Vote, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.dao.vote.VoteDAORead";

	public VoteDAORead()
	{
		super(Vote.class);
	}

	@SuppressWarnings("unchecked")
	public Vote getVote(Question question, User user)
	{
		Criteria cr = getCriteria();
		cr.add(Restrictions.eq("question", question));
		cr.add(Restrictions.eq("user", user));

		return ListUtils.getFirstElement(cr.list());
	}

	public Long getVotesNum(Option option)
	{
		Criteria cr = getCriteria();
		cr.add(Restrictions.eq("option", option));
		cr.setProjection(Projections.count("ID"));

		return (Long) cr.uniqueResult();
	}
}
