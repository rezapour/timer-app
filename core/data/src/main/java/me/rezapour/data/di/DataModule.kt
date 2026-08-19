package me.rezapour.data.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.rezapour.data.controller.TimerEngineController
import me.rezapour.data.mapper.Mapper
import me.rezapour.data.mapper.RoutineDbMapper
import me.rezapour.data.repository.RoutineSessionRepositoryImpl
import me.rezapour.data.repository.RoutineRepositoryImpl
import me.rezapour.db.entites.RoutineEntity
import me.rezapour.domain.controller.TimerEngine
import me.rezapour.domain.model.Routine
import me.rezapour.domain.repository.RoutineSessionRepository
import me.rezapour.domain.repository.RoutineRepository
import me.rezapour.timer_core.CoreTimer
import me.rezapour.timer_core.impl.DefaultCoreTimer
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::RoutineRepositoryImpl) bind RoutineRepository::class
    single<Mapper<RoutineEntity, Routine>> { RoutineDbMapper() }

    single<CoreTimer> {
        DefaultCoreTimer(scope = get(named("TimerScope")))
    }

    single<TimerEngine> {
        TimerEngineController(coreTimer = get(), scope = get(named("TimerScope")))
    }

    singleOf(::RoutineSessionRepositoryImpl) bind RoutineSessionRepository::class

    single<CoroutineScope>(named("TimerScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

}

object DataModule {
    val modules: List<Module> = listOf(dataModule)
}