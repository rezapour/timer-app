package me.rezapour.domain.controller

import kotlinx.coroutines.flow.StateFlow

interface TimerEngine {

    val status: StateFlow<TimerStatus>
    val snapshot: StateFlow<TimerSnapshot?>

    fun configure(durationMs: Long)
    fun start()
    fun pause()
    fun resume()
    fun stop()
    fun reset()
    fun close()
}

sealed interface TimerStatus {
    data object Idle : TimerStatus
    data object Running : TimerStatus
    data object Paused : TimerStatus
    data object Finished : TimerStatus
    data object Stopped : TimerStatus
}

data class TimerSnapshot(
    val durationMs: Long = 0,
    val elapsedMs: Long = 0,
    val remainingMs: Long = 0,
    val isCounting: Boolean = false
)