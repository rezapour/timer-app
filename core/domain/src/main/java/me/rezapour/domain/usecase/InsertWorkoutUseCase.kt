package me.rezapour.domain.usecase

import me.rezapour.domain.model.Workout
import me.rezapour.domain.repository.WorkoutRepository

class InsertWorkoutUseCase(
    private val workoutRepository: WorkoutRepository
) {

    suspend operator fun invoke(workout: Workout) {
        workoutRepository.insertWorkout(workout)
    }

}