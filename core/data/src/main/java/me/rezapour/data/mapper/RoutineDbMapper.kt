package me.rezapour.data.mapper

import me.rezapour.db.entites.RoutineEntity
import me.rezapour.domain.model.Routine

class RoutineDbMapper : Mapper<RoutineEntity, Routine> {
    override fun mapEntityToDomain(entity: RoutineEntity): Routine = Routine(
        id = entity.id,
        name = entity.name,
        workSeconds = entity.workSeconds,
        restSeconds = entity.restSeconds,
        rounds = entity.rounds

    )

    override fun mapEntityToDomain(entities: List<RoutineEntity>): List<Routine> =
        entities.map { mapEntityToDomain(it) }


    override fun mapDomainToEntity(domain: Routine): RoutineEntity = RoutineEntity(
        id = domain.id,
        name = domain.name,
        workSeconds = domain.workSeconds,
        restSeconds = domain.restSeconds,
        rounds = domain.rounds

    )

    override fun mapDomainToEntity(domains: List<Routine>): List<RoutineEntity> =
        domains.map { mapDomainToEntity(it) }

}