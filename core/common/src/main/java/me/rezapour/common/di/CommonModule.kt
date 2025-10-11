package me.rezapour.common.di

import me.rezapour.common.dispatcher.di.coroutineModule
import org.koin.core.module.Module

object CommonModule{
    val modules:List<Module> = listOf(coroutineModule)
}