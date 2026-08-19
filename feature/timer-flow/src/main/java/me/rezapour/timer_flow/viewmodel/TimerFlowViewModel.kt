package me.rezapour.timer_flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.rezapour.domain.coordinator.RoutineCoordinator
import me.rezapour.domain.model.Routine

class TimerFlowViewModel(private val routineCoordinator: RoutineCoordinator) : ViewModel() {

    val snapshot = routineCoordinator.timeTicker
    var paused: Boolean = false

    fun start() {
        val routine = Routine(
            name = "swiming",
            workSeconds = 10,
            restSeconds = 30,
            rounds = 5,
        )
        viewModelScope.launch {
            routineCoordinator.start(routine)
        }
    }

    fun pause() {
        viewModelScope.launch {
            paused = if (paused) {
                routineCoordinator.resumeTimer()
                false

            } else {
                routineCoordinator.pauseTimer()
                true
            }
        }
    }

}