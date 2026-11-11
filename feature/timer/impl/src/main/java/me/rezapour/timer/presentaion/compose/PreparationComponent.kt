package me.rezapour.timer.presentaion.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import kotlin.time.Duration.Companion.milliseconds
import me.rezapour.resources.R as res

@Composable
internal fun PreparationComponent(
    modifier: Modifier = Modifier,
    state: String,
    title: String,
    description: String,
    durationSeconds: Int = 3,
    onFinished: () -> Unit
) {

    var secondRemaining by remember(durationSeconds) {
        mutableIntStateOf(durationSeconds)
    }

    val currentOnFinished by rememberUpdatedState(onFinished)

    val progress = remember { Animatable(0f) }
    LaunchedEffect(durationSeconds) {
        secondRemaining = durationSeconds

        val timerJob = launch {
            repeat(durationSeconds) {
                delay(1000.milliseconds)
                secondRemaining--
            }
        }


        val progressJob = launch {
            progress.snapTo(0f)

            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = durationSeconds * 1000,
                    easing = LinearEasing
                )
            )
        }

        timerJob.join()
        progressJob.join()
        delay(500.milliseconds)
        currentOnFinished()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatusPill(
            text = state,
            icon = res.drawable.ic_stop_watch,
            color = IniTheme.materialColors.primary
        )

        Spacer(modifier = Modifier.height(IniTheme.spacing.xxl))

        AnimatedContent(
            targetState = secondRemaining,
            label = "Preparation Countdown",
        ) { seconds ->
            Text(
                text = if (seconds > 0) seconds.toString() else "Start",
                color = IniTheme.materialColors.primary,
                style = IniTheme.typography.displayLarge
            )
        }


        Spacer(modifier = Modifier.height(IniTheme.spacing.xxl))

        Text(
            text = title,
            style = IniTheme.typography.headlineMedium,
            color = IniTheme.materialColors.onSurface
        )
        Spacer(modifier = Modifier.height(IniTheme.spacing.s))

        Text(
            text = description,
            style = IniTheme.typography.bodyMedium,
            color = IniTheme.materialColors.outlineVariant
        )

        Spacer(modifier = Modifier.height(IniTheme.spacing.xl))


        LinearProgressIndicator(
            modifier = Modifier.size(height = 8.dp, width = 192.dp),
            progress = { progress.value },
            color = IniTheme.materialColors.primary,
            trackColor = IniTheme.materialColors.surfaceVariant,
            strokeCap = StrokeCap.Round
        )
    }

}


@IniPreview
@Composable
fun PreparationComponentPreview() {
    IniTheme {
        PreparationComponent(
            state = "STARTING",
            title = "Morning Workout",
            description = "Get Ready"
        ) {
        }
    }
}