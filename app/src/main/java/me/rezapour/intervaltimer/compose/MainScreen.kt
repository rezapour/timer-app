package me.rezapour.intervaltimer.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.rezapour.designsystem.components.button.IniDestructiveButton
import me.rezapour.designsystem.components.button.IniPrimaryButton
import me.rezapour.designsystem.components.button.IniSecondaryButton


@Composable
fun MainScreen(
    onAddWorkoutClicked: () -> Unit,
    onWorkoutListScreenClicked: () -> Unit,
    onWorkoutFlowScreenClicked: () -> Unit,
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            IniPrimaryButton (loading = false,text = "Add Workout",onClick = {
                onAddWorkoutClicked()
            })
            IniSecondaryButton(text = "My Workouts")  {
                onWorkoutListScreenClicked()
            }

            IniDestructiveButton(text = "Start Workout") {
                onWorkoutFlowScreenClicked()
            }
        }
    }


}
