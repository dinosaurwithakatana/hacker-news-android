package io.dwak.holohackernews.app.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
import io.dwak.holohackernews.app.models.Comment;
import io.dwak.holohackernews.app.models.Story;
import io.dwak.holohackernews.app.models.StoryDetail;
import io.dwak.holohackernews.app.network.HackerNewsService;
import io.dwak.holohackernews.app.network.ReadabilityService;
import io.dwak.holohackernews.app.network.models.NodeHNAPIComment;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStory;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStoryDetail;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by vishnu on 8/18/14.
 */
public class HackerNewsManager {
    @Nullable private static HackerNewsManager sInstance;
    @NonNull private final HackerNewsService mHackerNewsService;
    @Nullable private ReadabilityService mReadabilityService = null;
    @Nullable private List<Comment> mComments;

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

    @NonNull
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

    public void getItemDetails(@NonNull long storyId, @NonNull final HackerNewsCallback<StoryDetail> callback){
       mHackerNewsService.getItemDetails(storyId, new Callback<NodeHNAPIStoryDetail>() {
           @Override
           public void success(NodeHNAPIStoryDetail nodeHNAPIStoryDetail, Response response) {

           }

           @Override
           public void failure(RetrofitError error) {

           }
       });
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

    private Comment fromNodeHNAPIComment(NodeHNAPIComment nodeHNAPIComment){
        Comment comment = new Comment();
        comment.setContent(nodeHNAPIComment.getContent());
        comment.setId(nodeHNAPIComment.getId());
        comment.setLevel(nodeHNAPIComment.getLevel());
        comment.setTimeAgo(nodeHNAPIComment.getTimeAgo());
        comment.setUser(nodeHNAPIComment.getUser());
        List<Comment> childComments = new ArrayList<Comment>();

        return null;
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
