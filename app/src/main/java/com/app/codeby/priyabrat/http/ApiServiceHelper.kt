package com.app.codeby.priyabrat.http

import com.app.codeby.priyabrat.data.WeatherResponse

interface ApiServiceHelper {

    suspend fun getWeatherResponse(latitude: Double, longitude: Double): WeatherResponse
}