package com.acipi.evote.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.acipi.evote.MainActivity;
import com.acipi.evote.R;
import com.acipi.evote.db.dao.UserDAO;
import com.acipi.evote.db.service.UserService;
import com.acipi.evote.helpers.UIHelper;
import com.acipi.evote.rest.AbstractWebServiceTask;
import com.acipi.evote.rest.dto.AbstractOutDTO;
import com.acipi.evote.rest.dto.ErrorDTO;
import com.acipi.evote.rest.dto.SimpleDTO;
import com.acipi.evote.rest.dto.vote.AddQuestionInDTO;
import com.acipi.evote.utils.PropertiesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddQuestionFragment extends Fragment implements View.OnClickListener
{
    private OnFragmentInteractionListener mListener;
    private Context mContext;
    public View mView;

    private UserService userService;
    private HashMap<String, Object> userDetails;

    public static AddQuestionFragment newInstance()
    {
        AddQuestionFragment fragment = new AddQuestionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AddQuestionFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.mContext = getActivity().getApplicationContext();
        this.mView = inflater.inflate(R.layout.add_question, container, false);

        userService = new UserService(this.mContext);
        userDetails = userService.getUserDetails();

        Button submitQuestion = (Button) this.mView.findViewById(R.id.addQuestionBtn);
        submitQuestion.setOnClickListener(this);

        return this.mView;
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onAddQuestionFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                                         + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.addQuestionBtn)
        {
            EditText questionInput = (EditText) this.mView.findViewById(R.id.addQuestionCaption);
            EditText option1Input = (EditText) this.mView.findViewById(R.id.option1Descr);
            EditText option2Input = (EditText) this.mView.findViewById(R.id.option2Descr);
            EditText option3Input = (EditText) this.mView.findViewById(R.id.option3Descr);
            EditText option4Input = (EditText) this.mView.findViewById(R.id.option4Descr);
            EditText option5Input = (EditText) this.mView.findViewById(R.id.option5Descr);
            EditText option6Input = (EditText) this.mView.findViewById(R.id.option6Descr);
            String questionText = questionInput.getText().toString().trim();
            String option1Text = option1Input.getText().toString().trim();
            String option2Text = option2Input.getText().toString().trim();
            String option3Text = option3Input.getText().toString().trim();
            String option4Text = option4Input.getText().toString().trim();
            String option5Text = option5Input.getText().toString().trim();
            String option6Text = option6Input.getText().toString().trim();

            if (questionText.isEmpty())
            {
                UIHelper.displayMessage(this.mContext, "Please write something");
            }
            else
            {
                AddQuestionInDTO inDTO = new AddQuestionInDTO();
                inDTO.setUsername((String) userDetails.get(UserDAO.KEY_USERNAME));
                inDTO.setSessionToken((String) userDetails.get(UserDAO.KEY_SESSION_TOKEN));
                inDTO.setQuestionText(questionText);

                List<String> options = new ArrayList<String>();

                if(!option1Text.isEmpty())
                {
                    options.add(option1Text);
                }
                if(!option2Text.isEmpty())
                {
                    options.add(option2Text);
                }
                if(!option3Text.isEmpty())
                {
                    options.add(option3Text);
                }
                if(!option4Text.isEmpty())
                {
                    options.add(option4Text);
                }
                if(!option5Text.isEmpty())
                {
                    options.add(option5Text);
                }
                if(!option6Text.isEmpty())
                {
                    options.add(option6Text);
                }

                inDTO.setOptions(options);

                AddQuestionTask wst = new AddQuestionTask(AddQuestionTask.POST, this.mContext, "Adding Question...", inDTO);
                wst.execute(new String[]{PropertiesUtils.getUrlsProps(this.mContext).getProperty("vote.question.add_question")});
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        public void onAddQuestionFragmentInteraction(Uri uri);
    }

    private void readAddQuestionResponse(SimpleDTO outDTO)
    {
        UIHelper.displayMessage(this.mContext, outDTO.getResult());

        Intent intent = new Intent(this.mContext, MainActivity.class);

//        intent.putExtra("number", MainActivity.MY_PHOTOS_FRAGMENT_NUMBER);
        startActivity(intent);
    }

    private void readAddQuestionError(ErrorDTO errorDTO)
    {
        UIHelper.displayMessage(this.mContext, errorDTO.getMessage());
    }

    private class AddQuestionTask extends AbstractWebServiceTask<AddQuestionInDTO, SimpleDTO>
    {
        private Context myContext;
        private String processMessage = "Processing...";

        public AddQuestionTask(int taskType, Context myContext, String processMessage, AddQuestionInDTO inDTO)
        {
            super(taskType, inDTO, SimpleDTO.class);
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
            readAddQuestionResponse((SimpleDTO) outDTO);
//            UIHelper.hideProgressDialog();
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readAddQuestionError(errorDTO);
//            UIHelper.hideProgressDialog();
        }
    }
}
