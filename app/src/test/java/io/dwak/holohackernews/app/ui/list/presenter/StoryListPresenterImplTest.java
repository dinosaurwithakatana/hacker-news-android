package io.dwak.holohackernews.app.ui.list.presenter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.dwak.holohackernews.app.base.BasePresenterTest;
import io.dwak.holohackernews.app.dagger.module.TestNetworkModule;
import io.dwak.holohackernews.app.model.Feed;
import io.dwak.holohackernews.app.model.json.StoryJson;
import io.dwak.holohackernews.app.ui.list.view.StoryListView;
import rx.Observable;
import rx.Subscriber;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StoryListPresenterImplTest extends BasePresenterTest<StoryListPresenter>{
    final StoryListView view = mock(StoryListView.class);

    @Test
    public void testSetFeed() throws Exception {
        TestNetworkModule testNetworkModule = new TestNetworkModule();
        final List<StoryJson> storyJsons = new ArrayList<>();
        storyJsons.add(new StoryJson(1l, "title", "url", "domain", 10, "user", "timeAgo", 3, "Job"));
        when(testNetworkModule.mockedService.getTopStories()).thenReturn(Observable.create(new Observable.OnSubscribe<List<StoryJson>>() {
            @Override
            public void call(final Subscriber<? super List<StoryJson>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(storyJsons);
                    subscriber.onCompleted();
                }
            }
        }));
        getComponent(view, testNetworkModule).inject(this);
        getPresenter().onAttachToView();
        getPresenter().setFeed(Feed.TOP);
        verify(view).displayStories(anyListOf(StoryJson.class));
    }

}
