package me.rezapour.db.entites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "interval_table",
    foreignKeys = [ForeignKey(
        entity = WorkoutEntity::class,
        parentColumns = ["id"],
        childColumns = ["workoutId"],
        onDelete = ForeignKey.CASCADE
    )
    ],
    indices = [Index(value = ["workoutId"])]
)
data class IntervalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val workoutId: Long,
    val name: String? = null,
    val durationSeconds: Long,
    val type: IntervalTypeEntity,
    val position:Int
)

enum class IntervalTypeEntity {
    WORK,
    REST
}