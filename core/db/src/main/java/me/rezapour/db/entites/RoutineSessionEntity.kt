package me.rezapour.db.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "routine_session_table")
class RoutineSessionEntity(
    @PrimaryKey(autoGenerate = true) val sessionId: Long = 0,
    val timerId: Long,
    val currentRound: Int,
    val currentPhase: TimerPhaseEntity,
    val status: RunTimerStatusEntity,
    val phaseEndAt: Date,
    val pausedRemainingMs: Long,
    val startedAt: Date
)

enum class TimerPhaseEntity {
    INTERVAL,
    REST
}

enum class RunTimerStatusEntity {
    PAUSE,
    RUNNING
}

