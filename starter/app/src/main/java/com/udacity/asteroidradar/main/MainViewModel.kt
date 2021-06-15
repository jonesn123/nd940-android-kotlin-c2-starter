package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.ApodProperty
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val asteroidRepository = AsteroidRepository(
        AsteroidDatabase.getInstance(application).asteroidDao,
        NasaApi.retrofitService,
        application.getString(R.string.nasa_api_key)
    )
    private val _asteroids = MutableLiveData<List<Asteroid>>()

    val asteroid: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _imageProperty = MutableLiveData<ApodProperty>()
    val imageProperty: LiveData<ApodProperty>
        get() = _imageProperty

    init {
        fetchImage()
        fetchAsteroids(Constants.RangeEndDate.WEEK_END_DATE_DAYS)
    }

    fun fetchAsteroids(endDate: Constants.RangeEndDate) {
        viewModelScope.launch {
            try {
                _asteroids.value = asteroidRepository.fetchAsteroids(endDate)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                fetchAsFromDatabase()
            }
        }
    }

    fun fetchAsFromDatabase() {
        viewModelScope.launch {
            try {
                _asteroids.value = asteroidRepository.getAsteroidsFromDao()
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    private fun fetchImage() {
        viewModelScope.launch {
            try {
                _imageProperty.value = asteroidRepository.getPlanetaryApod()
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}