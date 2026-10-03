package me.rezapour.workout.domain.usecase

import me.rezapour.workout.domain.repository.WorkoutRepository


class DeleteWorkoutUseCase(
    private val workoutRepository: WorkoutRepository
) {

    suspend operator fun invoke(id: Long) {
        workoutRepository.deleteWorkout(id)
    }

}
