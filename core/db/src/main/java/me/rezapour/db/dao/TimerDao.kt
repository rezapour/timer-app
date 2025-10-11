package me.rezapour.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.rezapour.db.entites.TimerDbEntity

@Dao
interface TimerDao {

    @Insert()
    suspend fun insertTimer(timer: TimerDbEntity)

    @Query("SELECT * FROM timer_table")
    fun getTimers(): Flow<List<TimerDbEntity>>

}