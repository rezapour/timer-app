package me.rezapour.workout.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.rezapour.workout.api.domain.model.Workout
import me.rezapour.workout.domain.repository.WorkoutRepository


class GetWorkoutsUseCase(
    private val workoutRepository: WorkoutRepository
) {

    operator fun invoke(): Flow<List<Workout>> {
        return workoutRepository.getWorkouts()
    }

}
