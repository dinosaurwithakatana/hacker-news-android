package io.dwak.holohackernews.app.ui.about;

import android.app.Fragment;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.ui.storylist.MainActivity;

public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar !=null){
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
            toolbar.setNavigationOnClickListener(v -> navigateUpToFromChild(AboutActivity.this,
                    IntentCompat.makeMainActivity(new ComponentName(AboutActivity.this, MainActivity.class))));
            setSupportActionBar(toolbar);
        }
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_about, container, false);
        }
    }
}
