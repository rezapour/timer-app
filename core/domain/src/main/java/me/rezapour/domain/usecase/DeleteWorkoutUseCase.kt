package me.rezapour.domain.usecase

import me.rezapour.domain.repository.WorkoutRepository

class DeleteWorkoutUseCase(
    private val workoutRepository: WorkoutRepository
) {

    suspend operator fun invoke(id: Long) {
        workoutRepository.deleteWorkout(id)
    }

}
