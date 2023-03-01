package com.polyhabr.poly_back.controller.model.article.request

import com.polyhabr.poly_back.utility.DateTimeUtils
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(
    name = "SortArticleRequest",
    description = "Data object for SortArticleRequest",
)
data class SortArticleRequest(
    @field:Schema(example = "sort for view, default turn off, true - asc, false - desc")
    var fieldView: Boolean? = null,

    @field:Schema(example = "sort for like, default turn off, true - asc, false - desc")
    var fieldRating: Boolean? = null,

    @field:Schema(example = "sort for like, default 1w, 1w/1m/1y, null - all")
    var datRange: String? = null,
) : Serializable {
    companion object {
        const val docs = "" +
                "@field:Schema(example = \"sort for view, default turn off, true - asc, false - desc\")\n" +
                "    var fieldView: Boolean? = null,\n" +
                "\n" +
                "    @field:Schema(example = \"sort for like, default turn off, true - asc, false - desc\")\n" +
                "    var fieldRating: Boolean? = null,\n" +
                "\n" +
                "    @field:Schema(example = \"sort for like, default 1w, 1w/1m/1y, null - all\")\n" +
                "    var datRange: String? = null,"
    }

    fun getMillis(): Long {
        return when (datRange) {
            "1w" -> {
                DateTimeUtils.wMillis
            }

            "1m" -> {
                DateTimeUtils.mMillis
            }

            "1y" -> {
                DateTimeUtils.yMillis
            }

            else -> Long.MAX_VALUE
        }
    }
}
