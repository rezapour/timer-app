package me.rezapour.domain.repository

import me.rezapour.domain.model.RoutineSession

interface RoutineSessionRepository {
    suspend fun insertRoutineSession(routineSession: RoutineSession):Long

    suspend fun updateRoutineSession(routineSession: RoutineSession)

}