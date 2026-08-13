package me.rezapour.timer_core

import kotlinx.coroutines.flow.StateFlow
import me.rezapour.timer_core.model.CoreTimerSnapshot
import me.rezapour.timer_core.model.CoreTimerStatus
import me.rezapour.timer_core.model.CountDirection

interface CoreTimer {

    val status: StateFlow<CoreTimerStatus>
    val snapshot: StateFlow<CoreTimerSnapshot?>


    fun configure(direction: CountDirection, durationMs: Long)
    fun start()
    fun pause()
    fun resume()
    fun stop()
    fun reset()
    fun close()
}