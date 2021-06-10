package com.app.codeby.priyabrat.http

import com.app.codeby.priyabrat.data.WeatherResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiServiceHelperImpl @Inject constructor(private val apiService: ApiService) : ApiServiceHelper {

    override suspend fun getWeatherResponse(latitude: Double, longitude: Double): WeatherResponse = apiService.getWeatherResponse(latitude, longitude)
}