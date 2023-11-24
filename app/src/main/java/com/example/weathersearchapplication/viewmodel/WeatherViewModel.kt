package com.example.weathersearchapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weathersearchapplication.model.WeatherDataResponse
import com.example.weathersearchapplication.retrofit.Retrofit2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel():ViewModel() {

    private val _weatherData = MutableLiveData<WeatherDataResponse>()
    val weatherData:LiveData<WeatherDataResponse> get() = _weatherData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading:LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError:LiveData<Boolean> get() = _isError

    var errorMessage:String = ""

    fun getWeatherData(city:String){


        _isLoading.value = true
        _isError.value = false

        val client = Retrofit2.getApiService().getCurrentWeather(city= city)

        // Send API request using Retrofit
        client.enqueue(object : Callback<WeatherDataResponse> {

            override fun onResponse(
                call: Call<WeatherDataResponse>,
                response: Response<WeatherDataResponse>
            ) {
                val responseBody = response.body()
                if (!response.isSuccessful || responseBody == null) {
                    onError("Data Processing Error")
                    return
                }

                _isLoading.value = false
                _weatherData.postValue(responseBody)
            }

            override fun onFailure(call: Call<WeatherDataResponse>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }

        })
    }

    private fun onError(inputMessage: String?) {

        val message = if (inputMessage.isNullOrBlank() or inputMessage.isNullOrEmpty()) "Unknown Error"
        else inputMessage

        errorMessage = StringBuilder("ERROR: ")
            .append("$message some data may not displayed properly").toString()

        _isError.value = true
        _isLoading.value = false
    }
}