package io.dwak.holohackernews.app.ui.detail.view

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.recyclerview.MvpViewHolder
import io.dwak.holohackernews.app.butterknife.bindView
import io.dwak.holohackernews.app.dagger.component.DaggerInteractorComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.model.json.CommentJson
import io.dwak.holohackernews.app.ui.detail.presenter.CommentPresenter

class CommentViewHolder(view : View)
: MvpViewHolder<CommentPresenter>(view), CommentView {
    val content : TextView by bindView(R.id.comment_content)
    val commentSubmitter : TextView by bindView(R.id.comment_submitter)
    val submissionTime : TextView by bindView(R.id.comment_submission_time)
    val colorCode : FrameLayout by bindView(R.id.color_code)
    val commentContainer : View by bindView(R.id.comments_container)
    companion object {
        fun create(inflater: LayoutInflater, parent : ViewGroup) : CommentViewHolder {
            return CommentViewHolder(inflater.inflate(R.layout.comments_list_item, parent, false))
        }
    }
    override fun inject() {
        DaggerPresenterComponent.builder()
                .interactorComponent(DaggerInteractorComponent.builder()
                        .interactorModule(InteractorModule(itemView.context))
                        .build())
                .presenterModule(PresenterModule(this))
                .build()
                .inject(this)
    }

    fun bind(model : CommentJson, isOriginalPoster : Boolean?){
        presenter.isOriginalPoster = isOriginalPoster
        presenter.comment = model
    }

    override fun displayComment(content : CharSequence?, submitter : String?, submissionTime : String?, level : Int?, isOriginalPoster : Boolean?) {
        this.content.text = content
        this.commentSubmitter.text = submitter
        this.submissionTime.text = submissionTime
        when(isOriginalPoster) {
            true ->  this.commentSubmitter.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
            false ->  this.commentSubmitter.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.black))
            else -> {}
        }

        if(level!! > 0){
            setLeftMargin(itemView.context.resources.getDimension(R.dimen.comment_content_left_margin).toInt())
        }
        else {
            setLeftMargin(0)
        }

        with(this.colorCode) {
            when(level % 8){
                0 -> setBackgroundResource(R.color.comment_level_0)
                1 -> setBackgroundResource(R.color.comment_level_1)
                2 -> setBackgroundResource(R.color.comment_level_2)
                3 -> setBackgroundResource(R.color.comment_level_3)
                4 -> setBackgroundResource(R.color.comment_level_4)
                5 -> setBackgroundResource(R.color.comment_level_5)
                6 -> setBackgroundResource(R.color.comment_level_6)
                7 -> setBackgroundResource(R.color.comment_level_7)
            }
        }
    }

    private fun setLeftMargin(leftMargin: Int) {
        val layoutParams : FrameLayout.LayoutParams = FrameLayout.LayoutParams(commentContainer.layoutParams)
        layoutParams.setMargins(leftMargin, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin)
        commentContainer.layoutParams = layoutParams
    }

}