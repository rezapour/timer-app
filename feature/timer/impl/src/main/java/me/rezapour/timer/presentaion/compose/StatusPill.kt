package me.rezapour.timer.presentaion.compose


import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.resources.R as res

@Composable
internal fun StatusPill(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes icon: Int,
    color: Color,
    contentDescription: String? = null
) {
    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.1f),
        contentColor = color,
        shape = IniTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = IniTheme.spacing.l,
                vertical = IniTheme.spacing.s
            ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(15.dp),
                painter = painterResource(icon),
                contentDescription = contentDescription
            )

            Spacer(modifier = Modifier.width(IniTheme.spacing.s))

            Text(
                text = text,
                style = IniTheme.typography.labelMedium
            )
        }
    }
}

@IniPreview
@Composable
private fun StatusPillPreview() {
    IniTheme {
        StatusPill(
            text = "STARTING",
            icon = res.drawable.ic_stop_watch,
            color = IniTheme.materialColors.primary
        )
    }
}

