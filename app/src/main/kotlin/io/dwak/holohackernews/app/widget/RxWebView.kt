package io.dwak.holohackernews.app.widget

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import rx.Observable
import rx.android.MainThreadSubscription

class RxWebView
@JvmOverloads
constructor(context : Context,
            attrs : AttributeSet? = null,
            defStyle : Int = 0)
: WebView(context, attrs, defStyle) {
  private var onScroll : ((Int, Int, Int, Int) -> (Unit))? = null

  override fun onScrollChanged(l : Int, t : Int, oldl : Int, oldt : Int) {
    super.onScrollChanged(l, t, oldl, oldt)
    onScroll?.invoke(l, t, oldl, oldt)
  }

  data class WebViewScrollEvent(val l : Int, val t : Int, val oldL : Int, val oldT : Int)

  fun scrolls() = Observable.create<WebViewScrollEvent> {
    if (!it.isUnsubscribed) {
      onScroll = { l, t, oldL, oldT ->
        it.onNext(WebViewScrollEvent(l, t, oldL, oldT))
      }
    }

    it.add(object: MainThreadSubscription() { override fun onUnsubscribe() { onScroll = null } })
  }
}

