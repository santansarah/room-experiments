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

    suspend fun getWeather(lat: Long, longitude: Long): Boolean {
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