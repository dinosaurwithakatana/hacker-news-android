package io.dwak.rx.events.events.abslistview;

import android.view.View;
import android.widget.AbsListView;

/**
 * Created by vishnu on 10/12/14.
 */
public class RxListItemClickEvent {
    AbsListView mAbsListView;
    View mView;
    int mPosition;
    long mId;

    public RxListItemClickEvent(AbsListView absListView, View view, int position, long id) {
        mAbsListView = absListView;
        mView = view;
        mPosition = position;
        mId = id;
    }

    public AbsListView getAbsListView() {
        return mAbsListView;
    }

    public View getView() {
        return mView;
    }

    public int getPosition() {
        return mPosition;
    }

    public long getId() {
        return mId;
    }
}
