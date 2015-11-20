package io.dwak.holohackernews.app.ui.list.presenter;

import org.junit.Test;

import io.dwak.holohackernews.app.base.BasePresenterTest;
import io.dwak.holohackernews.app.dagger.component.DaggerTestNetworkComponent;
import io.dwak.holohackernews.app.dagger.component.DaggerTestPresenterComponent;
import io.dwak.holohackernews.app.dagger.module.PresenterModule;
import io.dwak.holohackernews.app.model.Feed;
import io.dwak.holohackernews.app.model.json.StoryJson;
import io.dwak.holohackernews.app.ui.list.view.StoryListView;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StoryListPresenterImplTest extends BasePresenterTest<StoryListPresenter>{
    final StoryListView view = mock(StoryListView.class);

    @Override
    protected void inject() {
        DaggerTestPresenterComponent.builder()
                .presenterModule(new PresenterModule(view))
                .networkComponent(DaggerTestNetworkComponent.create())
                .build()
                .inject(this);
    }

    @Test
    public void testSetFeed() throws Exception {
        getPresenter().setFeed(Feed.TOP);
        verify(view).displayStories(anyListOf(StoryJson.class));
    }
}
