package com.polyhabr.poly_back.repository.model

import com.google.gson.annotations.SerializedName
import java.util.*

class Comment(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("user_id")
    val userId: Int = 0,

    @SerializedName("article_id")
    val articleId: Int = 0,

    @SerializedName("date")
    var date: Date = Date(),

    @SerializedName("text")
    var text: String = "Очень интересно",
) {
}