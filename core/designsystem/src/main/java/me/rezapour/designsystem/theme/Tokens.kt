package me.rezapour.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalSpacing = staticCompositionLocalOf { Spacing() }
val LocalSizes = staticCompositionLocalOf { Sizes() }

@Immutable
data class Spacing(
    val xs: Dp = 4.dp,
    val s: Dp = 8.dp,
    val m: Dp = 16.dp,
    val l: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp,
    val gutter: Dp = 16.dp,
    val screenMargin: Dp = 20.dp
)

@Immutable
data class Sizes(

    val touchTarget: Dp = 48.dp,
    val primaryFab: Dp = 80.dp,
    val secondaryFab: Dp = 64.dp,

    val buttonHeight: Dp = 56.dp,
    val buttonIcon: Dp = 20.dp,
    val buttonLoader: Dp = 20.dp,
    val buttonLoaderStroke: Dp = 2.dp,

)