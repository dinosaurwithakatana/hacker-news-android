package io.dwak.holohackernews.app.ui.list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.dwak.holohackernews.app.butterknife.bindView
import com.jakewharton.rxbinding.view.clicks
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.MvpViewHolder
import io.dwak.holohackernews.app.dagger.component.DaggerNetworkComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.model.json.StoryJson
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenter
import rx.Observable

class StoryViewHolder(view : View)
: MvpViewHolder<StoryItemPresenter>(view), StoryItemView {
    val storyPoints : TextView by bindView(R.id.story_points)
    companion object {
        fun create(inflater : LayoutInflater, parent : ViewGroup) : StoryViewHolder {
            return StoryViewHolder(inflater.inflate(R.layout.comments_header, parent, false))
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
        storyPoints.text = story.points?.toString()
        adapterObservable.mergeWith(itemView.clicks().map { story })
    }
}