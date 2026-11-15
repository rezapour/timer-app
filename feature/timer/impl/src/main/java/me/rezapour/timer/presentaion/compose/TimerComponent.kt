package me.rezapour.timer.presentaion.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.components.timer_button.IniTimerButtonPause
import me.rezapour.designsystem.components.timer_button.IniTimerButtonSkip
import me.rezapour.designsystem.components.timer_button.IniTimerButtonStop
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.ui.formatter.TimerDurationFormatter
import me.rezapour.resources.R as res

@Composable
internal fun TimerComponent(
    modifier: Modifier = Modifier,
    timerLengthMilliseconds: Long,
    remainingTimerMilliseconds: Long,
    intervalName: String,
    intervalColor: Color = IniTheme.materialColors.secondary,
    currentRound: Int,
    rounds: Int,
    nextIntervalName: String,
    nextIntervalDuration: Long,
    nexIntervalIcon: Int,
    contentDescription: String? = null,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onSkip: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatusPill(
            text = intervalName,
            color = intervalColor,
        )

        Spacer(modifier = Modifier.height(IniTheme.spacing.s))

        Text(
            text = "Round $currentRound of $rounds",
            style = IniTheme.typography.bodyLarge,
            color = IniTheme.materialColors.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(IniTheme.spacing.xxl))

        Box(
            modifier = Modifier.padding(IniTheme.spacing.s),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = TimerDurationFormatter.formatForToMMSS(remainingTimerMilliseconds / 1000),
                style = IniTheme.typography.displayLarge,
                color = IniTheme.materialColors.onSurfaceVariant
            )

            CircularProgressIndicator(
                modifier = Modifier.size(259.dp),
                progress = { .50f },
                strokeWidth = 12.dp,
                color = intervalColor,
                trackColor = intervalColor.copy(alpha = 0.15f)

            )
        }

        Spacer(modifier = Modifier.height(IniTheme.spacing.xl))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = IniTheme.colors.callOutContainer,
            shape = IniTheme.shapes.small,
            border = BorderStroke(
                width = 1.dp,
                color = IniTheme.materialColors.outlineVariant
            )
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = IniTheme.spacing.m,
                        vertical = IniTheme.spacing.l
                    )
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = modifier.size(16.dp),
                        painter = painterResource(nexIntervalIcon),
                        tint = IniTheme.materialColors.onSurfaceVariant,
                        contentDescription = contentDescription,
                    )

                    Spacer(modifier = Modifier.width(IniTheme.spacing.s))

                    Text(
                        text = "Next: $nextIntervalName",
                        style = IniTheme.typography.bodyMedium,
                        color = IniTheme.materialColors.onSurfaceVariant
                    )
                }

                Text(
                    text = TimerDurationFormatter.formatForToMMSS(nextIntervalDuration / 1000),
                    style = IniTheme.typography.labelMedium,
                    color = IniTheme.materialColors.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(IniTheme.spacing.xl))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = IniTheme.spacing.l),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IniTimerButtonStop() {
                onStop()
            }
            IniTimerButtonPause(backgroundColor = intervalColor) {
                onPause()
            }
            IniTimerButtonSkip() {
                onSkip()
            }
        }
    }
}

@Composable
@IniPreview
private fun TimerComponentPreview() {
    IniTheme {
        TimerComponent(

            timerLengthMilliseconds = 180000,
            remainingTimerMilliseconds = 143000,
            intervalName = "Work",
            intervalColor = IniTheme.materialColors.secondary,
            currentRound = 2,
            rounds = 5,
            nextIntervalName = "Rest",
            nextIntervalDuration = 15000,
            nexIntervalIcon = res.drawable.ic_pause,
            onPause = {},
            onStop = {},
            onSkip = {}
        )
    }
}