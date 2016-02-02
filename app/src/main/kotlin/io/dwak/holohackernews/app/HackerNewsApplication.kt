package io.dwak.holohackernews.app

import android.app.Application
import com.bugsnag.android.Bugsnag
import com.facebook.stetho.Stetho
import io.dwak.holohackernews.app.dagger.component.DaggerAppComponent
import io.dwak.holohackernews.app.dagger.module.AppModule
import timber.log.Timber

class HackerNewsApplication : Application() {

    companion object {
        lateinit var instance : HackerNewsApplication
    }

    val appComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        Timber.plant(appComponent.tree);
        if("release".equals(BuildConfig.BUILD_TYPE)){
            Bugsnag.init(this);
        }
        instance = this
    }

}