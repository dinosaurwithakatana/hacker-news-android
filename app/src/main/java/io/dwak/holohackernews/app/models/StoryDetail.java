package io.dwak.holohackernews.app.models;

import android.support.annotation.StringDef;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class StoryDetail extends SugarRecord<StoryDetail>{
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({JOB, LINK, ASK})
    public @interface StoryType{}
    public static final String JOB = "job";
    public static final String LINK = "link";
    public static final String ASK = "ask";
    public static final String STORY_DETAIL_ID = "mStoryDetailId";

    private Long mStoryDetailId;
    private String mTitle;
    private String mUrl;
    private String mDomain;
    private Integer mPoints;
    private String mUser;
    private String mTimeAgo;
    private Integer mCommentsCount;
    private String mContent;
    @Ignore private Object mPoll;
    private String mLink;
    @Ignore private List<Comment> mCommentList;
    private Long mMoreCommentsId;
    private String mType;

    public StoryDetail() {
    }

    public StoryDetail(Long storyDetailId, String title, String url,
                       String domain, Integer points, String user,
                       String timeAgo, Integer commentsCount, String content,
                       Object poll, String link, List<Comment> commentList,
                       Long moreCommentsId, @StoryType String type) {
        mStoryDetailId = storyDetailId;
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

    public Long getStoryDetailId() {
        return mStoryDetailId;
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

    public List<Comment> getCommentList() {
        return mCommentList;
    }

    public void setCommentList(List<Comment> commentList) {
        mCommentList = commentList;
    }

    public Long getMoreCommentsId() {
        return mMoreCommentsId;
    }

    @StoryType
    public String getType() {
        return mType;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
