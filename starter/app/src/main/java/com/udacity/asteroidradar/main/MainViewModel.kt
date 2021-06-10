package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DateUtils
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class MainViewModel : ViewModel() {

    private val service = NasaApi.retrofitService
    private val _asteroidProperties = MutableLiveData<List<Asteroid>>()

    val asteroidProperties: LiveData<List<Asteroid>>
        get() = _asteroidProperties

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String>
        get() = _imageUrl

    init {
        fetchFeed()
        fetchImage()
    }

    private fun fetchFeed() {
        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
                val result = service.getFeed(
                    startDate = DateUtils.getFormattedDate(Date()),
                    endDate = DateUtils.getFormattedDate(calendar.time),
                    apiKey = Constants.API_KEY
                )
                _asteroidProperties.value = parseAsteroidsJsonResult(JSONObject(result))

            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    private fun fetchImage() {
        viewModelScope.launch {
            try {
                val result = service.getPlanetaryApod(Constants.API_KEY)
                _imageUrl.value = result.imageUrl
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }

    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}