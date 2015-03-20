package io.dwak.holohackernews.app.ui.storylist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import io.dwak.holohackernews.app.HoloHackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseActivity;
import io.dwak.holohackernews.app.preferences.UserPreferenceManager;
import io.dwak.holohackernews.app.ui.NavigationDrawerItem;
import io.dwak.holohackernews.app.ui.about.AboutActivity;
import io.dwak.holohackernews.app.ui.settings.SettingsActivity;
import io.dwak.holohackernews.app.ui.storydetail.StoryDetailActivity;
import io.dwak.holohackernews.app.ui.storydetail.StoryDetailFragment;

public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        StoryListFragment.OnStoryListFragmentInteractionListener {

    public static final String STORY_ID = "STORY_ID";
    public static final String DETAILS_CONTAINER_VISIBLE = "DETAILS_CONTAINER_VISIBLE";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private boolean mIsDualPane;
    private StoryDetailFragment mStoryDetailFragment;
    private Fragment mStoryListFragment;
    private View mDetailsContainer;
    private Toolbar mToolbar;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(UserPreferenceManager.isNightModeEnabled(this) ? R.style.AppThemeNight : R.style.AppTheme);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            setSupportActionBar(mToolbar);
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mDetailsContainer = findViewById(R.id.details_container);
        // The attributes you want retrieved
        mIsDualPane = mDetailsContainer != null;

        if(savedInstanceState != null){
            boolean detailsContainerVisible = savedInstanceState.getBoolean(DETAILS_CONTAINER_VISIBLE, false);
            if(mDetailsContainer != null && detailsContainerVisible){
                mDetailsContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mDetailsContainer != null) {
            outState.putBoolean(DETAILS_CONTAINER_VISIBLE, mDetailsContainer.getVisibility() == View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNavigationDrawerItemSelected(NavigationDrawerItem drawerItem) {
        // update the main content by replacing fragments
        if(drawerItem != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            switch (drawerItem.getId()) {
                case 0:
                    mTitle = getString(R.string.title_section_top);
                    mStoryListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_TOP);
                    loadFeedList(fragmentManager);
                    break;
                case 1:
                    mTitle = getString(R.string.title_section_best);
                    mStoryListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_BEST);
                    loadFeedList(fragmentManager);
                    break;
                case 2:
                    mTitle = getString(R.string.title_section_newest);
                    mStoryListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_NEW);
                    loadFeedList(fragmentManager);
                    break;
                case 3:
                    mTitle = getString(R.string.title_section_show);
                    mStoryListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_SHOW);
                    loadFeedList(fragmentManager);
                    break;
                case 4:
                    mTitle = getString(R.string.title_section_show_new);
                    mStoryListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_SHOW_NEW);
                    loadFeedList(fragmentManager);
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
    }

    private void loadFeedList(FragmentManager fragmentManager) {
        if(!(mStoryListFragment!=null && fragmentManager.findFragmentByTag(StoryListFragment.class.getSimpleName() + mTitle) !=null)){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mStoryListFragment, StoryListFragment.class.getSimpleName() + mTitle)
                    .commit();
            setTitle(mTitle);
        }
    }

    public void restoreActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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

    @SuppressLint("NewApi")
    @Override
    public void onStoryListFragmentInteraction(long id) {
        if (HoloHackerNewsApplication.isDebug()) {
            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
        }

        if(mIsDualPane && mDetailsContainer !=null){
            mStoryDetailFragment = StoryDetailFragment.newInstance(id);
            mDetailsContainer.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_container, mStoryDetailFragment)
                    .commit();
        }
        else {
            Intent detailIntent = new Intent(this, StoryDetailActivity.class);
            detailIntent.putExtra(STORY_ID, id);
            startActivity(detailIntent);
            overridePendingTransition(R.anim.offscreen_left_to_view, R.anim.fadeout);
        }
    }

    @Override
    public void onBackPressed() {
        if (mIsDualPane && mStoryDetailFragment != null && mStoryDetailFragment.isLinkViewVisible()) {
            mStoryDetailFragment.hideLinkView();
        }
        else {
            super.onBackPressed();
        }
    }
}
