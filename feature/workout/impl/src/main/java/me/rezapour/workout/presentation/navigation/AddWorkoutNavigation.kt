package me.rezapour.workout.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import me.rezapour.workout.presentation.add_workout.compose.AddWorkoutScreen
import me.rezapour.workout.presentation.my_workouts.compose.MyWorkoutsScreen

@Serializable
data object AddWorkoutRoute: NavKey
@Serializable
data object MyWorkoutsRoute: NavKey

fun EntryProviderScope<NavKey>.workoutFeature(backStack: NavBackStack<NavKey> ) {
    entry<AddWorkoutRoute> {

        AddWorkoutScreen(onNavigationBack = {
            backStack.removeLastOrNull()
        })
    }

    entry<MyWorkoutsRoute> {
        MyWorkoutsScreen() {
            backStack.removeLastOrNull()
        }
    }
}
