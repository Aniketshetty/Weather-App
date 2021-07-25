package com.open.weatherapp.di

import android.app.Application
import androidx.room.Room
import com.open.weatherapp.api.ApiServices
import com.open.weatherapp.database.AppDB
import com.open.weatherapp.database.WeatherDAO
import com.open.weatherapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofitInstance(BASE_URL: String): ApiServices =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices::class.java)


    @Provides
    @Singleton
    internal fun provideDatabase(application: Application): AppDB {
        return Room.databaseBuilder(
            application, AppDB::class.java, "waether.db")
            .allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    internal fun provideWeatherDao(appDB: AppDB): WeatherDAO {
        return appDB.weatherDao()
    }

}