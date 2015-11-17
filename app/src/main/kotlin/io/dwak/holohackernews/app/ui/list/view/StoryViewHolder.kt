package io.dwak.holohackernews.app.ui.list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.base.mvp.MvpViewHolder
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenter

class StoryViewHolder(itemView : View) : MvpViewHolder<StoryItemPresenter>(itemView), StoryItemView{
    companion object {
        fun create(inflater : LayoutInflater, parent : ViewGroup) : StoryViewHolder {
            return StoryViewHolder(inflater.inflate(R.layout.comments_header, parent, false))
        }
    }
    override fun inject() {
        DaggerPresenterComponent.builder()
                .presenterModule(PresenterModule(this))
                .build()
                .inject(this)
    }
}