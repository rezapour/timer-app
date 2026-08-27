package me.rezapour.db.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "workout_session_table")
class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val sessionId: Long = 0,
    val workoutId: Long,
    val currentRound: Int,
    val currentPhase: WorkoutPhaseEntity,
    val status: RunWorkoutStatusEntity,
    val phaseEndAt: Date,
    val pausedRemainingMs: Long,
    val startedAt: Date
)

enum class WorkoutPhaseEntity {
    INTERVAL,
    REST
}

enum class RunWorkoutStatusEntity {
    PAUSE,
    RUNNING
}

