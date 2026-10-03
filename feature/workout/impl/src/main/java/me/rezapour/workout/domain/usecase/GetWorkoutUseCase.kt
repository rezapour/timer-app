package me.rezapour.workout.domain.usecase

import me.rezapour.workout.api.domain.model.Workout
import me.rezapour.workout.domain.repository.WorkoutRepository


class GetWorkoutUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(workoutId: Long): Workout? = repository.getWorkout(workoutId)
}