package me.rezapour.workout.presentation.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import me.rezapour.workout.presentation.add_workout.compose.AddWorkoutScreen
import me.rezapour.workout.presentation.workout_list.compose.WorkoutListScreen


data object AddWorkoutScreen
data object WorkoutListScreen

fun EntryProviderScope<Any>.WorkoutFeature(backStack: SnapshotStateList<Any>) {
    entry<AddWorkoutScreen> {

        AddWorkoutScreen(onNavigationBack = {
            backStack.removeLastOrNull()
        })
    }

    entry<WorkoutListScreen> {
        WorkoutListScreen() {
            backStack.removeLastOrNull()
        }
    }
}
