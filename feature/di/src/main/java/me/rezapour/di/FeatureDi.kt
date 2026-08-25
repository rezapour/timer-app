package me.rezapour.di

import me.rezapour.add_routine.di.AddTimerModule
import me.rezapour.timer_flow.di.TimerFlowModule
import me.rezapour.timer_list.di.TimerListModule
import org.koin.core.module.Module

object FeatureDi {
    val modules: List<Module> =
        AddTimerModule.modules + TimerListModule.modules + TimerFlowModule.modules
}