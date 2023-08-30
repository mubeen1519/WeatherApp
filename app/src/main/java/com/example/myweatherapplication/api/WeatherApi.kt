package com.example.myweatherapplication.api

import com.example.myweatherapplication.models.current.CurrentWeather
import com.example.myweatherapplication.models.forecast.WeatherForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(@Query("zip") zip_code:String,@Query("appid") appId:String):Response<CurrentWeather>

    @GET("data/2.5/forecast")
    suspend fun getWeeklyForecast(@Query("zip") zip_code:String,@Query("appid") appId:String):Response<WeatherForecast>

//    @GET("data/2.5/forecast")
//    suspend fun getWeeklyForecast(@Query("lat") lat:String,@Query("lon") long:String,@Query("cnt") numberOfDays:String,@Query("appid") appId:String):Response<WeatherForecast>
}