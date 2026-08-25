package me.rezapour.designsystem.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.designsystem.R as res


@Composable
fun IniButtonPicker(
    modifier: Modifier = Modifier,
    increaseMode: Boolean = true,
    onClick: () -> Unit
) {

    IconButton(
        modifier = modifier
            .size(54.dp)
            .clip(IniTheme.shapes.medium)
            .background(
                color = IniTheme.materialColors.primaryContainer,
            ),
        onClick = onClick
    ) {
        Icon(
            painter = if (increaseMode)
                painterResource(res.drawable.ic_plus)
            else
                painterResource(res.drawable.ic_minus),
            contentDescription = null,
            tint = IniTheme.materialColors.primary,
        )
    }
}

@IniPreview
@Composable
private fun IniButtonPickerPreview() {
    IniTheme() {
        IniButtonPicker(increaseMode = true) {

        }
    }

}
