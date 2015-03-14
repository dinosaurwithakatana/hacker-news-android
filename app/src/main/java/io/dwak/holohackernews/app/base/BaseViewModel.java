package io.dwak.holohackernews.app.base;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import io.dwak.holohackernews.app.manager.hackernews.LongTypeAdapter;
import io.dwak.holohackernews.app.network.HackerNewsService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by vishnu on 2/15/15.
 */
public class BaseViewModel {
    private Resources mResources;
    private HackerNewsService mHackerNewsService;

    public void setResources(@NonNull Resources resources){
        mResources = resources;
    }

    public Resources getResources() {
        return mResources;
    }

    public HackerNewsService getHackerNewsService(){
        if(mHackerNewsService==null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Long.class, new LongTypeAdapter());
            Gson gson = gsonBuilder.create();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setConverter(new GsonConverter(gson))
                    .setClient(new OkClient(new OkHttpClient()))
                    .setEndpoint("https://whispering-fortress-7282.herokuapp.com/")
                    .build();

            mHackerNewsService = restAdapter.create(HackerNewsService.class);
        }

        return mHackerNewsService;
    }

    public void onAttachToView() {

    }

    public void onDetachFromView() {
        mHackerNewsService = null;
        mResources = null;
    }
}
