package com.example.myweatherapplication.mvvm.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapplication.models.current.CurrentWeather
import com.example.myweatherapplication.mvvm.repository.WeatherRepository
import com.example.myweatherapplication.utils.ConstantsClass
import com.example.myweatherapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    private val _textFieldValue = MutableLiveData("55101")

    val textFieldValue: LiveData<String>
        get() = _textFieldValue

    fun updateTextFieldValue(newValue: String) {
        _textFieldValue.value = newValue
    }

    private val _errorMessage = MutableLiveData<String?>(null)

    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun resetErrorMessage() {
        _errorMessage.value = null
    }

    val currentWeatherResponse: MutableState<NetworkResult<CurrentWeather>> =
        mutableStateOf(NetworkResult.Empty(""))
    val weatherForecastResponse: MutableState<NetworkResult<List<com.example.myweatherapplication.models.forecast.WeatherDetails>>> =
        mutableStateOf(NetworkResult.Empty(""))

    init {
        getCurrentWeatherData(textFieldValue.value.toString(), ConstantsClass.API_KEY)
        getWeatherForecastData(textFieldValue.value.toString(), ConstantsClass.API_KEY)
//        getWeatherForecastData("44.95", "-93.08", "7", "2d208f920ad72e089f6616a5bf7d2d59")
    }

    fun getCurrentWeatherData(zip_code: String, apiKey: String) = viewModelScope.launch {
        if (isValidZipCode(zip_code)) {
            _errorMessage.value = null
            repository.getCurrentWeatherData(zip_code, apiKey)
                .onStart {
                    currentWeatherResponse.value = NetworkResult.Loading(true)
                }.catch {
                    currentWeatherResponse.value =
                        NetworkResult.Failure(it.message ?: "Unknown Error")
                }.collect {
                    if (it.isSuccessful && it.body() != null) {
                        it.body()?.let { data ->
                            currentWeatherResponse.value = NetworkResult.Success(data)
                        }

                    }

                }
        } else {
            _errorMessage.value =
                "Zip code Should be exactly 5 digits long and contain numbers only"
        }
    }

    private fun isValidZipCode(zipCode: String): Boolean {
        return zipCode.length == 5 && zipCode.isDigitsOnly()
    }

    fun getWeatherForecastData(zip_code: String, apiKey: String) =
        viewModelScope.launch {
            repository.getForecastWeatherData(zip_code, apiKey)
                .onStart {
                    weatherForecastResponse.value = NetworkResult.Loading(true)
                }.catch {
                    weatherForecastResponse.value =
                        NetworkResult.Failure(it.message ?: "Unknown Error")
                }.collect {
                    if (it.isSuccessful && it.body() != null) {
                        it.body()?.let { data ->
                            ConstantsClass.SunRiseTime = data.city.sunrise
                            ConstantsClass.SunSetTime = data.city.sunset
                            weatherForecastResponse.value = NetworkResult.Success(data.list)
                        }
                    }
                }
        }
}