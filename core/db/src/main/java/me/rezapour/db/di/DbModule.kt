package me.rezapour.db.di

import me.rezapour.db.AppDataBase
import me.rezapour.db.dao.WorkoutDao
import me.rezapour.db.dao.WorkoutSessionDao
import org.koin.core.module.Module
import org.koin.dsl.module

val dbModule = module {
    single<AppDataBase> { AppDataBase.databaseBuilder(get()) }
    single<WorkoutDao> { get<AppDataBase>().workoutDao() }
    single<WorkoutSessionDao> { get<AppDataBase>().workoutSessionDao() }
}

object DbModule {
    val modules: List<Module> = listOf(dbModule)
}
