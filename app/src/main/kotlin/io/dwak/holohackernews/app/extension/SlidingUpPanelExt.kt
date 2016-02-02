package io.dwak.holohackernews.app.extension

import android.view.View
import com.jakewharton.rxbinding.internal.MainThreadSubscription
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import rx.Observable

enum class PanelEvent { COLLAPSED, EXPANDED, HIDDEN, ANCHORED }

fun SlidingUpPanelLayout.panelSlides() : Observable<PanelEvent> {
    return Observable.create {
        if(!it.isUnsubscribed){
            setPanelSlideListener(object: SlidingUpPanelLayout.PanelSlideListener {
                override fun onPanelSlide(panel : View?, slideOffset : Float) {}
                override fun onPanelExpanded(panel : View?) = it.onNext(PanelEvent.EXPANDED)
                override fun onPanelCollapsed(panel : View?) = it.onNext(PanelEvent.COLLAPSED)
                override fun onPanelHidden(panel : View?) = it.onNext(PanelEvent.HIDDEN)
                override fun onPanelAnchored(panel : View?) = it.onNext(PanelEvent.ANCHORED)
            })

            it.add(object: MainThreadSubscription(){
                override fun onUnsubscribe() = setPanelSlideListener(null)
            })
        }
    }
}