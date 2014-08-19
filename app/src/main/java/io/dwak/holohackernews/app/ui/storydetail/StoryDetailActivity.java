package io.dwak.holohackernews.app.ui.storydetail;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.ui.storylist.MainActivity;

public class StoryDetailActivity extends FragmentActivity {

    private StoryCommentsFragment mStoryCommentsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        Intent intent = getIntent();
        long storyId = 0;
        if(intent !=null){
            Bundle extras = intent.getExtras();
            if(extras!=null){
                storyId = extras.getLong(MainActivity.STORY_ID);
            }
        }
        if (savedInstanceState == null) {
            mStoryCommentsFragment = StoryCommentsFragment.newInstance(storyId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mStoryCommentsFragment)
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
        if(mStoryCommentsFragment.isLinkViewVisible()){
            mStoryCommentsFragment.hideLinkView();
        }
        else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fadein, R.anim.view_right_to_offscreen);
        }
    }
}
