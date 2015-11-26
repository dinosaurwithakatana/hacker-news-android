package io.dwak.holohackernews.app.ui.main.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.base.mvp.MvpActivity
import io.dwak.holohackernews.app.base.base.mvp.databinding.DataBindingMvpActivity
import io.dwak.holohackernews.app.dagger.component.DaggerNetworkComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.databinding.MainActivityBinding
import io.dwak.holohackernews.app.extension.navigateTo
import io.dwak.holohackernews.app.ui.list.view.StoryListFragment
import io.dwak.holohackernews.app.ui.main.presenter.MainPresenter

class MainActivity : DataBindingMvpActivity<MainPresenter, MainActivityBinding>(),
                     MainView,
                     StoryListFragment.StoryListInteractionListener {

    override fun inject() {
        DaggerPresenterComponent.builder()
        .presenterModule(PresenterModule(this))
        .networkComponent(DaggerNetworkComponent.create())
        .build()
        .inject(this);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewBinding(R.layout.activity_main)
    }

    override fun navigateToStoryList() {
        navigateTo(StoryListFragment.newInstance(), addToBackStack = false)
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