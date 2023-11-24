package com.example.weathersearchapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.weathersearchapplication.databinding.ActivityMainBinding
import com.example.weathersearchapplication.model.WeatherDataResponse
import com.example.weathersearchapplication.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        weatherViewModel = WeatherViewModel()
        subscribe()

        binding.btnSendRequest.setOnClickListener {
            if (binding.etCityName.text.isNullOrEmpty() || binding.etCityName.text.isNullOrBlank()){
                binding.etCityName.error = "Field can't be null"
            }else{
                weatherViewModel.getWeatherData(binding.etCityName.text.toString())
            }
        }
    }

    private fun subscribe() {
        weatherViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) binding.tvResult.text = resources.getString(R.string.loading)
        }

        weatherViewModel.isError.observe(this) { isError ->
            if (isError) {
                binding.imgCondition.visibility = View.GONE
                binding.tvResult.text = weatherViewModel.errorMessage
            }
        }

        weatherViewModel.weatherData.observe(this) { weatherData ->
            setResultText(weatherData)
        }
    }

    private fun setResultText(weatherData: WeatherDataResponse) {
        val resultText = StringBuilder("Result:\n")

        weatherData.location.let { location ->
            resultText.append("Name: ${location?.name}\n")
            resultText.append("Region: ${location?.region}\n")
            resultText.append("Country: ${location?.country}\n")
            resultText.append("Timezone ID: ${location?.tzId}\n")
            resultText.append("Local Time: ${location?.localtime}\n")
        }

        weatherData.current.let { current ->
            current?.condition.let { condition ->
                resultText.append("Condition: ${condition?.text}\n")
                setResultImage(condition?.icon)
            }
            resultText.append("Celcius: ${current?.tempC}\n")
            resultText.append("Fahrenheit: ${current?.tempF}\n")
        }


        binding.tvResult.text = resultText
    }

    private fun setResultImage(imageUrl: String?) {
        // Display image when image url is available
        imageUrl.let { url ->
            Glide.with(applicationContext)
                .load("https:$url")
                .into(binding.imgCondition)

            binding.imgCondition.visibility = View.VISIBLE
            return
        }

        // Hide image when image url is null
        binding.imgCondition.visibility = View.GONE
    }
}