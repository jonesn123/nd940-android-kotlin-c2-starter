package com.udacity.asteroidradar.api

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface NasaApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getFeed(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): String

    @GET("planetary/apod")
    suspend fun getPlanetaryApod(
        @Query("api_key") apiKey: String
    ): ApodProperty

}

data class AsteroidProperty(
    @Json(name = "near_earth_objects")
    val nearEarthObject: String
)

data class CloseApproachData(
    @Json(name = "close_approach_date")
    val closeApproachDate: String,
    @Json(name = "relative_velocity")
    val relativeVelocity: RelativeVelocity,
    @Json(name = "miss_distance")
    val missDistance: MissDistance
)

data class RelativeVelocity(
    @Json(name = "kilometers_per_second")
    val relativeVelocity: Double
)

data class MissDistance(
    @Json(name = "astronomical")
    val distanceFromEarth: Double
)

data class ApodProperty(
    @Json(name = "url")
    val imageUrl: String,
    @Json(name = "media_type")
    val mediaType: String,
    @Json(name = "title")
    val title: String
)

object NasaApi {
    val retrofitService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}