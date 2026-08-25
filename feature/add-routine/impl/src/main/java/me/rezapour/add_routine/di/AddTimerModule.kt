package me.rezapour.add_routine.di

import me.rezapour.add_routine.presentation.viewmodel.AddRoutineViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val addTimerModule = module {
    viewModelOf(::AddRoutineViewModel)
}

object AddTimerModule{
    val modules:List<Module> = listOf(addTimerModule)
}