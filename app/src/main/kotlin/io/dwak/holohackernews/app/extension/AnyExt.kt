package io.dwak.holohackernews.app.extension

fun consume(f : () -> Unit) : Boolean {
    f()
    return true
}
