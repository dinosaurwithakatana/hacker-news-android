package io.dwak.holohackernews.app.models;

import java.util.List;

/**
 * Created by vishnu on 5/3/14.
 */
public class StoryDetail {
    private Long mId;
    private String mTitle;
    private String mUrl;
    private String mDomain;
    private Integer mPoints;
    private String mUser;
    private String mTimeAgo;
    private Integer mCommentsCount;
    private String mContent;
    private Object mPoll;
    private String mLink;
    private List<Comment> mCommentList;
    private Long mMoreCommentsId;
    private String mType;

    public StoryDetail(Long id, String title, String url,
                       String domain, Integer points, String user,
                       String timeAgo, Integer commentsCount, String content,
                       Object poll, String link, List<Comment> commentList,
                       Long moreCommentsId, String type) {
        mId = id;
        mTitle = title;
        mUrl = url;
        mDomain = domain;
        mPoints = points;
        mUser = user;
        mTimeAgo = timeAgo;
        mCommentsCount = commentsCount;
        mContent = content;
        mPoll = poll;
        mLink = link;
        mCommentList = commentList;
        mMoreCommentsId = moreCommentsId;
        mType = type;
    }

    @Override
    public String toString() {
        return "StoryDetail{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mDomain='" + mDomain + '\'' +
                ", mPoints=" + mPoints +
                ", mUser='" + mUser + '\'' +
                ", mTimeAgo='" + mTimeAgo + '\'' +
                ", mCommentsCount=" + mCommentsCount +
                ", mContent='" + mContent + '\'' +
                ", mPoll=" + mPoll +
                ", mLink='" + mLink + '\'' +
                ", mCommentList=" + mCommentList +
                ", mMoreCommentsId=" + mMoreCommentsId +
                ", mType='" + mType + '\'' +
                '}';
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getDomain() {
        return mDomain;
    }

    public int getPoints() {
        return mPoints;
    }

    public String getUser() {
        return mUser;
    }

    public String getTimeAgo() {
        return mTimeAgo;
    }

    public int getCommentsCount() {
        return mCommentsCount;
    }

    public String getContent() {
        return mContent;
    }

    public Object getPoll() {
        return mPoll;
    }

    public String getLink() {
        return mLink;
    }

    public List<Comment> getCommentList() {
        return mCommentList;
    }

    public Long getMoreCommentsId() {
        return mMoreCommentsId;
    }

    public String getType() {
        return mType;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
