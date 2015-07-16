package com.acipi.evote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.acipi.evote.db.dao.UserDAO;
import com.acipi.evote.db.service.UserService;
import com.acipi.evote.helpers.UIHelper;
import com.acipi.evote.rest.AbstractWebServiceTask;
import com.acipi.evote.rest.dto.AbstractOutDTO;
import com.acipi.evote.rest.dto.ErrorDTO;
import com.acipi.evote.rest.dto.SimpleDTO;
import com.acipi.evote.rest.dto.vote.CastVoteInDTO;
import com.acipi.evote.rest.dto.vote.OptionSingleOutDTO;
import com.acipi.evote.rest.dto.vote.QuestionFeedSingleOutDTO;
import com.acipi.evote.rest.dto.vote.QuestionInDTO;
import com.acipi.evote.utils.PropertiesUtils;

import java.util.HashMap;

public class CastVoteActivity extends Activity implements View.OnClickListener
{
    private UserService userService;
    private HashMap<String, Object> userDetails;

    private Long questionID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cast_vote);

        if (getIntent().hasExtra("questionID"))
        {
            this.questionID = getIntent().getExtras().getLong("questionID");
        }

        userService = new UserService(this);
        userDetails = userService.getUserDetails();

        Button cancelVote = (Button) findViewById(R.id.cancelVote);
        cancelVote.setOnClickListener(this);

        Button castVote = (Button) findViewById(R.id.castVote);
        castVote.setOnClickListener(this);

        QuestionInDTO inDTO = new QuestionInDTO();
        inDTO.setQuestionID(questionID);
        inDTO.setUsername((String) userDetails.get(UserDAO.KEY_USERNAME));
        inDTO.setSessionToken((String) userDetails.get(UserDAO.KEY_SESSION_TOKEN));

        GetQuestionTask wst = new GetQuestionTask(GetQuestionTask.POST, this, "Loading question...", inDTO);
        wst.execute(new String[]{PropertiesUtils.getUrlsProps(this).getProperty("vote.question.get_question_for_cast_vote")});
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.castVote)
        {
            RadioGroup options = (RadioGroup) findViewById(R.id.options);

            int selectedId = options.getCheckedRadioButtonId();

            RadioButton selectedOption = (RadioButton) findViewById(selectedId);

            OptionSingleOutDTO option = (OptionSingleOutDTO) selectedOption.getTag(R.id.TAG_OBJECT);

            CastVoteInDTO inDTO = new CastVoteInDTO();
            inDTO.setUsername((String) userDetails.get(UserDAO.KEY_USERNAME));
            inDTO.setSessionToken((String) userDetails.get(UserDAO.KEY_SESSION_TOKEN));
            inDTO.setQuestionID(questionID);
            inDTO.setOptionID(option.getOptionID());

            CastVoteTask wst = new CastVoteTask(CastVoteTask.POST, this, "Casting Vote...", inDTO);
            wst.execute(new String[]{PropertiesUtils.getUrlsProps(this).getProperty("vote.question.cast_vote")});
        }
        else if (v.getId() == R.id.cancelVote)
        {
            Intent intent = new Intent(this, MainActivity.class);

            intent.putExtra("number", MainActivity.HOME_FRAGMENT_NUMBER);
            startActivity(intent);

            finish();
        }
    }

    private void readGetQuestionResponse(QuestionFeedSingleOutDTO outDTO)
    {
        if (outDTO.getQuestion() != null)
        {
            TextView questionCaption = (TextView) findViewById(R.id.questionCaption);
            questionCaption.setText(outDTO.getQuestion().getQuestionText());
        }

        if (outDTO.getOptions() != null)
        {
            RadioGroup options = (RadioGroup) findViewById(R.id.options);

            if (outDTO.getOptions().size() >= 1)
            {
                RadioButton option1 = (RadioButton) findViewById(R.id.option1);
                OptionSingleOutDTO option1DTO = outDTO.getOptions().get(0);

                option1.setText(option1DTO.getOptionText());
                option1.setTag(R.id.TAG_OBJECT, option1DTO);
                option1.setVisibility(View.VISIBLE);
            }
            if (outDTO.getOptions().size() >= 2)
            {
                RadioButton option2 = (RadioButton) findViewById(R.id.option2);
                OptionSingleOutDTO option2DTO = outDTO.getOptions().get(1);

                option2.setText(option2DTO.getOptionText());
                option2.setTag(R.id.TAG_OBJECT, option2DTO);
                option2.setVisibility(View.VISIBLE);
            }
            if (outDTO.getOptions().size() >= 3)
            {
                RadioButton option3 = (RadioButton) findViewById(R.id.option3);
                OptionSingleOutDTO option3DTO = outDTO.getOptions().get(2);

                option3.setText(option3DTO.getOptionText());
                option3.setTag(R.id.TAG_OBJECT, option3DTO);
                option3.setVisibility(View.VISIBLE);
            }
            if (outDTO.getOptions().size() >= 4)
            {
                RadioButton option4 = (RadioButton) findViewById(R.id.option4);
                OptionSingleOutDTO option4DTO = outDTO.getOptions().get(3);

                option4.setText(option4DTO.getOptionText());
                option4.setTag(R.id.TAG_OBJECT, option4DTO);
                option4.setVisibility(View.VISIBLE);
            }
            if (outDTO.getOptions().size() >= 5)
            {
                RadioButton option5 = (RadioButton) findViewById(R.id.option5);
                OptionSingleOutDTO option5DTO = outDTO.getOptions().get(4);

                option5.setText(option5DTO.getOptionText());
                option5.setTag(R.id.TAG_OBJECT, option5DTO);
                option5.setVisibility(View.VISIBLE);
            }
            if (outDTO.getOptions().size() >= 6)
            {
                RadioButton option6 = (RadioButton) findViewById(R.id.option6);
                OptionSingleOutDTO option6DTO = outDTO.getOptions().get(5);

                option6.setText(option6DTO.getOptionText());
                option6.setTag(R.id.TAG_OBJECT, option6DTO);
                option6.setVisibility(View.VISIBLE);
            }
        }
    }

    private void readGetQuestionError(ErrorDTO errorDTO)
    {
        UIHelper.displayMessage(this, errorDTO.getMessage());
    }

    private void readCastVoteResponse(SimpleDTO outDTO)
    {
        UIHelper.displayMessage(this, outDTO.getResult());

        Intent intent = new Intent(this, VoteResultsActivity.class);
        intent.putExtra("questionID", questionID);

        startActivity(intent);

        finish();
    }

    private void readCastVoteError(ErrorDTO errorDTO)
    {
        UIHelper.displayMessage(this, errorDTO.getMessage());
    }

    private class CastVoteTask extends AbstractWebServiceTask<CastVoteInDTO, SimpleDTO>
    {
        private Context myContext;
        private String processMessage = "Processing...";

        public CastVoteTask(int taskType, Context myContext, String processMessage, CastVoteInDTO inDTO)
        {
            super(taskType, inDTO, SimpleDTO.class);
            this.myContext = myContext;
            this.processMessage = processMessage;
        }

        @Override
        public void onInitActions()
        {
            UIHelper.showProgressDialog(this.myContext, this.processMessage);
        }

        @Override
        public void handleResponse(AbstractOutDTO outDTO)
        {
            readCastVoteResponse((SimpleDTO) outDTO);
            UIHelper.hideProgressDialog();
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readCastVoteError(errorDTO);
            UIHelper.hideProgressDialog();
        }
    }

    private class GetQuestionTask extends AbstractWebServiceTask<QuestionInDTO, QuestionFeedSingleOutDTO>
    {
        private Context myContext;
        private String processMessage = "Processing...";

        public GetQuestionTask(int taskType, Context myContext, String processMessage, QuestionInDTO inDTO)
        {
            super(taskType, inDTO, QuestionFeedSingleOutDTO.class);
            this.myContext = myContext;
            this.processMessage = processMessage;
        }

        @Override
        public void onInitActions()
        {
//            UIHelper.showProgressDialog(this.myContext, this.processMessage);
        }

        @Override
        public void handleResponse(AbstractOutDTO outDTO)
        {
            readGetQuestionResponse((QuestionFeedSingleOutDTO) outDTO);
//            UIHelper.hideProgressDialog();
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readGetQuestionError(errorDTO);
//            UIHelper.hideProgressDialog();
        }
    }
}
