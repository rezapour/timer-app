package me.rezapour.db.converter

import androidx.room.TypeConverter
import me.rezapour.db.entites.RunTimerStatusEntity
import me.rezapour.db.entites.TimerPhaseEntity
import java.util.Date

class DataBaseConverter {

    @TypeConverter
    fun fromTimerPhase(value: TimerPhaseEntity): String = value.name

    @TypeConverter
    fun toTimerPhase(value: String): TimerPhaseEntity = TimerPhaseEntity.valueOf(value)

    @TypeConverter
    fun fromRunTimerStatus(value: RunTimerStatusEntity): String = value.name

    @TypeConverter
    fun toRunTimerStatus(value: String): RunTimerStatusEntity = RunTimerStatusEntity.valueOf(value)

    @TypeConverter
    fun fromDate(value: Date):Long = value.time

    @TypeConverter
    fun toDate(value:Long): Date = Date(value)
}