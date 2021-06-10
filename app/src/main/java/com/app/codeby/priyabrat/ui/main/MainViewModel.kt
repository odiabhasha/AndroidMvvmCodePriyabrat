package com.app.codeby.priyabrat.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.codeby.priyabrat.Constants
import com.app.codeby.priyabrat.data.Resource
import com.app.codeby.priyabrat.data.WeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
public class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    var latitude = 0.0
    var longitude = 0.0
    var isRepeatable = true
    val liveDataWeatherResponse = MutableLiveData<Resource<WeatherResponse>>()

    /**
     * Checks if latitude and longitude are 0.0.
     * 1. If yes, will give error message.
     * 2. If no, will call MainRepository.getWeatherResponse using coroutine viewModelScope.
     * 3. Will be called Every N no of times, Configured in Constants.API_CALL_REPEAT_TIME.
     * 4. If Error Response, repetition will be cancelled
     */
    fun getWeatherResponse() {
        if (latitude == 0.0 || longitude == 0.0) {
            liveDataWeatherResponse.postValue(Resource.Error("Latitude & Longitude Can't be Zero"))
            return
        }
        else
            liveDataWeatherResponse.postValue(Resource.Loading)

        viewModelScope.launch {
            while (isRepeatable) {
                mainRepository.getWeatherResponse(latitude, longitude)
                    .catch {
                        isRepeatable = false
                        liveDataWeatherResponse.postValue(
                            Resource.Error(
                                it.localizedMessage ?: "Something Went Wrong"
                            )
                        )
                    }
                    .collect {
                        isRepeatable = true
                        mainRepository.cacheResponse(it)
                        liveDataWeatherResponse.postValue(Resource.Success(it))
                    }
                delay(Constants.API_CALL_REPEAT_TIME)
            }
        }
    }
}