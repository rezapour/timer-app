@file:OptIn(ExperimentalMaterial3Api::class)

package me.rezapour.workout.presentation.my_workouts.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.rezapour.designsystem.components.MainBottomNavigation
import me.rezapour.designsystem.components.MainTab
import me.rezapour.designsystem.components.floating_button.IniPrimaryFloatingButton
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.workout.presentation.my_workouts.model.WorkoutItem
import me.rezapour.workout.presentation.my_workouts.viewmodel.MyWorkoutsAction
import me.rezapour.workout.presentation.my_workouts.viewmodel.MyWorkoutsUiEffect
import me.rezapour.workout.presentation.my_workouts.viewmodel.MyWorkoutsUiState
import me.rezapour.workout.presentation.my_workouts.viewmodel.MyWorkoutsViewmodel
import org.koin.compose.viewmodel.koinViewModel
import me.rezapour.resources.R as res

@Composable
fun MyWorkoutsScreen(
    viewmodel: MyWorkoutsViewmodel = koinViewModel(),
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    navigateToAddWorkout: () -> Unit,
    navigateToEditWorkout: (Long) -> Unit,
) {
    val uiState = viewmodel.uiState.collectAsStateWithLifecycle().value

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewmodel) {
        viewmodel.uiEffect.collect { effect ->
            when (effect) {
                is MyWorkoutsUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }

                MyWorkoutsUiEffect.NavigateAddWorkout -> navigateToAddWorkout()
                is MyWorkoutsUiEffect.NavigateEditWorkout -> navigateToEditWorkout(effect.workoutId)
                is MyWorkoutsUiEffect.StartWorkout -> TODO()
            }
        }
    }
    MyWorkoutsScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onTabSelected = onTabSelected,
        selectedTab = selectedTab,
        onAction = viewmodel::onAction
    )
}

@Composable
internal fun MyWorkoutsScreenContent(
    uiState: MyWorkoutsUiState,
    snackbarHostState: SnackbarHostState,
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    onAction: (MyWorkoutsAction) -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(res.string.my_workouts_title),
                        style = IniTheme.typography.headlineMedium,
                        color = IniTheme.materialColors.primary
                    )
                }
            )
        },
        bottomBar = {
            MainBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )
        },
        floatingActionButton = {

            if (uiState is MyWorkoutsUiState.Success && uiState.workouts.isNotEmpty())
                IniPrimaryFloatingButton(
                    icon = res.drawable.ic_add
                ) {
                    onAction(MyWorkoutsAction.AddWorkoutClicked)
                }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .navigationBarsPadding()
                .padding(vertical = 20.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            when (uiState) {
                is MyWorkoutsUiState.Error -> ErrorComponent() {
                    onAction(MyWorkoutsAction.RetryClicked)
                }

                is MyWorkoutsUiState.Loading -> {}
                is MyWorkoutsUiState.Success if uiState.workouts.isEmpty() -> {
                    NoWorkoutAvailableComponent(
                        modifier = Modifier
                    ) {
                        onAction(MyWorkoutsAction.AddWorkoutClicked)
                    }
                }

                is MyWorkoutsUiState.Success -> WorkoutList(
                    workoutList = uiState.workouts,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
fun WorkoutList(
    modifier: Modifier = Modifier,
    workoutList: List<WorkoutItem>,
    onAction: (MyWorkoutsAction) -> Unit
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(IniTheme.spacing.s),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = workoutList,
            key = { workout -> workout.id }
        ) {
            WorkoutListItem(
                modifier = Modifier.animateItem(),
                title = if (it.name.isNullOrEmpty()) stringResource(
                    res.string.my_workouts_empty_name,
                    it.id
                ) else it.name,
                workoutSeconds = it.workSeconds,
                restSeconds = it.restSeconds,
                rounds = it.rounds,
                total = it.totalSeconds,
            ) {
                onAction(MyWorkoutsAction.PlayClicked(it))
            }
        }
    }
}

@Composable
private fun NoWorkoutAvailableComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CallOutComponent(
        modifier = modifier,
        state = CalloutState.Info,
        icon = res.drawable.ic_no_workout,
        title = stringResource(res.string.my_workouts_no_workout_title),
        message = stringResource(res.string.my_workouts_no_workout_message),
        buttonValue = stringResource(res.string.my_workouts_no_workout_button_value),
        buttonIcon = res.drawable.ic_add,
        onClick = onClick
    )
}

@Composable
private fun ErrorComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CallOutComponent(
        modifier = modifier,
        state = CalloutState.Error,
        icon = res.drawable.ic_error,
        title = stringResource(res.string.my_workouts_error_title),
        message = stringResource(res.string.my_workouts_error_message),
        buttonValue = stringResource(res.string.my_workouts_error_button_value),
        buttonIcon = res.drawable.ic_retry,
        onClick = onClick
    )
}


@IniPreview
@Composable
private fun MyWorkoutsScreenPreview() {
    IniTheme {
        MyWorkoutsScreenContent(
            uiState = MyWorkoutsUiState.Error(
//                workouts = listOf(
//                    WorkoutItem(id = 1, "", 45, 60, 5),
//                    WorkoutItem(id = 2, "", 120, 300, 5),
//                    WorkoutItem(id = 3, "", 190, 300, 5),
//                    WorkoutItem(id = 4, "", 270, 300, 5),
//
//                    )
            ),
            snackbarHostState = SnackbarHostState(),
            selectedTab = MainTab.WORKOUTS,
            onTabSelected = {}
        ) { }
    }
}
