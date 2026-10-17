package me.rezapour.db.entites

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithIntervals(
    @Embedded
    val workout: WorkoutEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val intervals: List<IntervalEntity>
)