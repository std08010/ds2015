package com.acipi.evote.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.acipi.evote.R;
import com.acipi.evote.adapters.MyQuestionsListAdapter;
import com.acipi.evote.db.dao.UserDAO;
import com.acipi.evote.db.service.UserService;
import com.acipi.evote.helpers.UIHelper;
import com.acipi.evote.rest.AbstractWebServiceTask;
import com.acipi.evote.rest.dto.AbstractOutDTO;
import com.acipi.evote.rest.dto.ErrorDTO;
import com.acipi.evote.rest.dto.user.MyQuestionsGetAllQuestionsInDTO;
import com.acipi.evote.rest.dto.vote.HomeFeedOutDTO;
import com.acipi.evote.rest.dto.vote.QuestionFeedSingleOutDTO;
import com.acipi.evote.utils.PropertiesUtils;

import java.util.HashMap;
import java.util.List;

public class MyQuestionsFragment extends Fragment
{
    private ListView questionsList;

    private List<QuestionFeedSingleOutDTO> mQuestionsItems;
    private MyQuestionsListAdapter mQuestionsListAdapter;

    private OnFragmentInteractionListener mListener;
    private Context mContext;
    public View mView, footerView, headerView;

    private UserService userService;
    private HashMap<String, Object> userDetails;

    public static MyQuestionsFragment newInstance()
    {
        MyQuestionsFragment fragment = new MyQuestionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MyQuestionsFragment()
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
        this.mView = inflater.inflate(R.layout.fragment_myquestions, container, false);

        this.questionsList = (ListView) mView.findViewById(R.id.questionsList);

        headerView = inflater.inflate(R.layout.myquestions_list_header, null);
        footerView = inflater.inflate(R.layout.myquestions_list_footer, null);
        questionsList.addHeaderView(headerView);
        questionsList.addFooterView(footerView);

        userService = new UserService(this.mContext);
        userDetails = userService.getUserDetails();

        MyQuestionsGetAllQuestionsInDTO inDTO = new MyQuestionsGetAllQuestionsInDTO();
        inDTO.setUsername((String) userDetails.get(UserDAO.KEY_USERNAME));
        inDTO.setSessionToken((String) userDetails.get(UserDAO.KEY_SESSION_TOKEN));

        GetAllQuestionsTask wst = new GetAllQuestionsTask(GetAllQuestionsTask.POST, this.getActivity(), "Loading questions...", inDTO);

        // the passed String is the URL we will POST to
        wst.execute(new String[]{PropertiesUtils.getUrlsProps(this.getActivity()).getProperty("vote.question.my_questions")});

        return this.mView;
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onMyPhotosFragmentInteraction(uri);
        }
    }

    private void readGetAllQuestionsResponse(HomeFeedOutDTO outDTO)
    {
        ((TextView) this.mView.findViewById(R.id.myQuestions_info)).setVisibility(View.GONE);

        mQuestionsItems = outDTO.getFeeds();
        mQuestionsListAdapter = new MyQuestionsListAdapter(getActivity(), mQuestionsItems);

        questionsList.setAdapter(mQuestionsListAdapter);
    }

    private void readGetAllQuestionsError(ErrorDTO errorDTO)
    {
        displayMyQuestionsInfo(errorDTO.getMessage());
    }

    public void displayMyQuestionsInfo(String info)
    {
        TextView myPhotos_info = (TextView) this.mView.findViewById(R.id.myQuestions_info);
        myPhotos_info.setText(info);
        myPhotos_info.setVisibility(View.VISIBLE);
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
        public void onMyPhotosFragmentInteraction(Uri uri);
    }

    private class GetAllQuestionsTask extends AbstractWebServiceTask<MyQuestionsGetAllQuestionsInDTO, HomeFeedOutDTO>
    {
        private Context myContext;
        private String processMessage = "Processing...";

        public GetAllQuestionsTask(int taskType, Context myContext, String processMessage, MyQuestionsGetAllQuestionsInDTO inDTO)
        {
            super(taskType, inDTO, HomeFeedOutDTO.class);
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
            readGetAllQuestionsResponse((HomeFeedOutDTO) outDTO);
            UIHelper.hideProgressDialog();
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readGetAllQuestionsError(errorDTO);
            UIHelper.hideProgressDialog();
        }
    }
}
