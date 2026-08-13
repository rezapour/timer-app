package me.rezapour.domain.repository

import kotlinx.coroutines.flow.Flow
import me.rezapour.domain.model.Timer

interface TimerRepository {

    suspend fun insertTimer(timer: Timer)

    fun getTimers(): Flow<List<Timer>>

    suspend fun deleteTimer(id:Long)

}