package me.rezapour.domain.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.rezapour.domain.coordinator.WorkoutCoordinator
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
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

object DomainModule {
    val modules: List<Module> = listOf(useCaseModule)
}
