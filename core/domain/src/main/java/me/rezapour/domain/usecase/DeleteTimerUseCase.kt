package me.rezapour.domain.usecase

import me.rezapour.domain.repository.TimerRepository

class DeleteTimerUseCase(private val repository: TimerRepository) {

    suspend operator fun invoke(id: Long) {
        repository.deleteTimer(id)
    }
}