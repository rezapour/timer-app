package me.rezapour.domain.repository

import kotlinx.coroutines.flow.Flow
import me.rezapour.domain.model.Routine

interface RoutineRepository {

    suspend fun insertRoutine(routine: Routine):Long

    fun getRoutines(): Flow<List<Routine>>

    suspend fun deleteRoutine(id:Long)

}