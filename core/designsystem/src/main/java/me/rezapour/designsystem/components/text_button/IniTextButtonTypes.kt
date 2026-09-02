package me.rezapour.designsystem.components.text_button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview

@Composable
fun IniPrimaryTextButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    loading: Boolean = false,
    @DrawableRes icon: Int? = null,
    onClick: () -> Unit,
) {
    IniTextButton(
        modifier = modifier,
        text = text,
        enabled = enabled,
        loading = loading,
        resId = icon,
        colors = ButtonDefaults.textButtonColors(
            contentColor = IniTheme.materialColors.primary,
            disabledContentColor = IniTheme.materialColors.primary.copy(alpha = 0.7f),
        ),
        onClick = onClick
    )
}

@Composable
fun IniSecondaryTextButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    loading: Boolean = false,
    @DrawableRes icon: Int? = null,
    onClick: () -> Unit,
) {
    IniTextButton(
        modifier = modifier,
        text = text,
        enabled = enabled,
        loading = loading,
        resId = icon,
        colors = ButtonDefaults.textButtonColors(
            contentColor = IniTheme.materialColors.secondary,
            disabledContentColor = IniTheme.materialColors.secondary.copy(alpha = 0.7f),
        ),
        onClick = onClick
    )
}

@Composable
fun IniDestructiveTextButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    loading: Boolean = false,
    @DrawableRes icon: Int? = null,
    onClick: () -> Unit,
) {
    IniTextButton(
        modifier = modifier,
        text = text,
        enabled = enabled,
        loading = loading,
        resId = icon,
        colors = ButtonDefaults.textButtonColors(
            contentColor = IniTheme.materialColors.error,
            disabledContentColor = IniTheme.materialColors.error.copy(alpha = 0.7f),
        ),
        onClick = onClick
    )
}

@Composable
@IniPreview
fun TextButtonPreview() {
    IniTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

            IniPrimaryTextButton(text = "Enabled") {}
            IniPrimaryTextButton(text = "Disabled", enabled = false) {}
            IniPrimaryTextButton(text = "Loading", loading = true) {}

            IniSecondaryTextButton(text = "Enabled") {}
            IniSecondaryTextButton(text = "Disabled", enabled = false) {}
            IniSecondaryTextButton(text = "Loading", loading = true) {}

            IniDestructiveTextButton(text = "Enabled") {}
            IniDestructiveTextButton(text = "Disabled", enabled = false) {}
            IniDestructiveTextButton(text = "Loading", loading = true) {}

        }
    }
}