package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.reflect.KClass

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private fun provideRetrofitMoshi(baseUrl: String) = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

private fun provideRetrofitScalars(baseUrl: String) = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

fun <T : Any> createHttpClientMoshi(baseUrl: String, clazz: KClass<T>): T = provideRetrofitMoshi(baseUrl).create(clazz.java)
fun <T : Any> createHttpClientScalars(baseUrl: String, clazz: KClass<T>): T = provideRetrofitScalars(baseUrl).create(clazz.java)

fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<AsteroidDataTransferObject> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")
    val asteroidList = ArrayList<AsteroidDataTransferObject>()
    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

        for (i in 0 until dateAsteroidJsonArray.length()) {
            val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
            val id = asteroidJson.getLong("id")
            val codename = asteroidJson.getString("name")
            val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
            val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter").getJSONObject("kilometers").getDouble("estimated_diameter_max")
            val closeApproachData = asteroidJson.getJSONArray("close_approach_data").getJSONObject(0)
            val relativeVelocity = closeApproachData.getJSONObject("relative_velocity").getDouble("kilometers_per_second")
            val distanceFromEarth = closeApproachData.getJSONObject("miss_distance").getDouble("astronomical")
            val isPotentiallyHazardous = asteroidJson.getBoolean("is_potentially_hazardous_asteroid")
            val asteroid = AsteroidDataTransferObject(
                id = id,
                codename = codename,
                closeApproachDate = formattedDate,
                absoluteMagnitude = absoluteMagnitude,
                estimatedDiameter = estimatedDiameter,
                relativeVelocity = relativeVelocity,
                distanceFromEarth = distanceFromEarth,
                isPotentiallyHazardous = isPotentiallyHazardous
            )
            asteroidList.add(asteroid)
        }
    }
    return asteroidList
}

fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()
    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    return formattedDateList
}