package io.dwak.holohackernews.app.ui.storylist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.HackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModelFragment;
import io.dwak.holohackernews.app.dagger.component.DaggerViewModelComponent;
import io.dwak.holohackernews.app.models.Story;
import io.dwak.holohackernews.app.util.HNLog;
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
    public static final String STORY_LIST = "STORY_LIST";

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
        getViewModel().setIsRestoring(false);
        getViewModel().setStoryList(null);
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
                                .appComponent(HackerNewsApplication.getAppComponent())
                                .build()
                                .inject(this);
        if(savedInstanceState != null){
            getViewModel().setStoryList(savedInstanceState.getParcelableArrayList(STORY_LIST));
            getViewModel().setIsRestoring(true);
        }
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
            setupRecyclerView();
            showBetaPopup();
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

        setupSwipeToRefresh();

        if (savedInstanceState != null) {
            mLayoutManager.scrollToPosition(savedInstanceState.getInt(TOP_OF_LIST));
        }

        return view;
    }

    private void showBetaPopup() {
        if (!getViewModel().isReturningUser()) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.beta_prompt_message)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        Intent gPlusIntent = new Intent();
                        gPlusIntent.setAction(Intent.ACTION_VIEW);
                        gPlusIntent.setData(Uri.parse("https://plus.google.com/communities/112347719824323216860"));
                        getActivity().startActivity(gPlusIntent);
                    })
                    .setNegativeButton(R.string.beta_prompt_negative, (dialog, which) -> {

                    })
                    .create()
                    .show();
            getViewModel().setReturningUser(true);
        }
    }

    private void setupRecyclerView() {
        StoryListAdapter.StoryListAdapterListener listener = new StoryListAdapter.StoryListAdapterListener() {
            @Override
            public void onStoryClick(int position) {
                getViewModel().markStoryAsRead(mRecyclerAdapter.getItem(position))
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(new Subscriber<Story>() {
                                  @Override
                                  public void onCompleted() {
                                      mListener.onStoryListFragmentInteraction(mRecyclerAdapter.getItemId(position),
                                                                               getViewModel().getFeedType() == StoryListViewModel.FEED_TYPE_SAVED);
                                  }

                                  @Override
                                  public void onError(Throwable e) {

                                  }

                                  @Override
                                  public void onNext(Story story) {
                                      mRecyclerAdapter.markStoryAsRead(position, story);
                                  }
                              });
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
        };
        mRecyclerAdapter = new StoryListAdapter(getActivity(),
                                                new ArrayList<>(),
                                                listener,
                                                getViewModel().isNightMode());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(8));
    }

    private void setupSwipeToRefresh() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                                                 getResources().getColor(R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            if (getViewModel().getFeedType() == StoryListViewModel.FEED_TYPE_SAVED) {
                mSwipeRefreshLayout.setRefreshing(false);
                new AlertDialog.Builder(getActivity())
                        .setMessage(getString(R.string.action_refresh_saved_stories))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            getViewModel().saveAllStories()
                                          .observeOn(Schedulers.io())
                                          .subscribeOn(AndroidSchedulers.mainThread())
                                          .subscribe(new Observer<Object>() {
                                              @Override
                                              public void onCompleted() {
                                                  UIUtils.showToast(getActivity(), "Saved!");
                                              }

                                              @Override
                                              public void onError(Throwable e) {

                                              }

                                              @Override
                                              public void onNext(Object o) {

                                              }
                                          });
                        })
                        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                            mSwipeRefreshLayout.setRefreshing(false);
                        })
                        .show();
            }
            else {
                refresh();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TOP_OF_LIST, mLayoutManager.findFirstVisibleItemPosition());
        outState.putParcelableArrayList(STORY_LIST, getViewModel().getStoryList());
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
                            getViewModel().saveAllStories()
                                          .subscribeOn(Schedulers.io())
                                          .observeOn(AndroidSchedulers.mainThread())
                                          .subscribe(new Observer<Object>() {
                                              @Override
                                              public void onCompleted() {
//                                                  progressDialog.dismiss();
                                                  UIUtils.showToast(getActivity(), "Saved!");
                                              }

                                              @Override
                                              public void onError(Throwable e) {

                                              }

                                              @Override
                                              public void onNext(Object o) {
//                                                  progressDialog.incrementProgressBy(1);
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
