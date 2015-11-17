package io.dwak.holohackernews.app.ui.list.presenter

import io.dwak.holohackernews.app.base.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.ServiceComponent
import io.dwak.holohackernews.app.ui.list.view.StoryItemView

class StoryItemPresenterImpl(view : StoryItemView, serviceComponent : ServiceComponent)
: AbstractPresenter<StoryItemView>(view, serviceComponent), StoryItemPresenter{
    override fun inject() {
        serviceComponent.inject(this)
    }
}