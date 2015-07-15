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

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.dto.rest.user.RegistrationCheckUsernameAvailabilityInDTO;
import com.ds.di.dto.rest.user.RegistrationCheckUsernameAvailabilityOutDTO;
import com.ds.di.dto.rest.user.RegistrationCreateInDTO;
import com.ds.di.dto.rest.user.RegistrationCreateOutDTO;
import com.ds.di.model.user.User;
import com.ds.di.service.general.CountryServiceRead;
import com.ds.di.service.user.UserService;
import com.ds.di.service.user.UserServiceRead;
import com.ds.di.utils.AmazonS3UploadThread;
import com.ds.di.utils.EmailValidator;
import com.ds.di.utils.RestServiceUtils;
import com.ds.di.utils.SecurityUtils;
import com.ds.di.utils.UsernameValidator;

/**
 * @author Altin Cipi
 *
 */
@Component(value = RegistrationService.SPRING_KEY)
@Transactional(value = "transactionManager")
@Path("/secure/user/register")
public class RegistrationService
{
	public static final String	SPRING_KEY	= "com.ds.di.rest.user.RegistrationService";

	@Autowired
	@Qualifier(UserService.SPRING_KEY)
	private UserService			userService;

	@Autowired
	@Qualifier(UserServiceRead.SPRING_KEY)
	private UserServiceRead		userServiceRead;

	@Autowired
	@Qualifier(CountryServiceRead.SPRING_KEY)
	private CountryServiceRead	countryServiceRead;

	@Autowired
	@Qualifier(value = "myProperties")
	private Properties			myProperties;

	/**
	 * When the user clicks the register button in the registration page
	 * the client calls this function.
	 * Here we check if the user already exists otherwise we send OK and a session-key
	 * 
	 * @return
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response createNewUser(RegistrationCreateInDTO input)
	{
		Logger.getLogger(this.getClass()).info("createNewUser");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		RegistrationCreateOutDTO output = new RegistrationCreateOutDTO();

		try
		{
			User newUser = this.userService.getUser(input.getUsername());

			if (newUser == null)
			{
				boolean usernameValidation = new UsernameValidator().validate(input.getUsername());
				// password validation cannot be performed because the received password is encrypted
				// boolean passwordValidation = new PasswordValidator().validate(input.getPassword());
				boolean emailValidation = new EmailValidator().validate(input.getEmail());

				if (!(usernameValidation && emailValidation))
				{
					return RestServiceUtils.getErrorResponse(Status.CONFLICT, "Invalid username or email was found.");
				}

				newUser = new User();
				newUser.setUsername(input.getUsername());
				newUser.setPassword(SecurityUtils.generateStrongPasswordHash(input.getPassword()));
				newUser.setEmail(input.getEmail());
				newUser.setCountry(this.countryServiceRead.getCountry(input.getCountry()));
				newUser.setSessionToken(this.userService.generateSessionToken(newUser));
				this.userService.create(newUser);

				if (input.getAvatar() != null)
				{
					String fileName = "avatars/" + newUser.getID() + "_" + newUser.getUsername() + ".jpg";
					String fullS3Path = myProperties.getProperty("aws.s3.url") + "/" + myProperties.getProperty("aws.s3.bucketname") + "/" + fileName;

					newUser.setAvatarURL(fullS3Path);
					newUser = this.userService.update(newUser);

					AmazonS3UploadThread amazonS3Thread = new AmazonS3UploadThread(input.getAvatar(), fileName, myProperties.getProperty("aws.s3.bucketname"), myProperties.getProperty("aws.accessKey"), myProperties.getProperty("aws.secretKey"));
					amazonS3Thread.start();
				}

				output.setSessionToken(newUser.getSessionToken());
				if (newUser.getCountry() != null)
				{
					output.setCountryURL(newUser.getCountry().getFlagURL());
				}
				output.setAvatarURL(newUser.getAvatarURL());
			}
			else
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User cannot be saved. User already exists.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return RestServiceUtils.getErrorResponse(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.status(Status.OK).entity(output).build();
	}

	@POST
	@Path("/check_username_availability")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response checkUsernameAvailability(RegistrationCheckUsernameAvailabilityInDTO input)
	{
		Logger.getLogger(this.getClass()).info("checkUsernameAvailability");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		RegistrationCheckUsernameAvailabilityOutDTO output = new RegistrationCheckUsernameAvailabilityOutDTO();

		try
		{
			User user = this.userServiceRead.getUser(input.getUsername());

			if (user == null)
			{
				output.setAvailable(true);
				output.setReason("Username is available");
			}
			else
			{
				output.setAvailable(false);
				output.setReason("Username is already used by another user. Try something else.");
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
