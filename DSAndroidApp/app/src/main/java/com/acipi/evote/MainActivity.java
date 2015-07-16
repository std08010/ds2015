package com.acipi.evote;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.acipi.evote.db.service.UserService;
import com.acipi.evote.fragments.AddQuestionFragment;
import com.acipi.evote.fragments.HomeFragment;
import com.acipi.evote.fragments.MyQuestionsFragment;
import com.acipi.evote.fragments.NavigationDrawerFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, AddQuestionFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, MyQuestionsFragment.OnFragmentInteractionListener
{
    public static final int HOME_FRAGMENT_NUMBER = 0;
    public static final int ADD_QUESTION_FRAGMENT_NUMBER = 1;
    public static final int MY_QUESTIONS_FRAGMENT_NUMBER = 2;
    public static final int SETTINGS_FRAGMENT_NUMBER = 3;
    public static final int LOGOUT_FRAGMENT_NUMBER = 4;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {
        int pos = position;

        //hack to navigate to the fragment you want when you create MainActivity for the first time and not to the position 0 which is the default
        if (getIntent().hasExtra("number"))
        {
            pos = getIntent().getExtras().getInt("number");
            getIntent().removeExtra("number");
        }

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.container, PlaceholderFragment.newInstance(pos))
                       .commit();
    }

    public void onSectionAttached(int number)
    {
        String[] navMenuTitles = getResources().getStringArray(R.array.navDrawerItems);
        Fragment mFragment = null;
        Intent mIntent = null;

        switch (number)
        {
            case HOME_FRAGMENT_NUMBER:
                mTitle = navMenuTitles[HOME_FRAGMENT_NUMBER];
                mFragment = HomeFragment.newInstance();
                break;
            case ADD_QUESTION_FRAGMENT_NUMBER:
                mTitle = navMenuTitles[ADD_QUESTION_FRAGMENT_NUMBER];
                mFragment = AddQuestionFragment.newInstance();
                break;
            case MY_QUESTIONS_FRAGMENT_NUMBER:
                mTitle = navMenuTitles[MY_QUESTIONS_FRAGMENT_NUMBER];
                mFragment = MyQuestionsFragment.newInstance();
                break;
            case SETTINGS_FRAGMENT_NUMBER:
                mTitle = navMenuTitles[SETTINGS_FRAGMENT_NUMBER];
                break;
            case LOGOUT_FRAGMENT_NUMBER:
                UserService userService = new UserService(getApplicationContext());
                userService.logoutUser();

                // Switching to LoginActivity
                mIntent = new Intent(getApplicationContext(), LoginActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
        }

        if (mFragment != null && mIntent == null)
        {
            FragmentManager mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        }
        else if (mIntent != null && mFragment == null)
        {
            startActivity(mIntent);
            finish();
        }
    }

    public void restoreActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (!mNavigationDrawerFragment.isDrawerOpen())
        {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddQuestionFragmentInteraction(Uri uri)
    {

    }

    @Override
    public void onHomeFragmentInteraction(Uri uri)
    {

    }

    @Override
    public void onMyPhotosFragmentInteraction(Uri uri)
    {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity)
        {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
