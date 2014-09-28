package io.dwak.holohackernews.app.manager.readability;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.dwak.holohackernews.app.HoloHackerNewsApplication;
import io.dwak.holohackernews.app.manager.*;
import io.dwak.holohackernews.app.manager.Exception;
import io.dwak.holohackernews.app.models.ReadabilityArticle;
import io.dwak.holohackernews.app.network.ReadabilityService;
import io.dwak.holohackernews.app.network.models.NetworkReadabilityArticle;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vishnu on 9/28/14.
 */
public class ReadabilityManager {
    private static ReadabilityManager sInstance;
    @Nullable private ReadabilityService mReadabilityService = null;
    private boolean mEnabled = false;

    public ReadabilityManager() {
        if (!HoloHackerNewsApplication.isTRAVIS()) {
            RestAdapter readabilityRestAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://readability.com/api/content/v1/")
                    .build();

            mReadabilityService = readabilityRestAdapter.create(ReadabilityService.class);

            mEnabled = true;
        }
    }

    @NonNull
    public static ReadabilityManager getInstance() {
        if (sInstance == null) {
            sInstance = new ReadabilityManager();
        }

        return sInstance;
    }

    public void getReadabilityForArticle(@NonNull String url, @Nullable final Callback<ReadabilityArticle> callback){
        if (mReadabilityService != null) {
            mReadabilityService.getReadabilityForArticle(HoloHackerNewsApplication.getREADABILITY_TOKEN(), url, new retrofit.Callback<NetworkReadabilityArticle>() {
                @Override
                public void success(NetworkReadabilityArticle networkReadabilityArticle, Response response) {
                    if (callback != null) {
                        callback.onResponse(new ReadabilityArticle(networkReadabilityArticle.getTitle(), networkReadabilityArticle.getLeadImageUrl(), networkReadabilityArticle.getShortUrl(), networkReadabilityArticle.getContent()), null);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (callback != null) {
                        callback.onResponse(null, new Exception(error));
                    }
                }
            });
        }
    }

    public boolean isEnabled() {
        return mEnabled;
    }
}
