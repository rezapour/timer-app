package me.rezapour.workout.api.domain.model

data class Workout(
    val id: Long = 0,
    val name: String?,
    val rounds: Int,
    val intervals: List<Interval>
)

data class Interval(
    val id: Long = 0,
    val name: String?,
    val durationSeconds: Long,
    val type: IntervalType
) {
    val durationMilliSeconds:Long = durationSeconds * 1000
}

enum class IntervalType {
    WORK,
    REST,
}