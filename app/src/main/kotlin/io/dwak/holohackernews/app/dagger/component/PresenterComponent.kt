package io.dwak.holohackernews.app.dagger.component

import dagger.Component
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.dagger.scope.ViewScope
import io.dwak.holohackernews.app.ui.list.view.StoryListFragment
import io.dwak.holohackernews.app.ui.list.view.StoryViewHolder

@ViewScope
@Component(modules = arrayOf(PresenterModule::class), dependencies = arrayOf(NetworkComponent::class))
interface PresenterComponent {
    fun inject(storyListFragment : StoryListFragment)
    fun inject(storyItemViewHolder : StoryViewHolder)
}