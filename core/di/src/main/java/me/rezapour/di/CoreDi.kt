package me.rezapour.di

import me.rezapour.common.di.CommonModule
import me.rezapour.db.di.DbModule
import me.rezapour.domain.di.DomainModule
import org.koin.core.module.Module

object CoreDi {
    val modules: List<Module> =
        DbModule.modules + CommonModule.modules + DomainModule.modules
}