package io.dwak.holohackernews.app.ui.storydetail;

import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orm.StringUtil;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.dwak.holohackernews.app.HackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModel;
import io.dwak.holohackernews.app.dagger.component.DaggerNetworkServiceComponent;
import io.dwak.holohackernews.app.models.Comment;
import io.dwak.holohackernews.app.models.StoryDetail;
import io.dwak.holohackernews.app.models.User;
import io.dwak.holohackernews.app.network.HackerNewsService;
import io.dwak.holohackernews.app.network.UserService;
import io.dwak.holohackernews.app.network.models.NodeHNAPIComment;
import io.dwak.holohackernews.app.network.models.NodeHNAPIStoryDetail;
import rx.Observable;
import rx.Subscriber;

public class StoryDetailViewModel extends BaseViewModel {
    private final String ITEM_PREFIX = "item?id=";
    private Observable<NodeHNAPIStoryDetail> mItemDetails;
    private long mStoryId;
    private StoryDetail mStoryDetail;
    private boolean mSaved;
    @Inject HackerNewsService mService;
    @Inject UserService mUserService;
    private boolean mIsViewingReadability;
    private Observable<String> mUserTokenObservable = Observable.<String>create(subscriber -> {
        User user = Select.from(User.class)
                          .first();
        if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(user.getUserCookie());
            subscriber.onCompleted();
        }
    });

    public StoryDetailViewModel() {
        mStoryId = 0;
        mItemDetails = null;
        DaggerNetworkServiceComponent.builder()
                                     .appModule(HackerNewsApplication.getAppModule())
                                     .appComponent(HackerNewsApplication.getAppComponent())
                                     .build()
                                     .inject(this);
    }

    boolean isSaved() {
        return mSaved;
    }

    void setStoryDetail(@NonNull StoryDetail storyDetail) {
        mStoryDetail = storyDetail;
    }

    StoryDetail getStoryDetail() {
        return mStoryDetail;
    }

    public Observable<StoryDetail> getStoryDetailObservable() {
        if (mSaved) {
            return Observable.create(new Observable.OnSubscribe<StoryDetail>() {
                @Override
                public void call(Subscriber<? super StoryDetail> subscriber) {
                    StoryDetail storyDetailFromDB = Select.from(StoryDetail.class)
                                                          .where(new Condition(StringUtil.toSQLName(StoryDetail.STORY_DETAIL_ID)).eq(mStoryId))
                                                          .first();
                    List<Comment> comments = Select.from(Comment.class)
                                                   .where(new Condition(StringUtil.toSQLName("mStoryDetail"))
                                                                  .eq(storyDetailFromDB.getId()))
                                                   .list();
                    storyDetailFromDB.setCommentList(comments);
                    setStoryDetail(storyDetailFromDB);
                    subscriber.onNext(storyDetailFromDB);
                    subscriber.onCompleted();
                }
            });
        }
        else {
            return mItemDetails.map(nodeHNAPIStoryDetail -> {
                List<NodeHNAPIComment> nodeHNAPIComments = nodeHNAPIStoryDetail.commentList;
                List<NodeHNAPIComment> expandedComments = new ArrayList<NodeHNAPIComment>();
                for (NodeHNAPIComment nodeHNAPIComment : nodeHNAPIComments) {
                    expandComments(expandedComments, nodeHNAPIComment);
                }

                List<Comment> commentList = new ArrayList<Comment>();


                //noinspection ResourceType
                StoryDetail storyDetail = new StoryDetail(nodeHNAPIStoryDetail.id, nodeHNAPIStoryDetail.title,
                                                          nodeHNAPIStoryDetail.url, nodeHNAPIStoryDetail.domain,
                                                          nodeHNAPIStoryDetail.points, nodeHNAPIStoryDetail.user,
                                                          nodeHNAPIStoryDetail.timeAgo, nodeHNAPIStoryDetail.commentsCount,
                                                          nodeHNAPIStoryDetail.content, nodeHNAPIStoryDetail.poll,
                                                          nodeHNAPIStoryDetail.link, null, nodeHNAPIStoryDetail.moreCommentsId,
                                                          nodeHNAPIStoryDetail.type);

                for (NodeHNAPIComment expandedComment : expandedComments) {
                    if (expandedComment.user != null) {
                        Comment comment = new Comment(expandedComment.id, expandedComment.level,
                                                      expandedComment.user.toLowerCase().equals(nodeHNAPIStoryDetail.user.toLowerCase()),
                                                      expandedComment.user, expandedComment.timeAgo, expandedComment.content, storyDetail);
                        commentList.add(comment);
                    }
                }

                storyDetail.setCommentList(commentList);
                setStoryDetail(storyDetail);
                return storyDetail;
            });
        }
    }

    private void expandComments(List<NodeHNAPIComment> expandedComments, NodeHNAPIComment nodeHNAPIComment) {
        expandedComments.add(nodeHNAPIComment);
        if (nodeHNAPIComment.comments.size() == 0) {
            return;
        }

        for (NodeHNAPIComment childNodeHNAPIComment : nodeHNAPIComment.comments) {
            expandComments(expandedComments, childNodeHNAPIComment);
        }
    }

    public void setStoryId(long storyId) {
        mStoryId = storyId;
        mItemDetails = mService.getItemDetails(mStoryId);
    }

    long getStoryId() {
        return mStoryId;
    }

    void setLoadFromSaved(boolean saved) {
        mSaved = saved;
    }

    @Nullable
    String getReadabilityUrl() {
        try {
            return URLDecoder.decode("javascript:(%0A%28function%28%29%7Bwindow.baseUrl%3D%27//www.readability.com%27%3Bwindow.readabilityToken%3D%2798fX3vYgEcKF2uvS7HTuScqeDgegMF74HVHuLYwF%27%3Bvar%20s%3Ddocument.createElement%28%27script%27%29%3Bs.setAttribute%28%27type%27%2C%27text/javascript%27%29%3Bs.setAttribute%28%27charset%27%2C%27UTF-8%27%29%3Bs.setAttribute%28%27src%27%2CbaseUrl%2B%27/bookmarklet/read.js%27%29%3Bdocument.documentElement.appendChild%28s%29%3B%7D%29%28%29)", "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public boolean isViewingReadability() {
        return mIsViewingReadability;
    }

    public void setIsViewingReadability(boolean isViewingReadability) {
        mIsViewingReadability = isViewingReadability;
    }

    public Observable<Object> reply(Comment comment, String commentContent) {
        return mUserTokenObservable.flatMap(s -> mUserService.postComment(s,
                                                                          ITEM_PREFIX + comment.getCommentId(),
                                                                          "",
                                                                          comment.getCommentId(),
                                                                          commentContent));
    }

    public Observable<Object> upvote(Comment comment) {
        return mUserTokenObservable.flatMap(s -> mUserService.vote(s,
                                                                   comment.getCommentId(),
                                                                   "up",
                                                                   "",
                                                                   ITEM_PREFIX + comment.getCommentId()));
    }

    boolean isLoggedIn() {
        return User.isLoggedIn();
    }

    @ArrayRes int getCommentActions(){
        return isLoggedIn() ? R.array.authCommentActions : R.array.unAuthCommentActions;
    }
}
