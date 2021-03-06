/**
 * 
 */
package com.ds.di.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.ds.di.utils.RestServiceUtils;

/**
 * @author Altin Cipi
 *
 */
@Provider
public class AnyExceptionMapper implements ExceptionMapper<Exception>
{
	@Override
	public Response toResponse(Exception exception)
	{
		Logger.getRootLogger().error(exception.getMessage(), exception);
		return RestServiceUtils.getErrorResponse(Status.INTERNAL_SERVER_ERROR, exception.getMessage());
	}
}
