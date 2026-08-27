package me.rezapour.di

import me.rezapour.workout.di.AddWorkoutModule
import me.rezapour.timer_flow.di.TimerFlowModule
import org.koin.core.module.Module

object FeatureDi {
    val modules: List<Module> =
        AddWorkoutModule.modules  + TimerFlowModule.modules
}
