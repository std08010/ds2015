package com.acipi.evote.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.acipi.evote.R;
import com.acipi.evote.adapters.QuestionFeedsListAdapter;
import com.acipi.evote.db.dao.UserDAO;
import com.acipi.evote.db.service.UserService;
import com.acipi.evote.rest.AbstractWebServiceTask;
import com.acipi.evote.rest.dto.AbstractOutDTO;
import com.acipi.evote.rest.dto.ErrorDTO;
import com.acipi.evote.rest.dto.vote.HomeFeedGetAllInDTO;
import com.acipi.evote.rest.dto.vote.HomeFeedGetAnsweredInDTO;
import com.acipi.evote.rest.dto.vote.HomeFeedGetUnansweredInDTO;
import com.acipi.evote.rest.dto.vote.HomeFeedOutDTO;
import com.acipi.evote.rest.dto.vote.QuestionFeedSingleOutDTO;
import com.acipi.evote.utils.InfiniteScrollListView;
import com.acipi.evote.utils.InfiniteScrollListener;
import com.acipi.evote.utils.InfiniteScrollOnScrollListener;
import com.acipi.evote.utils.PropertiesUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment implements InfiniteScrollListener
{
    public static final int UNANSWERED_TYPE = 0;
    public static final int ANSWERED_TYPE = 1;
    public static final int ALL_TYPE = 2;

    public static final int LAST_DAY_TIME = 0;
    public static final int LAST_WEEK_TIME = 1;
    public static final int LAST_MONTH_TIME = 2;
    public static final int LAST_YEAR_TIME = 3;
    public static final int ALL_TIME_TIME = 4;

    public static final int INTERNATIONAL_PLACE = 0;
    public static final int LOCAL_PLACE = 1;

    private OnFragmentInteractionListener mListener;
    private Context mContext;
    public View mView, headerView, footerView;

    private InfiniteScrollListView questionFeedsList;
    private InfiniteScrollOnScrollListener scrollListener;
    private QuestionFeedsListAdapter mQuestionFeedsListAdapter;

    private QuestionFeedSingleOutDTO lastQuestionFeedItem;

    private int typeCategory, timeCategory, placeCategory;
    private boolean typeFirstTime = true, timeFirstTime = true, placeFirstTime = true;

    private UserService userService;
    private HashMap<String, Object> userDetails;

    private boolean executing = false;

    public static HomeFragment newInstance()
    {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment()
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
        this.mView = inflater.inflate(R.layout.fragment_home, container, false);
        this.headerView = inflater.inflate(R.layout.question_feed_list_header, null);
        this.footerView = inflater.inflate(R.layout.question_feed_list_footer, null);

        userService = new UserService(this.mContext);
        userDetails = userService.getUserDetails();

        this.setupHeaderSpinners();
        this.setupListView();

        return this.mView;
    }

    public void setupListView()
    {
        this.questionFeedsList = (InfiniteScrollListView) mView.findViewById(R.id.questionFeedsList);
        this.scrollListener = new InfiniteScrollOnScrollListener(this);
        this.questionFeedsList.setListener(this.scrollListener);

        this.questionFeedsList.addHeaderView(headerView);
        this.questionFeedsList.addFooterView(footerView);

        mQuestionFeedsListAdapter = new QuestionFeedsListAdapter(getActivity());
        this.questionFeedsList.setAdapter(mQuestionFeedsListAdapter);

        endIsNear();
    }

    public void setupHeaderSpinners()
    {
        Spinner typeSpinner = (Spinner) headerView.findViewById(R.id.question_feed_type);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.homeFeedTypeCategories));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                typeCategory = position;
                if (!typeFirstTime)
                {
                    questionFeedsList.resetItems();
                    lastQuestionFeedItem = null;
                    executing = false;
                    endIsNear();
                }
                else
                {
                    typeFirstTime = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        Spinner timeSpinner = (Spinner) headerView.findViewById(R.id.question_feed_time);
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.homeFeedTimeCategories));
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                timeCategory = position;
                if (!timeFirstTime)
                {
                    questionFeedsList.resetItems();
                    lastQuestionFeedItem = null;
                    executing = false;
                    endIsNear();
                }
                else
                {
                    timeFirstTime = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        Spinner placeSpinner = (Spinner) headerView.findViewById(R.id.question_feed_place);
        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.homeFeedPlaceCategories));
        placeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placeSpinner.setAdapter(placeAdapter);
        placeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                placeCategory = position;
                if (!placeFirstTime)
                {
                    questionFeedsList.resetItems();
                    lastQuestionFeedItem = null;
                    executing = false;
                    endIsNear();
                }
                else
                {
                    placeFirstTime = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    @Override
    public synchronized void endIsNear()
    {
        if (!executing)
        {
            executing = true;
            loadMore();
        }
    }

    public void loadMore()
    {
        String country;
        String untilDate;

        switch (this.placeCategory)
        {
            case LOCAL_PLACE:
                UserService userService = new UserService(this.mContext);
                HashMap<String, Object> userDetails = userService.getUserDetails();
                country = (String) userDetails.get(UserDAO.KEY_COUNTRY);
                if (country == null)
                {
                    country = getResources().getString(R.string.default_local_country);
                }
                break;
            default:
                country = null;
                break;
        }

        DateTime now = DateTime.now();
        switch (this.timeCategory)
        {
            case LAST_DAY_TIME:
                untilDate = now.minusDays(1).toString();
                break;
            case LAST_WEEK_TIME:
                untilDate = now.minusWeeks(1).toString();
                break;
            case LAST_MONTH_TIME:
                untilDate = now.minusMonths(1).toString();
                break;
            case LAST_YEAR_TIME:
                untilDate = now.minusYears(1).toString();
                break;
            default:
                untilDate = null;
                break;
        }

        switch (this.typeCategory)
        {
            case UNANSWERED_TYPE:
                HomeFeedGetUnansweredInDTO inUnansweredDTO = new HomeFeedGetUnansweredInDTO();
                inUnansweredDTO.setUsername((String) userDetails.get(UserDAO.KEY_USERNAME));
                inUnansweredDTO.setSessionToken((String) userDetails.get(UserDAO.KEY_SESSION_TOKEN));
                inUnansweredDTO.setCountry(country);
                inUnansweredDTO.setUntilDate(untilDate);
                inUnansweredDTO.setLimit(Integer.parseInt(PropertiesUtils.getSysProps(this.getActivity()).getProperty("home.feed.limit")));

                if (this.lastQuestionFeedItem != null && this.lastQuestionFeedItem.getQuestion() != null)
                {
                    DateTime lastDateTime = DateTime.parse(lastQuestionFeedItem.getQuestion().getInsertedAt());
                    inUnansweredDTO.setLastDate(lastDateTime.withZone(DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss:SSS"));
                    inUnansweredDTO.setLastID(lastQuestionFeedItem.getQuestion().getQuestionID());
                }

                GetUnansweredTask wstRecent = new GetUnansweredTask(GetUnansweredTask.POST, this.getActivity(), "Loading feed...", inUnansweredDTO);
                wstRecent.execute(new String[]{PropertiesUtils.getUrlsProps(this.getActivity()).getProperty("vote.home_feed.unanswered")});
                break;
            case ANSWERED_TYPE:
                HomeFeedGetAnsweredInDTO inAnsweredDTO = new HomeFeedGetAnsweredInDTO();
                inAnsweredDTO.setUsername((String) userDetails.get(UserDAO.KEY_USERNAME));
                inAnsweredDTO.setSessionToken((String) userDetails.get(UserDAO.KEY_SESSION_TOKEN));
                inAnsweredDTO.setCountry(country);
                inAnsweredDTO.setUntilDate(untilDate);
                inAnsweredDTO.setLimit(Integer.parseInt(PropertiesUtils.getSysProps(this.getActivity()).getProperty("home.feed.limit")));

                if (this.lastQuestionFeedItem != null && this.lastQuestionFeedItem.getQuestion() != null)
                {
                    DateTime lastDateTime = DateTime.parse(lastQuestionFeedItem.getQuestion().getInsertedAt());
                    inAnsweredDTO.setLastDate(lastDateTime.withZone(DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss:SSS"));
                    inAnsweredDTO.setLastID(lastQuestionFeedItem.getQuestion().getQuestionID());
                }

                GetAnsweredTask wstLiked = new GetAnsweredTask(GetAnsweredTask.POST, this.getActivity(), "Loading feed...", inAnsweredDTO);
                wstLiked.execute(new String[]{PropertiesUtils.getUrlsProps(this.getActivity()).getProperty("vote.home_feed.answered")});
                break;
            case ALL_TYPE:
                HomeFeedGetAllInDTO inAllDTO = new HomeFeedGetAllInDTO();
                inAllDTO.setUsername((String) userDetails.get(UserDAO.KEY_USERNAME));
                inAllDTO.setSessionToken((String) userDetails.get(UserDAO.KEY_SESSION_TOKEN));
                inAllDTO.setCountry(country);
                inAllDTO.setUntilDate(untilDate);
                inAllDTO.setLimit(Integer.parseInt(PropertiesUtils.getSysProps(this.getActivity()).getProperty("home.feed.limit")));

                if (this.lastQuestionFeedItem != null && this.lastQuestionFeedItem.getQuestion() != null)
                {
                    DateTime lastDateTime = DateTime.parse(lastQuestionFeedItem.getQuestion().getInsertedAt());
                    inAllDTO.setLastDate(lastDateTime.withZone(DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss:SSS"));
                    inAllDTO.setLastID(lastQuestionFeedItem.getQuestion().getQuestionID());
                }

                GetAllTask wstDisliked = new GetAllTask(GetAllTask.POST, this.getActivity(), "Loading feed...", inAllDTO);
                wstDisliked.execute(new String[]{PropertiesUtils.getUrlsProps(this.getActivity()).getProperty("vote.home_feed.all")});
                break;
            default:
                this.questionFeedsList.appendItems(null, "Don't know this!!");
                this.executing = false;
                break;
        }
    }

    private void readGetQuestionsResponse(HomeFeedOutDTO outDTO)
    {
        this.questionFeedsList.appendItems((ArrayList) outDTO.getFeeds(), null);

        if (outDTO != null && outDTO.getFeeds() != null && outDTO.getFeeds().size() > 0)
        {
            this.lastQuestionFeedItem = outDTO.getFeeds().get(outDTO.getFeeds().size() - 1);
        }

        this.executing = false;
    }

    private void readGetQuestionsError(ErrorDTO errorDTO)
    {
        this.questionFeedsList.appendItems(null, errorDTO.getMessage());
        this.executing = false;
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onHomeFragmentInteraction(uri);
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

    public interface OnFragmentInteractionListener
    {
        public void onHomeFragmentInteraction(Uri uri);
    }

    private class GetUnansweredTask extends AbstractWebServiceTask<HomeFeedGetUnansweredInDTO, HomeFeedOutDTO>
    {
        private Context myContext;
        private String processMessage = "Processing...";

        public GetUnansweredTask(int taskType, Context myContext, String processMessage, HomeFeedGetUnansweredInDTO inDTO)
        {
            super(taskType, inDTO, HomeFeedOutDTO.class);
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
            readGetQuestionsResponse((HomeFeedOutDTO) outDTO);
//            UIHelper.hideProgressDialog();
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readGetQuestionsError(errorDTO);
//            UIHelper.hideProgressDialog();
        }
    }

    private class GetAnsweredTask extends AbstractWebServiceTask<HomeFeedGetAnsweredInDTO, HomeFeedOutDTO>
    {
        private Context myContext;
        private String processMessage = "Processing...";

        public GetAnsweredTask(int taskType, Context myContext, String processMessage, HomeFeedGetAnsweredInDTO inDTO)
        {
            super(taskType, inDTO, HomeFeedOutDTO.class);
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
            readGetQuestionsResponse((HomeFeedOutDTO) outDTO);
//            UIHelper.hideProgressDialog();
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readGetQuestionsError(errorDTO);
//            UIHelper.hideProgressDialog();
        }
    }

    private class GetAllTask extends AbstractWebServiceTask<HomeFeedGetAllInDTO, HomeFeedOutDTO>
    {
        private Context myContext;
        private String processMessage = "Processing...";

        public GetAllTask(int taskType, Context myContext, String processMessage, HomeFeedGetAllInDTO inDTO)
        {
            super(taskType, inDTO, HomeFeedOutDTO.class);
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
            readGetQuestionsResponse((HomeFeedOutDTO) outDTO);
//            UIHelper.hideProgressDialog();
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readGetQuestionsError(errorDTO);
//            UIHelper.hideProgressDialog();
        }
    }
}
