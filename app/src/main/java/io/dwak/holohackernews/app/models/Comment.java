package io.dwak.holohackernews.app.models;

import com.orm.SugarRecord;

public class Comment extends SugarRecord<Comment>{
    private Long mCommentId;
    private int mLevel;
    private boolean mOriginalPoster;
    private String mUser;
    private String mTimeAgo;
    private String mContent;
    private StoryDetail mStoryDetail;

    private boolean mHidden;

    public Comment() {
    }

    public Comment(Long commentId, int level, boolean originalPoster, String user, String timeAgo, String content, StoryDetail storyDetail) {
        mCommentId = commentId;
        mLevel = level;
        mOriginalPoster = originalPoster;
        mUser = user;
        mTimeAgo = timeAgo;
        mContent = content;
        mStoryDetail = storyDetail;
    }

    public long getCommentId() {
        return mCommentId;
    }

    public int getLevel() {
        return mLevel;
    }

    public String getUser() {
        return mUser;
    }

    public String getTimeAgo() {
        return mTimeAgo;
    }

    public String getContent() {
        return mContent;
    }

    public void setCommentId(Long commentId) {
        mCommentId  = commentId;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public void setUser(String user) {
        mUser = user;
    }

    public void setTimeAgo(String timeAgo) {
        mTimeAgo = timeAgo;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public boolean isOriginalPoster() {
        return mOriginalPoster;
    }

    public void setOriginalPoster(boolean originalPoster) {
        mOriginalPoster = originalPoster;
    }

    public boolean isHidden() {
        return mHidden;
    }

    public void setHidden(boolean hidden) {
        mHidden = hidden;
    }

}
