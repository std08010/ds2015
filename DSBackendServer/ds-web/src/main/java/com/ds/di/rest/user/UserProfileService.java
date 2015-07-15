/**
 * 
 */
package com.ds.di.rest.user;

import java.util.ArrayList;
import java.util.List;
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

import com.ds.di.dto.rest.user.UserProfileGetAllInfoInDTO;
import com.ds.di.dto.rest.user.UserProfileGetAllInfoOutDTO;
import com.ds.di.dto.rest.vote.QuestionFeedSingleOutDTO;
import com.ds.di.dto.rest.vote.QuestionSingleOutDTO;
import com.ds.di.model.user.User;
import com.ds.di.model.vote.Question;
import com.ds.di.model.vote.Vote;
import com.ds.di.service.user.UserServiceRead;
import com.ds.di.service.vote.QuestionServiceRead;
import com.ds.di.service.vote.VoteServiceRead;
import com.ds.di.utils.RestServiceUtils;

/**
 * @author Altin Cipi
 *
 */
@Component(value = UserProfileService.SPRING_KEY)
@Transactional(value = "transactionManagerRead")
@Path("/secure/user/profile")
public class UserProfileService
{
	public static final String	SPRING_KEY	= "com.ds.di.rest.user.UserProfileService";

	@Autowired
	@Qualifier(UserServiceRead.SPRING_KEY)
	private UserServiceRead		userServiceRead;

	@Autowired
	@Qualifier(QuestionServiceRead.SPRING_KEY)
	private QuestionServiceRead	questionServiceRead;

	@Autowired
	@Qualifier(VoteServiceRead.SPRING_KEY)
	private VoteServiceRead		voteServiceRead;

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
	public Response getAllInfo(UserProfileGetAllInfoInDTO input)
	{
		Logger.getLogger(this.getClass()).info("getAllInfo");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		UserProfileGetAllInfoOutDTO output = new UserProfileGetAllInfoOutDTO();
		output.setQuestions(new ArrayList<QuestionFeedSingleOutDTO>());

		try
		{
			User user = this.userServiceRead.getUserByUsernameAndToken(input.getUsername(), input.getSessionToken());

			if (user == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe session has expired. Please logout and try to login again.");
			}

			User profileUser = this.userServiceRead.getUser(input.getProfileUsername());

			if (profileUser == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe this profile doesn't exist. Please try another user.");
			}

			output.setUsername(profileUser.getUsername());
			output.setAvatarURL(profileUser.getAvatarURL());
			if (profileUser.getCountry() != null)
			{
				output.setCountry(profileUser.getCountry().getName());
				output.setCountryFlagURL(profileUser.getCountry().getFlagURL());
			}

			List<Question> questions = this.questionServiceRead.getQuestions(profileUser);

			output.setAllQuestions(questions != null ? new Long(questions.size()) : new Long(0));

			for (Question question : questions)
			{
				QuestionSingleOutDTO questionSingleOutDTO = new QuestionSingleOutDTO();

				questionSingleOutDTO.setQuestionID(question.getID());
				questionSingleOutDTO.setQuestionText(question.getComment());
				questionSingleOutDTO.setInsertedAt(question.getInsertedAt().toString());

				QuestionFeedSingleOutDTO questionFeedSingleOutDTO = new QuestionFeedSingleOutDTO();
				questionFeedSingleOutDTO.setQuestion(questionSingleOutDTO);

				Vote vote = this.voteServiceRead.getVote(question, user);

				if (vote != null)
				{
					questionFeedSingleOutDTO.setAnswered(true);
				}
				else
				{
					questionFeedSingleOutDTO.setAnswered(false);
				}

				if (question.getUser() != null)
				{
					questionFeedSingleOutDTO.setUsername(question.getUser().getUsername());
					questionFeedSingleOutDTO.setAvatarURL(question.getUser().getAvatarURL());
				}

				output.getQuestions().add(questionFeedSingleOutDTO);
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
