package me.rezapour.timer.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.rezapour.timer.data.controller.TimerEngineController
import me.rezapour.timer.data.repository.WorkoutSessionRepositoryImpl
import me.rezapour.timer.domain.controller.TimerEngine
import me.rezapour.timer.domain.coordinator.WorkoutCoordinator
import me.rezapour.timer.domain.repository.WorkoutSessionRepository
import me.rezapour.timer.presentaion.viewmodel.TimerFlowViewModel
import me.rezapour.timer_core.CoreTimer
import me.rezapour.timer_core.impl.DefaultCoreTimer
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

private val presentationModule = module {
    viewModelOf(::TimerFlowViewModel)
}

val dataModule = module {
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

val domainModule = module {
    single {
        WorkoutCoordinator(
            insertWorkoutUseCase = get(),
            workoutSessionRepository = get(),
            timerEngine = get(),
            scope = get(
                named("TimerCoordinatorScope")
            )
        )
    }
    single<CoroutineScope>(named("TimerCoordinatorScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
}

object TimerModule {
    val modules: List<Module> = listOf(presentationModule, dataModule, domainModule)
}