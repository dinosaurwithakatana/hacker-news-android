package io.dwak.holohackernews.app.ui.detail.view

import android.view.View
import io.dwak.holohackernews.app.base.mvp.recyclerview.MvpViewHolder
import io.dwak.holohackernews.app.dagger.component.DaggerInteractorComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.ui.detail.presenter.CommentPresenter

class CommentViewHolder(view : View)
: MvpViewHolder<CommentPresenter>(view), CommentView {
    override fun inject() {
        DaggerPresenterComponent.builder()
                .interactorComponent(DaggerInteractorComponent.builder()
                        .interactorModule(InteractorModule(itemView.context))
                        .build())
                .presenterModule(PresenterModule(this))
                .build()
                .inject(this)
    }

    override fun displayComment(content: CharSequence) {
        throw UnsupportedOperationException()
    }
}