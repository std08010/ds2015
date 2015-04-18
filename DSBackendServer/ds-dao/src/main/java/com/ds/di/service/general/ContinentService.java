/**
 * 
 */
package com.ds.di.service.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.general.ContinentDAO;
import com.ds.di.model.general.Continent;
import com.ds.di.service.AbstractServiceImpl;
import com.ds.di.utils.Globals;

/**
 * @author Altin Cipi
 *
 */

@Service(value = ContinentService.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional
public class ContinentService extends AbstractServiceImpl<Continent, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.service.general.ContinentService";

	@Autowired
	@Qualifier(value = ContinentDAO.SPRING_KEY)
	private ContinentDAO		dao;

	@Override
	public ContinentDAO getDAO()
	{
		return this.dao;
	}
	
	public Continent getContinent(String name)
	{
		return this.dao.getContinent(name);
	}
}
