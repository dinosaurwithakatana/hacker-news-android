@file:JvmName("MaterialDrawerUtils")

package io.dwak.holohackernews.app.extension

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.jakewharton.rxbinding.internal.MainThreadSubscription
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import rx.Observable

fun PrimaryDrawerItem(id : Int, @StringRes titleRes : Int, @DrawableRes iconRes : Int)  : PrimaryDrawerItem
        = PrimaryDrawerItem().withName(titleRes).withIcon(iconRes).withIdentifier(id)

fun SecondaryDrawerItem(id : Int, @StringRes titleRes : Int, @DrawableRes iconRes : Int) : SecondaryDrawerItem
        = SecondaryDrawerItem().withName(titleRes).withIcon(iconRes).withIdentifier(id)

fun Drawer.itemClicks() : Observable<Int> {
    return Observable.create<Int> {
       if(!it.isUnsubscribed){
           setOnDrawerItemClickListener { view, i, iDrawerItem ->
               it.onNext(iDrawerItem.identifier)
               false
           }

           it.add(object: MainThreadSubscription() {
               override fun onUnsubscribe() {
                   onDrawerItemClickListener = null
               }
           })
       }
    }
}
