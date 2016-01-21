package io.dwak.holohackernews.app.widget

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

class ObservableWebView : WebView {
    var onScrollChangedCallback : OnScrollChangedCallback? = null

    constructor(context : Context) : super(context) {
    }

    constructor(context : Context, attrs : AttributeSet) : super(context, attrs) {
    }

    constructor(context : Context, attrs : AttributeSet, defStyle : Int) : super(context, attrs, defStyle) {
    }

    override fun onScrollChanged(l : Int, t : Int, oldl : Int, oldt : Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (onScrollChangedCallback != null) onScrollChangedCallback!!.onScroll(l, t, oldl, oldt)
    }

    /**
     * Implement in the activity/fragment/view that you want to listen to the webview
     */
    interface OnScrollChangedCallback {
        fun onScroll(l : Int, t : Int, oldL : Int, oldT : Int)
    }
}
