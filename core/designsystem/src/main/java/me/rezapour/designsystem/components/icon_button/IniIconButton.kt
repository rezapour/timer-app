package me.rezapour.designsystem.components.icon_button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import me.rezapour.designsystem.R
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview

@Composable
fun IniIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    contentDescription: String? = null,
    enabled: Boolean = true,
    tint: Color = LocalContentColor.current,
    iconSize: Dp = IniTheme.sizes.iconButtonIconSize,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier.size(IniTheme.sizes.iconButtonSize),
        enabled = enabled,
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            painter = painterResource(icon),
            tint = if (enabled) {
                tint
            } else {
                tint.copy(alpha = 0.38f)
            },
            contentDescription = contentDescription
        )
    }
}

@Composable
@IniPreview
private fun IniIconButtonPreview() {
    IniTheme() {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(it.calculateTopPadding()),
                verticalArrangement = Arrangement.Top
            ) {
                IniIconButton(
                    icon = R.drawable.ic_plus
                ) { }

                IniIconButton(
                    icon = R.drawable.ic_plus,
                    enabled = false
                ) { }
            }
        }
    }
}