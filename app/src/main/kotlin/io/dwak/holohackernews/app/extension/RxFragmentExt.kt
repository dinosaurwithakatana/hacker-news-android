package io.dwak.holohackernews.app.extension

import com.trello.rxlifecycle.FragmentEvent
import com.trello.rxlifecycle.components.support.RxFragment
import rx.Observable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
public fun <L> RxFragment.bindActivity() : ReadOnlyProperty<RxFragment, L> {
    val l = Lazy<RxFragment, L> { t -> t.activity as L }
    Observable.create<Unit> { }
            .compose(bindUntilEvent(FragmentEvent.DETACH))
            .subscribe({}, {}, { l.value = null })

    return l
}

// Like Kotlin's lazy delegate but the initializer gets the target and metadata passed to it
private class Lazy<T, L>(private val initializer: (T) -> L) : ReadOnlyProperty<T, L> {
    var value: L? = null

    override fun getValue(thisRef: T, property: KProperty<*>): L {
        if (value == null) {
            value = initializer(thisRef)
        }
        @Suppress("UNCHECKED_CAST")
        return value!!
    }
}




