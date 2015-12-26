package io.dwak.holohackernews.app.base.mvp.recyclerview

import android.support.v7.widget.RecyclerView

abstract class MvpAdapter<T : MvpViewHolder<*>> : RecyclerView.Adapter<T>(){

    override fun onViewRecycled(holder: T) {
        super.onViewRecycled(holder)
        holder.onRecycle()
    }
}

