package io.dwak.holohackernews.app.ui.storylist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import io.dwak.holohackernews.app.HackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModelActivity;
import io.dwak.holohackernews.app.dagger.component.DaggerViewModelComponent;
import io.dwak.holohackernews.app.ui.about.AboutActivity;
import io.dwak.holohackernews.app.ui.login.LoginActivity;
import io.dwak.holohackernews.app.ui.settings.SettingsActivity;
import io.dwak.holohackernews.app.ui.storydetail.StoryDetailActivity;
import io.dwak.holohackernews.app.ui.storydetail.StoryDetailFragment;
import rx.android.observables.AndroidObservable;

public class MainActivity extends BaseViewModelActivity<MainViewModel>
        implements StoryListFragment.OnStoryListFragmentInteractionListener {

    public static final String STORY_ID = "STORY_ID";
    public static final String DETAILS_CONTAINER_VISIBLE = "DETAILS_CONTAINER_VISIBLE";

    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @Optional @InjectView(R.id.details_container) View mDetailsContainer;

    @Inject MainViewModel mViewModel;

    private CharSequence mTitle;
    private boolean mIsDualPane;
    private StoryDetailFragment mStoryDetailFragment;
    private Drawer mDrawer;
    private AccountHeader mAccountHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        DaggerViewModelComponent.builder()
                                .appModule(HackerNewsApplication.getAppModule())
                                .appComponent(HackerNewsApplication.getAppComponent())
                                .build()
                                .inject(this);
        mTitle = getTitle();

        // Set up the drawer.
        mAccountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(mViewModel.getProfileItems())
                .withSavedInstance(savedInstanceState)
                .withProfileImagesVisible(true)
                .withHeaderBackground(getResources().getDrawable(R.drawable.orange_button))
                .withOnAccountHeaderListener((view, iProfile, b) -> {
                    switch (iProfile.getIdentifier()) {
                        case MainViewModel.ADD_ACCOUNT_PROFILE_ITEM:
                            MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            return true;
                        case MainViewModel.LOG_OUT_PROFILE_ITEM:
                            new AlertDialog.Builder(this)
                                    .setMessage(R.string.logout_dialog_message)
                                    .setPositiveButton(R.string.logout_confirm_button, (dialog, which) -> {
                                        mViewModel.logout();
                                    })
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .create()
                                    .show();
                            break;
                    }

                    return false;
                })
                .build();

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(mAccountHeader)
                .withAnimateDrawerItems(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(mViewModel.getDrawerItems().toArray(new IDrawerItem[mViewModel.getDrawerItems().size()]))
                .withOnDrawerItemClickListener((adapterView, view, i, l, iDrawerItem) -> {
                    Fragment storyListFragment;
                    switch (iDrawerItem.getIdentifier()) {
                        case MainViewModel.SECTION_TOP:
                            mTitle = getString(R.string.title_section_top);
                            storyListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_TOP);
                            loadFeedList(storyListFragment);
                            break;
                        case MainViewModel.SECTION_BEST:
                            mTitle = getString(R.string.title_section_best);
                            storyListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_BEST);
                            loadFeedList(storyListFragment);
                            break;
                        case MainViewModel.SECTION_NEWEST:
                            mTitle = getString(R.string.title_section_newest);
                            storyListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_NEW);
                            loadFeedList(storyListFragment);
                            break;
                        case MainViewModel.SECTION_SHOW_HN:
                            mTitle = getString(R.string.title_section_show);
                            storyListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_SHOW);
                            loadFeedList(storyListFragment);
                            break;
                        case MainViewModel.SECTION_SHOW_HN_NEW:
                            mTitle = getString(R.string.title_section_show_new);
                            storyListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_SHOW_NEW);
                            loadFeedList(storyListFragment);
                            break;
                        case MainViewModel.SECTION_ASK:
                            mTitle = getString(R.string.title_section_ask);
                            storyListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_ASK);
                            loadFeedList(storyListFragment);
                            break;
                        case MainViewModel.SECTION_SAVED:
                            mTitle = getString(R.string.title_section_saved);
                            storyListFragment = StoryListFragment.newInstance(StoryListViewModel.FEED_TYPE_SAVED);
                            loadFeedList(storyListFragment);
                            break;
                        case MainViewModel.SECTION_SETTINGS:
                            Intent settingsIntent = new Intent(this, SettingsActivity.class);
                            startActivity(settingsIntent);
                            break;
                        case MainViewModel.SECTION_ABOUT:
                            Intent aboutIntent = new Intent(this, AboutActivity.class);
                            startActivity(aboutIntent);
                            break;
                    }
                    return false;
                })
                .build();

        mDrawer.setSelectionByIdentifier(0);

        if (mToolbar != null) {
            mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            setSupportActionBar(mToolbar);
        }

        // The attributes you want retrieved
        mIsDualPane = mDetailsContainer != null;

        if (savedInstanceState != null) {
            boolean detailsContainerVisible = savedInstanceState.getBoolean(DETAILS_CONTAINER_VISIBLE, false);
            if (mDetailsContainer != null && detailsContainerVisible) {
                mDetailsContainer.setVisibility(View.VISIBLE);
            }
        }

        AndroidObservable.fromLocalBroadcast(this, new IntentFilter(LoginActivity.LOGIN_SUCCESS))
                         .subscribe(intent -> refreshNavigationDrawer(true));

        AndroidObservable.fromLocalBroadcast(this, new IntentFilter(LoginActivity.LOGOUT))
                         .subscribe(intent -> refreshNavigationDrawer(false));
    }

    private void refreshNavigationDrawer(boolean loggedIn) {
        if (loggedIn) {
            for (IProfile iProfile : mViewModel.getLoggedOutProfileItem()) {
                mAccountHeader.removeProfile(iProfile);
            }
            mAccountHeader.addProfiles(mViewModel.getLoggedInProfileItem());
        }
        else {
            for (IProfile iProfile : mViewModel.getLoggedInProfileItem()) {
                mAccountHeader.removeProfile(iProfile);
            }
            mAccountHeader.addProfiles(mViewModel.getLoggedOutProfileItem());
            mViewModel.clearLoggedInProfileItem();
        }

        mAccountHeader.toggleSelectionList(this);
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
    public void onStoryListFragmentInteraction(long id, boolean saved) {
        if (HackerNewsApplication.isDebug()) {
            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
        }

        if (mIsDualPane && mDetailsContainer != null) {
            mStoryDetailFragment = StoryDetailFragment.newInstance(id, saved);
            mDetailsContainer.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.details_container, mStoryDetailFragment)
                                       .commit();
        }
        else {
            Intent detailIntent = new Intent(this, StoryDetailActivity.class);
            detailIntent.putExtra(STORY_ID, id);
            detailIntent.putExtra(StoryDetailFragment.LOADING_FROM_SAVED, saved);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawer.getActionBarDrawerToggle().onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public MainViewModel getViewModel() {
        return mViewModel;
    }
}
