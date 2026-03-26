package me.rezapour.timer_core.model

sealed interface CoreTimerStatus {
    data object Idle : CoreTimerStatus
    data object Running : CoreTimerStatus
    data object Paused : CoreTimerStatus
    data object Finished : CoreTimerStatus
    data object Stopped : CoreTimerStatus
}

data class CoreTimerSnapshot(
    val direction: CountDirection,
    val durationMs: Long,     // total/target time
    val elapsedMs: Long,      // how much passed since start
    val remainingMs: Long,    // for DOWN: time left, for UP: time left to target
    val isCounting: Boolean
)

enum class CountDirection { DOWN, UP }