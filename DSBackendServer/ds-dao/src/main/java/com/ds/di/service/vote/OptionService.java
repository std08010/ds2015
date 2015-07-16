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

import com.ds.di.dao.vote.OptionDAO;
import com.ds.di.model.vote.Option;
import com.ds.di.model.vote.Question;
import com.ds.di.service.AbstractServiceImpl;
import com.ds.di.utils.Globals;

/**
 * @author Altin Cipi
 *
 */

@Service(value = OptionService.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional(value = "transactionManager")
public class OptionService extends AbstractServiceImpl<Option, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.service.vote.OptionService";

	@Autowired
	@Qualifier(value = OptionDAO.SPRING_KEY)
	private OptionDAO			dao;

	@Override
	public OptionDAO getDAO()
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
