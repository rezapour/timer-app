package me.rezapour.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview

@Composable
fun IniPill(
    modifier: Modifier = Modifier,
    value: String,
    size: IniPillSize = IniPillSize.Small,
    @DrawableRes icon: Int? = null,
    contentDescription: String? = null
) {
    Surface(
        modifier = modifier,
        color = IniTheme.colors.container,
        shape = IniTheme.shapes.large
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = size.horizontalPadding, vertical = size.verticalPadding
            ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            icon?.let {
                Icon(
                    modifier = Modifier.size(IniTheme.sizes.iniPillIconSize),
                    painter = painterResource(icon),
                    tint = IniTheme.materialColors.onSurfaceVariant,
                    contentDescription = contentDescription
                )
                Spacer(modifier = Modifier.width(IniTheme.spacing.xs))
            }
            Text(
                text = value,
                style = IniTheme.typography.labelMedium,
                color = IniTheme.materialColors.onSurfaceVariant
            )
        }

    }
}

sealed interface IniPillSize {
    @get:Composable
    @get:ReadOnlyComposable
    val verticalPadding: Dp

    @get:Composable
    @get:ReadOnlyComposable
    val horizontalPadding: Dp

    data object Small : IniPillSize {
        override val verticalPadding: Dp
            @Composable
            @ReadOnlyComposable
            get() = IniTheme.spacing.xxs
        override val horizontalPadding: Dp
            @Composable
            @ReadOnlyComposable
            get() = IniTheme.spacing.s

    }

    data object Large : IniPillSize {
        override val verticalPadding: Dp
            @Composable
            @ReadOnlyComposable
            get() = IniTheme.spacing.s
        override val horizontalPadding: Dp
            @Composable
            @ReadOnlyComposable
            get() = IniTheme.spacing.m

    }
}

@Composable
@IniPreview
fun IniPillPreview() {
    IniTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IniPill(value = "5 rounds • 5:00 total", size = IniPillSize.Large)
            IniPill(value = "45s")
        }

    }
}