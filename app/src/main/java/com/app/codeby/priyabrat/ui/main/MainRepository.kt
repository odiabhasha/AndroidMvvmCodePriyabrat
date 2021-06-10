package com.app.codeby.priyabrat.ui.main

import com.app.codeby.priyabrat.Constants
import com.app.codeby.priyabrat.data.WeatherResponse
import com.app.codeby.priyabrat.http.ApiServiceHelper
import com.app.codeby.priyabrat.utils.PrefUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiServiceHelper: ApiServiceHelper,
    private val preference: PrefUtils,
    private val gson: Gson
) {

    /**
     * Checks if a valid Cache data
     * 1. If yes, emits the Flow
     * 2. If no, gets it from Server using Retrofit and than emits the flow
     * 3. Save the reference time stamp
     */
    suspend fun getWeatherResponse(latitude: Double, longitude: Double): Flow<WeatherResponse> =
        flow {
            val cachedData = getCachedWeatherResponse()
            if (cachedData != null) {
                emit(cachedData)
            } else {
                val weatherData = apiServiceHelper.getWeatherResponse(latitude, longitude)
                preference.saveLong(Constants.KEY_TIME, System.currentTimeMillis())
                emit(weatherData)
            }
        }.flowOn(Dispatchers.IO)

    /**
     * Gets cached response and timestamp, does all required compare and returns value
     */
    private fun getCachedWeatherResponse(): WeatherResponse? {
        val response = preference.getString(Constants.KEY_RESPONSE)
        val timeStored = preference.getLong(Constants.KEY_TIME)
        return if (response.isNullOrEmpty() || timeStored == 0L)
            null
        else if ((System.currentTimeMillis() - timeStored) < (Constants.API_CALL_REPEAT_TIME - 1))
            gson.fromJson(response, WeatherResponse::class.java)
        else
            null
    }

    /**
     * Saves response as a Cache
     */
    fun cacheResponse(data: WeatherResponse) {
        val jsonString = gson.toJson(data)
        preference.saveString(Constants.KEY_RESPONSE,jsonString)
    }
}
