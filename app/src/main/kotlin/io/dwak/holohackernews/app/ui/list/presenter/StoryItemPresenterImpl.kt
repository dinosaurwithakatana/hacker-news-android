package io.dwak.holohackernews.app.ui.list.presenter

import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.AbstractPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import io.dwak.holohackernews.app.model.json.StoryJson
import io.dwak.holohackernews.app.ui.list.view.StoryItemView
import rx.Single

class StoryItemPresenterImpl(view : StoryItemView, interactorComponent: InteractorComponent)
: AbstractPresenter<StoryItemView>(view, interactorComponent), StoryItemPresenter{
    override var story: StoryJson? = null
        set(value) {
            field = value
            parseStory()
        }

    override fun inject() = interactorComponent.inject(this)

    override fun onAttachToView() {
        super.onAttachToView()
        with(viewSubscription){
            add(view.itemClicks
                    ?.map { story }
                    ?.subscribe({ view.onItemClick?.invoke(it!!) }))
        }
    }

    fun parseStory(){
        val title = story?.title
        val points = story?.points?.toString()
        val longAgo = story?.timeAgo
        val formatString = interactorComponent.resources.getString(R.string.comment_header_comment_count)
        val commentCount = formatString.format(story?.commentsCount.toString())
        val submittedBy = story?.user
        var domain : String? = null

        when(story?.type){
            "link" -> {
                story?.domain?.let {
                    domain = " | ${it.substring(0, if(it.length < 20) it.length else 20)} | "
                }
            }
            else -> {
                domain = " | ${story?.type} | "
            }
        }

        view.displayStoryDetails(title, points, domain, longAgo, commentCount, submittedBy)
    }

    override fun save(): Single<Boolean> {
        throw UnsupportedOperationException()
    }

}