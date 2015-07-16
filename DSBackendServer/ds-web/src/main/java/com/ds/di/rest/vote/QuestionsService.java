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

import com.ds.di.dto.rest.SimpleDTO;
import com.ds.di.dto.rest.user.MyQuestionsGetAllQuestionsInDTO;
import com.ds.di.dto.rest.vote.AddQuestionInDTO;
import com.ds.di.dto.rest.vote.CastVoteInDTO;
import com.ds.di.dto.rest.vote.DeleteQuestionInDTO;
import com.ds.di.dto.rest.vote.EditQuestionInDTO;
import com.ds.di.dto.rest.vote.HomeFeedOutDTO;
import com.ds.di.dto.rest.vote.OptionSingleInDTO;
import com.ds.di.dto.rest.vote.OptionSingleOutDTO;
import com.ds.di.dto.rest.vote.QuestionFeedSingleOutDTO;
import com.ds.di.dto.rest.vote.QuestionInDTO;
import com.ds.di.dto.rest.vote.QuestionSingleOutDTO;
import com.ds.di.model.user.User;
import com.ds.di.model.vote.Option;
import com.ds.di.model.vote.Question;
import com.ds.di.model.vote.Vote;
import com.ds.di.service.user.UserServiceRead;
import com.ds.di.service.vote.OptionService;
import com.ds.di.service.vote.OptionServiceRead;
import com.ds.di.service.vote.QuestionService;
import com.ds.di.service.vote.QuestionServiceRead;
import com.ds.di.service.vote.VoteService;
import com.ds.di.service.vote.VoteServiceRead;
import com.ds.di.utils.RestServiceUtils;

/**
 * @author Altin Cipi
 *
 */
@Component(value = QuestionsService.SPRING_KEY)
@Transactional(value = "transactionManager")
@Path("/secure/vote/questions")
public class QuestionsService
{
	public static final String	SPRING_KEY	= "com.ds.di.rest.vote.QuestionsService";

	@Autowired
	@Qualifier(UserServiceRead.SPRING_KEY)
	private UserServiceRead		userServiceRead;

	@Autowired
	@Qualifier(QuestionService.SPRING_KEY)
	private QuestionService		questionService;

	@Autowired
	@Qualifier(QuestionServiceRead.SPRING_KEY)
	private QuestionServiceRead	questionServiceRead;

	@Autowired
	@Qualifier(OptionService.SPRING_KEY)
	private OptionService		optionService;

	@Autowired
	@Qualifier(OptionServiceRead.SPRING_KEY)
	private OptionServiceRead	optionServiceRead;

	@Autowired
	@Qualifier(VoteService.SPRING_KEY)
	private VoteService			voteService;

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
	@Path("/add_question")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addQuestion(AddQuestionInDTO input)
	{
		Logger.getLogger(this.getClass()).info("addQuestion");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		SimpleDTO output = new SimpleDTO();
		output.setResult("Poll was created successfully");

		try
		{
			User user = this.userServiceRead.getUserByUsernameAndToken(input.getUsername(), input.getSessionToken());

			if (user == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe session has expired. Please logout and try to login again.");
			}

			Question question = new Question();
			question.setComment(input.getQuestionText());
			question.setUser(user);
			this.questionService.create(question);

			for (String optionText : input.getOptions())
			{
				Option option = new Option();
				option.setComment(optionText);
				option.setQuestion(question);
				this.optionService.create(option);
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
	@Path("/get_question_for_cast_vote")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getQuestionForCastVote(QuestionInDTO input)
	{
		Logger.getLogger(this.getClass()).info("getQuestionForCastVote");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		QuestionFeedSingleOutDTO output = new QuestionFeedSingleOutDTO();

		try
		{
			User user = null;

			user = this.userServiceRead.getUserByUsernameAndToken(input.getUsername(), input.getSessionToken());

			if (user == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe session has expired. Please logout and try to login again.");
			}

			Question question = null;

			question = this.questionServiceRead.find(input.getQuestionID());

			if (question == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "Question was not found. Please try again.");
			}

			Vote vote = this.voteServiceRead.getVote(question, user);

			if (vote != null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "You have already voted for this question. You cannot view this page.");
			}

			QuestionSingleOutDTO questionDTO = new QuestionSingleOutDTO();
			questionDTO.setQuestionID(question.getID());
			questionDTO.setQuestionText(question.getComment());
			questionDTO.setInsertedAt(question.getInsertedAt().toString());

			output.setQuestion(questionDTO);

			if (question.getUser() != null)
			{
				output.setUsername(question.getUser().getUsername());
				output.setAvatarURL(question.getUser().getAvatarURL());
			}

			output.setOptions(new ArrayList<OptionSingleOutDTO>());

			List<Option> options = this.optionServiceRead.getOptions(question);

			for (Option option : options)
			{
				OptionSingleOutDTO optionSingleOutDTO = new OptionSingleOutDTO();
				optionSingleOutDTO.setOptionID(option.getID());
				optionSingleOutDTO.setOptionText(option.getComment());
				optionSingleOutDTO.setInsertedAt(option.getInsertedAt().toString());

				output.getOptions().add(optionSingleOutDTO);
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
	@Path("/cast_vote")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response castVote(CastVoteInDTO input)
	{
		Logger.getLogger(this.getClass()).info("castVote");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		SimpleDTO output = new SimpleDTO();
		output.setResult("Your vote was cast successfully");

		try
		{
			User user = null;

			user = this.userServiceRead.getUserByUsernameAndToken(input.getUsername(), input.getSessionToken());

			if (user == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe session has expired. Please logout and try to login again.");
			}

			Question question = null;

			question = this.questionServiceRead.find(input.getQuestionID());

			if (question == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "Question was not found. Please try again.");
			}

			Option option = null;

			option = this.optionServiceRead.find(input.getOptionID());

			if (option == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "Option was not found. Please try again.");
			}

			Vote vote = this.voteServiceRead.getVote(question, user);

			if (vote != null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "You have already voted for this question. You cannot vote again.");
			}

			vote = new Vote();
			vote.setOption(option);
			vote.setQuestion(question);
			vote.setUser(user);
			this.voteService.create(vote);
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
	@Path("/get_question_for_vote_results")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getQuestionForVoteResults(QuestionInDTO input)
	{
		Logger.getLogger(this.getClass()).info("getQuestionForVoteResults");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		QuestionFeedSingleOutDTO output = new QuestionFeedSingleOutDTO();

		try
		{
			User user = null;

			user = this.userServiceRead.getUserByUsernameAndToken(input.getUsername(), input.getSessionToken());

			if (user == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe session has expired. Please logout and try to login again.");
			}

			Question question = null;

			question = this.questionServiceRead.find(input.getQuestionID());

			if (question == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "Question was not found. Please try again.");
			}

			Vote vote = this.voteServiceRead.getVote(question, user);

			if (vote == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "You have not voted for this question yet. You cannot view this page.");
			}

			QuestionSingleOutDTO questionDTO = new QuestionSingleOutDTO();
			questionDTO.setQuestionID(question.getID());
			questionDTO.setQuestionText(question.getComment());
			questionDTO.setInsertedAt(question.getInsertedAt().toString());

			output.setQuestion(questionDTO);

			if (question.getUser() != null)
			{
				output.setUsername(question.getUser().getUsername());
				output.setAvatarURL(question.getUser().getAvatarURL());
			}

			output.setOptions(new ArrayList<OptionSingleOutDTO>());

			List<Option> options = this.optionServiceRead.getOptions(question);

			for (Option option : options)
			{
				OptionSingleOutDTO optionSingleOutDTO = new OptionSingleOutDTO();
				optionSingleOutDTO.setOptionID(option.getID());
				optionSingleOutDTO.setOptionText(option.getComment());
				optionSingleOutDTO.setInsertedAt(option.getInsertedAt().toString());
				optionSingleOutDTO.setVotesNum(this.voteServiceRead.getVotesNum(option));

				output.getOptions().add(optionSingleOutDTO);
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
	@Path("/get_question_for_edit")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getQuestionForEdit(QuestionInDTO input)
	{
		Logger.getLogger(this.getClass()).info("getQuestionForEdit");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		QuestionFeedSingleOutDTO output = new QuestionFeedSingleOutDTO();

		try
		{
			User user = null;

			user = this.userServiceRead.getUserByUsernameAndToken(input.getUsername(), input.getSessionToken());

			if (user == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe session has expired. Please logout and try to login again.");
			}

			Question question = null;

			question = this.questionServiceRead.getQuestion(user, input.getQuestionID());

			if (question == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "Question was not found. Please try again.");
			}

			QuestionSingleOutDTO questionDTO = new QuestionSingleOutDTO();
			questionDTO.setQuestionID(question.getID());
			questionDTO.setQuestionText(question.getComment());
			questionDTO.setInsertedAt(question.getInsertedAt().toString());

			output.setQuestion(questionDTO);

			if (question.getUser() != null)
			{
				output.setUsername(question.getUser().getUsername());
				output.setAvatarURL(question.getUser().getAvatarURL());
			}

			output.setOptions(new ArrayList<OptionSingleOutDTO>());

			List<Option> options = this.optionServiceRead.getOptions(question);

			for (Option option : options)
			{
				OptionSingleOutDTO optionSingleOutDTO = new OptionSingleOutDTO();
				optionSingleOutDTO.setOptionID(option.getID());
				optionSingleOutDTO.setOptionText(option.getComment());
				optionSingleOutDTO.setInsertedAt(option.getInsertedAt().toString());

				output.getOptions().add(optionSingleOutDTO);
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
	@Path("/delete_question")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response deleteQuestion(DeleteQuestionInDTO input)
	{
		Logger.getLogger(this.getClass()).info("deleteQuestion");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		SimpleDTO output = new SimpleDTO();
		output.setResult("Question was deleted successfully");

		try
		{
			User user = this.userServiceRead.getUserByUsernameAndToken(input.getUsername(), input.getSessionToken());

			if (user == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe session has expired. Please logout and try to login again.");
			}

			Question question = this.questionServiceRead.getQuestion(user, input.getQuestionID());

			if (question == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "Question was not found. Please try again.");
			}

			this.questionService.delete(question);
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
	@Path("/edit_question")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response editQuestion(EditQuestionInDTO input)
	{
		Logger.getLogger(this.getClass()).info("editQuestion");
		
		if (input == null)
		{
			return RestServiceUtils.getErrorResponse(Status.BAD_REQUEST, "No input data received. Please check and resend.");
		}

		SimpleDTO output = new SimpleDTO();
		output.setResult("Question was edited successfully");

		try
		{
			User user = this.userServiceRead.getUserByUsernameAndToken(input.getUsername(), input.getSessionToken());

			if (user == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "User was not found. Maybe session has expired. Please logout and try to login again.");
			}

			Question question = this.questionServiceRead.getQuestion(user, input.getQuestionID());

			if (question == null)
			{
				return RestServiceUtils.getErrorResponse(Status.CONFLICT, "Question was not found. Please try again.");
			}

			question.setComment(input.getQuestionText());
			question = this.questionService.update(question);

			for (OptionSingleInDTO optionDTO : input.getOptions())
			{
				Option option = this.optionServiceRead.getOption(question, optionDTO.getOptionID());

				if (option != null)
				{
					option.setComment(optionDTO.getOptionText());
					this.optionService.update(option);
				}
				else
				{
					option = new Option();
					option.setQuestion(question);
					option.setComment(optionDTO.getOptionText());
					this.optionService.create(option);
				}
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
	@Path("/my_questions")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMyQuestions(MyQuestionsGetAllQuestionsInDTO input)
	{
		Logger.getLogger(this.getClass()).info("getMyQuestions");
		
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

			List<Question> feeds = this.questionServiceRead.getQuestions(user);

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
