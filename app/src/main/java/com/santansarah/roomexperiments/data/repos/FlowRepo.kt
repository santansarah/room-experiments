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

/*
    suspend fun getCityAndWeather(): Flow<List<CityAndWeather>> {

        var initialList: List<CityAndWeather> = listOf()
        var cityAndWeather: MutableList<CityAndWeather> = mutableListOf()

        cityDao.getCities().map() { cityList ->
            cityList.map {
                CityAndWeather(city = it, weather = false)
            }
        }.collect() {
            initialList = it
        }

        return flow {
            emit(initialList)

            initialList.map {
                //delay(500L)
                cityAndWeather.add(CityAndWeather(city = it.city, weather = true))
                emit(initialList.toList())
            }
        }

    }

*/

    suspend fun getCityAndWeather(): Flow<List<CityAndWeather>> {

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
            /*cityList.map { city ->
                val weather =  getWeather(city.lat, city.longitude)
                cityAndWeather.add(CityAndWeather(city = city, weather = weather))

                emit(cityAndWeather.toList())

            }*/
        }
    }


/*
    suspend fun getCityAndWeather(): Flow<List<CityAndWeather>> {
        return flow {
            cityDao.getCities().map { cityList ->
                cityList.map {
                    CityAndWeather(city = it, weather = false)
                }
            }.collect { cityWeatherList ->
                emit(cityWeatherList)
                cityWeatherList.map {
                    val weather = getWeather(it.city.lat, it.city.longitude)
                    emit(listOf(it.copy(weather = weather)))
                }
            }
        }
    }
*/

}