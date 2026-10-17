package me.rezapour.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import me.rezapour.db.entites.IntervalEntity
import me.rezapour.db.entites.WorkoutEntity
import me.rezapour.db.entites.WorkoutWithIntervals

@Dao
interface WorkoutDao {

    @Insert
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Insert
    suspend fun insertIntervals(intervals: List<IntervalEntity>)

    @Transaction
    suspend fun insertWorkoutWithIntervals(
        workout: WorkoutEntity,
        intervals: List<IntervalEntity>
    ): Long {
        val workoutId = insertWorkout(workout)
        insertIntervals(
            intervals.map { interval ->
                interval.copy(workoutId = workoutId)
            }
        )
        return workoutId
    }

    @Transaction
    @Query("SELECT * FROM workout_table")
    fun getWorkoutsWithIntervals(): Flow<List<WorkoutWithIntervals>>

    @Query("SELECT * FROM workout_table")
    fun getWorkouts(): Flow<List<WorkoutEntity>>

    @Transaction
    @Query("SELECT * FROM workout_table WHERE id=:id")
    suspend fun getWorkout(id: Long): WorkoutWithIntervals?

    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    @Query("DELETE FROM workout_table WHERE id= :id")
    suspend fun deleteWorkout(id: Long)

    @Query("DELETE FROM interval_table WHERE workoutId = :workoutId AND id NOT IN (:ids)")
    suspend fun deleteRemovedIntervals(
        workoutId: Long,
        ids: List<Long>
    )

    @Update
    suspend fun updateIntervals(intervals: List<IntervalEntity>)

    @Transaction
    suspend fun updateWorkoutWithIntervals(
        workout: WorkoutEntity,
        intervals: List<IntervalEntity>
    ) {
        updateWorkout(workout)

        val existingIntervals = intervals.filter { it.id != 0L }
        val newIntervals = intervals.filter { it.id == 0L }

        deleteRemovedIntervals(
            workoutId = workout.id,
            ids = existingIntervals.map { it.id }
        )

        updateIntervals(
            existingIntervals.map { it.copy(workoutId = workout.id) }
        )

        insertIntervals(
            newIntervals.map { it.copy(workoutId = workout.id) }
        )
    }
}
