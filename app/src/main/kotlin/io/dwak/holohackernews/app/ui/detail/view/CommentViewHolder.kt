package io.dwak.holohackernews.app.ui.detail.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.recyclerview.MvpViewHolder
import io.dwak.holohackernews.app.butterknife.bindView
import io.dwak.holohackernews.app.extension.getColorCompat
import io.dwak.holohackernews.app.model.json.CommentJson
import io.dwak.holohackernews.app.ui.detail.presenter.CommentPresenter

class CommentViewHolder(view : View)
: MvpViewHolder<CommentPresenter>(view), CommentView {
  val content : TextView by bindView(R.id.comment_content)
  val commentSubmitter : TextView by bindView(R.id.comment_submitter)
  val submissionTime : TextView by bindView(R.id.comment_submission_time)
  val colorCode : FrameLayout by bindView(R.id.color_code)
  val commentContainer : View by bindView(R.id.comments_container)
  val hiddenCommentCount : TextView by bindView(R.id.hidden_comment_count)
  private var callback : CommentActionCallbacks? = null

  companion object {
    fun create(inflater : LayoutInflater, parent : ViewGroup) : CommentViewHolder {
      return CommentViewHolder(inflater.inflate(R.layout.comments_list_item, parent, false))
    }
  }

  override fun inject() = objectGraph(this).inject(this)

  fun bind(model : CommentJson, isOriginalPoster : Boolean?, callback : CommentActionCallbacks) {
    presenter.isOriginalPoster = isOriginalPoster
    presenter.comment = model
    this.callback = callback
  }

  override fun displayComment(content : CharSequence?,
                              submitter : String?,
                              submissionTime : String?,
                              level : Int?,
                              isOriginalPoster : Boolean?,
                              isCollapsed : Boolean) {
    this.content.text = content
    this.commentSubmitter.text = submitter
    this.submissionTime.text = submissionTime

    with(this.commentSubmitter) {
      when (isOriginalPoster) {
        true  -> setTextColor(itemView.context.getColorCompat(R.color.colorPrimary))
        false -> setTextColor(itemView.context.getColorCompat(android.R.color.black))
      }
    }

    if (level!! > 0) {
      setLeftMargin(itemView.context.resources.getDimension(R.dimen.comment_content_left_margin).toInt())
    }
    else {
      setLeftMargin(0)
    }

    with(this.colorCode) {
      when (level % 8) {
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

    hiddenCommentCount.visibility = if (isCollapsed) View.VISIBLE else View.GONE
  }

  private fun setLeftMargin(leftMargin : Int) {
    val layoutParams : FrameLayout.LayoutParams = FrameLayout.LayoutParams(commentContainer.layoutParams)
    layoutParams.setMargins(leftMargin,
                            layoutParams.topMargin,
                            layoutParams.rightMargin,
                            layoutParams.bottomMargin)
    commentContainer.layoutParams = layoutParams
  }

  override fun collapseChildren() {
    callback?.onCommentClicked(this)
  }

  interface CommentActionCallbacks {
    fun onCommentClicked(holder : CommentViewHolder)
    fun onActionMenuClicked()
  }

}