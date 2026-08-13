@file:OptIn(ExperimentalMaterial3Api::class)

package me.rezapour.timer_list.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.rezapour.designsystem.R
import me.rezapour.designsystem.components.button.IniButton
import me.rezapour.designsystem.components.button.IniButtonPicker
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.timer_list.model.TimerItem
import me.rezapour.timer_list.viewmodel.TimerListAction
import me.rezapour.timer_list.viewmodel.TimerListUiEffect
import me.rezapour.timer_list.viewmodel.TimerListUiState
import me.rezapour.timer_list.viewmodel.TimerListViewModel
import me.rezapour.ui.formatter.TimerDurationFormatter
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TimerListScreen(viewmodel: TimerListViewModel = koinViewModel(), onNavigationBack: () -> Unit) {
    val uiState = viewmodel.uiState.collectAsStateWithLifecycle().value

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewmodel) {
        viewmodel.uiEffect.collect { effect ->
            when (effect) {
                TimerListUiEffect.NavigationBack -> onNavigationBack()
                is TimerListUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }

            }
        }
    }

    TimerListScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = viewmodel::onAction
    )
}

@Composable
internal fun TimerListScreenContent(
    uiState: TimerListUiState,
    snackbarHostState: SnackbarHostState,
    onAction: (TimerListAction) -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column() {
                        Text("Saved Timers")
                        Text("${uiState.timers.size} timers")
                    }

                },
                actions = {
                    IniButtonPicker(increaseMode = true) {

                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onAction(TimerListAction.BackPress)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null,
                        )
                    }
                },

                )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        TimerList(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .navigationBarsPadding(),
            timerList = uiState.timers,
            onAction = onAction
        )
    }
}

@Composable
fun TimerList(
    modifier: Modifier = Modifier,
    timerList: List<TimerItem>,
    onAction: (TimerListAction) -> Unit
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
            items = timerList,
            key = { it.id }
        ) {
            TimerItemComponent(
                modifier = Modifier.animateItem(),
                timerItem = it,
                onDeleteClicked = { id ->
                    onAction(TimerListAction.DeletePress(id))
                },
                onStartClicked = {})
        }
    }
}

@Composable
fun TimerItemComponent(
    modifier: Modifier = Modifier,
    timerItem: TimerItem,
    onDeleteClicked: (Long) -> Unit,
    onStartClicked: (Long) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(IniTheme.spacing.s),
        shape = RoundedCornerShape(IniTheme.appShapes.small)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(IniTheme.spacing.m), // Fixed: Using a fresh Modifier to avoid double-applying parent modifiers
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row {
                    Text(
                        text = "Run ${TimerDurationFormatter.formatForTimerList(timerItem.workSeconds)}",
                        style = IniTheme.typography.titleMedium,
                        color = IniTheme.colors.primary
                    )
                    Text(
                        text = " / ",
                        style = IniTheme.typography.titleMedium,
                        color = IniTheme.colors.tertiary
                    )
                    Text(
                        text = "Walk ${TimerDurationFormatter.formatForTimerList(timerItem.restSeconds)}",
                        style = IniTheme.typography.titleMedium,
                        color = IniTheme.colors.secondary
                    )
                }
                Text(
                    text = "${timerItem.rounds} Rounds * ${
                        TimerDurationFormatter.formatForTimerList(
                            timerItem.totalSeconds
                        )
                    } total",
                    style = IniTheme.typography.labelSmall,
                    color = IniTheme.colors.tertiary
                )
            }


            IniButton(icon = R.drawable.ic_delete) {
                onDeleteClicked(timerItem.id)
            }
            Spacer(modifier = Modifier.width(IniTheme.spacing.m))
            IniButton(icon = R.drawable.ic_play) {
                onStartClicked(timerItem.id)
            }
        }
    }
}

@Preview
@Composable
fun TimerListScreenPreview() {
    IniTheme {
        TimerListScreenContent(
            uiState = TimerListUiState(
                timers = listOf(
                    // Fixed: Provided unique IDs for each TimerItem to resolve the "Key '1' was already used" error in LazyColumn
                    TimerItem(id = 1, "", 45, 60, 5),
                    TimerItem(id = 2, "", 120, 300, 5),
                    TimerItem(id = 3, "", 190, 300, 5),
                    TimerItem(id = 4, "", 270, 300, 5),

                    )
            ),
            snackbarHostState = SnackbarHostState()
        ) { }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerItemPreview() {
    IniTheme {
        // Fixed: Call TimerItemComponent() instead of the TimerItem data class constructor
        TimerItemComponent(
            timerItem = TimerItem(id = 1, "", 3000, 30022, 5),
            onDeleteClicked = {},
            onStartClicked = {})
    }
}