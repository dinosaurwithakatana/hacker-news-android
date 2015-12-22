package io.dwak.holohackernews.app.ui.main.view

import io.dwak.holohackernews.app.base.mvp.PresenterView

interface MainView : PresenterView {
    fun navigateToStoryList()
    fun navigateToStoryDetail(itemId: Long?)
    fun populateNavigationDrawer()
    fun openNavigationDrawer()
    fun closeNavigationDrawer()
}