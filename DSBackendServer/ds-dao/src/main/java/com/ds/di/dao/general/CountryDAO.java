/**
 * 
 */
package com.ds.di.dao.general;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.AbstractDAOImpl;
import com.ds.di.model.general.Continent;
import com.ds.di.model.general.Country;
import com.ds.di.utils.Globals;

/**
 * @author Altin Cipi
 *
 */
@Repository(value = CountryDAO.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional
public class CountryDAO extends AbstractDAOImpl<Country, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.dao.general.CountryDAO";
	
	public CountryDAO()
	{
		super(Country.class);
	}
	
	public Country getCountry(String name)
	{
		return this.findByAttribute("name", name);
	}
	
	@SuppressWarnings("unchecked")
	public List<Country> getCountriesByContinent(Continent continent)
	{
		Criteria cr = getCriteria();
		cr.add(Restrictions.eq("continent", continent));
		cr.addOrder(Order.asc("name"));
		
		return cr.list();
	}
	
	public List<Country> getAllCountries()
	{
		return this.findAllByOrder("name", true);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAllCountryNames()
	{
		Query query = this.getSession().createQuery("SELECT name FROM Country ORDER BY name ASC");
		
		return query.list();
	}
}
