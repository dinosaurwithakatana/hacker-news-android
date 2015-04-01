package io.dwak.holohackernews.app.ui.storydetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dwak.holohackernews.app.models.Comment;
import io.dwak.holohackernews.app.models.StoryDetail;
import io.dwak.holohackernews.app.util.HNLog;

public class StoryDetailRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = StoryDetailRecyclerAdapter.class.getSimpleName();
    private ArrayList<StoryDetailRecyclerItem> mList;
    @NonNull private Context mContext;
    @NonNull private StoryDetailRecyclerListener mListener;
    private Map<Long, List<Comment>> mHiddenComments;

    public StoryDetailRecyclerAdapter(@NonNull Context context, @NonNull StoryDetailRecyclerListener listener) {
        mContext = context;
        mListener = listener;
        mList = new ArrayList<>();
        mHiddenComments = new HashMap<>();
    }

    public void updateHeader(@NonNull StoryDetail storyDetail){
        StoryDetailRecyclerItem<StoryDetail> item = new StoryDetailRecyclerItem<>(storyDetail, StoryDetailRecyclerItem.VIEW_TYPE_HEADER);
        if(!mList.isEmpty() && mList.get(0).getViewType() == StoryDetailRecyclerItem.VIEW_TYPE_HEADER) {
            mList.set(0, item);
            notifyItemChanged(0);
        }
        else {
            mList.add(0, item);
            notifyItemInserted(0);
        }
    }

    public void addComment(@NonNull Comment comment) {
        StoryDetailRecyclerItem<Comment> item = new StoryDetailRecyclerItem<Comment>(comment, StoryDetailRecyclerItem.VIEW_TYPE_COMMENT);
        mList.add(item);
        notifyItemInserted(mList.size());
    }

    public void addComment(int position, @NonNull Comment comment) {
        StoryDetailRecyclerItem<Comment> item = new StoryDetailRecyclerItem<Comment>(comment, StoryDetailRecyclerItem.VIEW_TYPE_COMMENT);
        mList.add(position, item);
        notifyItemInserted(position);
    }

    public void clear() {
        final StoryDetailRecyclerItem storyDetailRecyclerItem = mList.get(0);
        mList.clear();
        mList.add(storyDetailRecyclerItem);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HNLog.d(TAG, "#onCreateViewHolder");
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case StoryDetailRecyclerItem.VIEW_TYPE_HEADER:
                viewHolder = HeaderViewHolder.create(LayoutInflater.from(mContext), parent);
                break;
            case StoryDetailRecyclerItem.VIEW_TYPE_COMMENT:
                viewHolder = CommentViewHolder.create(
                        LayoutInflater.from(mContext),
                        parent);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HNLog.d(TAG, "#onBindViewHolder");
        switch (getItemViewType(position)) {
            case StoryDetailRecyclerItem.VIEW_TYPE_HEADER:
                HeaderViewHolder.bind(mContext,
                        (HeaderViewHolder) holder,
                        (StoryDetail) mList.get(0).getObject());
                break;
            case StoryDetailRecyclerItem.VIEW_TYPE_COMMENT:
                final Comment comment = (Comment) mList.get(position).getObject();
                int hiddenCommentCount = 0;
                if(mHiddenComments.containsKey(comment.getId())){
                    hiddenCommentCount = mHiddenComments.get(comment.getId()).size();
                }
                CommentViewHolder.bind(mContext,
                        (CommentViewHolder) holder,
                        comment,
                        hiddenCommentCount,
                        mListener,
                        position);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getViewType();
    }

    @Override
    public long getItemId(int position) {
        if (position > 0) {
            return ((Comment) mList.get(position).getObject()).getId();
        }
        else {
            return 0;
        }
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
        ArrayList<StoryDetailRecyclerItem> storyDetailRecyclerItemsToRemove = new ArrayList<>();
        Comment parentComment = (Comment) mList.get(position).getObject();
        List<StoryDetailRecyclerItem> possibleChildrenComments = mList.subList(position + 1, mList.size());
        for (StoryDetailRecyclerItem possibleChildrenComment : possibleChildrenComments) {
            if (((Comment) possibleChildrenComment.getObject()).getLevel() > parentComment.getLevel()) {
                childrenCommentPositions.add(mList.indexOf(possibleChildrenComment));
                childrenComments.add(((Comment) possibleChildrenComment.getObject()));
                storyDetailRecyclerItemsToRemove.add(possibleChildrenComment);
            }
            else {
                break;
            }
        }

        if (!storyDetailRecyclerItemsToRemove.isEmpty()) {
            for (Object o : storyDetailRecyclerItemsToRemove) {
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
        StoryDetailRecyclerItem parentCommentItem = mList.get(position);
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

    public interface StoryDetailRecyclerListener {
        void onCommentClicked(int position);
    }
}
