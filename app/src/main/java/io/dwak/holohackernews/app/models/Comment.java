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
