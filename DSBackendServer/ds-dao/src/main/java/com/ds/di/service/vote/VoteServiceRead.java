/**
 * 
 */
package com.ds.di.service.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.vote.VoteDAORead;
import com.ds.di.model.user.User;
import com.ds.di.model.vote.Option;
import com.ds.di.model.vote.Question;
import com.ds.di.model.vote.Vote;
import com.ds.di.service.AbstractServiceImplRead;
import com.ds.di.utils.Globals;

/**
 * @author Altin Cipi
 *
 */

@Service(value = VoteServiceRead.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional(value = "transactionManagerRead")
public class VoteServiceRead extends AbstractServiceImplRead<Vote, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.service.vote.VoteServiceRead";

	@Autowired
	@Qualifier(value = VoteDAORead.SPRING_KEY)
	private VoteDAORead			dao;

	@Override
	public VoteDAORead getDAO()
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
