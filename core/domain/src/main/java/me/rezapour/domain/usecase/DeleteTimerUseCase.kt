package me.rezapour.domain.usecase

import me.rezapour.domain.repository.RoutineRepository

class DeleteTimerUseCase(private val repository: RoutineRepository) {

    suspend operator fun invoke(id: Long) {
        repository.deleteRoutine(id)
    }
}