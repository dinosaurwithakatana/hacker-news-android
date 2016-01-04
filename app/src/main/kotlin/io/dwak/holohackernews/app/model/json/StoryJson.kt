package io.dwak.holohackernews.app.model.json

import com.squareup.moshi.Json

data class StoryJson(val id : Long,
                     val title : String?,
                     val url : String?,
                     val domain : String?,
                     val points : Int?,
                     val user : String?,
                     @Json(name = "time_ago") val timeAgo : String?,
                     @Json(name = "comments_count") val commentsCount : Int?,
                     val type : String?)