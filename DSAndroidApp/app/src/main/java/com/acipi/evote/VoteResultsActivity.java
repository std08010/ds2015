package com.acipi.evote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoteResultsActivity extends Activity implements View.OnClickListener
{
    private UserService userService;
    private HashMap<String, Object> userDetails;

    private Long questionID;
    private Long totalVotes = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_results);

        if (getIntent().hasExtra("questionID"))
        {
            this.questionID = getIntent().getExtras().getLong("questionID");
        }

        userService = new UserService(this);
        userDetails = userService.getUserDetails();

        QuestionInDTO inDTO = new QuestionInDTO();
        inDTO.setQuestionID(questionID);
        inDTO.setUsername((String) userDetails.get(UserDAO.KEY_USERNAME));
        inDTO.setSessionToken((String) userDetails.get(UserDAO.KEY_SESSION_TOKEN));

        GetQuestionTask wst = new GetQuestionTask(GetQuestionTask.POST, this, "Loading question...", inDTO);
        wst.execute(new String[]{PropertiesUtils.getUrlsProps(this).getProperty("vote.question.get_question_for_vote_results")});
    }

    @Override
    public void onClick(View v)
    {
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
            HorizontalBarChart chart = (HorizontalBarChart) findViewById(R.id.chart);

            BarData data = new BarData(getXAxisValues(), getDataSet(outDTO.getOptions()));
            chart.setData(data);
            chart.setDescription("Total Votes: " + this.totalVotes);
            chart.animateXY(2000, 2000);
            chart.invalidate();
        }
    }

    private ArrayList<BarDataSet> getDataSet(List<OptionSingleOutDTO> options)
    {
        ArrayList<BarDataSet> dataSets = new ArrayList<>();

        if (options.size() >= 1)
        {
            ArrayList<BarEntry> valueSet = new ArrayList<>();
            valueSet.add(new BarEntry(options.get(0).getVotesNum(), 0));
            this.totalVotes += options.get(0).getVotesNum();

            BarDataSet barDataSet = new BarDataSet(valueSet, options.get(0).getOptionText());
            barDataSet.setColor(Color.BLUE);

            dataSets.add(barDataSet);
        }
        if (options.size() >= 2)
        {
            ArrayList<BarEntry> valueSet = new ArrayList<>();
            valueSet.add(new BarEntry(options.get(1).getVotesNum(), 0));
            this.totalVotes += options.get(1).getVotesNum();

            BarDataSet barDataSet = new BarDataSet(valueSet, options.get(1).getOptionText());
            barDataSet.setColor(Color.CYAN);

            dataSets.add(barDataSet);
        }
        if (options.size() >= 3)
        {
            ArrayList<BarEntry> valueSet = new ArrayList<>();
            valueSet.add(new BarEntry(options.get(2).getVotesNum(), 0));
            this.totalVotes += options.get(2).getVotesNum();

            BarDataSet barDataSet = new BarDataSet(valueSet, options.get(2).getOptionText());
            barDataSet.setColor(Color.GREEN);

            dataSets.add(barDataSet);
        }
        if (options.size() >= 4)
        {
            ArrayList<BarEntry> valueSet = new ArrayList<>();
            valueSet.add(new BarEntry(options.get(3).getVotesNum(), 0));
            this.totalVotes += options.get(3).getVotesNum();

            BarDataSet barDataSet = new BarDataSet(valueSet, options.get(3).getOptionText());
            barDataSet.setColor(Color.MAGENTA);

            dataSets.add(barDataSet);
        }
        if (options.size() >= 5)
        {
            ArrayList<BarEntry> valueSet = new ArrayList<>();
            valueSet.add(new BarEntry(options.get(4).getVotesNum(), 0));
            this.totalVotes += options.get(4).getVotesNum();

            BarDataSet barDataSet = new BarDataSet(valueSet, options.get(4).getOptionText());
            barDataSet.setColor(Color.RED);

            dataSets.add(barDataSet);
        }
        if (options.size() >= 6)
        {
            ArrayList<BarEntry> valueSet = new ArrayList<>();
            valueSet.add(new BarEntry(options.get(5).getVotesNum(), 0));
            this.totalVotes += options.get(5).getVotesNum();

            BarDataSet barDataSet = new BarDataSet(valueSet, options.get(5).getOptionText());
            barDataSet.setColor(Color.YELLOW);

            dataSets.add(barDataSet);
        }

        return dataSets;
    }

    private ArrayList<String> getXAxisValues()
    {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Vote Results");
        return xAxis;
    }

    private void readGetQuestionError(ErrorDTO errorDTO)
    {
        UIHelper.displayMessage(this, errorDTO.getMessage());
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
