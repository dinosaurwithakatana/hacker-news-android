package io.dwak.holohackernews.app.ui.list.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.dwak.holohackernews.app.model.json.StoryJson

class StoryListAdapter(val context : Context, val storyCallback : (StoryJson) -> (Unit)) : RecyclerView.Adapter<StoryViewHolder>() {
    val inflater : LayoutInflater
    val list = arrayListOf<StoryJson>()

    init {
        inflater = LayoutInflater.from(context)
    }

    public fun addStory(story : StoryJson) {
        list.add(story)
        notifyItemInserted(list.size)
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent : ViewGroup?, viewType : Int)
            = StoryViewHolder.create(inflater, parent!!)

    override fun onBindViewHolder(holder : StoryViewHolder?, position : Int) {
        throw UnsupportedOperationException()
    }

}