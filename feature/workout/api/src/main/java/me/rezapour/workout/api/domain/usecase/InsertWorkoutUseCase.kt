package me.rezapour.workout.api.domain.usecase

import me.rezapour.workout.api.domain.model.Workout

interface InsertWorkoutUseCase {
    suspend operator fun invoke(workout: Workout):Long
}