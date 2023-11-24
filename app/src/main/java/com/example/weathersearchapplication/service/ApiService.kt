package com.example.weathersearchapplication.service

import com.example.weathersearchapplication.model.WeatherDataResponse
import com.example.weathersearchapplication.retrofit.Retrofit2
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("current.json")
    fun getCurrentWeather(
        @Query("key") key: String = Retrofit2.API_KEY,
        @Query("q") city: String,
        @Query("aqi") aqi: String = "no"
    ): Call<WeatherDataResponse>

}