package me.rezapour.domain.usecase

import me.rezapour.domain.repository.RoutineRepository

class GetTimersUseCase(
    private val routineRepository: RoutineRepository
) {
    operator fun invoke() = routineRepository.getRoutines()
}