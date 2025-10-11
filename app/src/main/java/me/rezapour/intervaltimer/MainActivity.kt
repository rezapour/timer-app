package me.rezapour.intervaltimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import me.rezapour.add_timer.compose.AddTimerScreen
import me.rezapour.designsystem.theme.IntervalTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntervalTimerTheme {
                AddTimerScreen()
            }
        }
    }
}