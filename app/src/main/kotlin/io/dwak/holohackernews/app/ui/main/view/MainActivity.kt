package io.dwak.holohackernews.app.ui.main.view

import android.os.Bundle
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.MvpActivity
import io.dwak.holohackernews.app.dagger.component.DaggerInteractorComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.extension.navigateTo
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.ui.list.view.StoryListFragment
import io.dwak.holohackernews.app.ui.main.presenter.MainPresenter

class MainActivity : MvpActivity<MainPresenter>(),
        MainView,
        StoryListFragment.StoryListInteractionListener {

    override fun inject() {
        DaggerPresenterComponent.builder()
                .presenterModule(PresenterModule(this))
                .interactorComponent(DaggerInteractorComponent.builder()
                        .interactorModule(InteractorModule(this))
                        .build())
                .build()
                .inject(this);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun navigateToStoryList() {
        navigateTo(StoryListFragment.newInstance(Feed.TOP), addToBackStack = false)
    }

    override fun navigateToStoryDetail(itemId: Long?) {

    }

    override fun populateNavigationDrawer() {
        throw UnsupportedOperationException()
    }

    override fun openNavigationDrawer() {
        throw UnsupportedOperationException()
    }

    override fun closeNavigationDrawer() {
        throw UnsupportedOperationException()
    }

}