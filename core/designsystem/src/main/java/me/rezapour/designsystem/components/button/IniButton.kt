package me.rezapour.designsystem.components.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.R
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.ui.compose.condition


@Composable
fun IniButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    @DrawableRes icon: Int? = null,
    enable:Boolean = true,
    onClick: () -> Unit,
) {

    val isSquare = icon != null && text == null
    Button(
        modifier = modifier.condition(isSquare){
            size(54.dp)
        },
        enabled = enable,
        shape = RoundedCornerShape(IniTheme.appShapes.medium),
        colors = ButtonColors(
            containerColor = IniTheme.colors.primary,
            contentColor = IniTheme.colors.onPrimary,
            disabledContainerColor = IniTheme.colors.primary.copy(alpha = 0.6f),
            disabledContentColor = IniTheme.colors.onPrimary.copy(alpha = 0.6f),
        ),
        contentPadding = if (isSquare) PaddingValues(0.dp) else ButtonDefaults.ContentPadding,
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    painter = painterResource(
                        icon
                    ),
                    contentDescription = null
                )
            }
            text?.let {
                Text(text = it)
            }
        }


    }
}

@Composable
@IniPreview
private fun IniButtonPreview() {
    IniTheme {
        IniButton(icon = R.drawable.ic_plus, text = "Text") {
        }
    }
}