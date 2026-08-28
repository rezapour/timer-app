@file:OptIn(ExperimentalMaterial3Api::class)

package me.rezapour.workout.presentation.workout_list.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.workout.presentation.workout_list.model.WorkoutItem
import me.rezapour.workout.presentation.workout_list.viewmodel.MyWorkoutsAction
import me.rezapour.workout.presentation.workout_list.viewmodel.MyWorkoutsUiEffect
import me.rezapour.workout.presentation.workout_list.viewmodel.MyWorkoutsUiState
import me.rezapour.workout.presentation.workout_list.viewmodel.MyWorkoutsViewmodel
import org.koin.compose.viewmodel.koinViewModel
import me.rezapour.resources.R as res

@Composable
fun MyWorkoutsScreen(
    viewmodel: MyWorkoutsViewmodel = koinViewModel(),
    onNavigationBack: () -> Unit
) {
    val uiState = viewmodel.uiState.collectAsStateWithLifecycle().value

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewmodel) {
        viewmodel.uiEffect.collect { effect ->
            when (effect) {
                is MyWorkoutsUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }

                MyWorkoutsUiEffect.NavigateAddWorkout -> TODO()
                is MyWorkoutsUiEffect.NavigateEditeWorkout -> TODO()
                is MyWorkoutsUiEffect.StartWorkout -> TODO()
            }
        }
    }

    MyWorkoutsScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = viewmodel::onAction
    )
}

@Composable
internal fun MyWorkoutsScreenContent(
    uiState: MyWorkoutsUiState,
    snackbarHostState: SnackbarHostState,
    onAction: (MyWorkoutsAction) -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(res.string.workout_list_title),
                        style = IniTheme.typography.headlineMedium,
                        color = IniTheme.materialColors.primary
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        WorkoutList(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .navigationBarsPadding(),
            workoutList = uiState.workouts,
            onAction = onAction
        )
    }
}

@Composable
fun WorkoutList(
    modifier: Modifier = Modifier,
    workoutList: List<WorkoutItem>,
    onAction: (MyWorkoutsAction) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            vertical = 20.dp,
            horizontal = 24.dp
        ),
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
                    res.string.workout_list_item_empty_name,
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


@IniPreview
@Composable
fun MyWorkoutsScreenPreview() {
    IniTheme {
        MyWorkoutsScreenContent(
            uiState = MyWorkoutsUiState(
                workouts = listOf(
                    WorkoutItem(id = 1, "", 45, 60, 5),
                    WorkoutItem(id = 2, "", 120, 300, 5),
                    WorkoutItem(id = 3, "", 190, 300, 5),
                    WorkoutItem(id = 4, "", 270, 300, 5),

                    )
            ),
            snackbarHostState = SnackbarHostState()
        ) { }
    }
}
