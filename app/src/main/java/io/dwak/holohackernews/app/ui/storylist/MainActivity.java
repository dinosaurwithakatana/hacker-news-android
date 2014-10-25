package io.dwak.holohackernews.app.ui.storylist;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import io.dwak.holohackernews.app.HoloHackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.manager.hackernews.FeedType;
import io.dwak.holohackernews.app.ui.NavigationDrawerItem;
import io.dwak.holohackernews.app.ui.about.AboutActivity;
import io.dwak.holohackernews.app.ui.settings.SettingsActivity;
import io.dwak.holohackernews.app.ui.storydetail.StoryDetailActivity;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        StoryListFragment.OnStoryListFragmentInteractionListener {

    public static final String STORY_ID = "STORY_ID";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            setSupportActionBar(toolbar);
        }
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(NavigationDrawerItem drawerItem) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment newFragment;
        switch (drawerItem.getId()) {
            case 0:
                mTitle = getString(R.string.title_section_top);
                newFragment = StoryListFragment.newInstance(FeedType.TOP);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, newFragment, StoryListFragment.class.getSimpleName())
                        .commit();
                setTitle(mTitle);
                break;
            case 1:
                mTitle = getString(R.string.title_section_best);
                newFragment = StoryListFragment.newInstance(FeedType.BEST);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, newFragment, StoryListFragment.class.getSimpleName())
                        .commit();
                setTitle(mTitle);
                break;
            case 2:
                mTitle = getString(R.string.title_section_newest);
                newFragment = StoryListFragment.newInstance(FeedType.NEW);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, newFragment, StoryListFragment.class.getSimpleName())
                        .commit();
                setTitle(mTitle);
                break;
            case 3:
                mTitle = getString(R.string.title_section_show);
                newFragment = StoryListFragment.newInstance(FeedType.SHOW);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, newFragment, StoryListFragment.class.getSimpleName())
                        .commit();
                setTitle(mTitle);
                break;
            case 4:
                mTitle = getString(R.string.title_section_show_new);
                newFragment = StoryListFragment.newInstance(FeedType.SHOW_NEW);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, newFragment, StoryListFragment.class.getSimpleName())
                        .commit();
                setTitle(mTitle);
                break;
            case 5:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case 6:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }

    }

    public void restoreActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
