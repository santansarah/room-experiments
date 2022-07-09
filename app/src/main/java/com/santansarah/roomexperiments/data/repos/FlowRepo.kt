package com.santansarah.roomexperiments.data.repos

import cityList
import com.santansarah.roomexperiments.data.local.City
import com.santansarah.roomexperiments.data.local.CityAndWeather
import com.santansarah.roomexperiments.data.local.CityDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class FlowRepository(private val cityDao: CityDao) {

    private var CURRENT_IDX = 0

    suspend fun insertCity() {
        if (CURRENT_IDX >= cityList.lastIndex)
            CURRENT_IDX = 0

        cityDao.insertCity(cityList[CURRENT_IDX])
        CURRENT_IDX++
    }

    suspend fun getWeather(lat: Float, longitude: Float): Boolean {
        // simulate a call to the API
        // insert into database cache
        delay(500L)
        return true
    }

    suspend fun getWeather(cities: List<City>): Flow<List<Boolean>> {
        return flow {
            cities.map {
                // simulate a call to the API
                // insert into database cache
                delay(500L)
                true
            }
        }
    }
    suspend fun getCityAndWeatherOneItem(): Flow<CityAndWeather> {
        return flow {
            cityDao.getCities().collect { cityList ->
                cityList.forEach {
                    // simulate a call to a weather api
                    delay(500L)
                    emit(CityAndWeather(city = it, weather = true))
                }
            }
        }
    }
}