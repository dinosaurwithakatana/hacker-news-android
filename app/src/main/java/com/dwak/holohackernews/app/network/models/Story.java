package com.dwak.holohackernews.app.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for a hackernews story
 * Created by vishnu on 4/21/14.
 */
public class Story {
    @SerializedName("id") private Long mStoryId;
    @SerializedName("title") private String mTitle;
    @SerializedName("url") private String mUrl;
    @SerializedName("domain") private String mDomain;
    @SerializedName("points") private int mPoints;
    @SerializedName("user") private String mSubmitter;
    @SerializedName("time_ago") private String mPublishedTime;
    @SerializedName("comments_count") private int mNumComments;
    @SerializedName("type") private String mType;

    public long getStoryId() {
        return mStoryId;
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

    public String getSubmitter() {
        return mSubmitter;
    }

    public String getPublishedTime() {
        return mPublishedTime;
    }

    public int getNumComments() {
        return mNumComments;
    }

    public String getType() {
        return mType;
    }

    @Override
    public String toString() {
        return "Story{" +
                "mStoryId='" + mStoryId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mDomain='" + mDomain + '\'' +
                ", mPoints=" + mPoints +
                ", mSubmitter='" + mSubmitter + '\'' +
                ", mPublishedTime='" + mPublishedTime + '\'' +
                ", mNumComments=" + mNumComments +
                ", mType='" + mType + '\'' +
                '}';
    }
}

