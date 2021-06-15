package com.udacity.asteroidradar

object Constants {
    const val API_QUERY_DATE_FORMAT = "YYYY-MM-dd"
    const val BASE_URL = "https://api.nasa.gov/"
    const val IMAGE_TYPE = "image"
    const val VIDEO_TYPE = "video"

    enum class RangeEndDate(val days: Int) {
        TODAY_END_DATE_DAYS(1),
        WEEK_END_DATE_DAYS(7)
    }
}