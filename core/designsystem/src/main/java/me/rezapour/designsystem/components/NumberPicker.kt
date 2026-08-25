package me.rezapour.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import me.rezapour.designsystem.R
import me.rezapour.designsystem.components.icon_button.IniIconButton
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview

@Composable
fun IniNumberPicker(
    modifier: Modifier = Modifier,
    value: String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    increaseEnabled: Boolean = true,
    decreaseEnabled: Boolean = true

) {
    Surface(
        modifier = modifier
            .size(
                height = IniTheme.sizes.numberPickerHeight,
                width = IniTheme.sizes.numberPickerWidth
            ),
        shape = IniTheme.shapes.small,
        border = BorderStroke(
            IniTheme.sizes.numberPickerBorderStroke,
            IniTheme.materialColors.outlineVariant
        ),
        contentColor = IniTheme.materialColors.onSurface,
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(IniTheme.sizes.numberPickerInnerPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IniIconButton(
                icon = R.drawable.ic_minus,
                enabled = decreaseEnabled,
                onClick = onDecrease,
            )
            Text(
                modifier = Modifier.width(IniTheme.sizes.numberPickerValueWidth),
                text = value,
                textAlign = TextAlign.Center,
                style = IniTheme.typography.bodyLarge,
                color = IniTheme.materialColors.onSurface
            )
            IniIconButton(
                icon = R.drawable.ic_plus,
                enabled = increaseEnabled,
                onClick = onIncrease
            )
        }
    }

}

@Composable
@IniPreview
private fun IniNumberPickerPreview() {
    IniTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(IniTheme.spacing.m)
        ) {
            // Default
            IniNumberPicker(
                value = "2",
                onIncrease = {},
                onDecrease = {}
            )

            // Larger value
            IniNumberPicker(
                value = "100",
                onIncrease = {},
                onDecrease = {}
            )

            // Decimal value
            IniNumberPicker(
                value = "1.5",
                onIncrease = {},
                onDecrease = {}
            )

            // Minimum reached
            IniNumberPicker(
                value = "1",
                decreaseEnabled = false,
                onIncrease = {},
                onDecrease = {}
            )

            // Maximum reached
            IniNumberPicker(
                value = "100",
                increaseEnabled = false,
                onIncrease = {},
                onDecrease = {}
            )

            // No changes allowed
            IniNumberPicker(
                value = "5",
                increaseEnabled = false,
                decreaseEnabled = false,
                onIncrease = {},
                onDecrease = {}
            )
        }
    }
}