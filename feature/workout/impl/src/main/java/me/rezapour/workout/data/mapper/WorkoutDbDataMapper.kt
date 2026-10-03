package me.rezapour.workout.data.mapper

import me.rezapour.db.entites.WorkoutEntity
import me.rezapour.workout.api.domain.model.Workout

class WorkoutDbDataMapper : DataMapper<WorkoutEntity, Workout> {
    override fun mapEntityToDomain(entity: WorkoutEntity): Workout = Workout(
        id = entity.id,
        name = entity.name,
        workSeconds = entity.workSeconds,
        restSeconds = entity.restSeconds,
        rounds = entity.rounds

    )

    override fun mapEntityToDomain(entities: List<WorkoutEntity>): List<Workout> =
        entities.map { mapEntityToDomain(it) }


    override fun mapDomainToEntity(domain: Workout): WorkoutEntity = WorkoutEntity(
        id = domain.id,
        name = domain.name,
        workSeconds = domain.workSeconds,
        restSeconds = domain.restSeconds,
        rounds = domain.rounds

    )

    override fun mapDomainToEntity(domains: List<Workout>): List<WorkoutEntity> =
        domains.map { mapDomainToEntity(it) }

}
