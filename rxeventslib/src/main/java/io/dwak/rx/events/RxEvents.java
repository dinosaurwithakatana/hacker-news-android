package io.dwak.rx.events;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;

import io.dwak.rx.events.events.abslistview.RxListItemClickEvent;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Contains static methods to create observables out of Android views
 * Created by vishnu on 10/12/14.
 */
public class RxEvents {
    /**
     * Creates an {@link rx.Observable} stream from {@link android.widget.TextView#addTextChangedListener(android.text.TextWatcher)}'s {@link android.text.TextWatcher#afterTextChanged(android.text.Editable)}
     * @param view TextView to subscribe to
     * @return Observable data stream for events from the TextView
     */
    public static Observable<String> observableFromTextChanged(TextView view) {
        String currentText = String.valueOf(view.getText());
        final BehaviorSubject<String> subject = BehaviorSubject.create(currentText);
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                subject.onNext(editable.toString());
            }
        });
        return subject;
    }

    /**
     * Creates an {@link rx.Observable} from a button click and returns the view to the subscriber
     * @param button Button to subscribe to
     * @return Observable data stream for click events from the button
     */
    public static Observable<View> observableFromButtonClick(Button button) {
        final PublishSubject<View> subject = PublishSubject.create();
        button.setOnClickListener((v) -> subject.onNext(v));
        return subject;
    }

    /**
     * Creates an {@link rx.Observable} from a Button's {@link android.view.MotionEvent#ACTION_DOWN} and {@link android.view.MotionEvent#ACTION_UP}
     * events
     * @param button Button to subscribe to
     * @return Observable data stream for motion events from the button
     */
    public static Observable<Boolean> observableFromButtonDown(Button button) {
        final PublishSubject<Boolean> subject = PublishSubject.create();
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    subject.onNext(true);
                    break;
                case MotionEvent.ACTION_UP:
                    subject.onNext(false);
                    break;
            }
            return false;
        });

        return subject;
    }

    /**
     * Creates an {@link rx.Observable} from a ListView to subscribe to list item clicks
     * @param listView Listview to subscribe to
     * @return {@link io.dwak.rx.events.events.abslistview.RxListItemClickEvent} that describes the event
     */
    public static Observable<RxListItemClickEvent> observableFromListItemClick(AbsListView listView){
        final PublishSubject<RxListItemClickEvent> subject = PublishSubject.create();
        listView.setOnItemClickListener((adapterView, view, i, l) -> subject.onNext(new RxListItemClickEvent(listView, view, i,l )));
        return subject;
    }
}
