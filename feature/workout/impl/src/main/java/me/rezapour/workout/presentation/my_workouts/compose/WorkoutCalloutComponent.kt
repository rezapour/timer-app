package me.rezapour.workout.presentation.my_workouts.compose


import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.rezapour.designsystem.components.button.IniPrimaryButton
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview
import me.rezapour.resources.R as res

@Composable
internal fun CallOutComponent(
    modifier: Modifier = Modifier,
    state: CalloutState,
    @DrawableRes icon: Int,
    title: String,
    message: String,
    buttonValue: String,
    buttonIcon: Int? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalloutIcon(
            state = state,
            icon = icon
        )
        Spacer(modifier = Modifier.height(IniTheme.spacing.xxl))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            color = IniTheme.materialColors.onBackground,
            style = IniTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(IniTheme.spacing.s))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = IniTheme.spacing.m),
            text = message,
            color = IniTheme.materialColors.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = IniTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(IniTheme.spacing.xxl))

        IniPrimaryButton(
            text = buttonValue,
            icon = buttonIcon,
            onClick = onClick
        )
    }
}

@Composable
private fun CalloutIcon(
    modifier: Modifier = Modifier,
    state: CalloutState,
    @DrawableRes icon: Int,
    contentDescription: String? = null
) {
    Surface(
        modifier = modifier.size(IniTheme.sizes.calloutIconBackgroundSize),
        shape = RoundedCornerShape(50),
        color = state.backgroundColor,
        border = BorderStroke(
            IniTheme.sizes.numberPickerBorderStroke,
            state.strokeColor
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(IniTheme.sizes.calloutIconSize),
                painter = painterResource(icon),
                tint = state.iconColor,
                contentDescription = contentDescription
            )
        }
    }

}

sealed interface CalloutState {
    @get:Composable
    @get:ReadOnlyComposable
    val iconColor: Color

    @get:Composable
    @get:ReadOnlyComposable
    val backgroundColor: Color

    @get:Composable
    @get:ReadOnlyComposable
    val strokeColor: Color

    data object Info : CalloutState {
        override val iconColor: Color
            @Composable
            @ReadOnlyComposable
            get() = IniTheme.materialColors.primary
        override val backgroundColor: Color
            @Composable
            @ReadOnlyComposable
            get() = IniTheme.colors.callOutContainer
        override val strokeColor: Color
            @Composable
            @ReadOnlyComposable
            get() = IniTheme.materialColors.surfaceVariant
    }

    data object Error : CalloutState {
        override val iconColor: Color
            @Composable
            @ReadOnlyComposable
            get() = IniTheme.materialColors.error
        override val backgroundColor: Color
            @Composable
            @ReadOnlyComposable
            get() = IniTheme.materialColors.errorContainer
        override val strokeColor: Color
            @Composable
            @ReadOnlyComposable
            get() = IniTheme.materialColors.errorContainer

    }
}

@Composable
@IniPreview
private fun CalloutPreviewComponent() {
    IniTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
//            CallOutComponent(
//                state = CalloutState.Info,
//                icon = res.drawable.ic_no_workout,
//                title = "No Workout yet",
//                message = "Create your first interval Timer to get started with your workout.",
//                buttonValue = "Create Workout"
//            ) { }

            CallOutComponent(
                state = CalloutState.Error,
                icon = res.drawable.ic_no_workout,
                title = "Something went wrong",
                message = "We couldn’t load your routines. Please\n" +
                        "check your connection or try again later.",
                buttonValue = "Retry"
            ) { }

        }
    }

}
