package io.dwak.holohackernews.app.ui.main.presenter

import io.dwak.holohackernews.app.base.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.NetworkComponent
import io.dwak.holohackernews.app.ui.main.view.MainView

class MainPresenterImpl(view : MainView, networkComponent: NetworkComponent)
: AbstractPresenter<MainView>(view, networkComponent), MainPresenter {

    override fun inject() = networkComponent.inject(this)

    override fun onAttachToView() {
        super.onAttachToView()
        view.navigateToStoryList()
    }
}