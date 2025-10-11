package me.rezapour.common.dispatcher.di

import me.rezapour.common.dispatcher.CoroutineDispatcherProvider
import me.rezapour.common.dispatcher.MainCoroutineDispatcher
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module




val coroutineModule = module{
    singleOf(::MainCoroutineDispatcher) bind CoroutineDispatcherProvider::class
}