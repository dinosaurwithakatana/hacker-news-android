package io.dwak.holohackernews.app.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vishnu on 5/3/14.
 */
public class NodeHNAPIStoryDetail {
    @SerializedName("id") private Long mId;
    @SerializedName("title") private String mTitle;
    @SerializedName("url") private String mUrl;
    @SerializedName("domain") private String mDomain;
    @SerializedName("points") private Integer mPoints;
    @SerializedName("user") private String mUser;
    @SerializedName("time_ago") private String mTimeAgo;
    @SerializedName("comments_count") private Integer mCommentsCount;
    @SerializedName("content") private String mContent;
    @SerializedName("poll") private Object mPoll;
    @SerializedName("link") private String mLink;
    @SerializedName("comments") private List<NodeHNAPIComment> mNodeHNAPICommentList;
    @SerializedName("more_comments_id") private Long mMoreCommentsId;
    @SerializedName("type") private String mType;

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
                ", mCommentList=" + mNodeHNAPICommentList +
                ", mMoreCommentsId=" + mMoreCommentsId +
                ", mType='" + mType + '\'' +
                '}';
    }

    public Long getId() {
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

    public Integer getPoints() {
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

    public List<NodeHNAPIComment> getNodeHNAPICommentList() {
        return mNodeHNAPICommentList;
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
