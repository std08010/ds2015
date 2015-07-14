package com.acipi.evote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.acipi.evote.adapters.UserProfileQuestionsListAdapter;
import com.acipi.evote.db.dao.UserDAO;
import com.acipi.evote.db.service.UserService;
import com.acipi.evote.helpers.UIHelper;
import com.acipi.evote.rest.AbstractWebServiceTask;
import com.acipi.evote.rest.dto.AbstractOutDTO;
import com.acipi.evote.rest.dto.ErrorDTO;
import com.acipi.evote.rest.dto.user.UserProfileGetAllInfoInDTO;
import com.acipi.evote.rest.dto.user.UserProfileGetAllInfoOutDTO;
import com.acipi.evote.rest.dto.vote.QuestionFeedSingleOutDTO;
import com.acipi.evote.rest.dto.vote.QuestionSingleOutDTO;
import com.acipi.evote.utils.PropertiesUtils;
import com.acipi.evote.utils.VolleySingleton;
import com.android.volley.toolbox.NetworkImageView;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

public class UserProfileActivity extends Activity implements OnClickListener
{
    private ListView photosList;
    private List<QuestionFeedSingleOutDTO> mQuestionItems;
    private UserProfileQuestionsListAdapter mQuestionsListAdapter;

    private UserService userService;
    private HashMap<String, Object> userDetails;

    private String username = "Anonymous";
    private String avatarURL;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        this.photosList = (ListView) findViewById(R.id.questionsList);

        if (getIntent().hasExtra("username"))
        {
            username = getIntent().getExtras().getString("username");
        }

        userService = new UserService(this);
        userDetails = userService.getUserDetails();

        UserProfileGetAllInfoInDTO inDTO = new UserProfileGetAllInfoInDTO();
        inDTO.setProfileUsername(username);
        inDTO.setUsername((String) userDetails.get(UserDAO.KEY_USERNAME));
        inDTO.setSessionToken((String) userDetails.get(UserDAO.KEY_SESSION_TOKEN));

        GetAllInfoTask wst = new GetAllInfoTask(GetAllInfoTask.POST, this, "Getting user's info...", inDTO);

        wst.execute(new String[]{PropertiesUtils.getUrlsProps(this).getProperty("user.profile.all_info")});
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.profilePicture)
        {
            Intent fullScreenIntent = new Intent(this, FullSceenImageActivity.class);
            fullScreenIntent.putExtra("imageURL", this.avatarURL);

            this.startActivity(fullScreenIntent);
//            mActivity.finish();
        }
    }

    private void readGetAllInfoResponse(UserProfileGetAllInfoOutDTO outDTO)
    {
        this.mQuestionItems = outDTO.getQuestions();
        this.mQuestionsListAdapter = new UserProfileQuestionsListAdapter(this, this.mQuestionItems);

        this.photosList.setAdapter(this.mQuestionsListAdapter);

        NetworkImageView profilePicture = (NetworkImageView) findViewById(R.id.profilePicture);
        TextView profileName = (TextView) findViewById(R.id.profileName);
        TextView profileCountry = (TextView) findViewById(R.id.profileCountry);
        NetworkImageView profileCountryFlag = (NetworkImageView) findViewById(R.id.profileCountryFlag);
        TextView userQuestionCount = (TextView) findViewById(R.id.userQuestionCount);

        profilePicture.setOnClickListener(this);

        NumberFormat nf = NumberFormat.getInstance();

        if (outDTO.getAvatarURL() != null)
        {
            this.avatarURL = outDTO.getAvatarURL();
            profilePicture.setImageUrl(outDTO.getAvatarURL(), VolleySingleton.getInstance(this).getImageLoader());
        }
        profileName.setText(outDTO.getUsername());
        profileCountry.setText(outDTO.getCountry());

        if (outDTO.getCountryFlagURL() != null)
        {
            profileCountryFlag.setImageUrl(outDTO.getCountryFlagURL(), VolleySingleton.getInstance(this).getImageLoader());
        }

        userQuestionCount.setText("Questions: " + nf.format(outDTO.getAllQuestions()));
    }

    private void readError(ErrorDTO errorDTO)
    {
        UIHelper.displayMessage(this, errorDTO.getMessage());
    }

    private class GetAllInfoTask extends AbstractWebServiceTask<UserProfileGetAllInfoInDTO, UserProfileGetAllInfoOutDTO>
    {
        private Context myContext;
        private String processMessage = "Processing...";

        public GetAllInfoTask(int taskType, Context myContext, String processMessage, UserProfileGetAllInfoInDTO inDTO)
        {
            super(taskType, inDTO, UserProfileGetAllInfoOutDTO.class);
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
            readGetAllInfoResponse((UserProfileGetAllInfoOutDTO) outDTO);
            UIHelper.hideProgressDialog();
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readError(errorDTO);
            UIHelper.hideProgressDialog();
        }
    }
}
