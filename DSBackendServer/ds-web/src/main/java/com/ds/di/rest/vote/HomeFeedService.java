/**
 * 
 */
package com.ds.di.rest.vote;

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

import com.ds.di.dto.rest.vote.HomeFeedGetAllInDTO;
import com.ds.di.dto.rest.vote.HomeFeedGetAnsweredInDTO;
import com.ds.di.dto.rest.vote.HomeFeedGetUnansweredInDTO;
import com.ds.di.dto.rest.vote.HomeFeedOutDTO;
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
@Component(value = HomeFeedService.SPRING_KEY)
@Transactional(value = "transactionManagerRead")
@Path("/secure/vote/home_feed")
public class HomeFeedService
{
	public static final String	SPRING_KEY	= "com.ds.di.rest.vote.HomeFeedService";

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
	@Path("/unanswered")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getUnanswered(HomeFeedGetUnansweredInDTO input)
	{
		Logger.getLogger(this.getClass()).info("getUnanswered");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		HomeFeedOutDTO output = new HomeFeedOutDTO();
		output.setFeeds(new ArrayList<QuestionFeedSingleOutDTO>());

		try
		{
			User user = null;

			user = this.userServiceRead.getUserByUsernameAndToken(input.getUsername(), input.getSessionToken());

			if (user == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe session has expired. Please logout and try to login again.");
			}

			List<Question> feeds = this.questionServiceRead.getUnanswered(user, input.getCountry(), input.getUntilDate(), input.getLimit(), input.getLastDate(), input.getLastID());

			for (Question feed : feeds)
			{
				QuestionSingleOutDTO questionDTO = new QuestionSingleOutDTO();
				questionDTO.setQuestionID(feed.getID());
				questionDTO.setQuestionText(feed.getComment());
				questionDTO.setInsertedAt(feed.getInsertedAt().toString());

				QuestionFeedSingleOutDTO feedDTO = new QuestionFeedSingleOutDTO();
				feedDTO.setQuestion(questionDTO);
				feedDTO.setAnswered(false);

				if (feed.getUser() != null)
				{
					feedDTO.setUsername(feed.getUser().getUsername());
					feedDTO.setAvatarURL(feed.getUser().getAvatarURL());
				}

				output.getFeeds().add(feedDTO);
			}
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
	@Path("/answered")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getAnswered(HomeFeedGetAnsweredInDTO input)
	{
		Logger.getLogger(this.getClass()).info("getAnswered");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		HomeFeedOutDTO output = new HomeFeedOutDTO();
		output.setFeeds(new ArrayList<QuestionFeedSingleOutDTO>());

		try
		{
			User user = null;

			user = this.userServiceRead.getUserByUsernameAndToken(input.getUsername(), input.getSessionToken());

			if (user == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe session has expired. Please logout and try to login again.");
			}

			List<Question> feeds = this.questionServiceRead.getAnswered(user, input.getCountry(), input.getUntilDate(), input.getLimit(), input.getLastDate(), input.getLastID());

			for (Question feed : feeds)
			{
				QuestionSingleOutDTO questionDTO = new QuestionSingleOutDTO();
				questionDTO.setQuestionID(feed.getID());
				questionDTO.setQuestionText(feed.getComment());
				questionDTO.setInsertedAt(feed.getInsertedAt().toString());

				QuestionFeedSingleOutDTO feedDTO = new QuestionFeedSingleOutDTO();
				feedDTO.setQuestion(questionDTO);
				feedDTO.setAnswered(true);

				if (feed.getUser() != null)
				{
					feedDTO.setUsername(feed.getUser().getUsername());
					feedDTO.setAvatarURL(feed.getUser().getAvatarURL());
				}

				output.getFeeds().add(feedDTO);
			}
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
	@Path("/all")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getAll(HomeFeedGetAllInDTO input)
	{
		Logger.getLogger(this.getClass()).info("getAll");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		HomeFeedOutDTO output = new HomeFeedOutDTO();
		output.setFeeds(new ArrayList<QuestionFeedSingleOutDTO>());

		try
		{
			User user = null;

			user = this.userServiceRead.getUserByUsernameAndToken(input.getUsername(), input.getSessionToken());

			if (user == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe session has expired. Please logout and try to login again.");
			}

			List<Question> feeds = this.questionServiceRead.getAll(input.getCountry(), input.getUntilDate(), input.getLimit(), input.getLastDate(), input.getLastID());

			for (Question feed : feeds)
			{
				QuestionSingleOutDTO questionDTO = new QuestionSingleOutDTO();
				questionDTO.setQuestionID(feed.getID());
				questionDTO.setQuestionText(feed.getComment());
				questionDTO.setInsertedAt(feed.getInsertedAt().toString());

				QuestionFeedSingleOutDTO feedDTO = new QuestionFeedSingleOutDTO();
				feedDTO.setQuestion(questionDTO);

				Vote vote = this.voteServiceRead.getVote(feed, user);

				if (vote != null)
				{
					feedDTO.setAnswered(true);
				}
				else
				{
					feedDTO.setAnswered(false);
				}

				if (feed.getUser() != null)
				{
					feedDTO.setUsername(feed.getUser().getUsername());
					feedDTO.setAvatarURL(feed.getUser().getAvatarURL());
				}

				output.getFeeds().add(feedDTO);
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
