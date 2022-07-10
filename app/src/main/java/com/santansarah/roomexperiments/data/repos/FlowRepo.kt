package com.santansarah.roomexperiments.data.repos

import cityList
import com.santansarah.roomexperiments.data.local.City
import com.santansarah.roomexperiments.data.local.CityAndWeather
import com.santansarah.roomexperiments.data.local.CityDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class FlowRepository(private val cityDao: CityDao) {

    private var cityIdx = 0

    suspend fun insertCity() {
        if (cityIdx >= cityList.lastIndex)
            cityIdx = 0

        cityDao.insertCity(cityList[cityIdx])
        cityIdx++
    }

    suspend fun getWeather(lat: Float, longitude: Float): Boolean {
        // simulate a call to the API
        // insert into database cache
        delay(500L)
        return true
    }

    suspend fun getCityAndWeather(): Flow<List<CityAndWeather>> {

        return cityDao.getCities().transform { cityList ->
            var cityAndWeather = cityList.map {
                CityAndWeather(city = it, weather = false)
            }.toMutableList()
            emit(cityAndWeather.toList())

            cityList.forEachIndexed() { idx, city ->

                val weather = getWeather(city.lat, city.longitude)
                cityAndWeather[idx] = cityAndWeather[idx].copy(weather = weather)
                emit(cityAndWeather.toList())

            }
        }
    }

    /*suspend fun getCityAndWeather(): Flow<List<CityAndWeather>> {

        return cityDao.getCities().buffer().transform { cityList ->
            var initialList = cityList.map {
                CityAndWeather(city = it, weather = false)
            }
            emit(initialList)

            var cityAndWeather: MutableList<CityAndWeather> = mutableListOf()
            cityAndWeather.addAll(initialList)

            initialList.forEachIndexed() { idx, cw ->

                val weather = getWeather(cw.city.lat, cw.city.longitude)
                cityAndWeather[idx] = cityAndWeather[idx].copy(weather = weather)
                emit(cityAndWeather.toList())

            }
        }
    }*/
}