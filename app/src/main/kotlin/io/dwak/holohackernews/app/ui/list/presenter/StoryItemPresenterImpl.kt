package io.dwak.holohackernews.app.ui.list.presenter

import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.NetworkComponent
import io.dwak.holohackernews.app.ui.list.view.StoryItemView

class StoryItemPresenterImpl(view : StoryItemView, networkComponent: NetworkComponent)
: AbstractPresenter<StoryItemView>(view, networkComponent), StoryItemPresenter{
    override fun inject() {
        networkComponent.inject(this)
    }
}