package com.example.roomlists

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.santansarah.roomexperiments.presentation.FlowViewModel
import com.santansarah.roomexperiments.presentation.theme.RoomExperimentsTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomExperimentsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {

                    // instantiate our ViewModel
                    val viewModel: FlowViewModel = viewModel(
                        factory = FlowViewModel.provideFactory(
                            LocalContext.current.applicationContext
                                    as Application
                        )
                    )

                    val uiState = viewModel.uiState.collectAsState()
                    val cities = uiState.value.cityList

                    Column(modifier = Modifier.fillMaxSize()) {
/*                        if (uiState.value.isLoading) {
                            Text(text = "Loading...",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center)
                        } else {*/
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(cities) { city ->
                                    Card(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = city.city.name + ", " + city.city.state,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }
                                }
                            }
                        //}
                        Button(
                            onClick = { viewModel.insertCity() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) { Text(text = "Add City") }
                    }
                }
            }
        }
    }
}
