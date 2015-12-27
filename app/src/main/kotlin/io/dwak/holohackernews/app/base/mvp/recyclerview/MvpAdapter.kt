package io.dwak.holohackernews.app.base.mvp.recyclerview

import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView

abstract class MvpAdapter<T : MvpViewHolder<*>> : RecyclerView.Adapter<T>(){
    /**
     * Used to setup the view holder, make sure to call super **AFTER** completing the bind
     */
    @CallSuper override fun onBindViewHolder(holder: T?, position: Int) {
        holder?.onBind()
    }

    override fun onViewRecycled(holder: T) {
        super.onViewRecycled(holder)
        holder.onRecycle()
    }
}

