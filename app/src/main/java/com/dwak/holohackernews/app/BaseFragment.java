package com.dwak.holohackernews.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;
import com.dwak.holohackernews.app.network.HackerNewsService;
import retrofit.RestAdapter;

/**
 * Created by vishnu on 5/3/14.
 */
public class BaseFragment extends Fragment {
    protected View mContainer;
    protected ProgressBar mProgressBar;
    protected HackerNewsService mService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://fathomless-island-9288.herokuapp.com/")
                .build();

        mService = restAdapter.create(HackerNewsService.class);
    }

    protected void showProgress(boolean showProgress){
        mContainer.setVisibility(showProgress ? View.INVISIBLE: View.VISIBLE);
        mProgressBar.setVisibility(showProgress ? View.VISIBLE : View.INVISIBLE);
    }
}
