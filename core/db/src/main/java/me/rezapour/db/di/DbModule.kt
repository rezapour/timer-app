package me.rezapour.db.di

import me.rezapour.db.AppDataBase
import me.rezapour.db.dao.RoutineDao
import me.rezapour.db.dao.RoutineSessionDao
import org.koin.core.module.Module
import org.koin.dsl.module

val dbModule = module {
    single<AppDataBase> { AppDataBase.databaseBuilder(get()) }
    single<RoutineDao> { get<AppDataBase>().RoutineDao() }
    single<RoutineSessionDao> { get<AppDataBase>().RoutineSessionDao() }
}

object DbModule {
    val modules: List<Module> = listOf(dbModule)
}