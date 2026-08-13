package me.rezapour.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.rezapour.db.entites.TimerEntity

@Dao
interface TimerDao {

    @Insert()
    suspend fun insertTimer(timer: TimerEntity)

    @Query("SELECT * FROM timer_table")
    fun getTimers(): Flow<List<TimerEntity>>

    @Query("DELETE FROM timer_table WHERE id= :id")
    fun deleteTimer(id:Long)

}