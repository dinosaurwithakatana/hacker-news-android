package io.dwak.holohackernews.app.ui.navigation.view

import io.dwak.holohackernews.app.base.mvp.PresenterView
import io.dwak.holohackernews.app.model.navigation.DrawerItem
import rx.Observer

interface NavigationDrawerView : PresenterView {
    val drawerObserver : Observer<DrawerItem>
}