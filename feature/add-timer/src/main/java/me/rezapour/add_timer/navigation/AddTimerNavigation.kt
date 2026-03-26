package me.rezapour.add_timer.navigation

import androidx.navigation3.runtime.EntryProviderScope
import me.rezapour.add_timer.compose.AddTimerScreen

data object AddTimerScreen

fun EntryProviderScope<Any>.addTimerScreen() {
    entry<AddTimerScreen> {
        AddTimerScreen()
    }
}