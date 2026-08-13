package me.rezapour.domain.usecase

import kotlinx.coroutines.withContext
import me.rezapour.domain.model.Timer
import me.rezapour.domain.repository.TimerRepository

class InsertTimerUseCase(
    private val timerRepository: TimerRepository
) {

    suspend operator fun invoke(timer: Timer) {
        timerRepository.insertTimer(timer)
    }

}