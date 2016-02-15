package io.dwak.holohackernews.app.widget

import android.webkit.WebChromeClient
import android.webkit.WebView
import com.jakewharton.rxrelay.PublishRelay

class RxWebChromeClient : WebChromeClient() {
  private val progressRelay = PublishRelay.create<Int>()
  override fun onProgressChanged(view : WebView?, newProgress : Int) {
    super.onProgressChanged(view, newProgress)
    progressRelay.call(newProgress)
  }

  fun progress() = progressRelay.asObservable()
}
