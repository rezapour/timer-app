package me.rezapour.domain.coordinator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.rezapour.domain.controller.TimerEngine
import me.rezapour.domain.controller.TimerSnapshot
import me.rezapour.domain.controller.TimerStatus
import me.rezapour.domain.model.Routine
import me.rezapour.domain.model.RoutinePhase
import me.rezapour.domain.model.RoutineSession
import me.rezapour.domain.repository.RoutineRepository
import me.rezapour.domain.repository.RoutineSessionRepository
import java.util.Date

class RoutineCoordinator(
    private val routineRepository: RoutineRepository,
    private val routineSessionRepository: RoutineSessionRepository,
    private val timerEngine: TimerEngine,
    private val scope: CoroutineScope
) {


    private var routine: Routine? = null

    val timeTicker: StateFlow<TimerSnapshot?> = timerEngine.snapshot

    private val _routineState: MutableStateFlow<RoutineSession?> = MutableStateFlow(null)
    val routineState: StateFlow<RoutineSession?> = _routineState.asStateFlow()


    init {
        scope.launch {
            timerEngine.status.collect { status ->
                if (status is TimerStatus.Finished)
                    checkNextTimer()

            }
        }
    }

    suspend fun start(routine: Routine, saveTimer: Boolean = false) {
        val timerToWorkout = if (saveTimer) {
            val id = routineRepository.insertRoutine(routine)
            routine.copy(id = id)
        } else {
            routine
        }
        val newRoutineSession = RoutineSession(
            routineId = timerToWorkout.id,
            currentPhase = RoutinePhase.INTERVAL,
            currentRound = 1,
            status = TimerStatus.Running,
            phaseEndAt = Date(Date().time + timerToWorkout.workMilliSecond),
            pausedRemainingMs = 0,
            startedAt = Date()
        )


        val id = routineSessionRepository.insertRoutineSession(newRoutineSession)
        this@RoutineCoordinator.routine = timerToWorkout
        _routineState.value = newRoutineSession.copy(sessionId = id)

        startNewTimer(timerToWorkout.workMilliSecond)

    }

    private fun startNewTimer(durationMs: Long) {
        timerEngine.configure(durationMs)
        timerEngine.start()
    }

    private suspend fun checkNextTimer() {
        val active = _routineState.value ?: error("Routine does not exist")
        val timer = routine ?: error("Timer is missing")

        when (active.currentPhase) {
            RoutinePhase.INTERVAL -> {
                if (active.currentRound == timer.rounds) {
                    timerFinished()
                } else {
                    startRest(active, timer)
                }
            }

            RoutinePhase.REST -> {
                startInterval(active, timer, active.currentRound + 1)
            }
        }
    }

    private suspend fun startInterval(session: RoutineSession, routine: Routine, round: Int) {
        val sessionState = session.copy(
            currentPhase = RoutinePhase.INTERVAL,
            currentRound = round,
            phaseEndAt = Date(Date().time + routine.workMilliSecond),
            pausedRemainingMs = 0,
        )
        updateSession(sessionState)
        startNewTimer(routine.workMilliSecond)
    }

    private suspend fun startRest(session: RoutineSession, routine: Routine) {
        val sessionState = session.copy(
            currentPhase = RoutinePhase.REST,
            phaseEndAt = Date(Date().time + routine.restMilliSecond),
            pausedRemainingMs = 0,
        )
        updateSession(sessionState)
        startNewTimer(routine.restMilliSecond)
    }

    suspend fun pauseTimer() {
        timerEngine.pause()

        val remainingMs = timeTicker.value?.remainingMs
            ?: error("Timer does not exist")

        val sessionState = routineState.value?.copy(
            status = TimerStatus.Paused,
            pausedRemainingMs = remainingMs
        ) ?: error("Session does not exist")

        updateSession(sessionState)
    }

    suspend fun resumeTimer() {
        val session = routineState.value
            ?: error("Session does not exist")

        val remainingMs = session.pausedRemainingMs

        val resumedSession = session.copy(
            status = TimerStatus.Running,
            phaseEndAt = Date(System.currentTimeMillis() + remainingMs),
            pausedRemainingMs = 0
        )

        updateSession(resumedSession)
        timerEngine.resume()
    }

    suspend fun stopTimer() {
        val sessionState = routineState.value?.copy(
            status = TimerStatus.Stopped
        ) ?: error("Session does not exist")
        updateSession(sessionState)
        timerEngine.stop()
        routine = null
    }

    private suspend fun updateSession(session: RoutineSession) {
        routineSessionRepository.updateRoutineSession(session)
        _routineState.value = session
    }

    private suspend fun timerFinished() {
        val sessionState = routineState.value?.copy(
            status = TimerStatus.Finished
        ) ?: error("Session does not exist")
        updateSession(sessionState)
        routine = null
    }
}