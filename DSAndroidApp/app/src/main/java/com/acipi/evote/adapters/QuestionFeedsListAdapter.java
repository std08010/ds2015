package com.acipi.evote.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acipi.evote.CastVoteActivity;
import com.acipi.evote.R;
import com.acipi.evote.UserProfileActivity;
import com.acipi.evote.VoteResultsActivity;
import com.acipi.evote.rest.dto.vote.QuestionFeedSingleOutDTO;
import com.acipi.evote.utils.InfiniteScrollAdapter;
import com.acipi.evote.utils.VolleySingleton;
import com.android.volley.toolbox.NetworkImageView;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class QuestionFeedsListAdapter extends InfiniteScrollAdapter implements OnClickListener
{
    private Activity mActivity;
    private List<QuestionFeedSingleOutDTO> mQuestionFeedsItems;
    private Intent mIntent;
    private String errorMessage;

    public QuestionFeedsListAdapter(Activity mActivity)
    {
        super(mActivity);
        this.mActivity = mActivity;
        this.mQuestionFeedsItems = new ArrayList<QuestionFeedSingleOutDTO>();
    }

    public QuestionFeedsListAdapter(Activity mActivity, List<QuestionFeedSingleOutDTO> mQuestionFeedsItems)
    {
        super(mActivity);
        this.mActivity = mActivity;
        this.mQuestionFeedsItems = mQuestionFeedsItems;
    }

    @Override
    public List getItems()
    {
        return mQuestionFeedsItems;
    }

    @Override
    public void addItems(Collection items, String errorMessage)
    {
        if (items != null)
        {
            if (items.size() > 0)
            {
                this.mQuestionFeedsItems.addAll(items);
            }
            else
            {
                super.setDoneLoading();
            }
            this.errorMessage = null;
        }
        else
        {
            this.errorMessage = errorMessage;
        }

        notifyDataSetChanged();
    }

    @Override
    public void resetItems()
    {
        this.mQuestionFeedsItems.clear();
        super.resetDoneLoading();

        notifyDataSetChanged();
    }

    @Override
    public Object getRealItem(int position)
    {
        return mQuestionFeedsItems.get(position);
    }

    @Override
    public View getLoadingView(LayoutInflater inflater, ViewGroup parent)
    {
        View v = inflater.inflate(R.layout.list_loading, null);

        TextView loadingText = (TextView) v.findViewById(R.id.loadingText);

        if (errorMessage != null)
        {
            loadingText.setText(errorMessage);
        }
        else
        {
            loadingText.setText(R.string.loadingText);
        }

        return v;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getRealView(LayoutInflater inflater, int position, View convertView, ViewGroup parent)
    {
        View v = inflater.inflate(R.layout.question_feed_list_item, null);

        QuestionFeedSingleOutDTO item = (QuestionFeedSingleOutDTO) getItem(position);

        NetworkImageView ownerAvatar = (NetworkImageView) v.findViewById(R.id.ownerAvatar);
        ownerAvatar.setOnClickListener(this);
        ownerAvatar.setTag(R.id.TAG_OBJECT, item);

        TextView ownerUsername = (TextView) v.findViewById(R.id.ownerUsername);
        ownerUsername.setOnClickListener(this);
        ownerUsername.setTag(R.id.TAG_OBJECT, item);

        TextView ownerFeedPublishDate = (TextView) v.findViewById(R.id.ownerFeedPublishDate);
        TextView question_feedCaption = (TextView) v.findViewById(R.id.question_feedCaption);

        if (item.getAvatarURL() != null)
        {
            ownerAvatar.setImageUrl(item.getAvatarURL(), VolleySingleton.getInstance(this.mActivity).getImageLoader());
        }
        ownerUsername.setText(item.getUsername());

        if (item.getQuestion() != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm", Locale.US);
            DateTime questionCreationDateTime = DateTime.parse(item.getQuestion().getInsertedAt());
            ownerFeedPublishDate.setText(sdf.format(questionCreationDateTime.toDate()));

            question_feedCaption.setText(item.getQuestion().getQuestionText());
            question_feedCaption.setOnClickListener(this);
            question_feedCaption.setTag(R.id.TAG_OBJECT, item);
        }

        return v;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.ownerAvatar || v.getId() == R.id.ownerUsername)
        {
            QuestionFeedSingleOutDTO item = (QuestionFeedSingleOutDTO) v.getTag(R.id.TAG_OBJECT);

            mIntent = new Intent(this.mActivity, UserProfileActivity.class);
            mIntent.putExtra("username", item.getUsername());

            this.mActivity.startActivity(mIntent);
        }
        else if (v.getId() == R.id.question_feedCaption)
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
