package io.dwak.holohackernews.app.ui.storylist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import io.dwak.holohackernews.app.HoloHackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModelActivity;
import io.dwak.holohackernews.app.ui.about.AboutActivity;
import io.dwak.holohackernews.app.ui.login.LoginActivity;
import io.dwak.holohackernews.app.ui.settings.SettingsActivity;
import io.dwak.holohackernews.app.ui.storydetail.StoryDetailActivity;
import io.dwak.holohackernews.app.ui.storydetail.StoryDetailFragment;

public class MainActivity extends BaseViewModelActivity<MainViewModel>
    implements StoryListFragment.OnStoryListFragmentInteractionListener {

    public static final String STORY_ID = "STORY_ID";
    public static final String DETAILS_CONTAINER_VISIBLE = "DETAILS_CONTAINER_VISIBLE";
    private CharSequence mTitle;
    private boolean mIsDualPane;
    private StoryDetailFragment mStoryDetailFragment;
    private View mDetailsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            setSupportActionBar(toolbar);
        }

        mTitle = getTitle();

        // Set up the drawer.
        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(new ProfileSettingDrawerItem()
                                     .withIdentifier(0)
                                     .withName("Login")
                                     .withDescription("Add an account")
                                     .withIcon(getResources().getDrawable(R.drawable.ic_add)))
                .withSavedInstance(savedInstanceState)
                .withProfileImagesVisible(true)
                .withHeaderBackground(R.drawable.account_header_bg)
                .withOnAccountHeaderListener((view, iProfile, b) -> {
                    switch (iProfile.getIdentifier()) {
                        case 0:
                            MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            return true;
                    }

                    return false;
                })
                .build();

        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(getViewModel().getDrawerItems().toArray(new IDrawerItem[getViewModel().getDrawerItems().size()]))
                .withOnDrawerItemClickListener((adapterView, view, i, l, iDrawerItem) -> {
                    Fragment storyListFragment;
                    switch (iDrawerItem.getIdentifier()) {
                        case 0:
                            mTitle = getString(R.string.title_section_top);
                            storyListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_TOP);
                            loadFeedList(storyListFragment);
                            break;
                        case 1:
                            mTitle = getString(R.string.title_section_best);
                            storyListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_BEST);
                            loadFeedList(storyListFragment);
                            break;
                        case 2:
                            mTitle = getString(R.string.title_section_newest);
                            storyListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_NEW);
                            loadFeedList(storyListFragment);
                            break;
                        case 3:
                            mTitle = getString(R.string.title_section_show);
                            storyListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_SHOW);
                            loadFeedList(storyListFragment);
                            break;
                        case 4:
                            mTitle = getString(R.string.title_section_show_new);
                            storyListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_SHOW_NEW);
                            loadFeedList(storyListFragment);
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
                    return false;
                })
                .build();

        drawer.setSelectionByIdentifier(0);

        mDetailsContainer = findViewById(R.id.details_container);
        // The attributes you want retrieved
        mIsDualPane = mDetailsContainer != null;

        if (savedInstanceState != null) {
            boolean detailsContainerVisible = savedInstanceState.getBoolean(DETAILS_CONTAINER_VISIBLE, false);
            if (mDetailsContainer != null && detailsContainerVisible) {
                mDetailsContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
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


    private void loadFeedList(Fragment storyListFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!(storyListFragment != null && fragmentManager.findFragmentByTag(StoryListFragment.class.getSimpleName() + mTitle) != null)) {
            fragmentManager.beginTransaction()
                           .replace(R.id.container, storyListFragment, StoryListFragment.class.getSimpleName() + mTitle)
                           .commit();
            setTitle(mTitle);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onStoryListFragmentInteraction(long id) {
        if (HoloHackerNewsApplication.isDebug()) {
            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
        }

        if (mIsDualPane && mDetailsContainer != null) {
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
