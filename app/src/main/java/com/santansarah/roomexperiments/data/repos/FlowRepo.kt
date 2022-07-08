package com.santansarah.roomexperiments.data.repos

import cityList
import com.santansarah.roomexperiments.data.local.City
import com.santansarah.roomexperiments.data.local.CityAndWeather
import com.santansarah.roomexperiments.data.local.CityDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class FlowRepository(private val cityDao: CityDao) {

    var CURRENT_IDX = 0

    suspend fun insertCity() {
        if (CURRENT_IDX >= cityList.lastIndex)
            CURRENT_IDX = 0

        cityDao.insertCity(cityList[CURRENT_IDX])
        CURRENT_IDX++
    }

    /**
     * testing version control.
     */
    suspend fun getCityList(): Flow<CityAndWeather> {
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