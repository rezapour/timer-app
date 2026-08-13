package me.rezapour.data.mapper

import me.rezapour.db.entites.TimerEntity
import me.rezapour.domain.model.Timer

class TimerDbMapper : Mapper<TimerEntity, Timer> {
    override fun mapEntityToDomain(entity: TimerEntity): Timer = Timer(
        id = entity.id,
        name = entity.name,
        workSeconds = entity.workSeconds,
        restSeconds = entity.restSeconds,
        rounds = entity.rounds

    )

    override fun mapEntityToDomain(entities: List<TimerEntity>): List<Timer> =
        entities.map { mapEntityToDomain(it) }


    override fun mapDomainToEntity(domain: Timer): TimerEntity = TimerEntity(
        id = domain.id,
        name = domain.name,
        workSeconds = domain.workSeconds,
        restSeconds = domain.restSeconds,
        rounds = domain.rounds

    )

    override fun mapDomainToEntity(domains: List<Timer>): List<TimerEntity> =
        domains.map { mapDomainToEntity(it) }

}