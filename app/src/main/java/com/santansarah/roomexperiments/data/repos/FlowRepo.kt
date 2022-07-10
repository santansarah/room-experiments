package com.santansarah.roomexperiments.data.repos

import cityList
import com.santansarah.roomexperiments.data.local.CityAndWeather
import com.santansarah.roomexperiments.data.local.CityDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class FlowRepository(private val cityDao: CityDao) {

    private var currentIdx = 0

    suspend fun insertCity() {
        if (currentIdx >= cityList.lastIndex)
            currentIdx = 0

        cityDao.insertCity(cityList[currentIdx])
        currentIdx++
    }

    suspend fun getWeather(lat: Float, longitude: Float): Boolean {
        // simulate a call to the API
        // insert into database cache
        //delay(500L)
        return true
    }

    suspend fun getCityAndWeatherAsList(): Flow<List<CityAndWeather>> {
        return cityDao.getCities().map { cityList ->
            cityList.map {
                val weather = getWeather(it.lat, it.longitude)
                CityAndWeather(city = it, weather = weather)
            }
        }
    }
}