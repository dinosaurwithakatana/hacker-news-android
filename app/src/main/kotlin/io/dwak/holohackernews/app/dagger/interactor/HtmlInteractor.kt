package io.dwak.holohackernews.app.dagger.interactor

interface HtmlInteractor {
    val htmlParser : HtmlParser

    interface HtmlParser {
        fun fromHtml(string : String?) : CharSequence
    }
}