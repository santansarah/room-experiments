package com.santansarah.roomexperiments.presentation

import android.app.Application
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.graphics.Bitmap.Config
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cityList
import com.santansarah.roomexperiments.data.local.CityAndWeather
import com.santansarah.roomexperiments.presentation.FlowViewModel
import com.santansarah.roomexperiments.presentation.theme.RoomExperimentsTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomExperimentsTheme(dynamicColor = false) {
                Surface() {

                    // instantiate our ViewModel
                    val viewModel: FlowViewModel = viewModel(
                        factory = FlowViewModel.provideFactory(
                            LocalContext.current.applicationContext
                                    as Application
                        )
                    )

                    val uiState = viewModel.uiState.collectAsState()
                    val cities = uiState.value.cityList

                    HomeScreenContent(cities = cities, viewModel::insertCity)

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    cities: List<CityAndWeather>,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFF0000),
                        Color(0xFF660000)
                    )
                )
            )
            .fillMaxSize()
    ) {
/*                        if (uiState.value.isLoading) {
                            Text(text = "Loading...",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center)
                        } else {*/
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cities) { city ->
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = city.city.name + ", " + city.city.state,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(10.dp)
                        )
                        Text(
                            text = if (city.weather) "H: 115 | L: 98" else "Loading...",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
        //}
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) { Text(text = "Add City") }
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    device = "id:pixel",
    apiLevel = 31,
    showBackground = true
)
@Composable
private fun HomeScreenPreview() {
    RoomExperimentsTheme(dynamicColor = false) {
            HomeScreenContent(cities = cityList.map {
                CityAndWeather(it, true)
            }, onClick = { })
    }
}
