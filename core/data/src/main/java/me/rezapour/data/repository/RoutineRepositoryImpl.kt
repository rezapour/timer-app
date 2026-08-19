package me.rezapour.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.rezapour.common.dispatcher.CoroutineDispatcherProvider
import me.rezapour.data.mapper.Mapper
import me.rezapour.db.dao.RoutineDao
import me.rezapour.db.entites.RoutineEntity
import me.rezapour.domain.model.Routine
import me.rezapour.domain.repository.RoutineRepository

class RoutineRepositoryImpl(
    private val dao: RoutineDao,
    private val dbMapper: Mapper<RoutineEntity, Routine>,
    private val dispatcher: CoroutineDispatcherProvider
) : RoutineRepository {
    override suspend fun insertRoutine(routine: Routine) = withContext(dispatcher.io) {
        return@withContext dao.insertTimer(dbMapper.mapDomainToEntity(routine))
    }

    override fun getRoutines(): Flow<List<Routine>> {
        return dao.getTimers()
            .map { dbMapper.mapEntityToDomain(it) }
            .flowOn(dispatcher.io)
    }

    override suspend fun deleteRoutine(id: Long) = withContext(dispatcher.io) {
        dao.deleteTimer(id)
    }
}