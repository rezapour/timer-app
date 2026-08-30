package me.rezapour.workout.presentation.my_workouts.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.components.IniPill
import me.rezapour.designsystem.components.icon_filled_button.IniPrimaryFilledIconButton
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.ui.formatter.TimerDurationFormatter
import me.rezapour.resources.R as res

@Composable
internal fun WorkoutListItem(
    modifier: Modifier = Modifier,
    title: String,
    workoutSeconds: Long,
    restSeconds: Long,
    rounds: Int,
    total: Long,
    onPlayClicked: () -> Unit

) {
    Surface(
        modifier = modifier,
        shape = IniTheme.shapes.medium,
        border = BorderStroke(
            IniTheme.sizes.numberPickerBorderStroke,
            IniTheme.materialColors.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(IniTheme.spacing.m)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(IniTheme.spacing.s),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    textAlign = TextAlign.Start,
                    style = IniTheme.typography.bodyLarge,
                    color = IniTheme.materialColors.onBackground
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(IniTheme.spacing.s),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IniPill(
                        value = stringResource(
                            res.string.my_workouts_item_rounds,
                            rounds
                        )
                    )
                    IniPill(
                        value = TimerDurationFormatter.formatForToMMSS(workoutSeconds),
                        icon = res.drawable.ic_stop_watch
                    )
                    IniPill(
                        value = TimerDurationFormatter.formatForToMMSS(restSeconds),
                        icon = res.drawable.ic_pause
                    )
                    TotalTimeComponent(
                        value = TimerDurationFormatter.formatForToMMSS(total),
                        icon = res.drawable.ic_clock
                    )

                }
            }
            IniPrimaryFilledIconButton(
                icon = res.drawable.ic_play,
                onClick = onPlayClicked
            )
        }
    }
}

@Composable
private fun TotalTimeComponent(
    modifier: Modifier = Modifier,
    value: String,
    @DrawableRes icon: Int,
    contentDescription: String? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(IniTheme.sizes.iniPillIconSize),
            painter = painterResource(icon),
            tint = IniTheme.materialColors.primary,
            contentDescription = contentDescription
        )
        Spacer(modifier = Modifier.width(IniTheme.spacing.xs))
        Text(
            text = value,
            style = IniTheme.typography.labelMedium,
            color = IniTheme.materialColors.primary
        )
    }
}


@Composable
@IniPreview
private fun WorkoutListItemPreview() {
    IniTheme {
        WorkoutListItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            title = "Morning Workout",
            workoutSeconds = 90,
            restSeconds = 75,
            rounds = 4,
            total = 250,
        ) {

        }
    }
}