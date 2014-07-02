package io.dwak.holohackernews.app;

import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        StoryListFragment.OnStoryListFragmentInteractionListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


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
                    newFragment = StoryListFragment.newInstance(StoryListFragment.FeedType.TOP);
                    break;
                case 1:
                    newFragment = StoryListFragment.newInstance(StoryListFragment.FeedType.BEST);
                    break;
                case 2:
                    newFragment = StoryListFragment.newInstance(StoryListFragment.FeedType.NEW);
                    break;


            }
            onSectionAttached(position + 1);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, newFragment, StoryListFragment.class.getSimpleName())
                    .commit();
        } else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStoryListFragmentInteraction(long id, View view) {
        if (HoloHackerNewsApplication.isDebug()) {
            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
        }

        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra(DetailActivity.STORY_ID, id);
        Log.d(TAG, String.valueOf(Build.VERSION.SDK_INT));
        startActivity(detailIntent);
    }
}
