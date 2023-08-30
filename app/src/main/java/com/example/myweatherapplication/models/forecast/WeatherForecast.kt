package com.example.myweatherapplication.models.forecast

data class WeatherForecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherDetails>,
    val message: Int
)