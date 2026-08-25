package me.rezapour.domain.usecase

import me.rezapour.domain.model.Routine
import me.rezapour.domain.repository.RoutineRepository

class InsertRoutineUseCase(
    private val routineRepository: RoutineRepository
) {

    suspend operator fun invoke(routine: Routine) {
        routineRepository.insertRoutine(routine)
    }

}