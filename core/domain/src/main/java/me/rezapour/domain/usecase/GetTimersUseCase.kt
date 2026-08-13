package me.rezapour.domain.usecase

import me.rezapour.domain.repository.TimerRepository

class GetTimersUseCase(
    private val timerRepository: TimerRepository
) {
    operator fun invoke() = timerRepository.getTimers()
}