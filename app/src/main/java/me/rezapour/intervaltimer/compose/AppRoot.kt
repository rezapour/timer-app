package me.rezapour.intervaltimer.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import me.rezapour.workout.presentation.navigation.AddWorkoutScreen
import me.rezapour.workout.presentation.navigation.WorkoutFeature
import me.rezapour.workout.presentation.navigation.WorkoutListScreen
import me.rezapour.timer_flow.compose.TimerFlowScreen



data object MainScreen
data object TimerFlow

@Composable
fun AppRoot() {
    val backStack = remember { mutableStateListOf<Any>(MainScreen) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<MainScreen> {
                MainScreen(onAddWorkoutClicked = {
                    backStack.add(AddWorkoutScreen)
                }, onWorkoutListScreenClicked = {
                    backStack.add(WorkoutListScreen)
                }, onWorkoutFlowScreenClicked = {
                    backStack.add(TimerFlow)
                }
                    )
            }
            WorkoutFeature(backStack)

            entry<TimerFlow> {
                TimerFlowScreen()
            }

        })
}
