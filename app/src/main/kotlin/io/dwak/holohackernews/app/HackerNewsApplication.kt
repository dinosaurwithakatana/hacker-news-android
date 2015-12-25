package io.dwak.holohackernews.app

import android.app.Application
import com.facebook.stetho.Stetho
import io.dwak.holohackernews.app.dagger.component.DaggerAppComponent
import io.dwak.holohackernews.app.dagger.module.AppModule

class HackerNewsApplication : Application() {
    val appComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }

}