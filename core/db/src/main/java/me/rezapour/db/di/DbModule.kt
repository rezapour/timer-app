package me.rezapour.db.di

import me.rezapour.db.AppDataBase
import me.rezapour.db.dao.TimerDao
import org.koin.core.module.Module
import org.koin.dsl.module

val dbModule = module {
    single<AppDataBase> { AppDataBase.databaseBuilder(get()) }
    single<TimerDao> { get<AppDataBase>().timerDao() }
}

object DbModule {
    val modules: List<Module> = listOf(dbModule)
}