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
        return@withContext dao.insertWorkoutWithIntervals(
            workout = dbMapper.mapWorkoutDomainToEntity(workout),

            intervals = workout.intervals.mapIndexed { index, interval ->
                dbMapper.mapIntervalDomainToEntity(
                    interval = interval,
                    workoutId = workout.id,
                    index = index
                )
            }
        )
    }

    override fun getWorkouts(): Flow<List<Workout>> {
        return dao.getWorkoutsWithIntervals()
            .map { workouts ->
                workouts.map {
                    dbMapper.mapEntityToDomain(
                        workoutEntity = it.workout,
                        intervalsEntity = it.intervals.sortedBy { entity -> entity.position })
                }
            }
            .flowOn(dispatcher.io)
    }

    override suspend fun deleteWorkout(id: Long) = withContext(dispatcher.io) {
        dao.deleteWorkout(id)
    }

    override suspend fun getWorkout(workoutId: Long): Workout? = withContext(dispatcher.io) {
        return@withContext dao.getWorkout(workoutId)?.let {
            dbMapper.mapEntityToDomain(
                workoutEntity = it.workout,
                intervalsEntity = it.intervals.sortedBy { entity -> entity.position })
        }
    }

    override suspend fun updateWorkout(workout: Workout) = withContext(dispatcher.io) {
        dao.updateWorkoutWithIntervals(
            workout = dbMapper.mapWorkoutDomainToEntity(workout),
            intervals = workout.intervals.mapIndexed { index, interval ->
                dbMapper.mapIntervalDomainToEntity(
                    interval = interval,
                    workoutId = workout.id,
                    index = index
                )
            })
    }
}