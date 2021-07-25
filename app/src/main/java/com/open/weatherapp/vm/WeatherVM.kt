package com.open.weatherapp.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.open.weatherapp.database.WeatherRecord
import com.open.weatherapp.repo.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherVM
@Inject
constructor(private val repository: WeatherRepo) : ViewModel() {
    var weatherR = WeatherRecord(0, "", "", "", "", "")

    private val allWeatherDetails: LiveData<WeatherRecord>

    init {
        getWeatherDB()
        allWeatherDetails = repository.getWeatherDB()
    }

    fun getWeatherDB(): LiveData<WeatherRecord> {
        return allWeatherDetails
    }

    fun getWeatherReport(latitude: String, longitude: String, appId: String) =
        viewModelScope.launch {
            repository.getWeather(latitude, longitude, appId).let { response ->
                if (response.isSuccessful) {
                    weatherR.city = response.body()?.name.toString()
                    weatherR.country = response.body()?.sys?.country.toString()
                    weatherR.description = response.body()?.weather?.get(0)?.description.toString()
                    weatherR.temperature = response.body()?.main?.temp.toString()
                    weatherR.speed = response.body()?.wind?.speed.toString()

                    repository.saveWeather(weatherR)
                } else {
                    Log.d("TAG", "GET Weather Error COde: ${response.code()}")
                }
            }
        }

}
