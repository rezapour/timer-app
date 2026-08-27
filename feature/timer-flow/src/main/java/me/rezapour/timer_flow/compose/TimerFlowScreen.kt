@file:OptIn(ExperimentalMaterial3Api::class)

package me.rezapour.timer_flow.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.rezapour.domain.controller.TimerSnapshot
import me.rezapour.timer_flow.viewmodel.TimerFlowViewModel
import me.rezapour.ui.formatter.TimerDurationFormatter
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TimerFlowScreen(viewModel: TimerFlowViewModel = koinViewModel()) {

    val snapshot by viewModel.snapshot.collectAsStateWithLifecycle()

    RoutineSessionContent(
        snapshot = snapshot,
        onClickStart = {
            viewModel.start()
        }
    ) { viewModel.pause()}
}

@Composable
fun RoutineSessionContent(
    snapshot: TimerSnapshot?,
    onClickStart: () -> Unit,
    onClickPause: () -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Timer")
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerComponent(snapshot = snapshot)


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = {
                        onClickStart()
                    }
                ) { Text("Start") }
                Spacer(modifier = Modifier.width(20.dp))

                Button(
                    onClick = {
                        onClickPause()
                    }
                ) { Text("Pause") }
            }

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
fun RoutineSessenScreenPreivew() {
    RoutineSessionContent(null,{},{})
}

