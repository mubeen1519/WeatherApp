package com.example.myweatherapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myweatherapplication.mvvm.viewmodel.WeatherViewModel
import com.example.myweatherapplication.ui.theme.MyWeatherApplicationTheme
import com.example.myweatherapplication.utils.ConstantsClass
import com.example.myweatherapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    val weatherViewModel: WeatherViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyWeatherApplicationTheme {
                navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavGraph(navController = navController)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun SetupNavGraph(navController: NavHostController) {

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(
                route = Screen.Home.route
            ) {
                WeatherApp(navController = navController, weatherViewModel)
            }
            composable(
                route = Screen.Forecast.route
            ) {
                ForecastScreen(weatherViewModel)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WeatherApp(navController: NavHostController, weatherViewModel: WeatherViewModel) {
        val textFieldValue by weatherViewModel.textFieldValue.observeAsState("")
        val errorMessage by weatherViewModel.errorMessage.observeAsState()

        if (errorMessage != null) {
            AlertDialog(onDismissRequest = { weatherViewModel.resetErrorMessage() },
                title = {
                        Text(text = "Invalid zip code")
                },
                confirmButton = {
                    Button(onClick = { weatherViewModel.resetErrorMessage() }) {
                        Text(text = "Ok")
                    }
                },
                text = {
                    Text(text = errorMessage.toString())
                }
            )
        }
        when (val result = weatherViewModel.currentWeatherResponse.value) {
            is NetworkResult.Success -> {
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text("Weather App") },
                        colors = TopAppBarDefaults.largeTopAppBarColors(Color.Green), // Set the background color of the TopAppBar
                        modifier = Modifier.fillMaxWidth()
                    )
                }) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, end = 15.dp, top = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextField(
                                value = textFieldValue, onValueChange = {
                                    weatherViewModel.updateTextFieldValue(it)
                                },
                                label = {
                                    Text(text = "Enter Your Zip code")
                                },
                                modifier = Modifier
                                    .padding(end = 15.dp)
                                    .fillMaxWidth(.73f)

                            )
                            Button(
                                onClick = {
                                    textFieldValue.let { zipCode ->
                                        weatherViewModel.getCurrentWeatherData(
                                            zipCode,
                                            ConstantsClass.API_KEY
                                        )
                                        weatherViewModel.getWeatherForecastData(
                                            zipCode,
                                            ConstantsClass.API_KEY
                                        )
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Gray,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "search icon"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = result.data.name,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center

                        ) {
                            Column(
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                val tempValue =
                                    String.format("%.2f", result.data.main.temp - 273.15)
                                Text(
                                    text = "${tempValue}°",
                                    fontSize = 50.sp,
                                    style = MaterialTheme.typography.headlineLarge

                                )
                                val feelsLike =
                                    String.format("%.2f", result.data.main.feels_like - 273.15)
                                Text(
                                    text = "Feels like ${feelsLike}°",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(modifier = Modifier.width(100.dp))
                            Image(
                                painter = painterResource(id = R.drawable.sun),
                                contentDescription = "Sun Icon",
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(60.dp, 0.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Column {
                                val minTempValue =
                                    String.format("%.2f", result.data.main.temp_min - 273.15)
                                val maxTempValue =
                                    String.format("%.2f", result.data.main.temp_max - 273.15)
                                Text(
                                    text = "Low ${minTempValue}°",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "High ${maxTempValue}°",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Humidity ${result.data.main.humidity}%",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Pressure ${result.data.main.pressure}hpa",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                navController.navigate(route = Screen.Forecast.route)
                            },
                            colors = ButtonDefaults.buttonColors(Color.Green)
                        ) {
                            Text(text = "Forecast")
                        }

                    }
                }
            }

            is NetworkResult.Failure -> {
                Text(text = result.errorMessage)
            }

            is NetworkResult.Loading -> {
                Text(text = "Please wait...")
            }

            else -> {}
        }


    }

    @Composable
    @Preview(showBackground = true)
    fun WeatherAppPreview() {
        WeatherApp(navController = rememberNavController(), weatherViewModel)
    }

}