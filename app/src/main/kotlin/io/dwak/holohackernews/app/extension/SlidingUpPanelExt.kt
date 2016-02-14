@file:JvmName("SlidingUpPanelLayoutUtils")
package io.dwak.holohackernews.app.extension

import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import rx.Observable
import rx.android.MainThreadSubscription

enum class PanelEvent { COLLAPSED, EXPANDED, HIDDEN, ANCHORED, SLIDE }
data class PanelData(val event : PanelEvent, val panel : View?, val slideOffset : Float? = null)

fun SlidingUpPanelLayout.panelSlides() : Observable<PanelData> {
    return Observable.defer<PanelData> {
        Observable.create {
            if (!it.isUnsubscribed) {
                setPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
                    override fun onPanelSlide(panel : View?, slideOffset : Float) = it.onNext(PanelData(PanelEvent.SLIDE, panel, slideOffset))
                    override fun onPanelExpanded(panel : View?) = it.onNext(PanelData(PanelEvent.EXPANDED, panel))
                    override fun onPanelCollapsed(panel : View?) = it.onNext(PanelData(PanelEvent.COLLAPSED, panel))
                    override fun onPanelHidden(panel : View?) = it.onNext(PanelData(PanelEvent.HIDDEN, panel))
                    override fun onPanelAnchored(panel : View?) = it.onNext(PanelData(PanelEvent.ANCHORED, panel))
                })

                it.add(object : MainThreadSubscription() {
                    override fun onUnsubscribe() = setPanelSlideListener(null)
                })
            }
        }
    }
}