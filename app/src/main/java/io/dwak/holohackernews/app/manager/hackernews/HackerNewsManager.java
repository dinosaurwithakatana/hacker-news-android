package io.dwak.holohackernews.app.manager.hackernews;

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

import io.dwak.holohackernews.app.manager.Callback;
import io.dwak.holohackernews.app.manager.Exception;
import io.dwak.holohackernews.app.models.Comment;
import io.dwak.holohackernews.app.models.Story;
import io.dwak.holohackernews.app.models.StoryDetail;
import io.dwak.holohackernews.app.network.HackerNewsService;
import io.dwak.holohackernews.app.network.models.NodeHNAPIComment;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStory;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStoryDetail;
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
    }

    @NonNull
    public static HackerNewsManager getInstance() {
        if (sInstance == null) {
            sInstance = new HackerNewsManager();
        }

        return sInstance;
    }

    public void getStories(@NonNull FeedType feedType, @NonNull final Callback<List<Story>> callback){
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

    public void getTopStoriesPageTwo(@NonNull final Callback<List<Story>> callback){
        mHackerNewsService.getTopStoriesPageTwo(new RetrofitStoryListCallback(callback));
    }

    public void getItemDetails(@NonNull long storyId, @NonNull final Callback<StoryDetail> callback){
       mHackerNewsService.getItemDetails(storyId, new retrofit.Callback<NodeHNAPIStoryDetail>() {
           @Override
           public void success(NodeHNAPIStoryDetail nodeHNAPIStoryDetail, Response response) {
               List<NodeHNAPIComment> nodeHNAPIComments = nodeHNAPIStoryDetail.getNodeHNAPICommentList();
               List<NodeHNAPIComment> expandedComments = new ArrayList<NodeHNAPIComment>();
               for (NodeHNAPIComment nodeHNAPIComment : nodeHNAPIComments) {
                   expandComments(expandedComments, nodeHNAPIComment);
               }

               List<Comment> commentList = new ArrayList<Comment>();

               for (NodeHNAPIComment expandedComment : expandedComments) {
                   Comment comment  = new Comment(expandedComment.getId(), expandedComment.getLevel(),
                           expandedComment.getUser().toLowerCase().equals(nodeHNAPIStoryDetail.getUser().toLowerCase()),
                           expandedComment.getUser(), expandedComment.getTimeAgo(), expandedComment.getContent());
                   commentList.add(comment);
               }

               StoryDetail storyDetail = new StoryDetail(nodeHNAPIStoryDetail.getId(), nodeHNAPIStoryDetail.getTitle(),
                       nodeHNAPIStoryDetail.getUrl(), nodeHNAPIStoryDetail.getDomain(),
                       nodeHNAPIStoryDetail.getPoints(), nodeHNAPIStoryDetail.getUser(),
                       nodeHNAPIStoryDetail.getTimeAgo(), nodeHNAPIStoryDetail.getCommentsCount(),
                       nodeHNAPIStoryDetail.getContent(), nodeHNAPIStoryDetail.getPoll(),
                       nodeHNAPIStoryDetail.getLink(), commentList, nodeHNAPIStoryDetail.getMoreCommentsId(),
                       nodeHNAPIStoryDetail.getType());

               callback.onResponse(storyDetail, null);
           }

           @Override
           public void failure(RetrofitError error) {
                callback.onResponse(null, new Exception(error));
           }
       });
    }
    private static class RetrofitStoryListCallback implements retrofit.Callback<List<NodeHNAPIStory>> {
        private final Callback<List<Story>> mCallback;

        public RetrofitStoryListCallback(Callback<List<Story>> callback) {
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
            mCallback.onResponse(null, new Exception(error));
        }
    }


    private void expandComments(List<NodeHNAPIComment> expandedComments, NodeHNAPIComment nodeHNAPIComment){
        expandedComments.add(nodeHNAPIComment);
        if (nodeHNAPIComment.getChildNodeHNAPIComments().size() == 0) {
            return;
        }

        for (NodeHNAPIComment childNodeHNAPIComment : nodeHNAPIComment.getChildNodeHNAPIComments()) {
            expandComments(expandedComments, childNodeHNAPIComment);
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
