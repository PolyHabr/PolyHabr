package com.polyhabr.poly_back.utility

import org.joda.time.format.DateTimeFormat
import java.sql.Timestamp

object DateTimeUtils {
    const val DEFAULT = "dd.MM.yyyy"
    val defaultFormat = DateTimeFormat.forPattern(DEFAULT)

    val wMillis = 604_800_000L
    val mMillis = 2_678_400_000L
    val yMillis = 31_536_000_000L
}