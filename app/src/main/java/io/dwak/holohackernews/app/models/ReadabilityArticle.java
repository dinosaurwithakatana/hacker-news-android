package io.dwak.holohackernews.app.models;

/**
 * Created by vishnu on 5/19/14.
 */
public class ReadabilityArticle {
    private String mTitle;
    private String mLeadImageUrl;
    private String mShortUrl;
    private String mContent;


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
