package com.example.roomlists

import cityList
import com.example.roomlists.data.local.City
import com.example.roomlists.data.local.CityAndWeather
import com.example.roomlists.data.local.CityDao
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

    suspend fun getCityList(): Flow<City> {
        return flow {
            cityDao.getCities().collect { cityList ->
                cityList.forEach {
                    delay(1000L)
                    emit(it)
                }
            }
        }
    }

    suspend fun getCityListAsCopy(): Flow<List<CityAndWeather>> {
        var newList: MutableList<CityAndWeather> = mutableListOf()

        return flow {
            //first, return the empty cityandweather
            cityDao.getCities().collect {
                newList = mutableListOf()
                for (i in 0 until it.count()) {
                    // simulate a network call, ie:
                    // val weather = weatherApi.getWeather(it.lat, it.long)
                    delay(1000L)
                    newList.add(CityAndWeather(it[i], true))
                    emit(newList.toList())  //emit the cities 1 at a time, building on the list
                }
            }
        }
    }

    suspend fun getCityListAsList(): Flow<List<City>> {
        return cityDao.getCities()
    }

    suspend fun getCityListWithDelay(): Flow<List<City>> {
        val cities = cityDao.getCities()
        return cities.onEach { cityList ->
            cityList.forEach {
                delay(1000L)
            }
        }
    }
}

suspend fun EmitNumbers(): Flow<Int> {
    return flow<Int> {
        val startingValue = 5
        var currentValue = startingValue
        emit(startingValue)
        while (currentValue > 0) {
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }
}