package io.dwak.holohackernews.app

import android.app.Application
import com.facebook.stetho.Stetho

class HackerNewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}