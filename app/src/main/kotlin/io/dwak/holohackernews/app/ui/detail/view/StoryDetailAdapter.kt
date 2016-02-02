package io.dwak.holohackernews.app.ui.detail.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.dwak.holohackernews.app.model.json.CommentJson
import io.dwak.holohackernews.app.model.json.StoryDetailJson
import io.dwak.holohackernews.app.ui.list.view.StoryViewHolder

class StoryDetailAdapter(context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ViewType { HEADER, COMMENT }
    data class StoryDetailItem<T>(val viewType : ViewType, val value : T)

    private val layoutInflater : LayoutInflater
    val itemList = arrayListOf<StoryDetailItem<*>>()

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    fun addHeader(storyDetail : StoryDetailJson) {
        itemList.add(StoryDetailItem(ViewType.HEADER, storyDetail))
        notifyItemInserted(0)
    }

    fun addComments(comments : List<CommentJson>) {
        comments.forEach {
            itemList.add(StoryDetailItem(ViewType.COMMENT, it))
        }
        notifyDataSetChanged()
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount() : Int = itemList.size
    override fun getItemViewType(position : Int) : Int = itemList[position].viewType.ordinal

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
                (holder as CommentViewHolder).bind(commentJson, isOriginalPoster)
            }
        }
    }

}