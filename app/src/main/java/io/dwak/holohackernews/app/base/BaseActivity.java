package io.dwak.holohackernews.app.base;

import android.os.Build;
import android.os.Bundle;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(UserPreferenceManager.isNightModeEnabled(this)
                    ? R.color.colorPrimaryDarkNight
                    : R.color.colorPrimaryDark));
        }
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
        if (getToolbar() != null)
            if (UserPreferenceManager.isNightModeEnabled(this)) {
                getToolbar().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkNight));
            }
            else {
                getToolbar().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
    }
}
