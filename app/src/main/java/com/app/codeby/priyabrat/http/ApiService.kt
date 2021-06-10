package com.app.codeby.priyabrat.http

import com.app.codeby.priyabrat.BuildConfig
import com.app.codeby.priyabrat.Constants
import com.app.codeby.priyabrat.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/data/2.5/weather")
    suspend fun getWeatherResponse(@Query("lat") latitude: Double, @Query("lon") longitude: Double, @Query("appid") appId: String = BuildConfig.API_KEY): WeatherResponse
}