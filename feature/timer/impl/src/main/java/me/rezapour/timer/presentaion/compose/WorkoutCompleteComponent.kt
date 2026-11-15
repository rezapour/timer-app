package me.rezapour.timer.presentaion.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.components.button.IniPrimaryButton
import me.rezapour.designsystem.components.text_button.IniPrimaryTextButton
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.ui.formatter.TimerDurationFormatter
import me.rezapour.resources.R as res

@Composable
fun WorkoutCompleteCard(
    modifier: Modifier = Modifier,
    totalTime: Long,
    rounds: Int,
    totalWorkTime: Long,
    totalRestTimer: Long
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DoneIcon()

        Spacer(modifier = Modifier.height(IniTheme.spacing.m))

        Text(
            text = stringResource(res.string.timer_workout_complete_title),
            style = IniTheme.typography.bodyMedium,
            color = IniTheme.materialColors.onBackground
        )

        Spacer(modifier = Modifier.height(IniTheme.spacing.s))

        Text(
            text = stringResource(res.string.timer_workout_complete_well_done),
            style = IniTheme.typography.bodyMedium,
            color = IniTheme.materialColors.onSurface
        )

        Spacer(modifier = Modifier.height(IniTheme.spacing.xl))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WorkoutDetailCard(
                modifier = Modifier.weight(1f),
                icon = res.drawable.ic_round,
                value = rounds.toString(),
                text = stringResource(res.string.timer_workout_complete_label_round)
            )

            WorkoutDetailCard(
                modifier = Modifier.weight(1f),
                icon = res.drawable.ic_stop_watch,
                value = TimerDurationFormatter.formatForToMMSS(totalTime),
                text = stringResource(res.string.timer_workout_complete_label_total_time)
            )
        }

        Spacer(modifier = Modifier.height(IniTheme.spacing.xl))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(IniTheme.shapes.small)
                .background(IniTheme.colors.completeWorkoutContainer)
                .padding(IniTheme.spacing.m),
            verticalArrangement = Arrangement.spacedBy(IniTheme.spacing.s),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WorkoutDetailRow(
                title = stringResource(res.string.timer_workout_complete_detail_row_work_time),
                color = IniTheme.materialColors.secondary,
                duration = TimerDurationFormatter.formatForToMMSS(totalWorkTime)
            )

            HorizontalDivider()

            WorkoutDetailRow(
                title = stringResource(res.string.timer_workout_complete_detail_row_rest_time),
                color = IniTheme.colors.rest,
                duration = TimerDurationFormatter.formatForToMMSS(totalRestTimer)
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                progress = { (totalWorkTime / totalTime.toFloat()).coerceIn(0f, 1f) },
                color = IniTheme.materialColors.secondary,
                trackColor = IniTheme.colors.rest,
                drawStopIndicator = {},
                gapSize = 0.dp
            )
        }

        Spacer(modifier = Modifier.height(IniTheme.spacing.xl))

        IniPrimaryButton(
            text = "Done"
        ) { }
        Spacer(modifier = Modifier.height(IniTheme.spacing.s))
        IniPrimaryTextButton(
            text = "Start Again",
            icon = res.drawable.ic_again
        ) { }
    }

}

@Composable
private fun DoneIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(96.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(IniTheme.materialColors.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(72.dp),
            painter = painterResource(res.drawable.ic_chech),
            tint = IniTheme.materialColors.onSecondaryContainer,
            contentDescription = null
        )
    }
}

@Composable
private fun WorkoutDetailCard(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    value: String,
    text: String,
    contentDescription: String? = null
) {
    Column(
        modifier = modifier
            .height(112.dp)
            .clip(IniTheme.shapes.small)
            .background(IniTheme.colors.container)
            .padding(IniTheme.spacing.m),
        verticalArrangement = Arrangement.spacedBy(IniTheme.spacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(icon),
            tint = IniTheme.materialColors.primary,
            contentDescription = contentDescription
        )

        Text(
            text = value,
            style = IniTheme.typography.headlineMedium,
            color = IniTheme.materialColors.onBackground
        )

        Text(
            text = text,
            style = IniTheme.typography.labelMedium,
            color = IniTheme.materialColors.onSurface
        )
    }
}

@Composable
private fun WorkoutDetailRow(
    modifier: Modifier = Modifier,
    title: String,
    duration: String,
    color: Color
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(IniTheme.spacing.s),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )

            Text(
                text = title,
                style = IniTheme.typography.bodyMedium,
                color = IniTheme.materialColors.onSurfaceVariant
            )
        }

        Text(
            text = duration,
            style = IniTheme.typography.labelMedium,
            color = IniTheme.materialColors.onBackground
        )
    }
}

@Composable
@IniPreview
private fun WorkoutCompleteCardPreview() {
    IniTheme() {
        WorkoutCompleteCard(
            modifier = Modifier.padding(24.dp),
            totalTime = 180,
            rounds = 5,
            totalWorkTime = 120,
            totalRestTimer = 60
        )
    }
}