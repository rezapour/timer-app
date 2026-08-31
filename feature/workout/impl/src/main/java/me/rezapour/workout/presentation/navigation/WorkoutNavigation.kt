package me.rezapour.workout.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import me.rezapour.designsystem.components.MainTab
import me.rezapour.workout.api.navigation.AddEditWorkoutRoute
import me.rezapour.workout.api.navigation.MyWorkoutsRoute
import me.rezapour.workout.presentation.add_workout.compose.AddWorkoutScreen
import me.rezapour.workout.presentation.my_workouts.compose.MyWorkoutsScreen

fun EntryProviderScope<NavKey>.workoutFeature(
    backStack: NavBackStack<NavKey>,
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
) {
    entry<AddEditWorkoutRoute> {

        AddWorkoutScreen(onNavigationBack = {
            backStack.removeLastOrNull()
        })
    }

    entry<MyWorkoutsRoute> {
        MyWorkoutsScreen(
            onTabSelected = onTabSelected,
            selectedTab = selectedTab,
            navigateToAddWorkout = {
                backStack.add(AddEditWorkoutRoute())
            },
            navigateToEditWorkout = { id ->
                backStack.add(AddEditWorkoutRoute())
            }
        )
    }
}