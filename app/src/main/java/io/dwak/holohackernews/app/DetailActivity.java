package io.dwak.holohackernews.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import io.dwak.holohackernews.app.R;

public class DetailActivity extends FragmentActivity implements
    StoryCommentsFragment.OnStoryFragmentInteractionListener,
    StoryLinkFragment.OnStoryLinkFragmentInteractionListener {

    public static final String STORY_ID = "STORY_ID";
    private String mStoryUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        long storyId = extras.getLong(STORY_ID);

        if (savedInstanceState == null) {
            BaseFragment fragment = StoryCommentsFragment.newInstance(storyId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, StoryCommentsFragment.class.getSimpleName())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStoryFragmentInteraction(String url) {
        mStoryUrl = url;
        StoryLinkFragment fragment;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.offscreen_up_to_view,
                R.anim.view_up_to_offscreen,
                R.anim.offscreen_up_to_view,
                R.anim.view_down_to_offscreen);

        if (getSupportFragmentManager().findFragmentByTag(url) == null) {
            fragment = StoryLinkFragment.newInstance(url);
            transaction.add(R.id.container, fragment, url);
        } else {
            fragment = (StoryLinkFragment) getSupportFragmentManager().findFragmentByTag(url);
        }
        transaction.show(fragment);
        transaction.commit();
    }

    @Override
    public void onStoryCommentsFragmentDetach() {
    }

    @Override
    public void onStoryLinkFragmentInteraction() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(mStoryUrl) != null
                && getSupportFragmentManager().findFragmentByTag(mStoryUrl).isVisible()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.offscreen_up_to_view,
                    R.anim.view_down_to_offscreen,
                    R.anim.offscreen_up_to_view,
                    R.anim.view_up_to_offscreen);
            transaction.hide(getSupportFragmentManager().findFragmentByTag(mStoryUrl));
            transaction.commit();
            return;
        } else if (getSupportFragmentManager().findFragmentByTag(StoryCommentsFragment.class.getSimpleName()) != null
                && getSupportFragmentManager().findFragmentByTag(StoryCommentsFragment.class.getSimpleName()).isVisible()) {
            if (getSupportFragmentManager().findFragmentByTag(mStoryUrl) != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(getSupportFragmentManager().findFragmentByTag(mStoryUrl));
                transaction.commit();
            }
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentByTag(StoryListFragment.class.getSimpleName()) != null
                && getSupportFragmentManager().findFragmentByTag(StoryListFragment.class.getSimpleName()).isVisible()) {
            super.onBackPressed();
        }
    }
}
