package me.rezapour.domain.usecase

import me.rezapour.domain.model.Workout
import me.rezapour.domain.repository.WorkoutRepository

class UpdateWorkoutUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(workout: Workout) = repository.updateWorkout(workout)
}