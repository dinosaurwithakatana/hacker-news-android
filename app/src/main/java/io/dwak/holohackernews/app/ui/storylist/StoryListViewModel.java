package io.dwak.holohackernews.app.ui.storylist;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModel;
import io.dwak.holohackernews.app.models.Story;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStory;
import io.dwak.holohackernews.app.preferences.LocalDataManager;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StoryListViewModel extends BaseViewModel{
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FEED_TYPE_TOP, FEED_TYPE_BEST, FEED_TYPE_NEW, FEED_TYPE_SHOW, FEED_TYPE_SHOW_NEW})
    public @interface FeedType{}
    public static final int FEED_TYPE_TOP = 0;
    public static final int FEED_TYPE_BEST = 1;
    public static final int FEED_TYPE_NEW = 2;
    public static final int FEED_TYPE_SHOW = 3;
    public static final int FEED_TYPE_SHOW_NEW = 4;

    private @FeedType int mFeedType;

    private boolean mPageTwoLoaded;

    @FeedType int getFeedType(){
        return mFeedType;
    }

    void setFeedType(@FeedType int feedType){
        mFeedType = feedType;
    }

    @NonNull
    String getTitle(){
        String title;
        switch (mFeedType) {
            case FEED_TYPE_TOP:
                title = getResources().getString(R.string.title_top);
                break;
            case FEED_TYPE_BEST:
                title = getResources().getString(R.string.title_best);
                break;
            case FEED_TYPE_NEW:
                title = getResources().getString(R.string.title_newest);
                break;
            default:
                title = getResources().getString(R.string.app_name);
        }

        return title;
    }

    private Observable<List<NodeHNAPIStory>> getObservable(){
        Observable<List<NodeHNAPIStory>> observable = null;
        switch (mFeedType) {
            case FEED_TYPE_TOP:
                observable = getHackerNewsService().getTopStories();
                break;
            case FEED_TYPE_BEST:
                observable = getHackerNewsService().getBestStories();
                break;
            case FEED_TYPE_NEW:
                observable = getHackerNewsService().getNewestStories();
                break;
            case FEED_TYPE_SHOW:
                observable = getHackerNewsService().getShowStories();
                break;
            case FEED_TYPE_SHOW_NEW:
                observable = getHackerNewsService().getShowNewStories();
                break;
        }

        return observable;
    }

    Observable<Story> getStories(){
        return getStories(getObservable());
    }

    private Observable<Story> getStories(Observable<List<NodeHNAPIStory>> nodeHNAPIStoriesObservable){
        return nodeHNAPIStoriesObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::from)
                .map(nodeHNAPIStory -> new Story(nodeHNAPIStory.getStoryId(),
                        nodeHNAPIStory.getTitle(),
                        nodeHNAPIStory.getUrl(),
                        nodeHNAPIStory.getDomain(),
                        nodeHNAPIStory.getPoints(),
                        nodeHNAPIStory.getSubmitter(),
                        nodeHNAPIStory.getPublishedTime(),
                        nodeHNAPIStory.getNumComments(),
                        nodeHNAPIStory.getType()));
    }

    Observable<Story> getTopStoriesPageTwo(){
        return getStories(getHackerNewsService().getTopStoriesPageTwo());
    }

    boolean isPageTwoLoaded() {
        return mPageTwoLoaded;
    }

    void setPageTwoLoaded(boolean pageTwoLoaded) {
        mPageTwoLoaded = pageTwoLoaded;
    }

    int[] getColorSchemeColors(){
        return new int[]{android.R.color.holo_orange_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_orange_light};
    }

    boolean isReturningUser(){
        return LocalDataManager.getInstance().isReturningUser();
    }

    void setReturningUser(boolean returningUser){
        LocalDataManager.getInstance().setReturningUser(returningUser);
    }
}
