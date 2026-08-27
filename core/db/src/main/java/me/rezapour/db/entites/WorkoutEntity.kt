package me.rezapour.db.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_table")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "work_seconds") val workSeconds: Long,
    @ColumnInfo(name = "rest_seconds") val restSeconds: Long,
    @ColumnInfo(name = "rounds") val rounds:Int
)
