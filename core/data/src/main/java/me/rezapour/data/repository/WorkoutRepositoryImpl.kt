package me.rezapour.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.rezapour.common.dispatcher.CoroutineDispatcherProvider
import me.rezapour.data.mapper.Mapper
import me.rezapour.db.dao.WorkoutDao
import me.rezapour.db.entites.WorkoutEntity
import me.rezapour.domain.model.Workout
import me.rezapour.domain.repository.WorkoutRepository

class WorkoutRepositoryImpl(
    private val dao: WorkoutDao,
    private val dbMapper: Mapper<WorkoutEntity, Workout>,
    private val dispatcher: CoroutineDispatcherProvider
) : WorkoutRepository {
    override suspend fun insertWorkout(workout: Workout) = withContext(dispatcher.io) {
        return@withContext dao.insertWorkout(dbMapper.mapDomainToEntity(workout))
    }

    override fun getWorkouts(): Flow<List<Workout>> {
        return dao.getWorkouts()
            .map { dbMapper.mapEntityToDomain(it) }
            .flowOn(dispatcher.io)
    }

    override suspend fun deleteWorkout(id: Long) = withContext(dispatcher.io) {
        dao.deleteWorkout(id)
    }
}
