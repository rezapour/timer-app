package me.rezapour.data.di

import me.rezapour.data.mapper.Mapper
import me.rezapour.data.mapper.TimerDbMapper
import me.rezapour.data.repository.TimerRepositoryImpl
import me.rezapour.db.entites.TimerDbEntity
import me.rezapour.domain.model.Timer
import me.rezapour.domain.repository.TimerRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::TimerRepositoryImpl) bind TimerRepository::class
    single<Mapper<TimerDbEntity, Timer>> { TimerDbMapper() }
}

object DataModule {
    val modules: List<Module> = listOf(dataModule)
}