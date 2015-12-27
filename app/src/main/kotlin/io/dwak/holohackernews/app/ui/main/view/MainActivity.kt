package io.dwak.holohackernews.app.ui.main.view

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.activity.MvpActivity
import io.dwak.holohackernews.app.butterknife.bindView
import io.dwak.holohackernews.app.dagger.component.DaggerInteractorComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.extension.navigateTo
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.navigation.DrawerItem
import io.dwak.holohackernews.app.model.navigation.DrawerItemType
import io.dwak.holohackernews.app.ui.list.view.StoryListFragment
import io.dwak.holohackernews.app.ui.main.presenter.MainPresenter
import io.dwak.holohackernews.app.ui.navigation.presenter.NavigationDrawerPresenter
import io.dwak.holohackernews.app.ui.navigation.view.NavigationDrawerView
import rx.Observer
import java.util.*
import javax.inject.Inject

class MainActivity : MvpActivity<MainPresenter>(),
        MainView,
        NavigationDrawerView,
        StoryListFragment.StoryListInteractionListener {

    private val toolbar : Toolbar by bindView(R.id.toolbar)
    lateinit var navigationPresenter : NavigationDrawerPresenter @Inject set
    override fun inject() {
        DaggerPresenterComponent.builder()
                .presenterModule(PresenterModule(this))
                .interactorComponent(DaggerInteractorComponent.builder()
                        .interactorModule(InteractorModule(this))
                        .build())
                .build()
                .inject(this);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navigationPresenter.prepareToAttachToView()
    }

    override fun onResume() {
        super.onResume()
        navigationPresenter.onAttachToView()
    }

    override fun onPause() {
        super.onPause()
        navigationPresenter.onDetachFromView()
    }

    override fun navigateToStoryList() {
        navigateTo(StoryListFragment.newInstance(Feed.TOP), addToBackStack = false)
    }

    override fun navigateToStoryDetail(itemId: Long?) {

    }

    override val drawerObserver: Observer<DrawerItem> = object: Observer<DrawerItem> {
        val drawerItems = ArrayList<IDrawerItem<*>>()

        override fun onCompleted() {
            DrawerBuilder().withToolbar(toolbar)
                    .withActivity(this@MainActivity)
                    .withDrawerItems(drawerItems)
                    .build()
        }

        override fun onNext(item: DrawerItem) {
            with(drawerItems) {
                when (item.type) {
                    DrawerItemType.PRIMARY -> add(PrimaryDrawerItem().withName(item.titleRes).withIcon(item.iconRes))
                    DrawerItemType.SECONDARY -> add(SecondaryDrawerItem().withName(item.titleRes).withIcon(item.iconRes))
                    DrawerItemType.DIVIDER -> add(DividerDrawerItem())
                }
            }
        }

        override fun onError(e: Throwable?) {
            throw UnsupportedOperationException()
        }
    }
}