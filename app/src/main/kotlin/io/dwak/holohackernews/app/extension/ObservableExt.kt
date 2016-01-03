@file:JvmName("ObservableUtils")

package io.dwak.holohackernews.app.extension

import rx.Observable

fun <T> Observable<T>.subscribe(onComplete : () -> Unit = {}){
    subscribe({}, {}, onComplete)
}

