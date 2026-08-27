package me.rezapour.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.rezapour.domain.model.Workout
import me.rezapour.domain.repository.WorkoutRepository

class GetWorkoutsUseCase(
    private val workoutRepository: WorkoutRepository
) {

    operator fun invoke(): Flow<List<Workout>> {
        return workoutRepository.getWorkouts()
    }

}
