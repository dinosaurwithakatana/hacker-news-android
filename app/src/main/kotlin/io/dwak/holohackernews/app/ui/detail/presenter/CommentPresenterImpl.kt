package io.dwak.holohackernews.app.ui.detail.presenter

import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.ui.detail.view.CommentView

class CommentPresenterImpl(view : CommentView, interactorComponent: InteractorComponent)
: AbstractPresenter<CommentView>(view, interactorComponent), CommentPresenter {
    override fun inject() = interactorComponent.inject(this)
}