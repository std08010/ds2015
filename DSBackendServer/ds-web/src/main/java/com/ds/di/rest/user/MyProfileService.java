/**
 * 
 */
package com.ds.di.rest.user;

import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dto.rest.user.MyProfileGetAllInfoInDTO;
import com.ds.di.dto.rest.user.MyProfileGetAllInfoOutDTO;
import com.ds.di.model.user.User;
import com.ds.di.service.user.UserService;
import com.ds.di.utils.RestServiceUtils;

/**
 * @author Altin Cipi
 *
 */
@Component(value = MyProfileService.SPRING_KEY)
@Transactional
@Path("/secure/user/myprofile")
public class MyProfileService
{
	public static final String	SPRING_KEY	= "com.ds.di.rest.user.MyProfileService";

	@Autowired
	@Qualifier(UserService.SPRING_KEY)
	private UserService			userService;

	@Autowired
	@Qualifier(value = "myProperties")
	private Properties			myProperties;

	/**
	 * @return
	 */
	@POST
	@Path("/all_info")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getAllInfo(MyProfileGetAllInfoInDTO input)
	{
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		MyProfileGetAllInfoOutDTO output = new MyProfileGetAllInfoOutDTO();

		try
		{
			User user = this.userService.getUserByUsernameAndToken(input.getUsername(), input.getSessionToken());

			if (user == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe session has expired. Please logout and try to login again.");
			}

			output.setUsername(user.getUsername());
			output.setEmail(user.getEmail());
			output.setAvatarURL(user.getAvatarURL());
			if (user.getCountry() != null)
			{
				output.setCountry(user.getCountry().getName());
				output.setCountryFlagURL(user.getCountry().getFlagURL());
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
