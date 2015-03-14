package io.dwak.holohackernews.app.ui.storylist;

import android.support.annotation.NonNull;

import java.util.List;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModel;
import io.dwak.holohackernews.app.manager.hackernews.FeedType;
import io.dwak.holohackernews.app.models.Story;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StoryListViewModel extends BaseViewModel{
    private FeedType mFeedType;

    private boolean mPageTwoLoaded;

    @NonNull
    FeedType getFeedType(){
        return mFeedType;
    }

    void setFeedType(@NonNull FeedType feedType){
        mFeedType = feedType;
    }

    @NonNull
    String getTitle(){
        String title;
        switch (mFeedType) {
            case TOP:
                title = "Top";
                break;
            case BEST:
                title = "Best";
                break;
            case NEW:
                title = "Newest";
                break;
            default:
                title = getResources().getString(R.string.app_name);
        }

        return title;
    }

    private Observable<List<NodeHNAPIStory>> getObservable(){
        Observable<List<NodeHNAPIStory>> observable = null;
        switch (mFeedType) {
            case TOP:
                observable = getHackerNewsService().getTopStories();
                break;
            case BEST:
                observable = getHackerNewsService().getBestStories();
                break;
            case NEW:
                observable = getHackerNewsService().getNewestStories();
                break;
            case SHOW:
                observable = getHackerNewsService().getShowStories();
                break;
            case SHOW_NEW:
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

}
