@file:OptIn(ExperimentalMaterial3Api::class)

package me.rezapour.workout.presentation.add_workout.compose


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.rezapour.workout.presentation.add_workout.compose.WorkoutConfigItem
import me.rezapour.workout.presentation.add_workout.viewmodel.AddWorkoutAction
import me.rezapour.workout.presentation.add_workout.viewmodel.AddWorkoutUiEffect
import me.rezapour.workout.presentation.add_workout.viewmodel.AddWorkoutUiState
import me.rezapour.workout.presentation.add_workout.viewmodel.AddWorkoutViewModel
import me.rezapour.designsystem.components.IniPill
import me.rezapour.designsystem.components.IniPillSize
import me.rezapour.designsystem.components.button.IniPrimaryButton
import me.rezapour.designsystem.components.icon_button.IniIconButton
import me.rezapour.designsystem.components.textfield.IniTextField
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.ui.formatter.TimerDurationFormatter
import org.koin.compose.viewmodel.koinViewModel
import me.rezapour.resources.R as res


@Composable
fun AddWorkoutScreen(
    viewModel: AddWorkoutViewModel = koinViewModel(),
    onNavigationBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val snackbarHost = remember { SnackbarHostState() }



    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                AddWorkoutUiEffect.NavigationBack -> onNavigationBack()
                is AddWorkoutUiEffect.ShowSnackBar -> snackbarHost.showSnackbar(effect.errorMessage)
            }
        }
    }

    AddWorkoutContent(
        uiState = uiState,
        snackBarHostState = snackbarHost,
        onAction = viewModel::onAction,
    )
}


@Composable
private fun AddWorkoutContent(
    uiState: AddWorkoutUiState,
    snackBarHostState: SnackbarHostState,
    onAction: (AddWorkoutAction) -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(res.string.add_workout_title),
                        style = IniTheme.typography.headlineMedium,
                        color = IniTheme.materialColors.primary
                    )
                },
                navigationIcon = {
                    IniIconButton(
                        icon = res.drawable.ic_button_close,
                        iconSize = 32.dp,
                        tint = IniTheme.materialColors.primary
                    ) {
                        onAction(AddWorkoutAction.BackClicked)
                    }

                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        Content(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),

                ),
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiState: AddWorkoutUiState,
    onAction: (AddWorkoutAction) -> Unit
) {

    Column(
        modifier = modifier.padding(
            top = 24.dp,
            start = 20.dp,
            end = 20.dp,
            bottom = 20.dp
        ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IniTextField(
            value = uiState.name,
            onValueChange = { newValue ->
                onAction(AddWorkoutAction.OnNameChanged(newValue))
            },
            placeholder = stringResource(res.string.add_workout_name_placeholder),
            label = stringResource(res.string.add_workout_name_label)
        )

        Spacer(modifier = Modifier.height(IniTheme.spacing.xl))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(res.string.add_workout_interval_configuration),
            style = IniTheme.typography.labelMedium,
            color = IniTheme.materialColors.onSurfaceVariant,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(IniTheme.spacing.xs))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(IniTheme.spacing.m))

        WorkoutConfigItem(
            title = stringResource(res.string.add_workout_work_duration),
            unit = stringResource(res.string.add_workout_seconds),
            unitColor = IniTheme.colors.work,
            icon = res.drawable.ic_workout,
            tint = IniTheme.colors.workContent,
            iconContainerColor = IniTheme.colors.workContainer,
            value = uiState.workoutSecond.toString(),
            onIncreased = { onAction(AddWorkoutAction.WorkoutIncreased) },
            onDecreased = { onAction(AddWorkoutAction.WorkoutDecreased) },
            decreasedEnabled = uiState.workDecreasedEnabled
        )
        Spacer(modifier = Modifier.height(IniTheme.spacing.m))


        WorkoutConfigItem(
            title = stringResource(res.string.add_workout_rest_duration),
            unit = stringResource(res.string.add_workout_seconds),
            unitColor = IniTheme.colors.rest,
            icon = res.drawable.ic_rest,
            tint = IniTheme.colors.restContent,
            iconContainerColor = IniTheme.colors.restContainer,
            value = uiState.restSecond.toString(),
            onIncreased = { onAction(AddWorkoutAction.RestIncreased) },
            onDecreased = { onAction(AddWorkoutAction.RestDecreased) },
            decreasedEnabled = uiState.restDecreasedEnabled
        )
        Spacer(modifier = Modifier.height(IniTheme.spacing.m))

        WorkoutConfigItem(
            title = stringResource(res.string.add_workout_total_rounds),
            unit = stringResource(res.string.add_workout_repetitions),
            unitColor = IniTheme.colors.round,
            icon = res.drawable.ic_round,
            tint = IniTheme.colors.roundContent,
            iconContainerColor = IniTheme.colors.roundContainer,
            value = uiState.rounds.toString(),
            onIncreased = { onAction(AddWorkoutAction.RoundIncreased) },
            onDecreased = { onAction(AddWorkoutAction.RoundDecreased) },
            decreasedEnabled = uiState.roundDecreasedEnabled
        )
        Spacer(modifier = Modifier.height(IniTheme.spacing.m))
        HorizontalDivider()


        Spacer(modifier = Modifier.height(IniTheme.spacing.m))
        IniPill(
            value = stringResource(
                res.string.add_workout_summary,
                uiState.rounds,
                TimerDurationFormatter.formatForToMMSS(uiState.total),
            ),
            size = IniPillSize.Large
        )
        Spacer(modifier = Modifier.height(IniTheme.spacing.m))
        IniPrimaryButton(
            text = stringResource(res.string.add_workout_save)
        ) {
            onAction(AddWorkoutAction.SaveWorkout)
        }
    }
}


@Preview
@Composable
private fun AddWorkoutContentPreview() {
    IniTheme {
        AddWorkoutContent(
            AddWorkoutUiState(),
            SnackbarHostState(),
            onAction = {},
        )
    }
}
