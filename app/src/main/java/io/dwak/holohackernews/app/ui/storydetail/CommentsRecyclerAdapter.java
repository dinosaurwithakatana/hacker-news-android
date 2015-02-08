package io.dwak.holohackernews.app.ui.storydetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dwak.holohackernews.app.models.Comment;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = CommentsRecyclerAdapter.class.getSimpleName();
    ArrayList<CommentRecyclerItem> mList;
    @NonNull private Context mContext;
    @NonNull private CommentsRecyclerListener mListener;
    private Map<Long, List<Comment>> mHiddenComments;

    public CommentsRecyclerAdapter(@NonNull Context context, @NonNull CommentsRecyclerListener listener) {
        mContext = context;
        mListener = listener;
        mList = new ArrayList<>();
        mHiddenComments = new HashMap<>();
    }

    public void addHeaderView(@NonNull View headerView) {
        CommentRecyclerItem<View> item = new CommentRecyclerItem<>(headerView, CommentRecyclerItem.VIEW_TYPE_HEADER);
        mList.add(0, item);
        notifyItemInserted(0);
    }

    public void updateHeaderView(View headerView) {
        CommentRecyclerItem<View> item = new CommentRecyclerItem<>(headerView, CommentRecyclerItem.VIEW_TYPE_HEADER);
        mList.set(0, item);
        notifyItemChanged(0);
    }

    public void addComment(@NonNull Comment comment) {
        CommentRecyclerItem<Comment> item = new CommentRecyclerItem<Comment>(comment, CommentRecyclerItem.VIEW_TYPE_COMMENT);
        mList.add(item);
        notifyItemInserted(mList.size());
    }

    public void addComment(int position, @NonNull Comment comment) {
        CommentRecyclerItem<Comment> item = new CommentRecyclerItem<Comment>(comment, CommentRecyclerItem.VIEW_TYPE_COMMENT);
        mList.add(position, item);
        notifyItemInserted(position);
    }

    public void clear() {
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
                viewHolder = HeaderViewHolder.create(mContext, (View) mList.get(0).getObject());
                break;
            case CommentRecyclerItem.VIEW_TYPE_COMMENT:
                viewHolder = CommentViewHolder.create(LayoutInflater.from(mContext), mListener::onCommentClicked);
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
                final Comment comment = (Comment) mList.get(position).getObject();
                int hiddenCommentCount = 0;
                if(mHiddenComments.containsKey(comment.getId())){
                    hiddenCommentCount = mHiddenComments.get(comment.getId()).size();
                }
                CommentViewHolder.bind(mContext, (CommentViewHolder) holder, comment, hiddenCommentCount);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getViewType();
    }

    @Override
    public long getItemId(int position) {
        return ((Comment) mList.get(position).getObject()).getId();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position).getObject();
    }

    public void hideChildComments(int position) {
        ArrayList<Comment> childrenComments = new ArrayList<>();
        ArrayList<Integer> childrenCommentPositions = new ArrayList<>();
        ArrayList<CommentRecyclerItem> commentRecyclerItemsToRemove = new ArrayList<>();
        Comment parentComment = (Comment) mList.get(position).getObject();
        List<CommentRecyclerItem> possibleChildrenComments = mList.subList(position + 1, mList.size());
        for (CommentRecyclerItem possibleChildrenComment : possibleChildrenComments) {
            if (((Comment) possibleChildrenComment.getObject()).getLevel() > parentComment.getLevel()) {
                childrenCommentPositions.add(mList.indexOf(possibleChildrenComment));
                childrenComments.add(((Comment) possibleChildrenComment.getObject()));
                commentRecyclerItemsToRemove.add(possibleChildrenComment);
            }
            else {
                break;
            }
        }

        if (!commentRecyclerItemsToRemove.isEmpty()) {
            for (Object o : commentRecyclerItemsToRemove) {
                mList.remove(o);
            }
        }

        if (!childrenCommentPositions.isEmpty()) {
            notifyItemRangeRemoved(childrenCommentPositions.get(0), childrenCommentPositions.size());
            mHiddenComments.put(parentComment.getId(), childrenComments);
            notifyItemChanged(position);
        }
    }

    public void showChildComments(int position) {
        CommentRecyclerItem parentCommentItem = mList.get(position);
        final Comment parentComment = (Comment) parentCommentItem.getObject();
        List<Comment> hiddenCommentsForParent = mHiddenComments.get(parentComment.getId());
        int insertIndex = mList.indexOf(parentCommentItem) + 1;
        for (Comment comment : hiddenCommentsForParent) {
            addComment(insertIndex, comment);
            insertIndex++;
        }

        notifyItemRangeInserted(mList.indexOf(parentCommentItem) + 1, hiddenCommentsForParent.size() - 1);
        mHiddenComments.remove(parentComment.getId());
        notifyItemChanged(position);
    }

    public boolean areChildrenHidden(int position) {
        Comment comment = (Comment) mList.get(position).getObject();
        return mHiddenComments.containsKey(comment.getId());
    }

    public interface CommentsRecyclerListener {
        void onCommentClicked(int position);
    }
}
