package me.rezapour.timer_flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.rezapour.domain.coordinator.WorkoutCoordinator
import me.rezapour.workout.api.domain.model.Interval
import me.rezapour.workout.api.domain.model.IntervalType
import me.rezapour.workout.api.domain.model.Workout


class TimerFlowViewModel(private val workoutCoordinator: WorkoutCoordinator) : ViewModel() {

    val snapshot = workoutCoordinator.timeTicker
    var paused: Boolean = false

    fun start() {
        val workout = Workout(
            name = "swiming",
            rounds = 5,
            intervals = listOf(
                Interval(
                    name = "work",
                    durationSeconds = 60L,
                    type = IntervalType.WORK
                ),
                Interval(
                    name = "Rest",
                    durationSeconds = 30L,
                    type = IntervalType.REST
                )
            )
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
