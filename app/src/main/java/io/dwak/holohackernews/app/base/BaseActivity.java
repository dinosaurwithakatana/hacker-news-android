package io.dwak.holohackernews.app.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.preferences.UserPreferenceManager;

public class BaseActivity extends RxAppCompatActivity{
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(UserPreferenceManager.getInstance().isNightModeEnabled()
                    ? R.color.colorPrimaryDarkNight
                    : R.color.colorPrimaryDark));
        }

        setTheme(UserPreferenceManager.getInstance().isNightModeEnabled()
                ? R.style.AppThemeNight
                : R.style.AppTheme);
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
        if (getToolbar() != null) {
            if (UserPreferenceManager.getInstance().isNightModeEnabled()) {
                getToolbar().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkNight));
            }
            else {
                getToolbar().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }
}
