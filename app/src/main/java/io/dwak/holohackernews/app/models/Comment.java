package io.dwak.holohackernews.app.models;

/**
 * Created by vishnu on 5/3/14.
 */
public class Comment {
    private Long mId;
    private int mLevel;
    private boolean mOriginalPoster;
    private String mUser;
    private String mTimeAgo;
    private String mContent;

    public Comment() {
    }

    public Comment(Long id, int level, boolean originalPoster, String user, String timeAgo, String content) {
        mId = id;
        mLevel = level;
        mOriginalPoster = originalPoster;
        mUser = user;
        mTimeAgo = timeAgo;
        mContent = content;
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

    @Override
    public String toString() {
        return "Comment{" +
                "mId=" + mId +
                ", mLevel=" + mLevel +
                ", mUser='" + mUser + '\'' +
                ", mTimeAgo='" + mTimeAgo + '\'' +
                ", mContent='" + mContent + '\'' +
                '}';
    }

    public boolean isOriginalPoster() {
        return mOriginalPoster;
    }

    public void setOriginalPoster(boolean originalPoster) {
        mOriginalPoster = originalPoster;
    }
}
