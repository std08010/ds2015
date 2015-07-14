/**
 * 
 */
package com.ds.di.service.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.vote.VoteDAO;
import com.ds.di.model.user.User;
import com.ds.di.model.vote.Option;
import com.ds.di.model.vote.Question;
import com.ds.di.model.vote.Vote;
import com.ds.di.service.AbstractServiceImpl;
import com.ds.di.utils.Globals;

/**
 * @author Altin Cipi
 *
 */

@Service(value = VoteService.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional(value = "transactionManager")
public class VoteService extends AbstractServiceImpl<Vote, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.service.vote.VoteService";

	@Autowired
	@Qualifier(value = VoteDAO.SPRING_KEY)
	private VoteDAO				dao;

	@Override
	public VoteDAO getDAO()
	{
		return this.dao;
	}

	public Vote getVote(Question question, User user)
	{
		return this.getDAO().getVote(question, user);
	}

	public Long getVotesNum(Option option)
	{
		return this.getDAO().getVotesNum(option);
	}
}
