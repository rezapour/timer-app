package me.rezapour.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalSizes = staticCompositionLocalOf { Sizes() }

@Immutable
data class Sizes(

    val touchTarget: Dp = 48.dp,
    val primaryFab: Dp = 80.dp,
    val secondaryFab: Dp = 64.dp,

    //IniButton Sizes
    val buttonHeight: Dp = 56.dp,
    val buttonIcon: Dp = 20.dp,
    val buttonLoader: Dp = 20.dp,
    val buttonLoaderStroke: Dp = 2.dp,


    //IniIconButton Sizes
    val iconButtonSize: Dp = 40.dp,
    val iconButtonIconSize: Dp = 14.dp,

    //IniNumberPicker
    val numberPickerWidth: Dp = 154.dp,
    val numberPickerHeight: Dp = 50.dp,
    val numberPickerInnerPadding: Dp = 5.dp,
    val numberPickerBorderStroke: Dp = 1.dp,
    val numberPickerValueWidth:Dp = 56.dp
)