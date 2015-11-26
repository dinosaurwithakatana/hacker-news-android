package io.dwak.holohackernews.app.ui.list.view

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.base.mvp.MvpFragment
import io.dwak.holohackernews.app.base.base.mvp.databinding.DataBindingMvpFragment
import io.dwak.holohackernews.app.dagger.component.DaggerNetworkComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.databinding.StoryListFragmentBinding
import io.dwak.holohackernews.app.model.json.StoryJson
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenter

class StoryListFragment : DataBindingMvpFragment<StoryListPresenter, StoryListFragmentBinding>(), StoryListView {
    var adapter : StoryListAdapter? = null
    var interactionListener : StoryListInteractionListener? = null

    companion object {
        fun newInstance() = StoryListFragment()
    }

    override fun inject() {
        DaggerPresenterComponent.builder()
                .presenterModule(PresenterModule(this))
                .networkComponent(DaggerNetworkComponent.create())
                .build()
                .inject(this);
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(activity is StoryListInteractionListener){
            interactionListener = activity as StoryListInteractionListener
        }
    }

    override fun onCreateView(inflater : LayoutInflater?, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        createViewBinding(inflater!!, R.layout.fragment_storylist_list, container!!)
        return viewBinding.root
    }

    override fun onViewCreated(view : View?, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StoryListAdapter(activity)
        adapter?.itemClicks?.subscribe { presenter.storyClicked(it) }
        viewBinding.storyList.adapter = adapter
        viewBinding.storyList.layoutManager = LinearLayoutManager(activity)
    }

    override fun displayStories(storyList : List<StoryJson>) {
        storyList.forEach { adapter?.addStory(it) }
    }

    override fun navigateToStoryDetail(itemId : Long?) {
        interactionListener?.navigateToStoryDetail(itemId)
    }

    interface StoryListInteractionListener {
        fun navigateToStoryDetail(itemId: Long?)
    }
}