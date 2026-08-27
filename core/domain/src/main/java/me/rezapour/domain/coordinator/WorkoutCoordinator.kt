package me.rezapour.domain.coordinator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.rezapour.domain.controller.TimerEngine
import me.rezapour.domain.controller.TimerSnapshot
import me.rezapour.domain.controller.TimerStatus
import me.rezapour.domain.model.Workout
import me.rezapour.domain.model.WorkoutPhase
import me.rezapour.domain.model.WorkoutSession
import me.rezapour.domain.repository.WorkoutRepository
import me.rezapour.domain.repository.WorkoutSessionRepository
import java.util.Date

class WorkoutCoordinator(
    private val workoutRepository: WorkoutRepository,
    private val workoutSessionRepository: WorkoutSessionRepository,
    private val timerEngine: TimerEngine,
    private val scope: CoroutineScope
) {


    private var workout: Workout? = null

    val timeTicker: StateFlow<TimerSnapshot?> = timerEngine.snapshot

    private val _workoutState: MutableStateFlow<WorkoutSession?> = MutableStateFlow(null)
    val workoutState: StateFlow<WorkoutSession?> = _workoutState.asStateFlow()


    init {
        scope.launch {
            timerEngine.status.collect { status ->
                if (status is TimerStatus.Finished)
                    checkNextTimer()

            }
        }
    }

    suspend fun start(workout: Workout, saveTimer: Boolean = false) {
        val timerToWorkout = if (saveTimer) {
            val id = workoutRepository.insertWorkout(workout)
            workout.copy(id = id)
        } else {
            workout
        }
        val newWorkoutSession = WorkoutSession(
            workoutId = timerToWorkout.id,
            currentPhase = WorkoutPhase.INTERVAL,
            currentRound = 1,
            status = TimerStatus.Running,
            phaseEndAt = Date(Date().time + timerToWorkout.workMilliSecond),
            pausedRemainingMs = 0,
            startedAt = Date()
        )


        val id = workoutSessionRepository.insertWorkoutSession(newWorkoutSession)
        this@WorkoutCoordinator.workout = timerToWorkout
        _workoutState.value = newWorkoutSession.copy(sessionId = id)

        startNewTimer(timerToWorkout.workMilliSecond)

    }

    private fun startNewTimer(durationMs: Long) {
        timerEngine.configure(durationMs)
        timerEngine.start()
    }

    private suspend fun checkNextTimer() {
        val active = _workoutState.value ?: error("Workout does not exist")
        val timer = workout ?: error("Workout is missing")

        when (active.currentPhase) {
            WorkoutPhase.INTERVAL -> {
                if (active.currentRound == timer.rounds) {
                    timerFinished()
                } else {
                    startRest(active, timer)
                }
            }

            WorkoutPhase.REST -> {
                startInterval(active, timer, active.currentRound + 1)
            }
        }
    }

    private suspend fun startInterval(session: WorkoutSession, workout: Workout, round: Int) {
        val sessionState = session.copy(
            currentPhase = WorkoutPhase.INTERVAL,
            currentRound = round,
            phaseEndAt = Date(Date().time + workout.workMilliSecond),
            pausedRemainingMs = 0,
        )
        updateSession(sessionState)
        startNewTimer(workout.workMilliSecond)
    }

    private suspend fun startRest(session: WorkoutSession, workout: Workout) {
        val sessionState = session.copy(
            currentPhase = WorkoutPhase.REST,
            phaseEndAt = Date(Date().time + workout.restMilliSecond),
            pausedRemainingMs = 0,
        )
        updateSession(sessionState)
        startNewTimer(workout.restMilliSecond)
    }

    suspend fun pauseTimer() {
        timerEngine.pause()

        val remainingMs = timeTicker.value?.remainingMs
            ?: error("Timer does not exist")

        val sessionState = workoutState.value?.copy(
            status = TimerStatus.Paused,
            pausedRemainingMs = remainingMs
        ) ?: error("Session does not exist")

        updateSession(sessionState)
    }

    suspend fun resumeTimer() {
        val session = workoutState.value
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
        val sessionState = workoutState.value?.copy(
            status = TimerStatus.Stopped
        ) ?: error("Session does not exist")
        updateSession(sessionState)
        timerEngine.stop()
        workout = null
    }

    private suspend fun updateSession(session: WorkoutSession) {
        workoutSessionRepository.updateWorkoutSession(session)
        _workoutState.value = session
    }

    private suspend fun timerFinished() {
        val sessionState = workoutState.value?.copy(
            status = TimerStatus.Finished
        ) ?: error("Session does not exist")
        updateSession(sessionState)
        workout = null
    }
}
