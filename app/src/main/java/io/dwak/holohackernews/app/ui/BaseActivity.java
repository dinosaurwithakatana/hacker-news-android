package io.dwak.holohackernews.app.ui;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import io.dwak.holohackernews.app.R;

/**
 * Created by vishnu on 11/2/14.
 */
public class BaseActivity extends ActionBarActivity{
    private Toolbar mToolbar;

    public Toolbar getToolbar(){
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }
}
