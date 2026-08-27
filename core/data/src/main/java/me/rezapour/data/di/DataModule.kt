package me.rezapour.data.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.rezapour.data.controller.TimerEngineController
import me.rezapour.data.mapper.Mapper
import me.rezapour.data.mapper.WorkoutDbMapper
import me.rezapour.data.repository.WorkoutSessionRepositoryImpl
import me.rezapour.data.repository.WorkoutRepositoryImpl
import me.rezapour.db.entites.WorkoutEntity
import me.rezapour.domain.controller.TimerEngine
import me.rezapour.domain.model.Workout
import me.rezapour.domain.repository.WorkoutSessionRepository
import me.rezapour.domain.repository.WorkoutRepository
import me.rezapour.timer_core.CoreTimer
import me.rezapour.timer_core.impl.DefaultCoreTimer
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::WorkoutRepositoryImpl) bind WorkoutRepository::class
    single<Mapper<WorkoutEntity, Workout>> { WorkoutDbMapper() }

    single<CoreTimer> {
        DefaultCoreTimer(scope = get(named("TimerScope")))
    }

    single<TimerEngine> {
        TimerEngineController(coreTimer = get(), scope = get(named("TimerScope")))
    }

    singleOf(::WorkoutSessionRepositoryImpl) bind WorkoutSessionRepository::class

    single<CoroutineScope>(named("TimerScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

}

object DataModule {
    val modules: List<Module> = listOf(dataModule)
}
