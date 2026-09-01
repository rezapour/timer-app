package me.rezapour.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import me.rezapour.db.entites.WorkoutEntity

@Dao
interface WorkoutDao {

    @Insert()
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Query("SELECT * FROM workout_table")
    fun getWorkouts(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM WORKOUT_TABLE WHERE id=:id")
    suspend fun getWorkout(id:Long): WorkoutEntity?

    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    @Query("DELETE FROM workout_table WHERE id= :id")
    suspend fun deleteWorkout(id: Long)
}
