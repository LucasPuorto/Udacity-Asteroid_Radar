package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidsApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val asteroidsMutableLiveData = MutableLiveData<List<Asteroid>>()
    val asteroidsLiveData: LiveData<List<Asteroid>> get() = asteroidsMutableLiveData

    init {
        getAsteroids()
    }

    private fun getAsteroids() {
        viewModelScope.launch {
            try {
                asteroidsMutableLiveData.value = parseAsteroidsJsonResult(JSONObject(AsteroidsApi.asteroidsRetrofitService.getAsteroids()))
            } catch (e: Exception) {
                asteroidsMutableLiveData.value = ArrayList()
            }
        }
    }
}
