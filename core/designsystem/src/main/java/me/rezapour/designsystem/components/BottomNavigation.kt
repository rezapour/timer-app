package me.rezapour.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.resources.R as res

@Composable
fun MainBottomNavigation(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {

    NavigationBar(
        containerColor = IniTheme.colors.container
    ) {
        IniNavigationBarItem(
            selected = selectedTab == MainTab.WORKOUTS,
            onClick = { onTabSelected(MainTab.WORKOUTS) },
            icon = res.drawable.ic_stop_watch,
            label = stringResource(res.string.navigation_bar_tab_workouts)
        )

        IniNavigationBarItem(
            selected = selectedTab == MainTab.HISTORY,
            onClick = { onTabSelected(MainTab.HISTORY) },
            icon = res.drawable.ic_history,
            label = stringResource(res.string.navigation_bar_tab_history)
        )

        IniNavigationBarItem(
            selected = selectedTab == MainTab.SETTINGS,
            onClick = { onTabSelected(MainTab.SETTINGS) },
            icon = res.drawable.ic_setting,
            label = stringResource(res.string.navigation_bar_tab_setting)
        )
    }
}

@Composable
private fun RowScope.IniNavigationBarItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    label: String? = null,
    selected: Boolean,
    onClick: () -> Unit,
    contentDescription: String? = null
) {
    NavigationBarItem(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                modifier = Modifier.size(IniTheme.sizes.navigationItemIconSize),
                painter = painterResource(icon),
                contentDescription = contentDescription
            )
        },
        label = {
            label?.let {
                Text(
                    text = label,
                    style = IniTheme.typography.labelMedium
                )
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = IniTheme.materialColors.onSecondaryContainer,
            selectedTextColor = IniTheme.materialColors.onSecondaryContainer,
            indicatorColor = IniTheme.materialColors.secondaryContainer,
            unselectedIconColor = IniTheme.materialColors.onSurfaceVariant,
            unselectedTextColor = IniTheme.materialColors.onSurfaceVariant
        )

    )
}

enum class MainTab {
    WORKOUTS,
    HISTORY,
    SETTINGS
}

@Preview
@Composable
private fun MainBottomNavigationPreview() {
    IniTheme {
        MainBottomNavigation(
            selectedTab = MainTab.HISTORY,
            onTabSelected = {})
    }
}