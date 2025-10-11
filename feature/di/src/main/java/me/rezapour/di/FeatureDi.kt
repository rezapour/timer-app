package me.rezapour.di

import me.rezapour.add_timer.di.AddTimerModule
import org.koin.core.module.Module

object FeatureDi {
    val modules: List<Module> = AddTimerModule.modules
}