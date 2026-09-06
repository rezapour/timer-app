package me.rezapour.workout.domain.usecase

import me.rezapour.workout.api.domain.model.Workout
import me.rezapour.workout.api.domain.usecase.InsertWorkoutUseCase
import me.rezapour.workout.domain.repository.WorkoutRepository

class InsertWorkoutUseCaseImpl(
    private val workoutRepository: WorkoutRepository
) : InsertWorkoutUseCase {
    override suspend operator fun invoke(workout: Workout): Long {
        return workoutRepository.insertWorkout(workout)
    }
}