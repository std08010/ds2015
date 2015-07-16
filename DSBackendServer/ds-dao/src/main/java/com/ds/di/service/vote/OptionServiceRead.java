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

import com.ds.di.dao.vote.OptionDAORead;
import com.ds.di.model.vote.Option;
import com.ds.di.model.vote.Question;
import com.ds.di.service.AbstractServiceImplRead;
import com.ds.di.utils.Globals;

/**
 * @author Altin Cipi
 *
 */

@Service(value = OptionServiceRead.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional(value = "transactionManagerRead")
public class OptionServiceRead extends AbstractServiceImplRead<Option, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.service.vote.OptionServiceRead";

	@Autowired
	@Qualifier(value = OptionDAORead.SPRING_KEY)
	private OptionDAORead		dao;

	@Override
	public OptionDAORead getDAO()
	{
		return this.dao;
	}

	public List<Option> getOptions(Question question)
	{
		return this.getDAO().getOptions(question);
	}

	public Option getOption(Question question, Long optionID)
	{
		return this.getDAO().getOption(question, optionID);
	}
}
