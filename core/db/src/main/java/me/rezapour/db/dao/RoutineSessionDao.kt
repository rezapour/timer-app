package me.rezapour.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import me.rezapour.db.entites.RoutineSessionEntity

@Dao
interface RoutineSessionDao {

    @Insert
    suspend fun insertActiveTimer(activeTimer: RoutineSessionEntity): Long

    @Update
    suspend fun updateActiveTimer(activeTimer: RoutineSessionEntity)
}