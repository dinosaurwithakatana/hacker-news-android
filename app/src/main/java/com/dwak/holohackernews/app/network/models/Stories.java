package com.dwak.holohackernews.app.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vishnu on 4/21/14.
 */
public class Stories {
    @SerializedName("stories")
    List<Story> mStories;

    public List<Story> getStories() {
        return mStories;
    }

    @Override
    public String toString() {
        return "Stories{" +
                "mStories=" + mStories +
                '}';
    }
}
