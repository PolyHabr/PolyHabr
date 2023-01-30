package com.polyhabr.poly_back.repository.model

import com.google.gson.annotations.SerializedName

class ArticleType(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("name")
    var name: String = "default",
) {
}