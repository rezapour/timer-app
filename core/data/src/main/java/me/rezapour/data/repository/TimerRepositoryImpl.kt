package me.rezapour.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.rezapour.common.dispatcher.CoroutineDispatcherProvider
import me.rezapour.data.mapper.Mapper
import me.rezapour.db.dao.TimerDao
import me.rezapour.db.entites.TimerEntity
import me.rezapour.domain.model.Timer
import me.rezapour.domain.repository.TimerRepository

class TimerRepositoryImpl(
    private val dao: TimerDao,
    private val dbMapper: Mapper<TimerEntity, Timer>,
    private val dispatcher: CoroutineDispatcherProvider
) : TimerRepository {
    override suspend fun insertTimer(timer: Timer) = withContext(dispatcher.io) {
        dao.insertTimer(dbMapper.mapDomainToEntity(timer))
    }

    override fun getTimers(): Flow<List<Timer>> {
        return dao.getTimers()
            .map { dbMapper.mapEntityToDomain(it) }
            .flowOn(dispatcher.io)
    }

    override suspend fun deleteTimer(id: Long) = withContext(dispatcher.io) {
        dao.deleteTimer(id)
    }
}