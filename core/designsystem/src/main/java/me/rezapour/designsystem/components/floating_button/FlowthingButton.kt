package me.rezapour.designsystem.components.floating_button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.components.icon_filled_button.IniPrimaryFilledIconButton
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.resources.R as res

@Composable
private fun IniFloatingButton(
    modifier: Modifier,
    enabled: Boolean,
    colors: ButtonColors,
    @DrawableRes icon: Int,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.size(IniTheme.sizes.floatingButtonSize),
        shape = IniTheme.shapes.large,
        contentPadding = PaddingValues(0.dp),
        colors = colors,
        enabled = enabled,
        onClick = onClick,
    ) {


        Icon(
            painter = painterResource(icon),
            modifier = Modifier.size(IniTheme.sizes.floatingButtonIconSize),
            tint = LocalContentColor.current,
            contentDescription = contentDescription
        )

    }
}

@Composable
fun IniPrimaryFloatingButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
) {
    IniFloatingButton(
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        colors = ButtonDefaults.buttonColors(
            containerColor = IniTheme.materialColors.primary,
            contentColor = IniTheme.materialColors.onPrimary,
            disabledContainerColor = IniTheme.materialColors.primary.copy(alpha = 0.5f),
            disabledContentColor = IniTheme.materialColors.onPrimary.copy(alpha = 0.7f),
        ),
        onClick = onClick
    )
}

@IniPreview
@Composable
private fun IniFloatingButton() {
    IniTheme() {
        IniPrimaryFilledIconButton(
            icon = res.drawable.ic_add
        ) { }
    }
}