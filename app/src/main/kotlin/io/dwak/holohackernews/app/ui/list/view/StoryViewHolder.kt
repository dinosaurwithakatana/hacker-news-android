package io.dwak.holohackernews.app.ui.list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.jakewharton.rxbinding.view.clicks
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.base.mvp.MvpViewHolder
import io.dwak.holohackernews.app.base.base.mvp.databinding.DataBindingMvpViewHolder
import io.dwak.holohackernews.app.dagger.component.DaggerNetworkComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.databinding.StoryItemViewBinding
import io.dwak.holohackernews.app.model.json.StoryJson
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenter
import rx.Observable

class StoryViewHolder(viewBinding : StoryItemViewBinding)
: DataBindingMvpViewHolder<StoryItemPresenter, StoryItemViewBinding>(viewBinding.root), StoryItemView {
    companion object {
        fun create(inflater : LayoutInflater, parent : ViewGroup) : StoryViewHolder {
            return StoryViewHolder(createViewBinding(inflater, R.layout.comments_header, parent) as StoryItemViewBinding)
        }
    }

    override fun inject() {
        DaggerPresenterComponent.builder()
                .presenterModule(PresenterModule(this))
                .networkComponent(DaggerNetworkComponent.create())
                .build()
                .inject(this)
    }

    fun bind(story : StoryJson, adapterObservable : Observable<StoryJson>) {
        viewBinding?.storyPoints?.text = story.points?.toString()
        adapterObservable.mergeWith(itemView.clicks().map { story })
    }
}