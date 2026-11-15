@file:OptIn(ExperimentalMaterial3Api::class)

package me.rezapour.timer.presentaion.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.rezapour.timer.domain.controller.TimerSnapshot
import me.rezapour.timer.presentaion.viewmodel.TimerFlowViewModel
import me.rezapour.ui.formatter.TimerDurationFormatter
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TimerFlowScreen(viewModel: TimerFlowViewModel = koinViewModel()) {

    WorkoutSessionContent()
}

@Composable
fun WorkoutSessionContent(

) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            PreparationComponent(
//                modifier = Modifier,
//                state = "STARTING",
//                title = "Morning run",
//                description = "Get Ready"
//            ) {
//
//            }
            WorkoutCompleteCard(
                modifier = Modifier.padding(24.dp),
                totalTime = 180,
                rounds = 5,
                totalWorkTime = 120,
                totalRestTimer = 60
            )

        }

    }
}

@Composable
fun TimerComponent(
    modifier: Modifier = Modifier,
    snapshot: TimerSnapshot?
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = TimerDurationFormatter.formatToLabelBreakLine(
                (snapshot?.remainingMs)?.div(1000) ?: 0
            )
        )


        val rawProgress = snapshot?.let {
            val total = it.durationMs.toFloat()
            val remaining = it.remainingMs.toFloat()
            if (total > 0f) (1f - remaining / total).coerceIn(0f, 1f) else 0f
        } ?: 0f

        val animatedProgress by animateFloatAsState(
            targetValue = rawProgress,
            animationSpec = tween(
                durationMillis = 950,
                easing = LinearEasing
            ),
            label = "progress_animation"
        )


        CircularProgressIndicator(
            progress = {
                animatedProgress
            }, // 0f..1f
            strokeWidth = 8.dp,
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview
@Composable
fun WorkoutSessionScreenPreview() {

}
