package io.dwak.holohackernews.app.ui.storydetail;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.ui.storylist.MainActivity;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class StoryDetailActivity extends SwipeBackActivity{

    private StoryDetailFragment mStoryDetailFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
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
        if(mStoryDetailFragment.isLinkViewVisible()){
            mStoryDetailFragment.hideLinkView();
        }
        else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fadein, R.anim.view_right_to_offscreen);
        }
    }
}
