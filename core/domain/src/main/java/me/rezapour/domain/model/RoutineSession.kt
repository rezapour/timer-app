package me.rezapour.domain.model

import me.rezapour.domain.controller.TimerStatus
import java.util.Date

data class RoutineSession(
    val sessionId: Long = 0,
    val routineId: Long,
    val currentRound: Int,
    val currentPhase: RoutinePhase,
    val status: TimerStatus,
    val phaseEndAt: Date,
    val pausedRemainingMs: Long,
    val startedAt: Date
)

enum class RoutinePhase {
    INTERVAL,
    REST
}