package com.example.myweatherapplication.utils

sealed class NetworkResult<T> {
    data class Loading<T>(val isLoading: Boolean) : NetworkResult<T>()
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Failure<T>(val errorMessage: String) : NetworkResult<T>()
    data class Empty<T>(val dummy:String) : NetworkResult<T>()
}