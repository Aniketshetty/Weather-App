package com.open.weatherapp.utils

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.open.weatherapp.api.ApiServices
import com.open.weatherapp.database.AppDB
import com.open.weatherapp.database.WeatherDAO
import com.open.weatherapp.database.WeatherRecord
import com.open.weatherapp.model.Weather
import retrofit2.Response

//@HiltWorker
class WeatherPeriodicSync
(
    context: Context,
    workerParams: WorkerParameters
) :
    Worker(context, workerParams) {
    var apiInterface: ApiServices? = null
    lateinit var weatherDAO : WeatherDAO
    lateinit var response : Response<Weather>




    override fun doWork(): Result {

         response = apiInterface?.getPeriodicWeatherData(
             Constants.Latitude,
             Constants.Longitude,
             Constants.APP_ID
         )!!
        if (response.isSuccessful) {
            var weatherR = WeatherRecord(0,"","","","","")
            weatherR.city = response.body()?.name.toString()
            weatherR.country = response.body()?.sys?.country.toString()
            weatherR.description = response.body()?.weather?.get(0)?.description.toString()
            weatherR.temperature = response.body()?.main?.temp.toString()
            weatherR.speed = response.body()?.wind?.speed.toString()
            weatherDAO.insert(weatherR)

        } else {
            return Result.failure()
        }
        return Result.success()
    }

}
