package com.example.roomlists.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface CityDao {

    @Query("SELECT COUNT(*) FROM City")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<City>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: City)

    @Query("SELECT * FROM City")
    fun getCities(): Flow<List<City>>

}