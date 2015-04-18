/**
 * 
 */
package com.ds.di.service.general;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dao.general.CountryDAO;
import com.ds.di.model.general.Continent;
import com.ds.di.model.general.Country;
import com.ds.di.service.AbstractServiceImpl;
import com.ds.di.utils.Globals;

/**
 * @author Altin Cipi
 *
 */

@Service(value = CountryService.SPRING_KEY)
@Scope(Globals.SPRING_SCOPE_PROTOTYPE)
@Transactional
public class CountryService extends AbstractServiceImpl<Country, Long>
{
	public static final String	SPRING_KEY	= "com.ds.di.service.general.CountryService";

	@Autowired
	@Qualifier(value = CountryDAO.SPRING_KEY)
	private CountryDAO			dao;

	@Override
	public CountryDAO getDAO()
	{
		return this.dao;
	}

	public Country getCountry(String name)
	{
		return this.dao.getCountry(name);
	}

	public List<Country> getCountriesByContinent(Continent continent)
	{
		return this.dao.getCountriesByContinent(continent);
	}

	public List<Country> getAllCountries()
	{
		return this.dao.getAllCountries();
	}

	public List<String> getAllCountryNames()
	{
		return this.dao.getAllCountryNames();
	}
}
