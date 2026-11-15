package me.rezapour.designsystem.components.timer_button


import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
internal fun IniTimerButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color,
    contentColor: Color,
    size: Dp,
    iconSize: Dp,
    @DrawableRes icon: Int,
    contentDescription: String? = null,
    onClick: () -> Unit
) {

    IconButton(
        modifier = modifier.size(size),
        colors = IconButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContentColor = backgroundColor.copy(alpha = 0.4f),
            disabledContainerColor = contentColor.copy(alpha = 0.7f),
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            painter = painterResource(icon),
            contentDescription = contentDescription
        )
    }

}