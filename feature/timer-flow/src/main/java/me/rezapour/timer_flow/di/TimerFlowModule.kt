package me.rezapour.timer_flow.di

import me.rezapour.timer_flow.viewmodel.TimerFlowViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val timerFlowModule = module {
    viewModelOf(::TimerFlowViewModel)
}

object TimerFlowModule {
    val modules: List<Module> = listOf(timerFlowModule)
}