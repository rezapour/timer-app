package me.rezapour.timer.domain.repository

import me.rezapour.timer.domain.model.WorkoutSession

interface WorkoutSessionRepository {
    suspend fun insertWorkoutSession(workoutSession: WorkoutSession):Long

    suspend fun updateWorkoutSession(workoutSession: WorkoutSession)

}