package me.rezapour.domain.repository

import me.rezapour.domain.model.WorkoutSession

interface WorkoutSessionRepository {
    suspend fun insertWorkoutSession(workoutSession: WorkoutSession):Long

    suspend fun updateWorkoutSession(workoutSession: WorkoutSession)

}