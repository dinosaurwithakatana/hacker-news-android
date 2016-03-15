package io.dwak.holohackernews.app.extension

import android.webkit.WebView

fun WebView.safeBack() { if(canGoBack()) goBack() }
fun WebView.safeForward() { if(canGoForward()) goForward() }

