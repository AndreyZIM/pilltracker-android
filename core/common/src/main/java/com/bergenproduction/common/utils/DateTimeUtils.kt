package com.bergenproduction.common.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val DEFAULT_DATE_PATTERN = "dd.MM.yyyy"

fun Long.toDateString(pattern: String = DEFAULT_DATE_PATTERN): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(Date(this))