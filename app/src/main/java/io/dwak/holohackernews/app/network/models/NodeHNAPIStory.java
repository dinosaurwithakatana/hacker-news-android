package io.dwak.holohackernews.app.network.models;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import io.dwak.holohackernews.app.models.Story;

@JsonObject
public class NodeHNAPIStory {
    @JsonField(name = "id") public Long id;
    @JsonField(name = "title") public String title;
    @JsonField(name = "url") public String url;
    @JsonField(name = "domain") public String domain;
    @JsonField(name = "points") public int points;
    @JsonField(name = "user") public String user;
    @JsonField(name = "time_ago") public String timeAgo;
    @JsonField(name = "comments_count") public int commentsCount;
    @JsonField(name = "type") public String type;

    public NodeHNAPIStory() {
    }

    private NodeHNAPIStory(Long id, String title, String url, String domain, int points, String user, String timeAgo, int commentsCount, String type) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.domain = domain;
        this.points = points;
        this.user = user;
        this.timeAgo = timeAgo;
        this.commentsCount = commentsCount;
        this.type = type;
    }


    public static NodeHNAPIStory fromStory(Story story) {
        return new NodeHNAPIStory(story.getStoryId(),
                                  story.getTitle(),
                                  story.getUrl(),
                                  story.getDomain(),
                                  story.getPoints(),
                                  story.getSubmitter(),
                                  story.getPublishedTime(),
                                  story.getNumComments(),
                                  story.getType());
    }
}

