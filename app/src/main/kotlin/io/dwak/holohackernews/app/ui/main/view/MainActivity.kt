package io.dwak.holohackernews.app.ui.main.view

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.widget.FrameLayout
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.activity.MvpActivity
import io.dwak.holohackernews.app.butterknife.bindOptionalView
import io.dwak.holohackernews.app.butterknife.bindView
import io.dwak.holohackernews.app.dagger.component.DaggerInteractorComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.extension.PrimaryDrawerItem
import io.dwak.holohackernews.app.extension.SecondaryDrawerItem
import io.dwak.holohackernews.app.extension.itemClicks
import io.dwak.holohackernews.app.extension.navigateTo
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.navigation.DrawerItemModel
import io.dwak.holohackernews.app.model.navigation.DrawerItemType
import io.dwak.holohackernews.app.ui.list.view.StoryListFragment
import io.dwak.holohackernews.app.ui.main.presenter.MainPresenter
import io.dwak.holohackernews.app.ui.navigation.presenter.NavigationDrawerPresenter
import io.dwak.holohackernews.app.ui.navigation.view.NavigationDrawerView
import rx.Observable
import rx.Observer
import java.util.*
import javax.inject.Inject

class MainActivity : MvpActivity<MainPresenter>(),
        MainView,
        NavigationDrawerView,
        StoryListFragment.StoryListInteractionListener {

    override var drawerClicks: Observable<Int>? = null
    private val toolbar : Toolbar by bindView(R.id.toolbar)
    private val detailsContainer : FrameLayout? by bindOptionalView(R.id.details_container)
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
        navigationPresenter.items.subscribe(observer)
    }

    override fun onResume() {
        super.onResume()
        navigationPresenter.onAttachToView()
    }

    override fun onPause() {
        super.onPause()
        navigationPresenter.onDetachFromView()
    }

    override fun navigateToStoryList(feed : Feed) {
        navigateTo(StoryListFragment.newInstance(feed), addToBackStack = false)
    }

    override fun navigateToStoryDetail(itemId: Long?) {
    }

    override fun navigateToSettings() {
    }

    override fun navigateToAbout() {
    }

    override val observer = object: Observer<DrawerItemModel> {
        val drawerItems = ArrayList<IDrawerItem<*>>()

        override fun onCompleted() {
            val accountHeader = AccountHeaderBuilder().withActivity(this@MainActivity)
                    .withProfileImagesVisible(true)
                    .withHeaderBackground(ContextCompat.getDrawable(this@MainActivity, R.drawable.orange_button))
                    .build()
            drawerClicks = DrawerBuilder().withToolbar(toolbar)
                    .withActivity(this@MainActivity)
                    .withAccountHeader(accountHeader)
                    .withDrawerItems(drawerItems)
                    .build()
                    .itemClicks()
        }

        override fun onNext(item: DrawerItemModel) {
            with(drawerItems) {
                when (item.type) {
                    DrawerItemType.PRIMARY -> add(PrimaryDrawerItem(item.id, item.titleRes, item.iconRes))
                    DrawerItemType.SECONDARY -> add(SecondaryDrawerItem(item.id, item.titleRes, item.iconRes))
                    DrawerItemType.DIVIDER -> add(DividerDrawerItem())
                }
            }
        }

        override fun onError(e: Throwable?) {
            throw UnsupportedOperationException()
        }
    }
}