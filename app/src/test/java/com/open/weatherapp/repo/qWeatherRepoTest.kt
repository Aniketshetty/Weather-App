package com.open.weatherapp.repo

import com.open.weatherapp.api.ApiServices
import com.open.weatherapp.database.WeatherDAO
import kotlinx.coroutines.runBlocking
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import org.junit.Assert.*
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class qWeatherRepoTest @Inject constructor(
    private val apiServices: ApiServices,
    private val weatherDAO: WeatherDAO
) {

    @Test
    fun getWeather() {
        var x = ""
        runBlocking {
            var response = apiServices.getCurrentWeatherData(
                "19.286169234138175",
                "72.87462815346466",
                "b2d56ff4a0b8721bddd853ebea818bd2"
            )
            x = response.body()?.sys?.country.toString()
            assertEquals("IN", x)
        }
    }

    @Test
    fun getWeatherDB() {
    }
}