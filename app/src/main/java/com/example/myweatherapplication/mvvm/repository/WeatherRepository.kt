package com.example.myweatherapplication.mvvm.repository

import com.example.myweatherapplication.api.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherApi: WeatherApi) {

    suspend fun getCurrentWeatherData(zip_code:String,apiKey:String)  = flow {
        emit(weatherApi.getCurrentWeather(zip_code,apiKey))

    }.flowOn(Dispatchers.IO)

    suspend fun getForecastWeatherData(zip_code:String,apiKey:String) = flow {
        emit(weatherApi.getWeeklyForecast(zip_code, apiKey))
    }
}