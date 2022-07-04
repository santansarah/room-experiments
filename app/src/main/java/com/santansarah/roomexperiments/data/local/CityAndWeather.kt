package com.santansarah.roomexperiments.data.local

import com.santansarah.roomexperiments.data.local.City

data class CityAndWeather(
    val city: City,
    val weather: Boolean = false
)
