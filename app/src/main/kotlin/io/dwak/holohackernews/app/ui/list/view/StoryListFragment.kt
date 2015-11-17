package io.dwak.holohackernews.app.ui.list.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.base.mvp.MvpFragment
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.component.DaggerServiceComponent
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.model.json.StoryJson
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenter

class StoryListFragment : MvpFragment<StoryListPresenter>(), StoryListView {
    val recycler : RecyclerView by bindView(R.id.recycler_view)
    var adapter : StoryListAdapter? = null

    override fun inject() {
        DaggerPresenterComponent.builder()
                .presenterModule(PresenterModule(this))
                .serviceComponent(DaggerServiceComponent.create())
                .build()
                .inject(this);
    }

    override fun onCreateView(inflater : LayoutInflater?, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        return inflater?.inflate(R.layout.fragment_storylist_list, container, false)
    }

    override fun onViewCreated(view : View?, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StoryListAdapter(activity, { presenter.storyClicked(it) })
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(activity)
    }

    override fun displayStories(storyList : List<StoryJson>) {
        storyList.forEach { adapter?.addStory(it) }
    }

    override fun navigateToStoryDetail(itemId : Long?) {
        throw UnsupportedOperationException()
    }
}