package me.rezapour.workout.data.mapper

interface DataMapper<Entity, Domain> {

    fun mapEntityToDomain(entity: Entity): Domain

    fun mapEntityToDomain(entities: List<Entity>): List<Domain>

    fun mapDomainToEntity(domain: Domain): Entity

    fun mapDomainToEntity(domains: List<Domain>): List<Entity>
}