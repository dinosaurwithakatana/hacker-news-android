package io.dwak.holohackernews.app.ui.storydetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.preferences.UserPreferenceManager;
import io.dwak.holohackernews.app.ui.storylist.MainActivity;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import tv.acfun.a63.swipe.SwipeAppCompatActivity;

public class StoryDetailActivity extends SwipeAppCompatActivity {

    @InjectView(R.id.toolbar) Toolbar mToolbar;
    private StoryDetailFragment mStoryDetailFragment;
    private boolean mSaved;
    private boolean mFromExternal;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        long storyId = 0;
        mFromExternal = false;
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                storyId = extras.getLong(MainActivity.STORY_ID);
                mSaved = extras.getBoolean(StoryDetailFragment.LOADING_FROM_SAVED);
            }

            final Uri data = intent.getData();
            if (data != null) {
                storyId = Long.parseLong(data.getQueryParameter("id"));
                mFromExternal = true;
            }
        }
        if (mToolbar != null) {
            mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(UserPreferenceManager.getInstance().isSwipeBackEnabled()) {
            getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        }
        else{
            getSwipeBackLayout().setEnableGesture(false);
        }
        if (savedInstanceState == null) {
            mStoryDetailFragment = StoryDetailFragment.newInstance(storyId, mSaved);
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.container, mStoryDetailFragment)
                                       .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mStoryDetailFragment != null && mStoryDetailFragment.isLinkViewVisible() && !UserPreferenceManager.getInstance().showLinkFirst()) {
            mStoryDetailFragment.hideLinkView();
        }
        else {
            super.onBackPressed();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            }
            else {
                overridePendingTransition(R.anim.fadein, R.anim.view_right_to_offscreen);
            }
        }
    }
}
