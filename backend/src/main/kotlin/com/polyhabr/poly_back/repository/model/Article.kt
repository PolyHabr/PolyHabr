package com.polyhabr.poly_back.repository.model

import com.google.gson.annotations.SerializedName
import java.util.Date

class Article(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("user_id")
    val userId: Int = 0,

    @SerializedName("type_id")
    val typeId: Int = 0,

    @SerializedName("preview_text")
    var previewText: String = "",

    @SerializedName("date")
    var date: Date = Date(),

    @SerializedName("file_pdf")
    var filePdf: String? = null,

    @SerializedName("count_likes")
    var countLikes: Long = 0
) {

}