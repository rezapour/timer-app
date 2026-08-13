package me.rezapour.intervaltimer.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.rezapour.designsystem.components.button.IniButtonPicker


@Composable
fun MainScreen(
    onAddTimerClicked: () -> Unit,
    onTimerListScreenClicked:() -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            IniButtonPicker (onClick = {
                onAddTimerClicked()
            })
            IniButtonPicker (increaseMode = false){
                onTimerListScreenClicked()
            }
        }
    }


}