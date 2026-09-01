package me.rezapour.domain.usecase

import me.rezapour.domain.model.Workout
import me.rezapour.domain.repository.WorkoutRepository

class GetWorkoutUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(workoutId: Long): Workout? = repository.getWorkout(workoutId)
}