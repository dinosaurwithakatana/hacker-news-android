package io.dwak.holohackernews.app.network.models;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class NodeHNAPIStoryDetail {
    @JsonField(name = "id") public Long id;
    @JsonField(name = "title") public String title;
    @JsonField(name = "url") public String url;
    @JsonField(name = "domain") public String domain;
    @JsonField(name = "points") public Integer points;
    @JsonField(name = "user") public String user;
    @JsonField(name = "time_ago") public String timeAgo;
    @JsonField(name = "comments_count") public Integer commentsCount;
    @JsonField(name = "content") public String content;
    @JsonField(name = "poll") public Object poll;
    @JsonField(name = "link") public String link;
    @JsonField(name = "comments") public List<NodeHNAPIComment> commentList;
    @JsonField(name = "more_comments_id") public Long moreCommentsId;
    @JsonField(name = "type") public String type;

}
