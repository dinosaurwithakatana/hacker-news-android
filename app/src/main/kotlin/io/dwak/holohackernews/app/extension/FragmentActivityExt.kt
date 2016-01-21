@file:JvmName("FragmentActivityUtils")

package io.dwak.holohackernews.app.extension

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import io.dwak.holohackernews.app.R


@JvmOverloads
fun FragmentActivity.navigateTo(fragment : Fragment,
                                container : Int = R.id.container,
                                addToBackStack : Boolean = true,
                                backStackTag : String = fragment.toString(),
                                extraOperations : (FragmentActivity.() -> (Unit))? = null) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(container, fragment)
    if(addToBackStack) transaction.addToBackStack(backStackTag)
    extraOperations?.invoke(this)
    transaction.commit()
}

fun FragmentActivity.getInt(key : String, defaultValue : Int = 0) = intent.getIntExtra(key, defaultValue)
fun FragmentActivity.getLong(key : String, defaultValue : Long = 0) = intent.getLongExtra(key, defaultValue)
fun FragmentActivity.getBoolean(key : String, defaultValue : Boolean = false) = intent.getBooleanExtra(key, defaultValue)
fun FragmentActivity.getString(key : String, defaultValue : String? = null) : String? = intent.getStringExtra(key)

