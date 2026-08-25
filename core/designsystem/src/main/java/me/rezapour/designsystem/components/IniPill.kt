package me.rezapour.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview

@Composable
fun IniPill(
    modifier: Modifier = Modifier,
    value: String
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFE5EEFF),
        shape = IniTheme.shapes.large
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = 16.dp, vertical = 8.dp
            ),
            text = value,
            style = IniTheme.typography.labelMedium,
            color = IniTheme.materialColors.onSurface
        )
    }
}

@Composable
@IniPreview
fun IniPillPreview() {
    IniTheme {
        IniPill(value = "5 rounds • 5:00 total")
    }
}