package me.rezapour.intervaltimer.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import me.rezapour.designsystem.components.MainTab
import me.rezapour.timer_flow.compose.TimerFlowScreen
import me.rezapour.workout.presentation.navigation.MyWorkoutsRoute
import me.rezapour.workout.presentation.navigation.workoutFeature


@Serializable
data object ActiveWorkoutRoute : NavKey

@Composable
fun AppRoot() {

    var selectedTab by rememberSaveable {
        mutableStateOf(MainTab.WORKOUTS)
    }
    val workoutsBackStack = rememberNavBackStack(MyWorkoutsRoute)

    val activeBackStack = when (selectedTab) {
        MainTab.WORKOUTS -> workoutsBackStack
        MainTab.HISTORY -> workoutsBackStack // TODO replace with historyBackStack
        MainTab.SETTINGS -> workoutsBackStack // TODO replace with settingsBackStack
    }
    NavDisplay(
        backStack = activeBackStack,
        onBack = { activeBackStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            workoutFeature(
                backStack = workoutsBackStack,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
            )

            entry<ActiveWorkoutRoute> {
                TimerFlowScreen()
            }

        })
}
