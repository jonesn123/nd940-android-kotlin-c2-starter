package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asteroid: Asteroid)

    @Update
    suspend fun update(asteroid: Asteroid)

    @Query("SELECT * from asteroid_table WHERE id = :id")
    suspend fun get(id: Long): Asteroid?

    @Query("SELECT * from asteroid_table")
    suspend fun getAsteroids(): List<Asteroid>

    @Query("DELETE FROM asteroid_table")
    suspend fun clear()
}