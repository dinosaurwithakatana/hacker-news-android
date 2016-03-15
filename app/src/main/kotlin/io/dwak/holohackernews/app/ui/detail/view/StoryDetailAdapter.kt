package io.dwak.holohackernews.app.ui.detail.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.dwak.holohackernews.app.model.json.CommentJson
import io.dwak.holohackernews.app.model.json.StoryDetailJson
import io.dwak.holohackernews.app.ui.list.view.StoryViewHolder
import rx.Observable
import rx.Subscriber

class StoryDetailAdapter(context : Context)
: RecyclerView.Adapter<RecyclerView.ViewHolder>(), CommentViewHolder.CommentActionCallbacks {
  enum class ViewType { HEADER, COMMENT }
  data class StoryDetailItem<T>(val viewType : ViewType, val value : T)

  private val layoutInflater : LayoutInflater
  val itemList = arrayListOf<StoryDetailItem<*>>()
  private val events = Observable.defer<ClickEvent>{ Observable.create{ } }

  init {
    layoutInflater = LayoutInflater.from(context)
    setHasStableIds(true)
  }

  fun addHeader(storyDetail : StoryDetailJson) {
    itemList.add(StoryDetailItem(ViewType.HEADER, storyDetail))
    notifyItemInserted(0)
  }

  fun addComments(comments : Observable<CommentJson>) {
    comments.map { StoryDetailItem(ViewType.COMMENT, it) }
            .subscribe { itemList.add(it) }
    notifyDataSetChanged()
  }

  fun clear() {
    itemList.clear()
    notifyDataSetChanged()
  }

  override fun getItemCount() : Int = itemList.size
  override fun getItemViewType(position : Int) : Int = itemList[position].viewType.ordinal

  override fun getItemId(position : Int) : Long {
    itemList[position].let {
      when (it.viewType) {
        ViewType.HEADER  -> return (it.value as StoryDetailJson).id!!
        ViewType.COMMENT -> return (it.value as CommentJson).id!!
      }
    }
  }

  override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : RecyclerView.ViewHolder? {
    when (viewType) {
      ViewType.HEADER.ordinal  -> return StoryViewHolder.create(layoutInflater, parent)
      ViewType.COMMENT.ordinal -> return CommentViewHolder.create(layoutInflater, parent)
      else                     -> return null
    }
  }

  override fun onBindViewHolder(holder : RecyclerView.ViewHolder, position : Int) {
    when (getItemViewType(position)) {
      ViewType.HEADER.ordinal  -> (holder as StoryViewHolder).bind(itemList[position].value as StoryDetailJson)
      ViewType.COMMENT.ordinal -> {
        val commentJson = itemList[position].value as CommentJson
        val isOriginalPoster = commentJson.user?.equals((itemList[0].value as StoryDetailJson?)?.user)
        (holder as CommentViewHolder).bind(commentJson, isOriginalPoster, this)
      }
    }
  }

  override fun onActionMenuClicked() {
    throw UnsupportedOperationException()
  }

  override fun onCommentClicked(holder : CommentViewHolder) {
    throw UnsupportedOperationException()
  }

  fun subscribe(subscriber : Subscriber<ClickEvent>){
  }
  enum class Event { COMMENT, ACTION }
  data class ClickEvent(val event : Event, val viewHolder : RecyclerView.ViewHolder, val comment: CommentJson)
}
