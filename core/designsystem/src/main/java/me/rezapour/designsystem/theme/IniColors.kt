package me.rezapour.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalColors = compositionLocalOf { IniColors() }

@Immutable
class IniColors(
    darkTheme: Boolean = false
) {
    val work = if (darkTheme) Color(0xFF006C49) else Color(0xFF006C49)
    val workContent = if (darkTheme) Color(0xFF00714D) else Color(0xFF00714D)
    val workContainer = if (darkTheme) Color(0xFF6CF8BB) else Color(0xFF6CF8BB)

    val rest = if (darkTheme) Color(0xFF603B00) else Color(0xFF603B00)
    val restContent = if (darkTheme) Color(0xFF2A1700) else Color(0xFF2A1700)
    val restContainer = if (darkTheme) Color(0xFFFFDDB8) else Color(0xFFFFDDB8)

    val round = if (darkTheme) Color(0xFF24389C) else Color(0xFF24389C)
    val roundContent = if (darkTheme) Color(0xFF00105C) else Color(0xFF00105C)
    val roundContainer = if (darkTheme) Color(0xFFDEE0FF) else Color(0xFFDEE0FF)

    val container = if (darkTheme) Color(0xFF1D2638) else Color(0xFFE5EEFF)

    val callOutContainer = if (darkTheme) Color(0xFF292930) else Color(0xFFDCE9FF)
}