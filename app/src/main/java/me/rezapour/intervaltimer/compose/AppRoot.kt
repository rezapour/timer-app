package me.rezapour.intervaltimer.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import me.rezapour.timer_flow.compose.TimerFlowScreen
import me.rezapour.workout.presentation.navigation.AddWorkoutRoute
import me.rezapour.workout.presentation.navigation.MyWorkoutsRoute
import me.rezapour.workout.presentation.navigation.workoutFeature


@Serializable
data object MainRoute : NavKey

@Serializable
data object ActiveWorkoutRoute : NavKey

@Composable
fun AppRoot() {
    val backStack = rememberNavBackStack(MainRoute)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<MainRoute> {
                MainScreen(onAddWorkoutClicked = {
                    backStack.add(AddWorkoutRoute)
                }, onWorkoutListScreenClicked = {
                    backStack.add(MyWorkoutsRoute)
                }, onWorkoutFlowScreenClicked = {
                    backStack.add(ActiveWorkoutRoute)
                }
                )
            }
            workoutFeature(backStack)

            entry<ActiveWorkoutRoute> {
                TimerFlowScreen()
            }

        })
}
