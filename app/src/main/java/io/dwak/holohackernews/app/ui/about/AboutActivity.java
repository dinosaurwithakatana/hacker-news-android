package io.dwak.holohackernews.app.ui.about;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.HackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseActivity;
import io.dwak.holohackernews.app.base.BaseViewModelFragment;
import io.dwak.holohackernews.app.dagger.component.DaggerViewModelComponent;
import io.dwak.holohackernews.app.models.AboutLicense;

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
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, AboutFragment.newInstance())
                    .commit();
        }
    }

    public static class AboutFragment extends BaseViewModelFragment<AboutViewModel> {
        @Inject AboutViewModel mViewModel;
        @InjectView(R.id.recycler_view) RecyclerView mRecyclerView;

        public static Fragment newInstance() {
            return new AboutFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_about, container, false);
            ButterKnife.inject(this, view);
            DaggerViewModelComponent.builder()
                                    .appComponent(HackerNewsApplication.getAppComponent())
                                    .build()
                                    .inject(this);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            AboutAdapter aboutAdapter = new AboutAdapter();
            mRecyclerView.setAdapter(aboutAdapter);
            aboutAdapter.addHeader();
            for (AboutLicense license : getViewModel().getLicenses()) {
                aboutAdapter.addLicense(license);
            }

            return view;
        }

        @Override
        protected AboutViewModel getViewModel() {
            return mViewModel;
        }
    }
}
