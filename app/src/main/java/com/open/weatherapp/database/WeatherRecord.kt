package com.open.weatherapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "city")
    var city: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "temperature")
    var temperature: String,
    @ColumnInfo(name = "speed")
    var speed: String,
    @ColumnInfo(name = "country")
    var country: String
)