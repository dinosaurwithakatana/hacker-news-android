package io.dwak.holohackernews.app.model.json

import com.squareup.moshi.Json

data class StoryDetailJson(val id : Long?,
                           val title : String?,
                           val url : String?,
                           val domain : String?,
                           val points : Int?,
                           val user : String?,
                           @Json(name = "time_ago") val timeAgo : String?,
                           @Json(name = "comments_count") val commentsCount : Int?,
                           val content : String?,
                           val poll : Any?,
                           val link : String?,
                           val comments : List<CommentJson>?,
                           @Json(name = "more_comments_id") val moreCommentsId : Long?,
                           val type : String?)

