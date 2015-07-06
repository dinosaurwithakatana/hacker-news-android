package io.dwak.holohackernews.app.ui.storylist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.HackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModelFragment;
import io.dwak.holohackernews.app.dagger.component.DaggerViewModelComponent;
import io.dwak.holohackernews.app.models.Story;
import io.dwak.holohackernews.app.util.UIUtils;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class StoryListFragment extends BaseViewModelFragment<StoryListViewModel> {

    public static final String FEED_TO_LOAD = "feed_to_load";
    private static final String TAG = StoryListFragment.class.getSimpleName();
    public static final String TOP_OF_LIST = "TOP_OF_LIST";

    @InjectView(R.id.story_list) RecyclerView mRecyclerView;
    @InjectView(R.id.swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;

    private OnStoryListFragmentInteractionListener mListener;
    private StoryListAdapter mRecyclerAdapter;
    private LinearLayoutManager mLayoutManager;

    @Inject StoryListViewModel mViewModel;

    public static StoryListFragment newInstance(@StoryListViewModel.FeedType int feedType) {
        StoryListFragment fragment = new StoryListFragment();
        Bundle args = new Bundle();
        args.putInt(FEED_TO_LOAD, feedType);
        fragment.setArguments(args);
        return fragment;
    }

    private void refresh() {
        mRecyclerAdapter.clear();
            react(getViewModel().getStories(), false);
    }

    private void react(Observable<Story> stories, boolean pageTwo) {
        stories.subscribe(new Subscriber<Story>() {
            @Override
            public void onCompleted() {
                showProgress(false);
                mSwipeRefreshLayout.setRefreshing(false);
                getViewModel().setPageTwoLoaded(pageTwo);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof RetrofitError) {
                    Toast.makeText(getActivity(), R.string.story_list_error_toast_message, Toast.LENGTH_SHORT).show();
                }
                else {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNext(Story story) {
                if (story.getStoryId() != null && mRecyclerAdapter.getPositionOfItem(story) == -1) {
                    mRecyclerAdapter.addStory(story);
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnStoryListFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                                                 + " must implement OnStoryListFragmentInteractionListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerViewModelComponent.builder()
                                .appModule(HackerNewsApplication.getAppModule())
                                .appComponent(HackerNewsApplication.getAppComponent())
                                .build()
                                .inject(this);
        if (getArguments() != null) {
            @StoryListViewModel.FeedType final int feedType = getArguments().getInt(FEED_TO_LOAD);
            getViewModel().setFeedType(feedType);
        }

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storylist_list, container, false);
        ButterKnife.inject(this, view);

        getViewModel().setPageTwoLoaded(false);

        mContainer = view.findViewById(R.id.story_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getViewModel().getTitle());
        }
        showProgress(true);

        if (savedInstanceState == null || mRecyclerAdapter == null) {
            // Set the adapter
            mRecyclerAdapter = new StoryListAdapter(getActivity(),
                                                    new ArrayList<>(),
                                                    new StoryListAdapter.StoryListAdapterListener() {
                                                        @Override
                                                        public void onStoryClick(int position) {
                                                            mListener.onStoryListFragmentInteraction(mRecyclerAdapter.getItemId(position),
                                                                                                     getViewModel().getFeedType() == StoryListViewModel.FEED_TYPE_SAVED);
                                                        }

                                                        @Override
                                                        public void onStorySave(int position, boolean save) {
                                                            if (save) {
                                                                //noinspection unchecked
                                                                getViewModel().saveStory(mRecyclerAdapter.getItem(position))
                                                                              .subscribeOn(Schedulers.io())
                                                                              .observeOn(AndroidSchedulers.mainThread())
                                                                              .subscribe((Action1) o -> UIUtils.showToast(getActivity(), "Saved!"));
                                                            }
                                                            else {
                                                                getViewModel().deleteStory(mRecyclerAdapter.getItem(position))
                                                                              .subscribeOn(Schedulers.io())
                                                                              .observeOn(AndroidSchedulers.mainThread())
                                                                              .subscribe(new Subscriber<Object>() {
                                                                                  @Override
                                                                                  public void onCompleted() {
                                                                                      if (getViewModel().getFeedType() == StoryListViewModel.FEED_TYPE_SAVED) {
                                                                                          mRecyclerAdapter.removeItem(position);
                                                                                      }
                                                                                  }

                                                                                  @Override
                                                                                  public void onError(Throwable e) {

                                                                                  }

                                                                                  @Override
                                                                                  public void onNext(Object o) {

                                                                                  }
                                                                              });
                                                            }

                                                            mRecyclerAdapter.notifyItemChanged(position);
                                                        }
                                                    },
                                                    getViewModel().isNightMode());
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new SpacesItemDecoration(8));

            if (!getViewModel().isReturningUser()) {
                getViewModel().getBetaAlert(getActivity())
                              .show();
                getViewModel().setReturningUser(true);
            }

            refresh();
        }
        else {
            showProgress(false);
        }

        if (getViewModel().getFeedType() == StoryListViewModel.FEED_TYPE_TOP) {
            mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (!getViewModel().isPageTwoLoaded()) {
                        react(getViewModel().getTopStoriesPageTwo(), true);
                    }
                }
            });
        }

        mRecyclerView.setAdapter(mRecyclerAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                                                 getResources().getColor(R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            if (getViewModel().getFeedType() == StoryListViewModel.FEED_TYPE_SAVED) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(getString(R.string.action_refresh_saved_stories))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            ProgressDialog progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setMessage(getString(R.string.action_refresh_save_in_progress));
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            getViewModel().saveAllStories()
                                          .observeOn(Schedulers.io())
                                          .subscribeOn(AndroidSchedulers.mainThread())
                                          .subscribe(new Observer<Object>() {
                                              @Override
                                              public void onCompleted() {
                                                  progressDialog.dismiss();
                                                  mSwipeRefreshLayout.setRefreshing(false);
                                              }

                                              @Override
                                              public void onError(Throwable e) {

                                              }

                                              @Override
                                              public void onNext(Object o) {

                                              }
                                          });
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
            else {
                refresh();
            }
        });

        if (savedInstanceState != null) {
            mLayoutManager.scrollToPosition(savedInstanceState.getInt(TOP_OF_LIST));
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TOP_OF_LIST, mLayoutManager.findFirstVisibleItemPosition());
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (getViewModel().getFeedType() != StoryListViewModel.FEED_TYPE_SAVED) {
            inflater.inflate(R.menu.menu_story_list_save, menu);
        }
        else {
            inflater.inflate(R.menu.menu_story_list_saved, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                new AlertDialog.Builder(getActivity())
                        .setMessage(getString(R.string.action_share_delete_all_stories))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            ProgressDialog progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setMessage(getString(R.string.action_delete_in_progress_message));
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            getViewModel().deleteAllSavedStories()
                                          .observeOn(Schedulers.io())
                                          .subscribeOn(AndroidSchedulers.mainThread())
                                          .subscribe(new Observer<Object>() {
                                              @Override
                                              public void onCompleted() {
                                                  progressDialog.dismiss();
                                                  mRecyclerAdapter.removeAllItems();
                                              }

                                              @Override
                                              public void onError(Throwable e) {

                                              }

                                              @Override
                                              public void onNext(Object o) {

                                              }
                                          });
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
                return true;
            case R.id.action_save:
                new AlertDialog.Builder(getActivity())
                        .setMessage(getString(R.string.action_save_all_stories))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            ProgressDialog progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setMessage(String.format(getString(R.string.action_save_all_stories_in_progress), getViewModel().getNumberOfStories()));
                            progressDialog.setCancelable(false);
                            progressDialog.setMax(getViewModel().getNumberOfStories());
                            progressDialog.show();
                            getViewModel().saveAllStories()
                                          .subscribeOn(Schedulers.io())
                                          .observeOn(AndroidSchedulers.mainThread())
                                          .subscribe(new Observer<Object>() {
                                              @Override
                                              public void onCompleted() {
                                                  progressDialog.dismiss();
                                              }

                                              @Override
                                              public void onError(Throwable e) {

                                              }

                                              @Override
                                              public void onNext(Object o) {
                                                  progressDialog.incrementProgressBy(1);
                                              }
                                          });
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .create()
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public StoryListViewModel getViewModel() {
        return mViewModel;
    }

    public interface OnStoryListFragmentInteractionListener {
        void onStoryListFragmentInteraction(long id, boolean saved);
    }
}
