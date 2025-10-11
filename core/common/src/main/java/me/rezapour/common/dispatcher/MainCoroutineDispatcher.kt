package me.rezapour.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MainCoroutineDispatcher : CoroutineDispatcherProvider {
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val default: CoroutineDispatcher = Dispatchers.Default
}