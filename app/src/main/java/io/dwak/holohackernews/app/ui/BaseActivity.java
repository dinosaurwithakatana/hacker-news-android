package io.dwak.holohackernews.app.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.preferences.UserPreferenceManager;

/**
 * Created by vishnu on 11/2/14.
 */
public class BaseActivity extends ActionBarActivity{
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public Toolbar getToolbar(){
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(UserPreferenceManager.isNightModeEnabled(this)){
            getToolbar().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkNight));
        }
        else {
            getToolbar().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

    }
}
