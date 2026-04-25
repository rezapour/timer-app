package me.rezapour.timer_list.di

import me.rezapour.domain.model.Routine
import me.rezapour.timer_list.mapper.TimerItemMapper
import me.rezapour.timer_list.model.TimerItem
import me.rezapour.timer_list.viewmodel.TimerListViewModel
import me.rezapour.ui.mapper.Mapper
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val routineListModule = module {
    viewModelOf(::TimerListViewModel)
    single<Mapper<Routine, TimerItem>> { TimerItemMapper() }
}

object TimerListModule{
    val modules :List<Module> =listOf(routineListModule)
}