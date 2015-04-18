/**
 * 
 */
package com.ds.di.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ds.di.dto.rest.ErrorDTO;

/**
 * @author Altin Cipi
 *
 */
public class RestServiceUtils
{
	public static Response getErrorResponse(Status status, String errorMessage)
	{
		ErrorDTO error = new ErrorDTO();
		error.setMessage(status.getReasonPhrase() + " (" + status.getStatusCode() + "): " + errorMessage);
		error.setStatusCode(status.getStatusCode());
		
		return Response.status(status).entity(error).build();
	}
}
