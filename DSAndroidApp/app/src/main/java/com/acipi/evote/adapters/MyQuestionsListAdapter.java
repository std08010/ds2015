package com.acipi.evote.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acipi.evote.CastVoteActivity;
import com.acipi.evote.EditQuestionActivity;
import com.acipi.evote.MainActivity;
import com.acipi.evote.R;
import com.acipi.evote.VoteResultsActivity;
import com.acipi.evote.db.dao.UserDAO;
import com.acipi.evote.db.service.UserService;
import com.acipi.evote.helpers.UIHelper;
import com.acipi.evote.rest.AbstractWebServiceTask;
import com.acipi.evote.rest.dto.AbstractOutDTO;
import com.acipi.evote.rest.dto.ErrorDTO;
import com.acipi.evote.rest.dto.SimpleDTO;
import com.acipi.evote.rest.dto.vote.DeleteQuestionInDTO;
import com.acipi.evote.rest.dto.vote.QuestionFeedSingleOutDTO;
import com.acipi.evote.utils.PropertiesUtils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyQuestionsListAdapter extends BaseAdapter implements OnClickListener
{
    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<QuestionFeedSingleOutDTO> mPostsItems;
    private Intent mIntent;

    public MyQuestionsListAdapter(Activity mActivity, List<QuestionFeedSingleOutDTO> mPostsItems)
    {
        this.mActivity = mActivity;
        this.mPostsItems = mPostsItems;
    }

    @Override
    public int getCount()
    {
        return mPostsItems.size();
    }

    @Override
    public Object getItem(int location)
    {
        return mPostsItems.get(location);
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
            convertView = mInflater.inflate(R.layout.myquestions_list_item, null);
        }

        LinearLayout post = (LinearLayout) convertView.findViewById(R.id.post);

        QuestionFeedSingleOutDTO item = (QuestionFeedSingleOutDTO) getItem(position);

        post.setVisibility(View.VISIBLE);

        TextView postPublishDate = (TextView) convertView.findViewById(R.id.postPublishDate);
        TextView postCaption = (TextView) convertView.findViewById(R.id.postCaption);

        LinearLayout postButtonsArea = (LinearLayout) convertView.findViewById(R.id.postButtonsArea);

        if (item.getQuestion() != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
            DateTime photoCreationDateTime = DateTime.parse(item.getQuestion().getInsertedAt());
            postPublishDate.setText(sdf.format(photoCreationDateTime.toDate()));

            postCaption.setText(item.getQuestion().getQuestionText());

            postButtonsArea.setVisibility(View.VISIBLE);

            Button deleteQuestion = (Button) convertView.findViewById(R.id.deleteQuestion);
            Button editQuestion = (Button) convertView.findViewById(R.id.editQuestion);

            deleteQuestion.setOnClickListener(this);
            deleteQuestion.setTag(R.id.TAG_OBJECT, item);

            editQuestion.setOnClickListener(this);
            editQuestion.setTag(R.id.TAG_OBJECT, item);
        }

        return convertView;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.deleteQuestion)
        {
            final QuestionFeedSingleOutDTO item = (QuestionFeedSingleOutDTO) v.getTag(R.id.TAG_OBJECT);

            new AlertDialog.Builder(this.mActivity)
                    .setTitle("Delete Photo")
                    .setMessage(R.string.delete_poll_confirm)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            UserService userService = new UserService(mActivity);
                            HashMap<String, Object> userDetails = userService.getUserDetails();

                            DeleteQuestionInDTO inDTO = new DeleteQuestionInDTO();
                            inDTO.setUsername((String) userDetails.get(UserDAO.KEY_USERNAME));
                            inDTO.setSessionToken((String) userDetails.get(UserDAO.KEY_SESSION_TOKEN));
                            inDTO.setQuestionID(item.getQuestion() != null ? item.getQuestion().getQuestionID() : null);

                            DeleteQuestionTask wst = new DeleteQuestionTask(DeleteQuestionTask.POST, mActivity, "Deleting Poll...", inDTO);

                            wst.execute(new String[]{PropertiesUtils.getUrlsProps(mActivity).getProperty("vote.question.delete_question")});
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
        else if (v.getId() == R.id.editQuestion)
        {
            final QuestionFeedSingleOutDTO item = (QuestionFeedSingleOutDTO) v.getTag(R.id.TAG_OBJECT);

            mIntent = new Intent(this.mActivity, EditQuestionActivity.class);
            mIntent.putExtra("questionID", item.getQuestion().getQuestionID());

            this.mActivity.startActivity(mIntent);
        }
    }

    private void readDeleteQuestionResponse(SimpleDTO outDTO)
    {
        UIHelper.displayMessage(this.mActivity, outDTO.getResult());

        Intent intent = new Intent(this.mActivity, MainActivity.class);

        intent.putExtra("number", MainActivity.MY_QUESTIONS_FRAGMENT_NUMBER);
        this.mActivity.startActivity(intent);

        this.mActivity.finish();
    }

    private void readDeleteQuestionError(ErrorDTO errorDTO)
    {
        UIHelper.displayMessage(this.mActivity, errorDTO.getMessage());
    }

    private class DeleteQuestionTask extends AbstractWebServiceTask<DeleteQuestionInDTO, SimpleDTO>
    {
        private Context myContext;
        private String processMessage = "Processing...";

        public DeleteQuestionTask(int taskType, Context myContext, String processMessage, DeleteQuestionInDTO inDTO)
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
            readDeleteQuestionResponse((SimpleDTO) outDTO);
            UIHelper.hideProgressDialog();
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readDeleteQuestionError(errorDTO);
            UIHelper.hideProgressDialog();
        }
    }
}
