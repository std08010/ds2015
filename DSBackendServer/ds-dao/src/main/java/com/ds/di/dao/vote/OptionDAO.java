/**
 * 
 */
package com.ds.di.dao.vote;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.AbstractDAOImpl;
import com.ds.di.model.vote.Option;
import com.ds.di.model.vote.Question;
import com.ds.di.utils.Globals;
import com.ds.di.utils.lists.ListUtils;

/**
 * @author Altin Cipi
 *
 */
@Repository(value = OptionDAO.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional(value = "transactionManager")
public class OptionDAO extends AbstractDAOImpl<Option, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.dao.vote.OptionDAO";

	public OptionDAO()
	{
		super(Option.class);
	}

	@SuppressWarnings("unchecked")
	public List<Option> getOptions(Question question)
	{
		Criteria cr = getCriteria();
		cr.add(Restrictions.eq("question", question));

		return cr.list();
	}
	
	@SuppressWarnings("unchecked")
	public Option getOption(Question question, Long optionID)
	{
		Criteria cr = getCriteria();
		cr.add(Restrictions.eq("question", question));
		cr.add(Restrictions.eq("ID", optionID));

		return ListUtils.getFirstElement(cr.list());
	}
}
