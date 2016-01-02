package io.dwak.holohackernews.app.extension

import android.os.Bundle
import android.support.v4.app.Fragment
import java.io.Serializable

inline fun <T : Fragment> T.build(f : Bundle.() -> Unit) : T {
    val args = Bundle()
    f.invoke(args)
    arguments = args
    return this
}

//inline fun <reified T> Fragment.getArg(key : String) : T {
//    when (arguments.get(key)) {
//        is Serializable -> return arguments.getSerializable(key) as T
//        is Int -> return arguments.getInt(key) as T
//    }
//
//    throw RuntimeException("Type is not supported")
//}

@Suppress("UNCHECKED_CAST")
fun <T : Serializable> Fragment.getSerializable(key : String) = arguments.getSerializable(key) as T
fun Fragment.getInt(key : String, defaultValue : Int = 0) = arguments.getInt(key, defaultValue)
fun Fragment.getBoolean(key : String, defaultValue : Boolean = false) = arguments.getBoolean(key, defaultValue)
