package io.dwak.holohackernews.app.ui.storydetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.dwak.holohackernews.app.models.Comment;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = CommentsRecyclerAdapter.class.getSimpleName();
    ArrayList<CommentRecyclerItem> mList;
    private Context mContext;

    public CommentsRecyclerAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<CommentRecyclerItem>();
    }

    public void addHeaderView(View headerView){
        CommentRecyclerItem<View> item = new CommentRecyclerItem<>(headerView, CommentRecyclerItem.VIEW_TYPE_HEADER);
        mList.add(0, item);
        notifyItemInserted(0);
    }

    public void updateHeaderView(View headerView){
        CommentRecyclerItem<View> item = new CommentRecyclerItem<>(headerView, CommentRecyclerItem.VIEW_TYPE_HEADER);
        mList.set(0, item);
        notifyItemChanged(0);
    }

    public void addComment(Comment comment) {
        CommentRecyclerItem<Comment> item = new CommentRecyclerItem<Comment>(comment, CommentRecyclerItem.VIEW_TYPE_COMMENT);
        mList.add(item);
        notifyItemInserted(mList.size());
    }

    public void clear(){
        final CommentRecyclerItem commentRecyclerItem = mList.get(0);
        mList.clear();
        mList.add(commentRecyclerItem);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "#onCreateViewHolder");
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case CommentRecyclerItem.VIEW_TYPE_HEADER:
                viewHolder = HeaderViewHolder.create((View) mList.get(0).getObject());
                break;
            case CommentRecyclerItem.VIEW_TYPE_COMMENT:
                viewHolder = CommentViewHolder.create(LayoutInflater.from(mContext));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "#onBindViewHolder");
        switch (getItemViewType(position)) {
            case CommentRecyclerItem.VIEW_TYPE_HEADER:
                HeaderViewHolder.bind();
                break;
            case CommentRecyclerItem.VIEW_TYPE_COMMENT:
                CommentViewHolder.bind(mContext, (CommentViewHolder) holder, (Comment) mList.get(position).getObject());
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "#getItemViewType " + position);
        return mList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "#getItemCount " + mList.size());
        return mList.size();
    }

    public Object getItem(int position){
        return mList.get(position).getObject();
    }

}
