package io.dwak.holohackernews.app.extension

import com.melnykov.fab.FloatingActionButton
import rx.functions.Action1

fun FloatingActionButton.visibility() : Action1<in Boolean>
        = Action1 { if (it) show() else hide() }

