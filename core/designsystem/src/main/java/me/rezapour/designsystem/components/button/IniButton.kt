package me.rezapour.designsystem.components.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import me.rezapour.designsystem.theme.IniTheme


@Composable
internal fun IniButton(
    modifier: Modifier,
    text: String,
    enabled: Boolean,
    loading: Boolean,
    colors: ButtonColors,
    @DrawableRes resId: Int?,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .height(IniTheme.sizes.buttonHeight),
        contentPadding = PaddingValues(
            horizontal = IniTheme.spacing.l,
            vertical = IniTheme.spacing.s
        ),
        shape = IniTheme.shapes.large,
        colors = colors,
        enabled = enabled && !loading,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {


            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(IniTheme.sizes.buttonLoader),
                    color = LocalContentColor.current,
                    strokeWidth = IniTheme.sizes.buttonLoaderStroke
                )
                Spacer(modifier = Modifier.width(IniTheme.spacing.s))
            } else {
                resId?.let {
                    Icon(
                        painter = painterResource(resId),
                        modifier = Modifier.size(IniTheme.sizes.buttonIcon),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(IniTheme.spacing.s))
                }
            }

            Text(
                text = text,
                style = IniTheme.typography.labelLarge,
                maxLines = 1
            )
        }
    }
}