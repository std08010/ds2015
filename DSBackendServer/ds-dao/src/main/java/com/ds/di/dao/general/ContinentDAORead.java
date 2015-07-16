/**
 * 
 */
package com.ds.di.dao.general;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.AbstractDAOImplRead;
import com.ds.di.model.general.Continent;
import com.ds.di.utils.Globals;

/**
 * @author Altin Cipi
 *
 */
@Repository(value = ContinentDAORead.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional(value = "transactionManagerRead")
public class ContinentDAORead extends AbstractDAOImplRead<Continent, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.dao.general.ContinentDAORead";

	public ContinentDAORead()
	{
		super(Continent.class);
	}

	public Continent getContinent(String name)
	{
		return this.findByAttribute("name", name);
	}
}
