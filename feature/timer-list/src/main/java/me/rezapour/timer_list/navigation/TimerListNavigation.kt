package me.rezapour.timer_list.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import me.rezapour.timer_list.compose.TimerListScreen

data object TimerListScreen

fun EntryProviderScope<Any>.timerListScreen(backStack: SnapshotStateList<Any>) {
    entry<TimerListScreen> {
        TimerListScreen() {
            backStack.removeLastOrNull()
        }
    }
}