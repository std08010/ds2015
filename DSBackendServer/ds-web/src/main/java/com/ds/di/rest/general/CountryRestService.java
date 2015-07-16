/**
 * 
 */
package com.ds.di.rest.general;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dto.rest.general.CountryAllOutDTO;
import com.ds.di.dto.rest.general.CountryGetFlagURLInDTO;
import com.ds.di.dto.rest.general.CountryGetFlagURLOutDTO;
import com.ds.di.model.general.Country;
import com.ds.di.service.general.CountryServiceRead;
import com.ds.di.utils.RestServiceUtils;

/**
 * @author Altin Cipi
 *
 */
@Component(value = CountryRestService.SPRING_KEY)
@Transactional(value = "transactionManagerRead")
@Path("/general/country")
public class CountryRestService
{
	public static final String	SPRING_KEY	= "com.ds.di.rest.general.CountryRestService";

	@Autowired
	@Qualifier(CountryServiceRead.SPRING_KEY)
	private CountryServiceRead	countryServiceRead;

	/**
	 * @return
	 */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getAllCountries()
	{
		Logger.getLogger(this.getClass()).info("getAllCountries");
		
		CountryAllOutDTO output = new CountryAllOutDTO();

		try
		{
			output.setCountries(this.countryServiceRead.getAllCountryNames());
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return RestServiceUtils.getErrorResponse(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.status(Status.OK).entity(output).build();
	}

	/**
	 * @return
	 */
	@POST
	@Path("/get_flag_url")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFlagURL(CountryGetFlagURLInDTO input)
	{
		Logger.getLogger(this.getClass()).info("getFlagURL");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		CountryGetFlagURLOutDTO output = new CountryGetFlagURLOutDTO();

		try
		{
			Country country = this.countryServiceRead.getCountry(input.getCountryName());

			if (country != null)
			{
				output.setCountryFlagURL(country.getFlagURL());
			}
			else
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "Country was not found.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return RestServiceUtils.getErrorResponse(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.status(Status.OK).entity(output).build();
	}
}
