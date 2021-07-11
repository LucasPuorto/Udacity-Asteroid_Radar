package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
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
}

object AsteroidsApi {
    val asteroidsRetrofitService: AsteroidsApiService by lazy { retrofit.create(AsteroidsApiService::class.java) }
}