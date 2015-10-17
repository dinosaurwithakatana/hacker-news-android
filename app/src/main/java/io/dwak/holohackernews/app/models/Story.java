package io.dwak.holohackernews.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import io.dwak.holohackernews.app.network.models.NodeHNAPIStory;

public class Story extends SugarRecord<Story> implements Parcelable {
    private Long mStoryId;
    private String mTitle;
    private String mUrl;
    private String mDomain;
    private int mPoints;
    private String mSubmitter;
    private String mPublishedTime;
    private int mNumComments;
    private String mType;
    private boolean isSaved;
    private boolean mIsRead;

    public Story() {
    }

    private Story(Long storyId, String title, String url, String domain, int points, String submitter, String publishedTime, int numComments, String type) {
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

    public static Story fromNodeHNAPIStory(NodeHNAPIStory nodeHNAPIStory) {
        return new Story(nodeHNAPIStory.id,
                         nodeHNAPIStory.title,
                         nodeHNAPIStory.url,
                         nodeHNAPIStory.domain,
                         nodeHNAPIStory.points,
                         nodeHNAPIStory.user,
                         nodeHNAPIStory.timeAgo,
                         nodeHNAPIStory.commentsCount,
                         nodeHNAPIStory.type);
    }

    public Long getStoryId() {
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

    public boolean isSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Story story = (Story) o;

        if (mStoryId != null ? !mStoryId.equals(story.mStoryId) : story.mStoryId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mStoryId != null ? mStoryId.hashCode() : 0;
    }

    public boolean isRead() {
        return mIsRead;
    }

    public void setIsRead(boolean isRead) {
        this.mIsRead = isRead;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mStoryId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mUrl);
        dest.writeString(this.mDomain);
        dest.writeInt(this.mPoints);
        dest.writeString(this.mSubmitter);
        dest.writeString(this.mPublishedTime);
        dest.writeInt(this.mNumComments);
        dest.writeString(this.mType);
        dest.writeByte(isSaved ? (byte) 1 : (byte) 0);
        dest.writeByte(mIsRead ? (byte) 1 : (byte) 0);
    }

    protected Story(Parcel in) {
        this.mStoryId = (Long) in.readValue(Long.class.getClassLoader());
        this.mTitle = in.readString();
        this.mUrl = in.readString();
        this.mDomain = in.readString();
        this.mPoints = in.readInt();
        this.mSubmitter = in.readString();
        this.mPublishedTime = in.readString();
        this.mNumComments = in.readInt();
        this.mType = in.readString();
        this.isSaved = in.readByte() != 0;
        this.mIsRead = in.readByte() != 0;
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        public Story createFromParcel(Parcel source) {
            return new Story(source);
        }

        public Story[] newArray(int size) {
            return new Story[size];
        }
    };
}

