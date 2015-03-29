package io.dwak.holohackernews.app.ui.storydetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import io.dwak.holohackernews.app.HoloHackerNewsApplication;
import io.dwak.holohackernews.app.base.BaseViewModel;
import io.dwak.holohackernews.app.models.Comment;
import io.dwak.holohackernews.app.models.StoryDetail;
import io.dwak.holohackernews.app.network.models.NodeHNAPIComment;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStoryDetail;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by vishnu on 2/2/15.
 */
public class StoryDetailViewModel extends BaseViewModel{
    private Observable<NodeHNAPIStoryDetail> mItemDetails;
    private long mStoryId;
    private StoryDetail mStoryDetail;

    public StoryDetailViewModel() {
        mStoryId = 0;
        mItemDetails = null;
    }

    void setStoryDetail(@NonNull StoryDetail storyDetail){
        mStoryDetail = storyDetail;
    }

    StoryDetail getStoryDetail(){
        return mStoryDetail;
    }

    Observable<StoryDetail> getStoryDetailObservable() {
        return mItemDetails.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(nodeHNAPIStoryDetail -> {
                    List<NodeHNAPIComment> nodeHNAPIComments = nodeHNAPIStoryDetail.getNodeHNAPICommentList();
                    List<NodeHNAPIComment> expandedComments = new ArrayList<NodeHNAPIComment>();
                    for (NodeHNAPIComment nodeHNAPIComment : nodeHNAPIComments) {
                        expandComments(expandedComments, nodeHNAPIComment);
                    }

                    List<Comment> commentList = new ArrayList<Comment>();

                    for (NodeHNAPIComment expandedComment : expandedComments) {
                        if (expandedComment.getUser() != null) {
                            Comment comment = new Comment(expandedComment.getId(), expandedComment.getLevel(),
                                    expandedComment.getUser().toLowerCase().equals(nodeHNAPIStoryDetail.getUser().toLowerCase()),
                                    expandedComment.getUser(), expandedComment.getTimeAgo(), expandedComment.getContent());
                            commentList.add(comment);
                        }
                    }

                    return new StoryDetail(nodeHNAPIStoryDetail.getId(), nodeHNAPIStoryDetail.getTitle(),
                            nodeHNAPIStoryDetail.getUrl(), nodeHNAPIStoryDetail.getDomain(),
                            nodeHNAPIStoryDetail.getPoints(), nodeHNAPIStoryDetail.getUser(),
                            nodeHNAPIStoryDetail.getTimeAgo(), nodeHNAPIStoryDetail.getCommentsCount(),
                            nodeHNAPIStoryDetail.getContent(), nodeHNAPIStoryDetail.getPoll(),
                            nodeHNAPIStoryDetail.getLink(), commentList, nodeHNAPIStoryDetail.getMoreCommentsId(),
                            nodeHNAPIStoryDetail.getType());
                });
    }

    private void expandComments(List<NodeHNAPIComment> expandedComments, NodeHNAPIComment nodeHNAPIComment) {
        expandedComments.add(nodeHNAPIComment);
        if (nodeHNAPIComment.getChildNodeHNAPIComments().size() == 0) {
            return;
        }

        for (NodeHNAPIComment childNodeHNAPIComment : nodeHNAPIComment.getChildNodeHNAPIComments()) {
            expandComments(expandedComments, childNodeHNAPIComment);
        }
    }

    void setStoryId(long storyId){
        mStoryId = storyId;
        mItemDetails = HoloHackerNewsApplication.getInstance().getHackerNewsServiceInstance().getItemDetails(mStoryId);
    }

    @Nullable
    String getReadabilityUrl(){
        try {
            return URLDecoder.decode("javascript:(%0A%28function%28%29%7Bwindow.baseUrl%3D%27//www.readability.com%27%3Bwindow.readabilityToken%3D%2798fX3vYgEcKF2uvS7HTuScqeDgegMF74HVHuLYwF%27%3Bvar%20s%3Ddocument.createElement%28%27script%27%29%3Bs.setAttribute%28%27type%27%2C%27text/javascript%27%29%3Bs.setAttribute%28%27charset%27%2C%27UTF-8%27%29%3Bs.setAttribute%28%27src%27%2CbaseUrl%2B%27/bookmarklet/read.js%27%29%3Bdocument.documentElement.appendChild%28s%29%3B%7D%29%28%29)", "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
