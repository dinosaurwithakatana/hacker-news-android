package io.dwak.holohackernews.app.dagger.interactor

import android.text.Html

class HtmlInteractorImpl {
    companion object : HtmlInteractor.HtmlParser{
        override fun fromHtml(string : String?) : CharSequence {
            return Html.fromHtml(string)
        }

    }
}