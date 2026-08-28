package me.rezapour.workout.presentation.my_workouts.model

data class WorkoutItem(
    val id: Long = 0,
    val name: String?,
    val workSeconds: Long,
    val restSeconds: Long,
    val rounds: Int
) {
    val totalSeconds: Long
        get() = workSeconds * rounds + restSeconds * (rounds - 1)
}
