package me.rezapour.workout.data.repository_impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.rezapour.common.dispatcher.CoroutineDispatcherProvider
import me.rezapour.db.dao.WorkoutDao
import me.rezapour.workout.api.domain.model.Workout
import me.rezapour.workout.data.mapper.WorkoutDbDataMapper
import me.rezapour.workout.domain.repository.WorkoutRepository

class WorkoutRepositoryImpl(
    private val dao: WorkoutDao,
    private val dbMapper: WorkoutDbDataMapper,
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

    override suspend fun getWorkout(workoutId: Long): Workout? = withContext(dispatcher.io) {
        return@withContext dao.getWorkout(workoutId)?.let {
            dbMapper.mapEntityToDomain(it)
        }
    }

    override suspend fun updateWorkout(workout: Workout) = withContext(dispatcher.io) {
        dao.updateWorkout(dbMapper.mapDomainToEntity(workout))
    }
}