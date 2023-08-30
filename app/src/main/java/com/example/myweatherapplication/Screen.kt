package com.example.myweatherapplication

sealed class Screen(val route: String){
    object Home: Screen(route = "home_screen")
    object Forecast: Screen(route = "forecast_scree")
}

