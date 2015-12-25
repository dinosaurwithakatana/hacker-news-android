package io.dwak.holohackernews.app.ui.list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.MvpViewHolder
import io.dwak.holohackernews.app.butterknife.bindView
import io.dwak.holohackernews.app.dagger.component.DaggerInteractorComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.model.json.StoryJson
import io.dwak.holohackernews.app.ui.list.presenter.StoryItemPresenter
import rx.Observable

class StoryViewHolder(view : View)
: MvpViewHolder<StoryItemPresenter, StoryJson>(view), StoryItemView {

    val points : TextView by bindView(R.id.story_points)
    val title : TextView by bindView(R.id.story_title)
    val domain : TextView by bindView(R.id.story_domain)
    val longAgo : TextView by bindView(R.id.story_long_ago)
    val commentCount : TextView by bindView(R.id.comment_count)
    val submittedBy : TextView by bindView(R.id.story_submitter)

    companion object {
        fun create(inflater : LayoutInflater, parent : ViewGroup) : StoryViewHolder {
            return StoryViewHolder(inflater.inflate(R.layout.comments_header, parent, false))
        }
    }

    override fun inject() {
        DaggerPresenterComponent.builder()
                .presenterModule(PresenterModule(this))
                .interactorComponent(DaggerInteractorComponent.builder()
                        .interactorModule(InteractorModule(itemView.context))
                        .build())
                .build()
                .inject(this)
    }

    override fun bind(model: StoryJson, adapterObservable: Observable<StoryJson>) {
        super.bind(model, adapterObservable)
        presenter.story = model
    }

    override fun displayStoryDetails(title: String?, points: String?, domain: String?, longAgo: String?, commentsCount: String?, submittedBy: String?) {
        this.title.text = title
        this.longAgo.text = longAgo
        this.commentCount.text = commentsCount

        when (domain) {
            null -> this.domain.visibility = View.GONE
            else -> this.domain.text = domain
        }

        when (submittedBy) {
            null -> this.submittedBy.visibility = View.GONE
            else -> this.submittedBy.text = submittedBy
        }

        when(points){
            null -> this.points.visibility = View.INVISIBLE
            else -> this.points.text = points
        }
    }
}