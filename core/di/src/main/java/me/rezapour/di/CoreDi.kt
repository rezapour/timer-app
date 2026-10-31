package me.rezapour.di

import me.rezapour.common.di.CommonModule
import me.rezapour.db.di.DbModule
import org.koin.core.module.Module

object CoreDi {
    val modules: List<Module> =
        DbModule.modules + CommonModule.modules
}