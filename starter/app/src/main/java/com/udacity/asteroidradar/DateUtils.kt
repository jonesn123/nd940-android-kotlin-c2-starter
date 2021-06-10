package com.udacity.asteroidradar

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    @SuppressLint("WeekBasedYear")
    fun getFormattedDate(date: Date): String {
        val format = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return format.format(date)
    }
}