package com.open.weatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WeatherDAO {
    @Insert
    fun insert(weatherRecord: WeatherRecord)
    @Query("SELECT * FROM weather ORDER BY id DESC")
    fun getWeatherRecord(): LiveData<WeatherRecord>
    @Insert
    suspend fun saveWeatherRecord(weatherRecord: WeatherRecord)
    @Delete
    suspend fun deleteRecord(weatherRecord: WeatherRecord)

}