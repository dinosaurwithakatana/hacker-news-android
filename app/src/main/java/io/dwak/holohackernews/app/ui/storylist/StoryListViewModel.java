package io.dwak.holohackernews.app.ui.storylist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.orm.StringUtil;
import com.orm.SugarTransactionHelper;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.dwak.holohackernews.app.HackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModel;
import io.dwak.holohackernews.app.dagger.component.DaggerNetworkServiceComponent;
import io.dwak.holohackernews.app.models.Comment;
import io.dwak.holohackernews.app.models.Story;
import io.dwak.holohackernews.app.models.StoryDetail;
import io.dwak.holohackernews.app.network.HackerNewsService;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStory;
import io.dwak.holohackernews.app.preferences.LocalDataManager;
import io.dwak.holohackernews.app.preferences.UserPreferenceManager;
import io.dwak.holohackernews.app.ui.storydetail.StoryDetailViewModel;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StoryListViewModel extends BaseViewModel {

    private ArrayList<Story> mStories;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FEED_TYPE_TOP,
                    FEED_TYPE_BEST,
                    FEED_TYPE_NEW,
                    FEED_TYPE_SHOW,
                    FEED_TYPE_SHOW_NEW,
                    FEED_TYPE_SAVED,
                    FEED_TYPE_ASK})
    public @interface FeedType {
    }

    public static final int FEED_TYPE_TOP = 0;
    public static final int FEED_TYPE_BEST = 1;
    public static final int FEED_TYPE_NEW = 2;
    public static final int FEED_TYPE_SHOW = 3;
    public static final int FEED_TYPE_SHOW_NEW = 4;
    public static final int FEED_TYPE_SAVED = 5;
    public static final int FEED_TYPE_ASK = 6;

    private @FeedType int mFeedType;

    private boolean mPageTwoLoaded;
    @Inject HackerNewsService mService;

    @Inject
    public StoryListViewModel() {
        DaggerNetworkServiceComponent.builder()
                                     .appModule(HackerNewsApplication.getAppModule())
                                     .appComponent(HackerNewsApplication.getAppComponent())
                                     .build()
                                     .inject(this);
    }

    @FeedType
    int getFeedType() {
        return mFeedType;
    }

    void setFeedType(@FeedType int feedType) {
        mFeedType = feedType;
    }

    @NonNull
    @StringRes int getTitle() {
        @StringRes int title;
        switch (mFeedType) {
            case FEED_TYPE_TOP:
                title = R.string.title_top;
                break;
            case FEED_TYPE_BEST:
                title = R.string.title_best;
                break;
            case FEED_TYPE_NEW:
                title = R.string.title_newest;
                break;
            case FEED_TYPE_SAVED:
                title = R.string.title_section_saved;
                break;
            case FEED_TYPE_ASK:
                title = R.string.title_section_ask;
                break;
            default:
                title = R.string.app_name;
        }

        return title;
    }

    private Observable<List<NodeHNAPIStory>> getObservable() {
        Observable<List<NodeHNAPIStory>> observable = null;
        switch (mFeedType) {
            case FEED_TYPE_TOP:
                observable = mService.getTopStories();
                break;
            case FEED_TYPE_BEST:
                observable = mService.getBestStories();
                break;
            case FEED_TYPE_NEW:
                observable = mService.getNewestStories();
                break;
            case FEED_TYPE_SHOW:
                observable = mService.getShowStories();
                break;
            case FEED_TYPE_SHOW_NEW:
                observable = mService.getShowNewStories();
                break;
            case FEED_TYPE_ASK:
                observable = mService.getAskStories();
                break;
            case FEED_TYPE_SAVED:
                observable = Observable
                        .create(new Observable.OnSubscribe<List<Story>>() {
                            @Override
                            public void call(Subscriber<? super List<Story>> subscriber) {
                                if (!subscriber.isUnsubscribed()) {
                                    List<Story> list = Select.from(Story.class)
                                                             .list();
                                    subscriber.onNext(list);

                                    subscriber.onCompleted();
                                }
                            }
                        })
                        .map(stories -> {
                            List<NodeHNAPIStory> nodeHNAPIStories = new ArrayList<>();
                            for (Story story : stories) {
                                nodeHNAPIStories.add(NodeHNAPIStory.fromStory(story));
                            }
                            return nodeHNAPIStories;
                        });
                break;
        }

        return observable;
    }

    Observable<Story> getStories() {
        return getStories(getObservable());
    }

    private Observable<Story> getStories(Observable<List<NodeHNAPIStory>> nodeHNAPIStoriesObservable) {
        return nodeHNAPIStoriesObservable.observeOn(AndroidSchedulers.mainThread())
                                         .subscribeOn(Schedulers.io())
                                         .flatMap(Observable::from)
                                         .map(Story::fromNodeHNAPIStory)
                                         .map(story -> {
                                             Story mStoryId = Select.from(Story.class)
                                                                    .where(Condition.prop(StringUtil.toSQLName("mStoryId")).eq(story.getStoryId()))
                                                                    .first();

                                             if (mStoryId != null) {
                                                 story.setIsSaved(true);
                                             }

                                             return story;
                                         })
                                         .doOnNext(story -> {
                                             if (mStories == null) {
                                                 mStories = new ArrayList<>(30);
                                             }
                                             mStories.add(story);
                                         });
    }

    Observable<Story> getTopStoriesPageTwo() {
        return getStories(mService.getTopStoriesPageTwo());
    }

    boolean isPageTwoLoaded() {
        return mPageTwoLoaded;
    }

    void setPageTwoLoaded(boolean pageTwoLoaded) {
        mPageTwoLoaded = pageTwoLoaded;
    }

    boolean isReturningUser() {
        return LocalDataManager.getInstance().isReturningUser();
    }

    void setReturningUser(boolean returningUser) {
        LocalDataManager.getInstance().setReturningUser(returningUser);
    }

    @NonNull
    AlertDialog getBetaAlert(@NonNull Context context) {
        return new AlertDialog.Builder(context)
                .setMessage("There is a G+ community for the beta of this application! Wanna check it out?")
                .setPositiveButton("Ok!", (dialog, which) -> {
                    Intent gPlusIntent = new Intent();
                    gPlusIntent.setAction(Intent.ACTION_VIEW);
                    gPlusIntent.setData(Uri.parse("https://plus.google.com/communities/112347719824323216860"));
                    context.startActivity(gPlusIntent);
                })
                .setNegativeButton("Nah", (dialog, which) -> {

                })
                .create();
    }

    public Observable saveStory(Story item) {
        StoryDetailViewModel storyDetailViewModel = new StoryDetailViewModel();
        storyDetailViewModel.setStoryId(item.getStoryId());
        return storyDetailViewModel.getStoryDetailObservable()
                                   .doOnNext(storyDetail -> SugarTransactionHelper.doInTansaction(() -> {
                                       item.setIsSaved(true);
                                       item.save();

                                       storyDetail.save();
                                       for (Comment comment : storyDetail.getCommentList()) {
                                           comment.save();
                                       }
                                   }));
    }

    public Observable<Object> deleteStory(Story item) {
        return Observable.create(subscriber -> {
            SugarTransactionHelper.doInTansaction(() -> {
                Select.from(Story.class)
                      .where(Condition.prop(StringUtil.toSQLName("mStoryId"))
                                      .eq(item.getStoryId()))
                      .first()
                      .delete();

                StoryDetail storyDetail = Select.from(StoryDetail.class)
                                                .where(Condition.prop(StringUtil.toSQLName(StoryDetail.STORY_DETAIL_ID))
                                                                .eq(item.getStoryId()))
                                                .first();
                Select.from(Comment.class)
                      .where(Condition.prop(StringUtil.toSQLName("mStoryDetail"))
                                      .eq(storyDetail.getId()))
                      .first()
                      .delete();
                storyDetail.delete();
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            });
        });
    }

    public Observable<Object> saveAllStories() {
        return Observable.from(mStories)
                         .flatMap(story -> saveStory(story));
    }

    public Observable<Object> deleteAllSavedStories() {
        return Observable.create(subscriber -> {
            SugarTransactionHelper.doInTansaction(() -> {
                Story.deleteAll(Story.class);
                StoryDetail.deleteAll(StoryDetail.class);
                Comment.deleteAll(Comment.class);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            });
        });
    }

    public int getNumberOfStories() {
        return mStories.size();
    }

    boolean isNightMode() {
        return UserPreferenceManager.getInstance().isNightModeEnabled();
    }
}
