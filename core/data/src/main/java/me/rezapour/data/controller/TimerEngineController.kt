package me.rezapour.data.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.rezapour.domain.controller.TimerEngine
import me.rezapour.domain.controller.TimerSnapshot
import me.rezapour.domain.controller.TimerStatus
import me.rezapour.timer_core.CoreTimer
import me.rezapour.timer_core.model.CoreTimerStatus
import me.rezapour.timer_core.model.CountDirection

class TimerEngineController(
    private val coreTimer: CoreTimer,
    private val scope: CoroutineScope
) : TimerEngine {


    override val status: StateFlow<TimerStatus> = coreTimer.status.map { timerStatus ->
        when (timerStatus) {
            CoreTimerStatus.Finished -> TimerStatus.Finished
            CoreTimerStatus.Idle -> TimerStatus.Idle
            CoreTimerStatus.Paused -> TimerStatus.Paused
            CoreTimerStatus.Running -> TimerStatus.Running
            CoreTimerStatus.Stopped -> TimerStatus.Stopped
        }
    }.distinctUntilChanged()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimerStatus.Idle
        )


    override val snapshot: StateFlow<TimerSnapshot?> = coreTimer.snapshot.map { snapshot ->
        snapshot?.let {
            TimerSnapshot(
                durationMs = snapshot.durationMs,
                elapsedMs = snapshot.elapsedMs,
                remainingMs = snapshot.remainingMs,
                isCounting = snapshot.isCounting

            )
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TimerSnapshot()
    )

    override fun configure(durationMs: Long) {
        coreTimer.configure(
            direction = CountDirection.DOWN,
            durationMs = durationMs
        )
    }

    override fun start() {
        coreTimer.start()
    }

    override fun pause() {
        coreTimer.pause()
    }

    override fun resume() {
        coreTimer.resume()
    }

    override fun stop() {
        coreTimer.stop()
    }

    override fun reset() {
        coreTimer.reset()
    }

    override fun close() {
        coreTimer.close()
    }
}