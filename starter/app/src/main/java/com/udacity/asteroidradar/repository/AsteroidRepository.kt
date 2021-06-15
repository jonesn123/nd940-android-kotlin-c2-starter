package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.database.AsteroidDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import org.json.JSONObject
import android.util.Log
import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DateUtils
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import java.util.*
import kotlin.coroutines.CoroutineContext

class AsteroidRepository(
    private val asteroidDao: AsteroidDao,
    private val apiService: NasaApiService,
    private val apiKey: String
) {

    private val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()

    suspend fun fetchAsteroids(endDate: Constants.RangeEndDate): List<Asteroid> =
        withContext(coroutineContext) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, endDate.days)
            val feedString = apiService.getFeed(
                startDate = DateUtils.getFormattedDate(Date()),
                endDate = DateUtils.getFormattedDate(calendar.time),
                apiKey = apiKey
            )
            val asteroids = parseAsteroidsJsonResult(JSONObject(feedString), endDate)

            try {
                for (asteroid in asteroids) {
                    asteroidDao.insert(asteroid)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
            asteroids
        }

    suspend fun getAsteroidsFromDao(): List<Asteroid> =
        withContext(coroutineContext) {
            asteroidDao.getAsteroids()
        }

    suspend fun getPlanetaryApod() =
        withContext(coroutineContext) {
            apiService.getPlanetaryApod(apiKey)
        }

    companion object {
        private const val TAG = "AsteroidRepository"
    }
}