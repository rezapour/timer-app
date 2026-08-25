package me.rezapour.designsystem.components.button

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
fun IniPrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    loading: Boolean = false,
    @DrawableRes icon: Int? = null,
    onClick: () -> Unit,
) {
    IniButton(
        modifier = modifier,
        text = text,
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

@Composable
fun IniSecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    loading: Boolean = false,
    @DrawableRes resId: Int? = null,
    onClick: () -> Unit,
) {
    IniButton(
        modifier = modifier,
        text = text,
        enabled = enabled,
        loading = loading,
        resId = resId,
        colors = ButtonDefaults.buttonColors(
            containerColor = IniTheme.materialColors.secondaryContainer,
            contentColor = IniTheme.materialColors.onSecondaryContainer,
            disabledContainerColor = IniTheme.materialColors.secondaryContainer.copy(alpha = 0.5f),
            disabledContentColor = IniTheme.materialColors.onSecondaryContainer.copy(alpha = 0.7f),
        ),
        onClick = onClick
    )
}

@Composable
fun IniDestructiveButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    loading: Boolean = false,
    @DrawableRes resId: Int? = null,
    onClick: () -> Unit,
) {
    IniButton(
        modifier = modifier,
        text = text,
        enabled = enabled,
        loading = loading,
        resId = resId,
        colors = ButtonDefaults.buttonColors(
            containerColor = IniTheme.materialColors.error,
            contentColor = IniTheme.materialColors.onError,
            disabledContainerColor = IniTheme.materialColors.error.copy(alpha = 0.5f),
            disabledContentColor = IniTheme.materialColors.onError.copy(alpha = 0.7f),
        ),
        onClick = onClick
    )
}

@Composable
@IniPreview
fun ButtonPreview() {
    IniTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

            IniPrimaryButton(text = "Enabled") {}
            IniPrimaryButton(text = "Disabled", enabled = false) {}
            IniPrimaryButton(text = "Loading", loading = true) {}

            IniSecondaryButton(text = "Enabled") {}
            IniSecondaryButton(text = "Disabled", enabled = false) {}
            IniSecondaryButton(text = "Loading", loading = true) {}

            IniDestructiveButton(text = "Enabled") {}
            IniDestructiveButton(text = "Disabled", enabled = false) {}
            IniDestructiveButton(text = "Loading", loading = true) {}

        }
    }
}