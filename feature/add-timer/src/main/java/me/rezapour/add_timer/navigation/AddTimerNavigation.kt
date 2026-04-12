package me.rezapour.add_timer.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import me.rezapour.add_timer.compose.AddTimerScreen

data object AddTimerScreen

fun EntryProviderScope<Any>.addTimerScreen(backStack: SnapshotStateList<Any>) {
    entry<AddTimerScreen> {

        AddTimerScreen(onNavigationBack = {
            backStack.removeLastOrNull()
        })
    }
}