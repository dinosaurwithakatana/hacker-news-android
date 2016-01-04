package io.dwak.holohackernews.app.dagger.component

import dagger.Component
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.dagger.scope.ViewScope
import io.dwak.holohackernews.app.ui.detail.view.CommentViewHolder
import io.dwak.holohackernews.app.ui.detail.view.StoryDetailFragment
import io.dwak.holohackernews.app.ui.list.view.StoryListFragment
import io.dwak.holohackernews.app.ui.list.view.StoryViewHolder
import io.dwak.holohackernews.app.ui.main.view.MainActivity

@ViewScope
@Component(modules = arrayOf(PresenterModule::class),
        dependencies = arrayOf(InteractorComponent::class))
interface PresenterComponent {
    fun inject(storyListFragment : StoryListFragment)
    fun inject(storyItemViewHolder : StoryViewHolder)
    fun inject(mainView : MainActivity)
    fun inject(storyDetailView : StoryDetailFragment)
    fun inject(commentView : CommentViewHolder)
}