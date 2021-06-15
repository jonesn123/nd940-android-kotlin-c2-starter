package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshAsteroidWorker(appContext: Context, parms: WorkerParameters) :
    CoroutineWorker(appContext, parms) {

    companion object {
        const val WORK_NAME = "RefreshAsteroidWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(
            database.asteroidDao, NasaApi.retrofitService,
            applicationContext.getString(R.string.nasa_api_key)
        )

        return try {
            repository.fetchAsteroids(Constants.RangeEndDate.WEEK_END_DATE_DAYS)
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}