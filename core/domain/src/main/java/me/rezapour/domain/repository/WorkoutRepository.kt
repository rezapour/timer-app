package me.rezapour.domain.repository

import kotlinx.coroutines.flow.Flow
import me.rezapour.domain.model.Workout

interface WorkoutRepository {

    suspend fun insertWorkout(workout: Workout):Long

    fun getWorkouts(): Flow<List<Workout>>

    suspend fun deleteWorkout(id:Long)

}