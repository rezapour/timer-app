package me.rezapour.di

import me.rezapour.add_routine.di.AddTimerModule
import me.rezapour.timer_flow.di.TimerFlowModule
import org.koin.core.module.Module

object FeatureDi {
    val modules: List<Module> =
        AddTimerModule.modules  + TimerFlowModule.modules
}