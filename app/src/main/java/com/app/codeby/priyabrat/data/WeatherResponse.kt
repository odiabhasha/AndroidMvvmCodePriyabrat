package com.app.codeby.priyabrat.data


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("name")
    val cityName: String,
    @SerializedName("main")
    val details: Details,
    @SerializedName("weather")
    val weathers: List<Weather>,
    @SerializedName("dt")
    val date: Long
) {
    override fun toString(): String {
        return """
            City Name       : $cityName
            Weather         : ${if(weathers.isNullOrEmpty()) "Not Available" else weathers.last().description}
            Temperature     : ${details.temp} (°F)
            Max Temperature : ${details.tempMax} (°F)
            Min Temperature : ${details.tempMin} (°F)
            Feels Like      : ${details.feelsLike} (°F)
            Humidity        : ${details.tempMin} (%)
        """.trimIndent()
    }
}

data class Weather(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String
)

data class Details(
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("temp_min")
    val tempMin: Double
)