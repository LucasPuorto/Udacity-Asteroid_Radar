package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {

    @Query("SELECT * FROM asteroid_table WHERE close_approach_date >= date(:startDate) ORDER BY date(close_approach_date) ASC")
    fun getAllAsteroids(startDate: String): LiveData<List<DatabasesAsteroid>>

    @Query("SELECT * FROM asteroid_table WHERE close_approach_date = date(:today)")
    fun getAllTodayAsteroids(today: String): LiveData<List<DatabasesAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabasesAsteroid)

    @Query("SELECT * FROM asteroid_table WHERE id = :idAsteroid")
    fun getAsteroid(idAsteroid: Long): DatabasesAsteroid

    @Query("DELETE FROM asteroid_table WHERE close_approach_date < :today")
    fun clear(today: String)
}