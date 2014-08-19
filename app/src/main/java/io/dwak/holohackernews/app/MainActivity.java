package io.dwak.holohackernews.app;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Toast;

import io.dwak.holohackernews.app.manager.FeedType;

public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        StoryListFragment.OnStoryListFragmentInteractionListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String STORY_ID = "STORY_ID";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private String mStoryUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mStoryUrl = "";
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.Fragment newFragment = null;
        if (position < 3) {
            switch (position) {
                case 0:
                    newFragment = StoryListFragment.newInstance(FeedType.TOP);
                    break;
                case 1:
                    newFragment = StoryListFragment.newInstance(FeedType.BEST);
                    break;
                case 2:
                    newFragment = StoryListFragment.newInstance(FeedType.NEW);
                    break;


            }
            onSectionAttached(position + 1);
            if (fragmentManager.findFragmentByTag(mStoryUrl) != null
                    && fragmentManager.findFragmentByTag(mStoryUrl).isVisible()) {

                Fragment linkFragment = fragmentManager.findFragmentByTag(mStoryUrl);
                Fragment commentFragment = fragmentManager.findFragmentByTag(StoryCommentsFragment.class.getSimpleName());

                fragmentManager.beginTransaction()
                        .hide(linkFragment)
                        .remove(linkFragment)
                        .hide(commentFragment)
                        .remove(commentFragment)
                        .commit();
            }
            else if (fragmentManager.findFragmentByTag(StoryCommentsFragment.class.getSimpleName()) != null
                    && fragmentManager.findFragmentByTag(StoryCommentsFragment.class.getSimpleName()).isVisible()) {
                Fragment commentFragment = fragmentManager.findFragmentByTag(StoryCommentsFragment.class.getSimpleName());

                fragmentManager.beginTransaction()
                        .hide(commentFragment)
                        .remove(commentFragment)
                        .commit();
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.container, newFragment, StoryListFragment.class.getSimpleName())
                    .commit();
        }
        else {
            switch (position) {
                case 3:
                    Intent aboutIntent = new Intent(this, AboutActivity.class);
                    startActivity(aboutIntent);
                    break;
            }
        }
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
        setTitle(mTitle);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void setActionbarVisible(boolean visible) {
        if (visible) {
            getActionBar().show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setAttributes(params);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }
        else {
            getActionBar().hide();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
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
    public void onStoryListFragmentInteraction(long id) {
        if (HoloHackerNewsApplication.isDebug()) {
            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
        }

        Intent detailIntent = new Intent(this, StoryDetailActivity.class);
        detailIntent.putExtra(STORY_ID, id);
        startActivity(detailIntent);
        overridePendingTransition(R.anim.offscreen_left_to_view, R.anim.fadeout);
    }
}
