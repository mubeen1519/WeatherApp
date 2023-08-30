package com.example.myweatherapplication.di

import com.example.myweatherapplication.api.WeatherApi
import com.example.myweatherapplication.utils.ConstantsClass
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit():Retrofit{
        return Retrofit.Builder().baseUrl(ConstantsClass.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideWeatherApi(retrofit: Retrofit):WeatherApi{
        return retrofit.create(WeatherApi::class.java)
    }
}