package me.rezapour.data.mapper

import me.rezapour.db.entites.TimerDbEntity
import me.rezapour.domain.model.Timer

class TimerDbMapper : Mapper<TimerDbEntity, Timer> {
    override fun mapEntityToDomain(entity: TimerDbEntity): Timer = Timer(
        id = entity.id,
        name = entity.name,
        workSeconds = entity.workSeconds,
        restSeconds = entity.restSeconds,
        rounds = entity.rounds

    )

    override fun mapEntityToDomain(entities: List<TimerDbEntity>): List<Timer> =
        entities.map { mapEntityToDomain(it) }


    override fun mapDomainToEntity(domain: Timer): TimerDbEntity = TimerDbEntity(
        id = domain.id,
        name = domain.name,
        workSeconds = domain.workSeconds,
        restSeconds = domain.restSeconds,
        rounds = domain.rounds

    )

    override fun mapDomainToEntity(domains: List<Timer>): List<TimerDbEntity> =
        domains.map { mapDomainToEntity(it) }

}