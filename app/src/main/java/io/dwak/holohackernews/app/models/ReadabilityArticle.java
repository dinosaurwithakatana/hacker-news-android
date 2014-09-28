package io.dwak.holohackernews.app.models;

/**
 * Created by vishnu on 5/19/14.
 */
public class ReadabilityArticle {
    private String mTitle;
    private String mLeadImageUrl;
    private String mShortUrl;
    private String mContent;

    public ReadabilityArticle(String title, String leadImageUrl, String shortUrl, String content) {
        mTitle = title;
        mLeadImageUrl = leadImageUrl;
        mShortUrl = shortUrl;
        mContent = content;
    }

    public String getLeadImageUrl() {
        return mLeadImageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public String getShortUrl() {
        return mShortUrl;
    }
}
