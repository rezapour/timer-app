package me.rezapour.db.converter

import androidx.room.TypeConverter
import me.rezapour.db.entites.RunWorkoutStatusEntity
import me.rezapour.db.entites.WorkoutPhaseEntity
import java.util.Date

class DataBaseConverter {

    @TypeConverter
    fun fromWorkoutPhase(value: WorkoutPhaseEntity): String = value.name

    @TypeConverter
    fun toWorkoutPhase(value: String): WorkoutPhaseEntity = WorkoutPhaseEntity.valueOf(value)

    @TypeConverter
    fun fromRunWorkoutStatus(value: RunWorkoutStatusEntity): String = value.name

    @TypeConverter
    fun toRunWorkoutStatus(value: String): RunWorkoutStatusEntity = RunWorkoutStatusEntity.valueOf(value)

    @TypeConverter
    fun fromDate(value: Date):Long = value.time

    @TypeConverter
    fun toDate(value:Long): Date = Date(value)
}
