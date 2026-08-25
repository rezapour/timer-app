package me.rezapour.domain.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.rezapour.domain.coordinator.RoutineCoordinator
import me.rezapour.domain.usecase.DeleteTimerUseCase
import me.rezapour.domain.usecase.GetTimersUseCase
import me.rezapour.domain.usecase.InsertRoutineUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetTimersUseCase)
    singleOf(::InsertRoutineUseCase)
    singleOf(::DeleteTimerUseCase)
    single {
        RoutineCoordinator(
            routineRepository = get(),
            routineSessionRepository = get(),
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