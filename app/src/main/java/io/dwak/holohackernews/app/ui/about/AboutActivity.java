package io.dwak.holohackernews.app.ui.about;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseActivity;

public class AboutActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar !=null){
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            toolbar.setNavigationOnClickListener(v -> finish());
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new AboutFragment())
                    .commit();
        }
    }

    public static class AboutFragment extends Fragment {

        public AboutFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_about, container, false);
        }
    }
}
