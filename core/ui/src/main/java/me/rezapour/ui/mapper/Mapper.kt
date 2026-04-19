package me.rezapour.ui.mapper

interface Mapper<Domain, UI> {

    fun mapDomainToUIModel(domain: Domain): UI

    fun mapDomainToUIModel(domains: List<Domain>): List<UI>

    fun mapUIModelToDomain(item: UI): Domain

    fun mapUIModelToDomain(item: List<UI>): List<Domain>
}