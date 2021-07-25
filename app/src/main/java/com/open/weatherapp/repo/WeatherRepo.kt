package com.open.weatherapp.repo

import androidx.lifecycle.LiveData
import com.open.weatherapp.api.ApiServices
import com.open.weatherapp.database.WeatherDAO
import com.open.weatherapp.database.WeatherRecord
import com.open.weatherapp.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class WeatherRepo
@Inject
constructor(private val apiService: ApiServices, private val weatherDAO: WeatherDAO) {
    suspend fun getWeather(lat: String, long: String, app_id: String) =
        apiService.getCurrentWeatherData(lat, long, app_id)

    fun saveWeather(weather: WeatherRecord) = runBlocking {
        this.launch(Dispatchers.IO) {
            weatherDAO.saveWeatherRecord(weather)
        }
    }

    fun getWeatherDB(): LiveData<WeatherRecord> {
        return weatherDAO.getWeatherRecord()
    }
}
