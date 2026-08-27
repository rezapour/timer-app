package me.rezapour.timer_flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.rezapour.domain.coordinator.WorkoutCoordinator
import me.rezapour.domain.model.Workout

class TimerFlowViewModel(private val workoutCoordinator: WorkoutCoordinator) : ViewModel() {

    val snapshot = workoutCoordinator.timeTicker
    var paused: Boolean = false

    fun start() {
        val workout = Workout(
            name = "swiming",
            workSeconds = 10,
            restSeconds = 30,
            rounds = 5,
        )
        viewModelScope.launch {
            workoutCoordinator.start(workout)
        }
    }

    fun pause() {
        viewModelScope.launch {
            paused = if (paused) {
                workoutCoordinator.resumeTimer()
                false

            } else {
                workoutCoordinator.pauseTimer()
                true
            }
        }
    }

}
