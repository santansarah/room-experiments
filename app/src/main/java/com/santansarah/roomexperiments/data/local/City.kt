package com.santansarah.roomexperiments.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City(
    @PrimaryKey(autoGenerate = true)
    val cityId: Int = 0,
    val name: String = "",
    val state: String = "",
    val lat: Float = 0f,
    val longitude: Float = 0f
)
