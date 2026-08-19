package me.rezapour.designsystem.theme


import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class RoutineColors(
    val work: Color,
    val rest: Color,
    val completed: Color,
)

private val RoutineSemanticColors = RoutineColors(
    work = Work,
    rest = Rest,
    completed = Completed,
)

val ColorScheme.routine: RoutineColors
    get() = RoutineSemanticColors