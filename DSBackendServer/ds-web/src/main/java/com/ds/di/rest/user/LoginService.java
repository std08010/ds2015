/**
 * 
 */
package com.ds.di.rest.user;

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

import com.ds.di.dto.rest.user.LoginAskInDTO;
import com.ds.di.dto.rest.user.LoginAskOutDTO;
import com.ds.di.model.user.User;
import com.ds.di.service.user.UserService;
import com.ds.di.utils.RestServiceUtils;

/**
 * @author Altin Cipi
 *
 */
@Component(value = LoginService.SPRING_KEY)
@Transactional(value = "transactionManager")
@Path("/secure/user/login")
public class LoginService
{
	public static final String	SPRING_KEY	= "com.ds.di.rest.user.LoginService";

	@Autowired
	@Qualifier(UserService.SPRING_KEY)
	private UserService			userService;

	/**
	 * @return
	 */
	@POST
	@Path("/ask")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response askToLogin(LoginAskInDTO input)
	{
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		LoginAskOutDTO output = new LoginAskOutDTO();

		try
		{
			User user = this.userService.getUser(input.getUsername(), input.getPassword());

			if (user != null)
			{
				user.setSessionToken(userService.generateSessionToken(user));
				userService.update(user);

				output.setEmail(user.getEmail());
				output.setSessionToken(user.getSessionToken());
				if (user.getCountry() != null)
				{
					output.setCountry(user.getCountry().getName());
					output.setCountryURL(user.getCountry().getFlagURL());
				}
				output.setAvatarURL(user.getAvatarURL());
			}
			else
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Incorrect username or password.");
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
