package me.rezapour.timer.data.repository

import me.rezapour.db.dao.WorkoutSessionDao
import me.rezapour.db.entites.WorkoutSessionEntity
import me.rezapour.db.entites.RunWorkoutStatusEntity
import me.rezapour.db.entites.WorkoutPhaseEntity
import me.rezapour.timer.domain.controller.TimerStatus
import me.rezapour.timer.domain.model.WorkoutPhase
import me.rezapour.timer.domain.model.WorkoutSession
import me.rezapour.timer.domain.repository.WorkoutSessionRepository

class WorkoutSessionRepositoryImpl(private val dao: WorkoutSessionDao) : WorkoutSessionRepository {
    override suspend fun insertWorkoutSession(workoutSession: WorkoutSession): Long {
        val entity = WorkoutSessionEntity(
            workoutId = workoutSession.workoutId,
            currentRound = workoutSession.currentRound,
            currentPhase = when (workoutSession.currentPhase) {
                WorkoutPhase.INTERVAL -> WorkoutPhaseEntity.INTERVAL
                WorkoutPhase.REST -> WorkoutPhaseEntity.REST
            },
            status = when (workoutSession.status) {
                TimerStatus.Running -> RunWorkoutStatusEntity.RUNNING
                else -> RunWorkoutStatusEntity.PAUSE
            },
            phaseEndAt = workoutSession.phaseEndAt,
            pausedRemainingMs = workoutSession.pausedRemainingMs,
            startedAt = workoutSession.startedAt
        )
        return dao.insertActiveWorkout(entity)
    }

    override suspend fun updateWorkoutSession(workoutSession: WorkoutSession) {
        val entity = WorkoutSessionEntity(
            sessionId = workoutSession.sessionId,
            workoutId = workoutSession.workoutId,
            currentRound = workoutSession.currentRound,
            currentPhase = when (workoutSession.currentPhase) {
                WorkoutPhase.INTERVAL -> WorkoutPhaseEntity.INTERVAL
                WorkoutPhase.REST -> WorkoutPhaseEntity.REST
            },
            status = when (workoutSession.status) {
                TimerStatus.Running -> RunWorkoutStatusEntity.RUNNING
                else -> RunWorkoutStatusEntity.PAUSE
            },
            phaseEndAt = workoutSession.phaseEndAt,
            pausedRemainingMs = workoutSession.pausedRemainingMs,
            startedAt = workoutSession.startedAt
        )
        dao.updateActiveWorkout(entity)
    }
}
