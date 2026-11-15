package me.rezapour.designsystem.components.timer_button

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.resources.R as res
//TOdO: get the new play Icon . fixe the pause button color
@Composable
fun IniTimerButtonPause(
    modifier: Modifier = Modifier,
    isPaused: Boolean = false,
    enabled: Boolean = true,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    IniTimerButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        size = IniTheme.sizes.timerButtonLargeSize,
        iconSize = IniTheme.sizes.timerButtonLargeIconSize,
        backgroundColor = backgroundColor,
        contentColor = IniTheme.materialColors.onSecondary,
        icon = if (isPaused) res.drawable.ic_play else res.drawable.ic_pause_without_container
    )
}

@Composable
fun IniTimerButtonStop(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IniTimerButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        size = IniTheme.sizes.timerButtonMediumSize,
        iconSize = IniTheme.sizes.timerButtonMediumIconSize,
        backgroundColor = IniTheme.materialColors.errorContainer,
        contentColor = IniTheme.materialColors.onErrorContainer,
        icon = res.drawable.ic_stop
    )
}

@Composable
fun IniTimerButtonSkip(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IniTimerButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        size = IniTheme.sizes.timerButtonMediumSize,
        iconSize = IniTheme.sizes.timerButtonMediumIconSize,
        backgroundColor = IniTheme.colors.callOutContainer,
        contentColor = IniTheme.materialColors.onBackground,
        icon = res.drawable.ic_skip
    )
}

@Composable
@IniPreview
private fun IniTimerButtonPreview() {
    IniTheme {
        Column() {
            IniTimerButtonPause(
                backgroundColor = IniTheme.materialColors.secondary,
            ) { }

            IniTimerButtonStop() {

            }

            IniTimerButtonSkip() {

            }

        }
    }
}