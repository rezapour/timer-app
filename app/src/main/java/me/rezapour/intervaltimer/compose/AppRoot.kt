package me.rezapour.intervaltimer.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import me.rezapour.add_routine.presentation.navigation.AddTimerScreen
import me.rezapour.add_routine.presentation.navigation.RotuineFeature
import me.rezapour.add_routine.presentation.navigation.TimerListScreen
import me.rezapour.timer_flow.compose.TimerFlowScreen



data object MainScreen
data object TimerFlow

@Composable
fun AppRoot() {
    val backStack = remember { mutableStateListOf<Any>(MainScreen) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
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
            RotuineFeature(backStack)

            entry<TimerFlow> {
                TimerFlowScreen()
            }

        })
}