package io.dwak.holohackernews.app.ui.storydetail;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.ui.storylist.MainActivity;

public class StoryDetailActivity extends ActionBarActivity{

    private StoryDetailFragment mStoryDetailFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar !=null){
            toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
            toolbar.setNavigationOnClickListener(v -> navigateUpToFromChild(StoryDetailActivity.this,
                    IntentCompat.makeMainActivity(new ComponentName(StoryDetailActivity.this, MainActivity.class))));
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            setSupportActionBar(toolbar);
        }
//        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
//        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        Intent intent = getIntent();
        long storyId = 0;
        if(intent !=null){
            Bundle extras = intent.getExtras();
            if(extras!=null){
                storyId = extras.getLong(MainActivity.STORY_ID);
            }
        }
        if (savedInstanceState == null) {
            mStoryDetailFragment = StoryDetailFragment.newInstance(storyId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mStoryDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
    }

    @Override
    public void onBackPressed() {
        if(mStoryDetailFragment != null && mStoryDetailFragment.isLinkViewVisible()){
            mStoryDetailFragment.hideLinkView();
        }
        else {
            super.onBackPressed();
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP){
                finishAfterTransition();
            }
            else {
                overridePendingTransition(R.anim.fadein, R.anim.view_right_to_offscreen);
            }
        }
    }
}
