package com.example.myweatherapplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myweatherapplication.mvvm.viewmodel.WeatherViewModel
import com.example.myweatherapplication.utils.ConstantsClass
import com.example.myweatherapplication.utils.NetworkResult
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(weatherViewModel: WeatherViewModel){
    when(val result = weatherViewModel.weatherForecastResponse.value){
        is NetworkResult.Success -> {
            Scaffold(topBar = {
                TopAppBar(
                    title = { Text("Forecast") },
                    colors = TopAppBarDefaults.largeTopAppBarColors(Color.Gray), // Set the background color of the TopAppBar
                    modifier = Modifier.fillMaxWidth()
                )
            }) { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(result.data) { forecast ->
                        // convert timestamps to formatted date and time
//                        val date = Instant.ofEpochMilli(forecast.date).atZone(ZoneId.systemDefault()).toLocalDate()
//                        val sunrise = Instant.ofEpochMilli(forecast.sunrise).atZone(ZoneId.systemDefault()).toLocalDateTime()
//                        val sunset = Instant.ofEpochMilli(forecast.sunset).atZone(ZoneId.systemDefault()).toLocalDateTime()
//                        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
//                        val timeFormatter = DateTimeFormatter.ofPattern("h:mma")

                        Row {
                            Column()
                            {
                                Image(
                                    painter = painterResource(id = R.drawable.sun),
                                    contentDescription = "Sun Icon",
                                    modifier = Modifier
                                        .size(50.dp)

                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally)
                            {
                                Spacer(modifier = Modifier.height(12.dp))
//                                val dt = forecast.dt
//                                var day =  Date((dt * 1000).toLong());
                                val created: Int = forecast.dt
                                val formatter = SimpleDateFormat("MMM d", Locale.US)
                                val dateString: String = formatter.format(Date(created * 1000L))
                                Text(text = dateString, fontSize = 12.sp)
                            }
                            
                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                val tempValue = String.format("%.2f", forecast.main.temp - 273.15)
                                val minTempValue = String.format("%.2f", forecast.main.temp_min - 273.15)
                                val maxTempValue = String.format("%.2f", forecast.main.temp_max - 273.15)

                                Text(
                                    text = "Temp ${tempValue}°",fontSize = 12.sp)
                                Text("High: ${minTempValue}°  Low: ${maxTempValue}°",fontSize = 12.sp)
//                                Text("Low: ${maxTempValue}°",fontSize = 14.sp)

                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                val sunRiseTime: Int = ConstantsClass.SunRiseTime
                                val sunRiseTimeFormatter = SimpleDateFormat("hh:mm", Locale.US)
                                val sunRiseTimeDateString = sunRiseTimeFormatter.format(Date(sunRiseTime * 1000L))

                                val sunSetTime = ConstantsClass.SunSetTime
                                val sunSetTimeFormatter = SimpleDateFormat("hh:mm", Locale.US)
                                val sunSetTimeDateString: String = sunSetTimeFormatter.format(Date(sunSetTime * 1000L))
                                Text(text = "Sunrise : $sunRiseTimeDateString",fontSize = 12.sp)
                                Text(text = "SunSet : $sunSetTimeDateString",fontSize = 12.sp)

                            }

                        }

//                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        }
        is NetworkResult.Loading -> {
            Text(text = "Please wait...")
        }
        is NetworkResult.Failure ->{
            Text(text = result.errorMessage)
        }

        else -> {}
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun ForecastScreenPreview()
{
    ForecastScreen(MainActivity().weatherViewModel)
}