package io.dwak.holohackernews.app.models;

import java.util.List;

/**
 * Created by vishnu on 5/3/14.
 */
public class Comment {
    private Long mId;
    private int mLevel;
    private String mUser;
    private String mTimeAgo;
    private String mContent;
    private List<Comment> mChildComments;

    public Comment() {
    }

    public Comment(Long id, int level, String user, String timeAgo, String content, List<Comment> childComments) {
        mId = id;
        mLevel = level;
        mUser = user;
        mTimeAgo = timeAgo;
        mContent = content;
        mChildComments = childComments;
    }

    public long getId() {
        return mId;
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

    public List<Comment> getChildComments() {
        return mChildComments;
    }

    public void setId(Long id) {
        mId = id;
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

    public void setChildComments(List<Comment> childComments) {
        mChildComments = childComments;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "mId=" + mId +
                ", mLevel=" + mLevel +
                ", mUser='" + mUser + '\'' +
                ", mTimeAgo='" + mTimeAgo + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mChildComments=" + mChildComments +
                '}';
    }

}
