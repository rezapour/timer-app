package me.rezapour.designsystem.components.icon_filled_button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.resources.R as res


@Composable
private fun IniFilledIconButton(
    modifier: Modifier,
    enabled: Boolean,
    loading: Boolean,
    colors: ButtonColors,
    @DrawableRes resId: Int,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.size(IniTheme.sizes.filledIconButtonSize),
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(0.dp),
        colors = colors,
        enabled = enabled && !loading,
        onClick = onClick,
    ) {

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(IniTheme.sizes.filledIconButtonIconSize),
                color = LocalContentColor.current,
                strokeWidth = IniTheme.sizes.buttonLoaderStroke
            )
        } else {
            Icon(
                painter = painterResource(resId),
                modifier = Modifier.size(IniTheme.sizes.filledIconButtonIconSize),
                tint = LocalContentColor.current,
                contentDescription = contentDescription
            )
        }
    }
}

@Composable
fun IniPrimaryFilledIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
) {
    IniFilledIconButton(
        modifier = modifier,
        enabled = enabled,
        loading = loading,
        resId = icon,
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
private fun IniFilledIconButton() {
    IniTheme() {
        IniPrimaryFilledIconButton(
            icon = res.drawable.ic_play
        ) { }
    }
}