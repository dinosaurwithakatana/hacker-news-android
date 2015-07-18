package io.dwak.holohackernews.app.network.models;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class NodeHNAPIComment {
    @JsonField(name = "id") public Long id;
    @JsonField(name = "level") public int level;
    @JsonField(name = "user") public String user;
    @JsonField(name = "time_ago") public String timeAgo;
    @JsonField(name = "content") public String content;
    @JsonField(name = "comments") public List<NodeHNAPIComment> comments;
}
