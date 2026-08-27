package me.rezapour.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import me.rezapour.db.entites.WorkoutSessionEntity

@Dao
interface WorkoutSessionDao {

    @Insert
    suspend fun insertActiveWorkout(activeWorkout: WorkoutSessionEntity): Long

    @Update
    suspend fun updateActiveWorkout(activeWorkout: WorkoutSessionEntity)
}
