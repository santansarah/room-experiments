package com.santansarah.roomexperiments.presentation

import android.app.Application
import android.util.Log
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

            val startTimeMS = System.currentTimeMillis()
            flowRepository.getCityListAsCopy().collect { cityList ->
                viewModelState.update {
                    it.copy(cityList = cityList)
                    it.copy(elapsedTimeMS = System.currentTimeMillis() - startTimeMS)
                }
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
    //val myList: MutableStateFlow<List<City>> = mutableStateListOf<City>()
    //val myList: SnapshotStateList<City> = mutableStateListOf()
    //val myList: Flow<List<City>> = emptyFlow()
    val cityList: List<CityAndWeather> = emptyList(),
    //val isLoading: Boolean = true
)
