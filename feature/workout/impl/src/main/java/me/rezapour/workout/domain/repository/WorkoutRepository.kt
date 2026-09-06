package me.rezapour.workout.domain.repository

import kotlinx.coroutines.flow.Flow
import me.rezapour.workout.api.domain.model.Workout

interface WorkoutRepository {

    suspend fun insertWorkout(workout: Workout): Long

    fun getWorkouts(): Flow<List<Workout>>

    suspend fun deleteWorkout(id: Long)

    suspend fun getWorkout(workoutId: Long): Workout?

    suspend fun updateWorkout(workout: Workout)
}