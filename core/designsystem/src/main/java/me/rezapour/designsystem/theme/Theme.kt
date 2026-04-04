package me.rezapour.designsystem.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimary,
    onPrimary = OnLight,
    primaryContainer = GreenContainerDark,
    onPrimaryContainer = GreenContainerLight,

    secondary = OrangeSecondary,
    onSecondary = OnLight,
    secondaryContainer = OrangeContainerDark,
    onSecondaryContainer = Color(0xFFFFDCC2),

    tertiary = SkyTertiary,
    onTertiary = OnLight,
    tertiaryContainer = SkyContainerDark,
    onTertiaryContainer = Color(0xFFBFE9FF),

    background = BackgroundDark,
    onBackground = OnDark,

    surface = SurfaceDark,
    onSurface = OnDark,

    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = MutedDark,

    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,

    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = Color(0xFFFFDAD6),
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimaryDark,
    onPrimary = Color.White,
    primaryContainer = GreenContainerLight,
    onPrimaryContainer = Color(0xFF052E16),

    secondary = OrangeSecondaryDark,
    onSecondary = Color.White,
    secondaryContainer = OrangeContainerLight,
    onSecondaryContainer = Color(0xFF431407),

    tertiary = SkyTertiaryDark,
    onTertiary = Color.White,
    tertiaryContainer = SkyContainerLight,
    onTertiaryContainer = Color(0xFF082F49),

    background = BackgroundLight,
    onBackground = OnLight,

    surface = SurfaceLight,
    onSurface = OnLight,

    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = MutedLight,

    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,

    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = Color(0xFF410E0B),
)

@Composable
fun IniTheme(
    darkTheme: Boolean = true,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val spacing = Spacing()
    val appShapes = AppShapes()
    val sizes = Sizes()

    CompositionLocalProvider(
        LocalSpacing provides spacing,
        LocalShapes provides appShapes,
        LocalSizes provides sizes
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object IniTheme {
    val spacing: Spacing
        @Composable @ReadOnlyComposable get() = LocalSpacing.current
    val sizes: Sizes
        @Composable @ReadOnlyComposable get() = LocalSizes.current

    val appShapes: AppShapes
        @Composable @ReadOnlyComposable get() = LocalShapes.current

    val colors: ColorScheme
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable @ReadOnlyComposable get() = MaterialTheme.shapes
}
