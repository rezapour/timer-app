package me.rezapour.domain.model

import me.rezapour.domain.controller.TimerStatus
import java.util.Date

data class WorkoutSession(
    val sessionId: Long = 0,
    val workoutId: Long,
    val currentRound: Int,
    val currentPhase: WorkoutPhase,
    val status: TimerStatus,
    val phaseEndAt: Date,
    val pausedRemainingMs: Long,
    val startedAt: Date
)

enum class WorkoutPhase {
    INTERVAL,
    REST
}