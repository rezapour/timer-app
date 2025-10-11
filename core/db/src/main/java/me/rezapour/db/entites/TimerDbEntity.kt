package me.rezapour.db.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timer_table")
data class TimerDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "work_seconds") val workSeconds: Long,
    @ColumnInfo(name = "rest_seconds") val restSeconds: Long,
    @ColumnInfo(name = "rounds") val rounds:Int
)