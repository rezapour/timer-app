package me.rezapour.timer_core.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.rezapour.timer_core.CoreTimer
import me.rezapour.timer_core.model.CoreTimerSnapshot
import me.rezapour.timer_core.model.CoreTimerStatus
import me.rezapour.timer_core.model.CountDirection
import kotlin.time.TimeSource


class DefaultCoreTimer(
    private val scope: CoroutineScope,
    private val tickMs: Long = 1000L
) : CoreTimer {

    private val _status = MutableStateFlow<CoreTimerStatus>(CoreTimerStatus.Idle)
    override val status: StateFlow<CoreTimerStatus> = _status.asStateFlow()

    private val _snapshot = MutableStateFlow<CoreTimerSnapshot?>(null)
    override val snapshot: StateFlow<CoreTimerSnapshot?> = _snapshot.asStateFlow()

    // config
    private var direction: CountDirection = CountDirection.DOWN
    private var durationMs: Long = 0L

    // time tracking
    private var elapsedMs: Long = 0L

    // monotonic clock (safe vs system time changes)
    private val timeSource = TimeSource.Monotonic
    private var lastMark = timeSource.markNow()

    private var job: Job? = null

    override fun configure(direction: CountDirection, durationMs: Long) {
        require(durationMs > 0) { "durationMs must be > 0" }

        stopJob()

        this.direction = direction
        this.durationMs = durationMs
        this.elapsedMs = 0L
        this.lastMark = timeSource.markNow()

        _status.value = CoreTimerStatus.Idle
        _snapshot.value = CoreTimerSnapshot(
            direction = direction,
            durationMs = durationMs,
            elapsedMs = 0L,
            remainingMs = durationMs,
            isCounting = false
        )
    }

    override fun start() {
        val snap = _snapshot.value ?: return // not configured
        if (_status.value == CoreTimerStatus.Running) return
        if (_status.value == CoreTimerStatus.Finished) reset()

        _status.value = CoreTimerStatus.Running
        _snapshot.value = snap.copy(isCounting = true)

        lastMark = timeSource.markNow()
        startJob()
    }

    override fun pause() {
        if (_status.value != CoreTimerStatus.Running) return
        stopJob()
        _status.value = CoreTimerStatus.Paused
        _snapshot.value = _snapshot.value?.copy(isCounting = false)
    }

    override fun resume() {
        if (_status.value != CoreTimerStatus.Paused) return
        _status.value = CoreTimerStatus.Running
        _snapshot.value = _snapshot.value?.copy(isCounting = true)

        lastMark = timeSource.markNow()
        startJob()
    }

    override fun stop() {
        if (_snapshot.value == null) return // not configured
        stopJob()
        _status.value = CoreTimerStatus.Stopped
        _snapshot.value = _snapshot.value?.copy(isCounting = false)
    }

    override fun reset() {
        val snap = _snapshot.value ?: return // not configured
        stopJob()

        elapsedMs = 0L
        lastMark = timeSource.markNow()

        _status.value = CoreTimerStatus.Idle
        _snapshot.value = snap.copy(
            elapsedMs = 0L,
            remainingMs = durationMs,
            isCounting = false
        )
    }

    override fun close() {
        stopJob()
        // Don't cancel scope unless you created a dedicated scope for this timer.
    }

    private fun startJob() {
        stopJob()
        job = scope.launch {
            while (isActive && _status.value == CoreTimerStatus.Running) {
                delay(tickMs)
                tickOnce()
            }
        }
    }

    private fun tickOnce() {
        val snap = _snapshot.value ?: return

        val deltaMs = lastMark.elapsedNow().inWholeMilliseconds.coerceAtLeast(0L)
        lastMark = timeSource.markNow()

        // accumulate elapsed, clamp to duration
        elapsedMs = (elapsedMs + deltaMs).coerceAtMost(durationMs)

        val remaining = when (direction) {
            CountDirection.DOWN -> (durationMs - elapsedMs).coerceAtLeast(0L)
            CountDirection.UP -> (durationMs - elapsedMs).coerceAtLeast(0L) // "time left to target"
        }

        val newSnap = snap.copy(
            elapsedMs = elapsedMs,
            remainingMs = remaining,
            isCounting = true
        )
        _snapshot.value = newSnap

        // reached target => finished
        if (elapsedMs >= durationMs) {
            stopJob()
            _status.value = CoreTimerStatus.Finished
            _snapshot.value = newSnap.copy(isCounting = false)
        }
    }

    private fun stopJob() {
        job?.cancel()
        job = null
    }
}