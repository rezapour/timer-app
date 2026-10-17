package me.rezapour.workout.data.mapper

import me.rezapour.db.entites.IntervalEntity
import me.rezapour.db.entites.IntervalTypeEntity
import me.rezapour.db.entites.WorkoutEntity
import me.rezapour.workout.api.domain.model.Interval
import me.rezapour.workout.api.domain.model.IntervalType
import me.rezapour.workout.api.domain.model.Workout

class WorkoutDbDataMapper {
    fun mapEntityToDomain(
        workoutEntity: WorkoutEntity,
        intervalsEntity: List<IntervalEntity>
    ): Workout = Workout(
        id = workoutEntity.id,
        name = workoutEntity.name,
        rounds = workoutEntity.rounds,
        intervals = intervalsEntity.map {
            mapIntervalEntityToDomain(it)
        }
    )


    fun mapWorkoutDomainToEntity(domain: Workout): WorkoutEntity = WorkoutEntity(
        id = domain.id,
        name = domain.name,
        rounds = domain.rounds,
    )

    fun mapIntervalEntityToDomain(intervalEntity: IntervalEntity): Interval = Interval(
        id = intervalEntity.id,
        name = intervalEntity.name,
        durationSeconds = intervalEntity.durationSeconds,
        type = mapIntervalTypeToDomain(intervalEntity.type)
    )

    fun mapIntervalDomainToEntity(interval: Interval, workoutId: Long, index: Int): IntervalEntity =
        IntervalEntity(
            id = interval.id,
            workoutId = workoutId,
            name = interval.name,
            type = mapIntervalTypeToEntity(interval.type),
            durationSeconds = interval.durationSeconds,
            position = index,
        )

    private fun mapIntervalTypeToDomain(entity: IntervalTypeEntity): IntervalType {
        return when (entity) {
            IntervalTypeEntity.WORK -> IntervalType.WORK
            IntervalTypeEntity.REST -> IntervalType.REST
        }
    }

    private fun mapIntervalTypeToEntity(domain: IntervalType): IntervalTypeEntity {
        return when (domain) {
            IntervalType.WORK -> IntervalTypeEntity.WORK
            IntervalType.REST -> IntervalTypeEntity.REST
        }
    }
}
