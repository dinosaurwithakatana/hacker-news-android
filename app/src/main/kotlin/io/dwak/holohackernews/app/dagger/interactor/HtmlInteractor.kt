package io.dwak.holohackernews.app.dagger.interactor

interface HtmlInteractor {

    interface HtmlParser {
        fun fromHtml(string : String?) : CharSequence
    }
}