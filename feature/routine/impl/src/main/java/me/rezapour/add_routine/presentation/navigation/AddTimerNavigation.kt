package me.rezapour.add_routine.presentation.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import me.rezapour.add_routine.presentation.add_routine.compose.AddRoutineScreen
import me.rezapour.add_routine.presentation.routine_list.compose.MyWorkoutsScreen


data object AddTimerScreen
data object TimerListScreen

fun EntryProviderScope<Any>.RotuineFeature(backStack: SnapshotStateList<Any>) {
    entry<AddTimerScreen> {

        AddRoutineScreen(onNavigationBack = {
            backStack.removeLastOrNull()
        })
    }

    entry<TimerListScreen> {
        MyWorkoutsScreen() {
            backStack.removeLastOrNull()
        }
    }
}