package me.rezapour.add_routine.di

import me.rezapour.add_routine.presentation.add_routine.viewmodel.AddRoutineViewModel
import me.rezapour.add_routine.presentation.routine_list.mapper.TimerItemMapper
import me.rezapour.add_routine.presentation.routine_list.model.TimerItem
import me.rezapour.add_routine.presentation.routine_list.viewmodel.RoutineListViewModel
import me.rezapour.domain.model.Routine
import me.rezapour.ui.mapper.Mapper
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val addRoutineModule = module {
    viewModelOf(::AddRoutineViewModel)
}

private val routineListModule = module {
    viewModelOf(::RoutineListViewModel)
    single<Mapper<Routine, TimerItem>> { TimerItemMapper() }
}

object AddTimerModule {
    val modules: List<Module> = listOf(addRoutineModule, routineListModule)
}