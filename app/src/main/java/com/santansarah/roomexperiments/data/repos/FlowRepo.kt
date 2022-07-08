package com.santansarah.roomexperiments.data.repos

import android.util.Log
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

        Log.d("debug", "current_idx: $CURRENT_IDX")
        cityDao.insertCity(cityList[CURRENT_IDX])
        CURRENT_IDX++
    }

    suspend fun getCityList(): Flow<List<CityAndWeather>> {
       return cityDao.getCities().map { cityList ->
            cityList.map {
                delay(500L)
                CityAndWeather(city = it, weather = true)
            }
        }
    }
}