package io.dwak.holohackernews.app.ui.navigation.view

import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.navigation.DrawerItem
import rx.Observable
import rx.Observer

interface NavigationDrawerView : PresenterView {
    val drawerObserver : Observer<DrawerItem>
    var drawerClicks : Observable<Int>?
    fun navigateToStoryList(feed : Feed)
}