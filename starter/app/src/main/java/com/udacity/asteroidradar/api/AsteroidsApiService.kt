package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

interface AsteroidsApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String = getNextSevenDaysFormattedDates().first(),
        @Query("end_date") endDate: String = getNextSevenDaysFormattedDates().last(),
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): String

    @GET("planetary/apod")
    suspend fun getImageOfDay(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): PictureOfDay
}

object AsteroidsApi {
    val retrofitServiceScalars: AsteroidsApiService by lazy { createHttpClientScalars(Constants.BASE_URL, AsteroidsApiService::class) }
    val retrofitServiceMoshi: AsteroidsApiService by lazy { createHttpClientMoshi(Constants.BASE_URL, AsteroidsApiService::class) }
}