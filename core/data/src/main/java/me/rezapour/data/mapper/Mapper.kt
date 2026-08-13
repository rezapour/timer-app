package me.rezapour.data.mapper

interface Mapper<Entity, Domain> {

    fun mapEntityToDomain(entity: Entity): Domain

    fun mapEntityToDomain(entities: List<Entity>): List<Domain>

    fun mapDomainToEntity(domain: Domain): Entity

    fun mapDomainToEntity(domains: List<Domain>): List<Entity>
}