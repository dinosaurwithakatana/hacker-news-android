package io.dwak.holohackernews.app.manager.models;

/**
 * Data model for a hackernews story
 * Created by vishnu on 4/21/14.
 */
public class Story {
    private Long mStoryId;
    private String mTitle;
    private String mUrl;
    private String mDomain;
    private int mPoints;
    private String mSubmitter;
    private String mPublishedTime;
    private int mNumComments;
    private String mType;

    public Story() {
    }

    public Story(Long storyId, String title, String url, String domain, int points, String submitter, String publishedTime, int numComments, String type) {
        mStoryId = storyId;
        mTitle = title;
        mUrl = url;
        mDomain = domain;
        mPoints = points;
        mSubmitter = submitter;
        mPublishedTime = publishedTime;
        mNumComments = numComments;
        mType = type;
    }

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

    public void setStoryId(Long storyId) {
        mStoryId = storyId;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setDomain(String domain) {
        mDomain = domain;
    }

    public void setPoints(int points) {
        mPoints = points;
    }

    public void setSubmitter(String submitter) {
        mSubmitter = submitter;
    }

    public void setPublishedTime(String publishedTime) {
        mPublishedTime = publishedTime;
    }

    public void setNumComments(int numComments) {
        mNumComments = numComments;
    }

    public void setType(String type) {
        mType = type;
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

