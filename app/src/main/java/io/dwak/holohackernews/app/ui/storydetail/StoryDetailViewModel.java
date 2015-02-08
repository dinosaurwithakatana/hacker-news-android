package io.dwak.holohackernews.app.ui.storydetail;

import java.util.ArrayList;
import java.util.List;

import io.dwak.holohackernews.app.HoloHackerNewsApplication;
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
public class StoryDetailViewModel {
    private final Observable<NodeHNAPIStoryDetail> mItemDetails;
    private final long mStoryId;

    public StoryDetailViewModel(long storyId) {
        mStoryId = storyId;
        mItemDetails = HoloHackerNewsApplication.getInstance().getHackerNewsServiceInstance().getItemDetails(mStoryId);
    }

    public Observable<StoryDetail> getStoryDetail() {
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
}
