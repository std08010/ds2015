/**
 * 
 */
package com.ds.di.service.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.general.ContinentDAORead;
import com.ds.di.model.general.Continent;
import com.ds.di.service.AbstractServiceImplRead;
import com.ds.di.utils.Globals;

/**
 * @author Altin Cipi
 *
 */

@Service(value = ContinentServiceRead.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional(value = "transactionManagerRead")
public class ContinentServiceRead extends AbstractServiceImplRead<Continent, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.service.general.ContinentServiceRead";

	@Autowired
	@Qualifier(value = ContinentDAORead.SPRING_KEY)
	private ContinentDAORead	dao;

	@Override
	public ContinentDAORead getDAO()
	{
		return this.dao;
	}

	public Continent getContinent(String name)
	{
		return this.dao.getContinent(name);
	}
}
