package io.dwak.holohackernews.app.network.models;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject(fieldNamingPolicy = JsonObject.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
public class NodeHNAPIComment {
    @JsonField public Long id;
    @JsonField public int level;
    @JsonField public String user;
    @JsonField public String timeAgo;
    @JsonField public String content;
    @JsonField public List<NodeHNAPIComment> comments;
}
