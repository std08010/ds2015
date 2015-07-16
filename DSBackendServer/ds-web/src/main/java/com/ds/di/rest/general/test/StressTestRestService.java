/**
 * 
 */
package com.ds.di.rest.general.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dto.rest.SimpleDTO;
import com.ds.di.utils.RestServiceUtils;

/**
 * @author Altin Cipi
 *
 */
@Component(value = StressTestRestService.SPRING_KEY)
@Path("/general/test/stress")
public class StressTestRestService
{
	public static final String	SPRING_KEY	= "com.ds.di.rest.general.test.StressTestRestService";

	/**
	 * @return
	 */
	@GET
	@Path("/sleep_no_transactions")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response sleepTest()
	{
		Logger.getLogger(this.getClass()).info("sleepTest");

		SimpleDTO output = new SimpleDTO();

		try
		{
			Thread.sleep(1 * 60 * 1000);
			output.setResult("Have slept successfully for 1 minute");
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
	@GET
	@Path("/sleep_transactional")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Transactional(value = "transactionManager")
	public Response sleepTestTransactional()
	{
		Logger.getLogger(this.getClass()).info("sleepTestTransactional");

		SimpleDTO output = new SimpleDTO();

		try
		{
			Thread.sleep(2 * 60 * 1000);
			output.setResult("Have slept successfully for 2 minutes");
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return RestServiceUtils.getErrorResponse(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.status(Status.OK).entity(output).build();
	}
}
