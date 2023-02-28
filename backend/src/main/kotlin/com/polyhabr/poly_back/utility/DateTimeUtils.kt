package com.polyhabr.poly_back.utility

import org.joda.time.format.DateTimeFormat

object DateTimeUtils {
    const val DEFAULT = "dd.MM.yyyy"
    val defaultFormat = DateTimeFormat.forPattern(DEFAULT)
}