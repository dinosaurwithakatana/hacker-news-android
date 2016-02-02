package io.dwak.holohackernews.app.model.json

import com.squareup.moshi.Json

data class CommentJson(val id : Long?,
                       val level : Int?,
                       val user : String?,
                       @Json(name = "time_ago") val timeAgo : String?,
                       val content : String?,
                       val comments : MutableList<CommentJson>?)

