package io.dwak.holohackernews.app.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vishnu on 5/19/14.
 */
public class NetworkReadabilityArticle {
    @SerializedName("title") private String mTitle;
    @SerializedName("lead_image_url") private String mLeadImageUrl;
    @SerializedName("short_url") private String mShortUrl;
    @SerializedName("content") private String mContent;


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
