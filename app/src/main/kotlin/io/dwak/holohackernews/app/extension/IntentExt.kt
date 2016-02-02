package io.dwak.holohackernews.app.extension

import android.content.Intent

inline fun Intent.with(f : Intent.() -> Unit) : Intent {
    f()
    return this
}


