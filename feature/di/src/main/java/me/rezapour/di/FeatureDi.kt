package me.rezapour.di

import me.rezapour.workout.di.AddWorkoutModule
import me.rezapour.timer.di.TimerModule
import org.koin.core.module.Module

object FeatureDi {
    val modules: List<Module> =
        AddWorkoutModule.modules  + TimerModule.modules
}
