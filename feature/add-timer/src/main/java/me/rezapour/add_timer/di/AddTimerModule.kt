package me.rezapour.add_timer.di

import me.rezapour.add_timer.viewmodel.AddTimerViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val addTimerModule = module {
    viewModelOf(::AddTimerViewModel)
}

object AddTimerModule{
    val modules:List<Module> = listOf(addTimerModule)
}