package com.santansarah.roomexperiments.presentation

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.santansarah.roomexperiments.data.repos.FlowRepository
import com.santansarah.roomexperiments.data.local.AppDatabase
import com.santansarah.roomexperiments.data.local.CityAndWeather
import com.santansarah.roomexperiments.data.local.CityDao

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * No Hilt/DI here, so we'll have to create everything from scratch. We pass in the app context from
 * MainActivity. Makes me appr Hilt, ha ha :)
 */
class FlowViewModel(application: Application) : ViewModel() {

    /**
     * Our [AppDatabase.getInstance] is suspend, so we'll need to lateinit
     * these vars.
     */
    lateinit var appDb: AppDatabase
    lateinit var cityDao: CityDao
    lateinit var flowRepository: FlowRepository

    // UI state exposed to the UI
    //this is a hot flow shared with multiple composables/subscribers
    private val viewModelState = MutableStateFlow(UiState())
    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value
        )

    init {
        viewModelScope.launch {
            appDb = AppDatabase.getInstance(application)
            cityDao = appDb.cityDao()
            flowRepository = FlowRepository(cityDao)

            Log.d("flow", "collecting cities...")

            //val startTimeMS = System.currentTimeMillis()
            try {
            flowRepository.getCityAndWeather().collect() { cityAndWeather ->
                viewModelState.update {
                    it.copy(cityList = cityAndWeather)
                }
            } } catch (e: Throwable) {
                println("caught $e")
                Log.d("debug", "$e")
            }
        }
    }

    fun insertCity() {
        viewModelScope.launch { flowRepository.insertCity() }
    }

    /**
     * Factory for our viewmodel that takes in the app context as a dependency.
     * Again, something we have to do when we're not using Hilt.
     * We'll call this in MainActivity.
     */
    companion object {
        fun provideFactory(
            application: Application,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FlowViewModel(application) as T
            }
        }
    }
}

data class UiState(
    val elapsedTimeMS: Long = 0,
    val cityList: List<CityAndWeather> = listOf()
)
