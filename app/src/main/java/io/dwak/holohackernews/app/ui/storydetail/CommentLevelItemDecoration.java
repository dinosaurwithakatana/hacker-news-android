package io.dwak.holohackernews.app.ui.storydetail;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

/**
 * Created by vishnu on 2/2/15.
 */
public class CommentLevelItemDecoration extends RecyclerView.ItemDecoration{
    private int mLevel;

    public CommentLevelItemDecoration() {
        super();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }
}
