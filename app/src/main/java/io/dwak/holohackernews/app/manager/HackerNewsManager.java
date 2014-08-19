package io.dwak.holohackernews.app.manager;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.dwak.holohackernews.app.HoloHackerNewsApplication;
import io.dwak.holohackernews.app.models.Story;
import io.dwak.holohackernews.app.network.HackerNewsService;
import io.dwak.holohackernews.app.network.ReadabilityService;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStory;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by vishnu on 8/18/14.
 */
public class HackerNewsManager {
    private static HackerNewsManager sInstance;
    private final HackerNewsService mHackerNewsService;
    private ReadabilityService mReadabilityService = null;

    public HackerNewsManager() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Long.class, new LongTypeAdapter());
        Gson gson = gsonBuilder.create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint("http://fathomless-island-9288.herokuapp.com/")
                .build();

        mHackerNewsService = restAdapter.create(HackerNewsService.class);

        if (!HoloHackerNewsApplication.isTRAVIS()) {
            RestAdapter readabilityRestAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://readability.com/api/content/v1/")
                    .build();

            mReadabilityService = readabilityRestAdapter.create(ReadabilityService.class);
        }
    }

    public static HackerNewsManager getInstance() {
        if (sInstance == null) {
            sInstance = new HackerNewsManager();
        }

        return sInstance;
    }

    public void getStories(@NonNull FeedType feedType, @NonNull final HackerNewsCallback<List<Story>> callback){
       switch (feedType){
           case BEST:
               mHackerNewsService.getBestStories(new RetrofitStoryListCallback(callback));
               break;
           case NEW:
               mHackerNewsService.getNewestStories(new RetrofitStoryListCallback(callback));
               break;
           case TOP:
               mHackerNewsService.getTopStories(new RetrofitStoryListCallback(callback));
               break;
       }
    }

    public void getTopStoriesPageTwo(@NonNull final HackerNewsCallback<List<Story>> callback){
        mHackerNewsService.getTopStoriesPageTwo(new RetrofitStoryListCallback(callback));
    }

    private static class RetrofitStoryListCallback implements Callback<List<NodeHNAPIStory>> {
        private final HackerNewsCallback<List<Story>> mCallback;

        public RetrofitStoryListCallback(HackerNewsCallback<List<Story>> callback) {
            mCallback = callback;
        }

        @Override
        public void success(List<NodeHNAPIStory> stories, Response response) {
            List<Story> storyList = new ArrayList<Story>();
            for (NodeHNAPIStory nodeStory : stories) {
                Story story = new Story(nodeStory.getStoryId(),
                        nodeStory.getTitle(),
                        nodeStory.getUrl(),
                        nodeStory.getDomain(),
                        nodeStory.getPoints(),
                        nodeStory.getSubmitter(),
                        nodeStory.getPublishedTime(),
                        nodeStory.getNumComments(),
                        nodeStory.getType());
                storyList.add(story);
            }

            mCallback.onResponse(storyList, null);
        }

        @Override
        public void failure(RetrofitError error) {
            mCallback.onResponse(null, new HackerNewsException(error));
        }
    }

    class LongTypeAdapter implements JsonDeserializer<Long> {

        @Override
        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if ("".equals(json.getAsString())) {
                return 0l;
            }
            else {
                return json.getAsLong();
            }
        }
    }
}
