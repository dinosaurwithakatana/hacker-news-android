package io.dwak.holohackernews.app.network;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.dwak.holohackernews.app.model.json.StoryDetailJson;
import io.dwak.holohackernews.app.model.json.StoryJson;
import retrofit.http.Path;
import rx.Observable;
import rx.Subscriber;

public class TestHackerNewsService2 implements HackerNewsService2{
    @NotNull
    @Override
    public Observable<List<StoryJson>> getTopStories() {
        final List<StoryJson> storyJsons = new ArrayList<>();
        storyJsons.add(new StoryJson(1l, "title", "url", "domain", 10, "user", "timeAgo", 3, "Job"));

        return Observable.create(new Observable.OnSubscribe<List<StoryJson>>() {
            @Override
            public void call(final Subscriber<? super List<StoryJson>> subscriber) {
                if(!subscriber.isUnsubscribed()){
                    subscriber.onNext(storyJsons);
                    subscriber.onCompleted();
                }
            }
        });
    }

    @NotNull
    @Override
    public Observable<List<StoryJson>> getTopStoriesPageTwo() {
        return null;
    }

    @NotNull
    @Override
    public Observable<List<StoryJson>> getNewestStories() {
        return null;
    }

    @NotNull
    @Override
    public Observable<List<StoryJson>> getBestStories() {
        return null;
    }

    @NotNull
    @Override
    public Observable<List<StoryJson>> getShowHnStories() {
        return null;
    }

    @NotNull
    @Override
    public Observable<List<StoryJson>> getNewShowHnStories() {
        return null;
    }

    @NotNull
    @Override
    public Observable<List<StoryJson>> getAskHnStories() {
        return null;
    }

    @NotNull
    @Override
    public Observable<StoryDetailJson> getItemDetails(@Path("itemId") final long itemId) {
        return null;
    }
}
