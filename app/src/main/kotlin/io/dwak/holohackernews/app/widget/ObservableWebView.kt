package io.dwak.holohackernews.app.widget

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import rx.Observable

class ObservableWebView : WebView {
    var onScroll : ((Int, Int, Int, Int) -> (Unit))? = null

    constructor(context : Context) : super(context) {
    }

    constructor(context : Context, attrs : AttributeSet) : super(context, attrs) {
    }

    constructor(context : Context, attrs : AttributeSet, defStyle : Int) : super(context, attrs, defStyle) {
    }

    override fun onScrollChanged(l : Int, t : Int, oldl : Int, oldt : Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScroll?.invoke(l, t, oldl, oldt)
    }

    data class WebviewScrollEvent(val l : Int, val t : Int, val oldL : Int, val oldT : Int)
    fun scrolls() = Observable.create<WebviewScrollEvent> {
        if (!it.isUnsubscribed) {
            onScroll = { l, t, oldL, oldT ->
                it.onNext(WebviewScrollEvent(l, t, oldL, oldT))
            }
        }
    }
}
