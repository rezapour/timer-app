package me.rezapour.intervaltimer.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import me.rezapour.add_timer.navigation.AddTimerScreen
import me.rezapour.add_timer.navigation.addTimerScreen

data object MainScreen

@Composable
fun AppRoot() {
    val backStack = remember { mutableStateListOf<Any>(MainScreen) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<MainScreen> {
                MainScreen(onAddTimerClicked = {
                    backStack.add(AddTimerScreen)
                })
            }
            addTimerScreen(backStack)
        })
}