@file:OptIn(ExperimentalMaterial3Api::class)

package me.rezapour.add_timer.compose


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.rezapour.add_timer.viewmodel.AddTimerAction
import me.rezapour.add_timer.viewmodel.AddTimerUiEffect
import me.rezapour.add_timer.viewmodel.AddTimerUiState
import me.rezapour.add_timer.viewmodel.AddTimerViewModel
import me.rezapour.designsystem.components.button.IniButtonPicker
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.ui.formatter.TimerDurationFormatter
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun AddTimerScreen(viewModel: AddTimerViewModel = koinViewModel(), onNavigationBack: () -> Unit) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val snackbarHost = remember { SnackbarHostState() }



    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                AddTimerUiEffect.NavigationBack -> onNavigationBack()
                is AddTimerUiEffect.ShowSnackBar -> snackbarHost.showSnackbar(effect.errorMessage)
            }
        }
    }

    AddTimerContent(
        uiState = uiState,
        snackBarHostState = snackbarHost,
        onAction = viewModel::onAction,
        onNavigationBack = onNavigationBack
    )
}


@Composable
fun AddTimerContent(
    uiState: AddTimerUiState,
    snackBarHostState: SnackbarHostState,
    onAction: (AddTimerAction) -> Unit,
    onNavigationBack: () -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Add Timer")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigationBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null,
                        )
                    }

                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) {
        Content(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .navigationBarsPadding(),
            uiState = uiState,
            uiEvent = onAction
        )


    }
}

@Composable
fun Content(
    modifier: Modifier,
    uiState: AddTimerUiState,
    uiEvent: (AddTimerAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(IniTheme.spacing.m),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(80.dp),
                text = "Name:"
            )
            TextField(
                value = uiState.name ?: "",
                onValueChange = {
                    uiEvent(AddTimerAction.OnNameChanged(it))
                },
                singleLine = true,
            )
        }

        Spacer(modifier = Modifier.height(IniTheme.spacing.xl))

        Picker(
            title = "Run",
            value = TimerDurationFormatter.formatForPicker(uiState.workoutSecond),
            increaseValue = { uiEvent(AddTimerAction.OnWorkoutIncreased) },
            decreaseValue = { uiEvent(AddTimerAction.OnWorkoutDecreased) }
        )

        Spacer(modifier = Modifier.height(IniTheme.spacing.xl))

        Picker(
            title = "Rest",
            value = TimerDurationFormatter.formatForPicker(uiState.restSecond),
            increaseValue = { uiEvent(AddTimerAction.OnRestIncreased) },
            decreaseValue = { uiEvent(AddTimerAction.OnRestDecreased) }
        )

        Spacer(modifier = Modifier.height(IniTheme.spacing.xl))

        Picker(
            title = "Round",
            value = uiState.rounds.toString(),
            increaseValue = { uiEvent(AddTimerAction.OnRoundIncreased) },
            decreaseValue = { uiEvent(AddTimerAction.OnRoundDecreased) }
        )

        Spacer(modifier = Modifier.height(IniTheme.spacing.xl))

        Button(onClick = {
            uiEvent(AddTimerAction.SaveTimer)
        }) {
            Text("Save")
        }
    }
}

@Composable
fun Picker(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    increaseValue: () -> Unit,
    decreaseValue: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = IniTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(IniTheme.spacing.s))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IniButtonPicker(increaseMode = false) { decreaseValue() }
            Text(
                modifier = Modifier
                    .padding(start = IniTheme.spacing.s, end = IniTheme.spacing.s)
                    .sizeIn(minWidth = 80.dp),
                text = value,
                textAlign = TextAlign.Center,
                color = IniTheme.colors.onPrimaryContainer,
                style = IniTheme.typography.displaySmall
            )
            IniButtonPicker { increaseValue() }
        }


    }

}


@Preview
@Composable
fun AddTimerContentPreview() {
    IniTheme {
        AddTimerContent(AddTimerUiState(), SnackbarHostState(), onAction = {}, onNavigationBack = {})
    }
}

@IniPreview
@Composable
fun PickerPreview() {
    IniTheme {
        Picker(
            title = "run",
            value = "2 min",
            increaseValue = {},
            decreaseValue = {},
        )
    }
}