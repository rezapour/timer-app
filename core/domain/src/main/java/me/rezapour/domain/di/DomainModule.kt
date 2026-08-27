package me.rezapour.domain.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.rezapour.domain.coordinator.WorkoutCoordinator
import me.rezapour.domain.usecase.DeleteWorkoutUseCase
import me.rezapour.domain.usecase.GetWorkoutsUseCase
import me.rezapour.domain.usecase.InsertWorkoutUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetWorkoutsUseCase)
    singleOf(::InsertWorkoutUseCase)
    singleOf(::DeleteWorkoutUseCase)
    single {
        WorkoutCoordinator(
            workoutRepository = get(),
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
