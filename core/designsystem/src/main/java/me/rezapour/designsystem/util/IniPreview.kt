package me.rezapour.designsystem.util

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "LightMode",
    group = "LightMode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
internal annotation class LightMode

@Preview(
    name = "DarkMode",
    group = "LightMode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
internal annotation class DarkMode


@LightMode
@DarkMode
annotation class IniPreview