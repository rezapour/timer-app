package me.rezapour.add_routine.presentation.routine_list.model

data class TimerItem(
    val id: Long = 0,
    val name: String?,
    val workSeconds: Long,
    val restSeconds: Long,
    val rounds: Int
) {
    val totalSeconds: Long
        get() = workSeconds * rounds + restSeconds * (rounds - 1)
}