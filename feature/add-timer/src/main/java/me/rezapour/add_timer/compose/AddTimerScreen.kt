@file:OptIn(ExperimentalMaterial3Api::class)

package me.rezapour.add_timer.compose


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import me.rezapour.add_timer.viewmodel.AddTimerUiEvent
import me.rezapour.add_timer.viewmodel.AddTimerUiState
import me.rezapour.add_timer.viewmodel.AddTimerViewModel
import me.rezapour.designsystem.theme.IntervalTimerTheme
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun AddTimerScreen(viewModel: AddTimerViewModel = koinViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value


    AddTimerContent(uiState) { event ->
        viewModel.updateUiEvent(event)
    }
}


@Composable
fun AddTimerContent(
    uiState: AddTimerUiState,
    uiEvent: (AddTimerUiEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Add Timer")
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        modifier = Modifier,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null,
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Content(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .navigationBarsPadding(),
            uiState = uiState,
            uiEvent = uiEvent
        )

        if (uiState.errorMessage != null)
            LaunchedEffect(Unit) {
                scope.launch {
                    snackbarHostState.showSnackbar(uiState.errorMessage)
                }
            }

    }
}

@Composable
fun Content(
    modifier: Modifier,
    uiState: AddTimerUiState,
    uiEvent: (AddTimerUiEvent) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
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
                    uiEvent(AddTimerUiEvent.OnNameChanged(it))
                },
                singleLine = true,
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(80.dp),
                text = "Timer"
            )

            TextField(
                modifier = Modifier.size(60.dp),
                value = uiState.workMin,
                onValueChange = {
                    uiEvent(AddTimerUiEvent.OnWorkMinChanged(it))
                },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Min")
                }
            )
            Text(":")
            TextField(
                modifier = Modifier.size(60.dp),
                value = uiState.workSec,
                onValueChange = {
                    uiEvent(AddTimerUiEvent.OnWorkSecChanged(it))
                },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Sec")
                }
            )

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(80.dp),
                text = "Interval"
            )

            TextField(
                modifier = Modifier.size(60.dp),
                value = uiState.restMin,
                onValueChange = {
                    uiEvent(AddTimerUiEvent.OnRestMinChanged(it))
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Min")
                }
            )
            Text(":")
            TextField(
                modifier = Modifier.size(60.dp),
                value = uiState.restSec,
                onValueChange = {
                    uiEvent(AddTimerUiEvent.OnRestSecChanged(it))
                },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Sec")
                }
            )

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(80.dp),
                text = "Set"
            )

            TextField(
                modifier = Modifier.size(60.dp),
                value = uiState.rounds,
                onValueChange = {
                    uiEvent(AddTimerUiEvent.OnRoundsChanged(it))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                maxLines = 1,
                singleLine = true,
            )
        }

        Button(onClick = {
            uiEvent(AddTimerUiEvent.SaveTimer)
        }) {
            Text("Save")
        }


    }

}


@Preview
@Composable
fun AddTimerContentPreview() {
    IntervalTimerTheme {
        AddTimerScreen()
    }
}