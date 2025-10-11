package me.rezapour.domain.di

import me.rezapour.domain.usecase.GetTimersUseCase
import me.rezapour.domain.usecase.InsertTimerUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetTimersUseCase)
    singleOf(::InsertTimerUseCase)
}

object DomainModule {
    val modules: List<Module> = listOf(useCaseModule)
}