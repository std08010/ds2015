/**
 * 
 */
package com.ds.di.dao.general;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.AbstractDAOImpl;
import com.ds.di.model.general.Continent;
import com.ds.di.utils.Globals;

/**
 * @author Altin Cipi
 *
 */
@Repository(value = ContinentDAO.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional
public class ContinentDAO extends AbstractDAOImpl<Continent, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.dao.general.ContinentDAO";

	public ContinentDAO()
	{
		super(Continent.class);
	}
	
	public Continent getContinent(String name)
	{
		return this.findByAttribute("name", name);
	}
}
