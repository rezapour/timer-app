package me.rezapour.add_routine.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.components.IniNumberPicker
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.resources.R as res

@Composable
internal fun RoutineConfigItem(
    modifier: Modifier = Modifier,
    title: String,
    unit: String,
    unitColor: Color,
    @DrawableRes icon: Int,
    tint: Color,
    iconContainerColor: Color,
    value: String = "",
    onIncreased: () -> Unit,
    onDecreased: () -> Unit,
    increasedEnabled: Boolean = true,
    decreasedEnabled: Boolean = true
) {
    Surface(
        modifier = modifier,
        shape = IniTheme.shapes.medium,
        color = IniTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(IniTheme.spacing.m),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingItemIconComponent(
                    icon = icon,
                    tint = tint,
                    containerColor = iconContainerColor
                )

                Spacer(modifier = Modifier.width(IniTheme.spacing.m))

                SettingItemTextComponent(
                    title = title,
                    unit = unit,
                    unitColor = unitColor
                )
            }

            IniNumberPicker(
                value = value,
                onIncrease = onIncreased,
                onDecrease = onDecreased,
                increaseEnabled = increasedEnabled,
                decreaseEnabled = decreasedEnabled
            )
        }
    }
}

@Composable
private fun SettingItemIconComponent(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    tint: Color,
    containerColor: Color,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(
                color = containerColor,
                shape = RoundedCornerShape(50)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(icon),
            tint = tint,
            contentDescription = null
        )
    }
}

@Composable
private fun SettingItemTextComponent(
    modifier: Modifier = Modifier,
    title: String,
    unit: String,
    unitColor: Color
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = IniTheme.typography.bodyLarge,
            color = IniTheme.materialColors.onBackground
        )
        Spacer(modifier = Modifier.width(IniTheme.spacing.xs))
        Text(
            text = unit,
            style = IniTheme.typography.labelMedium,
            color = unitColor

        )
    }
}


@Composable
@IniPreview
private fun SettingItemPreview() {
    IniTheme {
        RoutineConfigItem(
            title = stringResource(res.string.add_routine_work_duration),
            unit = stringResource(res.string.add_routine_seconds),
            unitColor = IniTheme.colors.work,
            icon = res.drawable.ic_workout,
            tint = IniTheme.colors.workContent,
            iconContainerColor = IniTheme.colors.workContainer,
            value = "50",
            onIncreased = {},
            onDecreased = {}
        )
    }
}