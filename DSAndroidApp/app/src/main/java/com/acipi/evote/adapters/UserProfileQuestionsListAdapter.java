package com.acipi.evote.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.acipi.evote.CastVoteActivity;
import com.acipi.evote.R;
import com.acipi.evote.VoteResultsActivity;
import com.acipi.evote.db.service.UserService;
import com.acipi.evote.rest.dto.vote.QuestionFeedSingleOutDTO;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UserProfileQuestionsListAdapter extends BaseAdapter implements OnClickListener
{
    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<QuestionFeedSingleOutDTO> mQuestionItems;
    private Intent mIntent;

    private UserService userService;
    private HashMap<String, Object> userDetails;

    public UserProfileQuestionsListAdapter(Activity mActivity, List<QuestionFeedSingleOutDTO> mQuestionItems)
    {
        this.mActivity = mActivity;
        this.mQuestionItems = mQuestionItems;
    }

    @Override
    public int getCount()
    {
        return mQuestionItems.size();
    }

    @Override
    public Object getItem(int location)
    {
        return mQuestionItems.get(location);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (mInflater == null)
        {
            mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.user_profile_questions_list_item, null);
        }

        userService = new UserService(mActivity);
        userDetails = userService.getUserDetails();

        QuestionFeedSingleOutDTO item = (QuestionFeedSingleOutDTO) getItem(position);

        TextView user_profile_photoPublishDate = (TextView) convertView.findViewById(R.id.user_profile_questionPublishDate);
        TextView user_profile_questionCaption = (TextView) convertView.findViewById(R.id.user_profile_questionCaption);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm", Locale.US);
        DateTime photoCreationDateTime = DateTime.parse(item.getQuestion().getInsertedAt());
        user_profile_photoPublishDate.setText(sdf.format(photoCreationDateTime.toDate()));

        user_profile_questionCaption.setText(item.getQuestion().getQuestionText());
        user_profile_questionCaption.setOnClickListener(this);
        user_profile_questionCaption.setTag(R.id.TAG_OBJECT, item);

        return convertView;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.user_profile_questionCaption)
        {
            QuestionFeedSingleOutDTO item = (QuestionFeedSingleOutDTO) v.getTag(R.id.TAG_OBJECT);

            if(item.getAnswered())
            {
                mIntent = new Intent(this.mActivity, VoteResultsActivity.class);
                mIntent.putExtra("questionID", item.getQuestion().getQuestionID());
            }
            else
            {
                mIntent = new Intent(this.mActivity, CastVoteActivity.class);
                mIntent.putExtra("questionID", item.getQuestion().getQuestionID());
            }

            this.mActivity.startActivity(mIntent);
        }
    }
}
