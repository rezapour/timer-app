package me.rezapour.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalSpacing = staticCompositionLocalOf { Spacing() }
val LocalShapes = staticCompositionLocalOf { AppShapes() }
val LocalSizes = staticCompositionLocalOf { Sizes() }

@Immutable
data class Spacing(
    val xs: Dp = 4.dp,
    val s: Dp = 8.dp,
    val m: Dp = 16.dp,
    val l: Dp = 24.dp,
    val xl: Dp = 32.dp
)

@Immutable
data class AppShapes(
    val extraLarge: Dp = 24.dp,
    val large: Dp = 16.dp,
    val medium: Dp = 12.dp,
    val circle: Float = 0.5f // 50%
)

@Immutable
data class Sizes(
    val touchTarget: Dp = 48.dp,
    val primaryFab: Dp = 80.dp,
    val secondaryFab: Dp = 64.dp
)