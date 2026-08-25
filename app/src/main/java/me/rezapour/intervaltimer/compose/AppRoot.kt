package me.rezapour.intervaltimer.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import me.rezapour.add_timer.presentation.navigation.AddTimerScreen
import me.rezapour.add_timer.presentation.navigation.addTimerScreen
import me.rezapour.timer_flow.compose.TimerFlowScreen
import me.rezapour.timer_list.navigation.TimerListScreen
import me.rezapour.timer_list.navigation.timerListScreen

data object MainScreen
data object TimerFlow

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
                }, onTimerListScreenClicked = {
                    backStack.add(TimerListScreen)
                }, onTimeFlowScreenClicked = {
                    backStack.add(TimerFlow)
                }
                    )
            }
            addTimerScreen(backStack)
            timerListScreen(backStack)

            entry<TimerFlow> {
                TimerFlowScreen()
            }

        })
}