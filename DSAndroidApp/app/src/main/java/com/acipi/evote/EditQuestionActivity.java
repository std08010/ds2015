package com.acipi.evote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.acipi.evote.rest.dto.vote.AddQuestionInDTO;
import com.acipi.evote.rest.dto.vote.CastVoteInDTO;
import com.acipi.evote.rest.dto.vote.EditQuestionInDTO;
import com.acipi.evote.rest.dto.vote.OptionSingleInDTO;
import com.acipi.evote.rest.dto.vote.OptionSingleOutDTO;
import com.acipi.evote.rest.dto.vote.QuestionFeedSingleOutDTO;
import com.acipi.evote.rest.dto.vote.QuestionInDTO;
import com.acipi.evote.utils.PropertiesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditQuestionActivity extends Activity implements View.OnClickListener
{
    private UserService userService;
    private HashMap<String, Object> userDetails;

    private Long questionID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_question);

        if (getIntent().hasExtra("questionID"))
        {
            this.questionID = getIntent().getExtras().getLong("questionID");
        }

        userService = new UserService(this);
        userDetails = userService.getUserDetails();

        Button editQuestionBtn = (Button) findViewById(R.id.editQuestionBtn);
        editQuestionBtn.setOnClickListener(this);

        QuestionInDTO inDTO = new QuestionInDTO();
        inDTO.setQuestionID(questionID);
        inDTO.setUsername((String) userDetails.get(UserDAO.KEY_USERNAME));
        inDTO.setSessionToken((String) userDetails.get(UserDAO.KEY_SESSION_TOKEN));

        GetQuestionTask wst = new GetQuestionTask(GetQuestionTask.POST, this, "Loading question...", inDTO);
        wst.execute(new String[]{PropertiesUtils.getUrlsProps(this).getProperty("vote.question.get_question_for_edit")});
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.editQuestionBtn)
        {
            EditText questionInput = (EditText) findViewById(R.id.editQuestionCaption);
            EditText option1Input = (EditText) findViewById(R.id.option1Descr);
            EditText option2Input = (EditText) findViewById(R.id.option2Descr);
            EditText option3Input = (EditText) findViewById(R.id.option3Descr);
            EditText option4Input = (EditText) findViewById(R.id.option4Descr);
            EditText option5Input = (EditText) findViewById(R.id.option5Descr);
            EditText option6Input = (EditText) findViewById(R.id.option6Descr);
            String questionText = questionInput.getText().toString().trim();
            String option1Text = option1Input.getText().toString().trim();
            String option2Text = option2Input.getText().toString().trim();
            String option3Text = option3Input.getText().toString().trim();
            String option4Text = option4Input.getText().toString().trim();
            String option5Text = option5Input.getText().toString().trim();
            String option6Text = option6Input.getText().toString().trim();

            if (questionText.isEmpty())
            {
                UIHelper.displayMessage(this, "Please write something");
            }
            else
            {
                EditQuestionInDTO inDTO = new EditQuestionInDTO();
                inDTO.setUsername((String) userDetails.get(UserDAO.KEY_USERNAME));
                inDTO.setSessionToken((String) userDetails.get(UserDAO.KEY_SESSION_TOKEN));
                inDTO.setQuestionText(questionText);
                inDTO.setQuestionID(this.questionID);

                List<OptionSingleInDTO> options = new ArrayList<OptionSingleInDTO>();

                if (!option1Text.isEmpty())
                {
                    OptionSingleOutDTO option = (OptionSingleOutDTO) option1Input.getTag(R.id.TAG_OBJECT);

                    OptionSingleInDTO optionDTO = new OptionSingleInDTO();
                    optionDTO.setOptionText(option1Text);
                    optionDTO.setOptionID(option != null ? option.getOptionID() : new Long(-1));

                    options.add(optionDTO);
                }
                if (!option2Text.isEmpty())
                {
                    OptionSingleOutDTO option = (OptionSingleOutDTO) option2Input.getTag(R.id.TAG_OBJECT);

                    OptionSingleInDTO optionDTO = new OptionSingleInDTO();
                    optionDTO.setOptionText(option2Text);
                    optionDTO.setOptionID(option != null ? option.getOptionID() : new Long(-1));

                    options.add(optionDTO);
                }
                if (!option3Text.isEmpty())
                {
                    OptionSingleOutDTO option = (OptionSingleOutDTO) option3Input.getTag(R.id.TAG_OBJECT);

                    OptionSingleInDTO optionDTO = new OptionSingleInDTO();
                    optionDTO.setOptionText(option3Text);
                    optionDTO.setOptionID(option != null ? option.getOptionID() : new Long(-1));

                    options.add(optionDTO);
                }
                if (!option4Text.isEmpty())
                {
                    OptionSingleOutDTO option = (OptionSingleOutDTO) option4Input.getTag(R.id.TAG_OBJECT);

                    OptionSingleInDTO optionDTO = new OptionSingleInDTO();
                    optionDTO.setOptionText(option4Text);
                    optionDTO.setOptionID(option != null ? option.getOptionID() : new Long(-1));

                    options.add(optionDTO);
                }
                if (!option5Text.isEmpty())
                {
                    OptionSingleOutDTO option = (OptionSingleOutDTO) option5Input.getTag(R.id.TAG_OBJECT);

                    OptionSingleInDTO optionDTO = new OptionSingleInDTO();
                    optionDTO.setOptionText(option5Text);
                    optionDTO.setOptionID(option != null ? option.getOptionID() : new Long(-1));

                    options.add(optionDTO);
                }
                if (!option6Text.isEmpty())
                {
                    OptionSingleOutDTO option = (OptionSingleOutDTO) option6Input.getTag(R.id.TAG_OBJECT);

                    OptionSingleInDTO optionDTO = new OptionSingleInDTO();
                    optionDTO.setOptionText(option6Text);
                    optionDTO.setOptionID(option != null ? option.getOptionID() : new Long(-1));

                    options.add(optionDTO);
                }

                inDTO.setOptions(options);

                EditQuestionTask wst = new EditQuestionTask(EditQuestionTask.POST, this, "Adding Question...", inDTO);
                wst.execute(new String[]{PropertiesUtils.getUrlsProps(this).getProperty("vote.question.edit_question")});
            }
        }
    }

    private void readGetQuestionResponse(QuestionFeedSingleOutDTO outDTO)
    {
        if (outDTO.getQuestion() != null)
        {
            TextView editQuestionCaption = (TextView) findViewById(R.id.editQuestionCaption);
            editQuestionCaption.setText(outDTO.getQuestion().getQuestionText());
        }

        if (outDTO.getOptions() != null)
        {
            if (outDTO.getOptions().size() >= 1)
            {
                EditText option1 = (EditText) findViewById(R.id.option1Descr);
                OptionSingleOutDTO option1DTO = outDTO.getOptions().get(0);

                option1.setText(option1DTO.getOptionText());
                option1.setTag(R.id.TAG_OBJECT, option1DTO);
            }
            if (outDTO.getOptions().size() >= 2)
            {
                EditText option2 = (EditText) findViewById(R.id.option2Descr);
                OptionSingleOutDTO option2DTO = outDTO.getOptions().get(1);

                option2.setText(option2DTO.getOptionText());
                option2.setTag(R.id.TAG_OBJECT, option2DTO);
            }
            if (outDTO.getOptions().size() >= 3)
            {
                EditText option3 = (EditText) findViewById(R.id.option3Descr);
                OptionSingleOutDTO option3DTO = outDTO.getOptions().get(2);

                option3.setText(option3DTO.getOptionText());
                option3.setTag(R.id.TAG_OBJECT, option3DTO);
            }
            if (outDTO.getOptions().size() >= 4)
            {
                EditText option4 = (EditText) findViewById(R.id.option4Descr);
                OptionSingleOutDTO option4DTO = outDTO.getOptions().get(3);

                option4.setText(option4DTO.getOptionText());
                option4.setTag(R.id.TAG_OBJECT, option4DTO);
            }
            if (outDTO.getOptions().size() >= 5)
            {
                EditText option5 = (EditText) findViewById(R.id.option5Descr);
                OptionSingleOutDTO option5DTO = outDTO.getOptions().get(4);

                option5.setText(option5DTO.getOptionText());
                option5.setTag(R.id.TAG_OBJECT, option5DTO);
            }
            if (outDTO.getOptions().size() >= 6)
            {
                EditText option6 = (EditText) findViewById(R.id.option6Descr);
                OptionSingleOutDTO option6DTO = outDTO.getOptions().get(5);

                option6.setText(option6DTO.getOptionText());
                option6.setTag(R.id.TAG_OBJECT, option6DTO);
            }
        }
    }

    private void readGetQuestionError(ErrorDTO errorDTO)
    {
        UIHelper.displayMessage(this, errorDTO.getMessage());
    }

    private void readEditQuestionResponse(SimpleDTO outDTO)
    {
        UIHelper.displayMessage(this, outDTO.getResult());

        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("number", MainActivity.MY_QUESTIONS_FRAGMENT_NUMBER);
        this.startActivity(intent);

        this.finish();
    }

    private void readEditQuestionError(ErrorDTO errorDTO)
    {
        UIHelper.displayMessage(this, errorDTO.getMessage());
    }

    private class EditQuestionTask extends AbstractWebServiceTask<EditQuestionInDTO, SimpleDTO>
    {
        private Context myContext;
        private String processMessage = "Processing...";

        public EditQuestionTask(int taskType, Context myContext, String processMessage, EditQuestionInDTO inDTO)
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
            readEditQuestionResponse((SimpleDTO) outDTO);
            UIHelper.hideProgressDialog();
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readEditQuestionError(errorDTO);
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
