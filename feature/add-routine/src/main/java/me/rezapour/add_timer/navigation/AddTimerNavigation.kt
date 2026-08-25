package me.rezapour.add_timer.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import me.rezapour.add_timer.compose.AddRoutineScreen

data object AddTimerScreen

fun EntryProviderScope<Any>.addTimerScreen(backStack: SnapshotStateList<Any>) {
    entry<AddTimerScreen> {

        AddRoutineScreen(onNavigationBack = {
            backStack.removeLastOrNull()
        })
    }
}