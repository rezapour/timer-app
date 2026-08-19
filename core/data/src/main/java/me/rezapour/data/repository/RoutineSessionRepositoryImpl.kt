package me.rezapour.data.repository

import me.rezapour.db.dao.RoutineSessionDao
import me.rezapour.db.entites.RoutineSessionEntity
import me.rezapour.db.entites.RunTimerStatusEntity
import me.rezapour.db.entites.TimerPhaseEntity
import me.rezapour.domain.controller.TimerStatus
import me.rezapour.domain.model.RoutinePhase
import me.rezapour.domain.model.RoutineSession
import me.rezapour.domain.repository.RoutineSessionRepository

class RoutineSessionRepositoryImpl(private val dao: RoutineSessionDao) : RoutineSessionRepository {
    override suspend fun insertRoutineSession(routineSession: RoutineSession): Long {
        val entity = RoutineSessionEntity(
            timerId = routineSession.routineId,
            currentRound = routineSession.currentRound,
            currentPhase = when (routineSession.currentPhase) {
                RoutinePhase.INTERVAL -> TimerPhaseEntity.INTERVAL
                RoutinePhase.REST -> TimerPhaseEntity.REST
            },
            status = when (routineSession.status) {
                TimerStatus.Running -> RunTimerStatusEntity.RUNNING
                else -> RunTimerStatusEntity.PAUSE
            },
            phaseEndAt = routineSession.phaseEndAt,
            pausedRemainingMs = routineSession.pausedRemainingMs,
            startedAt = routineSession.startedAt
        )
        return dao.insertActiveTimer(entity)
    }

    override suspend fun updateRoutineSession(routineSession: RoutineSession) {
        val entity = RoutineSessionEntity(
            sessionId = routineSession.sessionId,
            timerId = routineSession.routineId,
            currentRound = routineSession.currentRound,
            currentPhase = when (routineSession.currentPhase) {
                RoutinePhase.INTERVAL -> TimerPhaseEntity.INTERVAL
                RoutinePhase.REST -> TimerPhaseEntity.REST
            },
            status = when (routineSession.status) {
                TimerStatus.Running -> RunTimerStatusEntity.RUNNING
                else -> RunTimerStatusEntity.PAUSE
            },
            phaseEndAt = routineSession.phaseEndAt,
            pausedRemainingMs = routineSession.pausedRemainingMs,
            startedAt = routineSession.startedAt
        )
        dao.updateActiveTimer(entity)
    }
}