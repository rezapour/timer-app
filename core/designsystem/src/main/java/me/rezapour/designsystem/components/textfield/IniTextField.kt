package me.rezapour.designsystem.components.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.rezapour.designsystem.theme.IniTheme
import me.rezapour.designsystem.util.IniPreview

@Composable
fun IniTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorText: String? = null
) {
    Column(
        modifier = modifier
    ) {

        label?.let {
            Text(
                text = it,
                style = IniTheme.typography.labelMedium,
                color = IniTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(IniTheme.spacing.s))
        }


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            textStyle = IniTheme.typography.bodyLarge,
            label = null,
            singleLine = true,
            enabled = enabled,
            placeholder = {
                placeholder?.let {
                    Text(
                        text = it,
                        style = IniTheme.typography.bodyLarge
                    )
                }
            },
            supportingText = {
                if (isError && !errorText.isNullOrBlank()) {
                    Text(
                        text = errorText,
                        style = IniTheme.typography.labelMedium,
                        color = IniTheme.colors.error
                    )
                }
            },
            shape = IniTheme.shapes.small,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = IniTheme.colors.onSurface,
                unfocusedTextColor = IniTheme.colors.onSurface,

                focusedContainerColor = IniTheme.colors.surface,
                unfocusedContainerColor = IniTheme.colors.surface,

                focusedBorderColor = IniTheme.colors.primary,
                unfocusedBorderColor = IniTheme.colors.outlineVariant,

                cursorColor = IniTheme.colors.primary,

                focusedPlaceholderColor = IniTheme.colors.outline,
                unfocusedPlaceholderColor = IniTheme.colors.outline,
            )
        )
    }


}

@Composable
@IniPreview
private fun IniTextFieldPreview() {
    IniTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(IniTheme.spacing.m)
        ) {
            IniTextField(
                value = "",
                onValueChange = {},
                label = "Routine Name",
                placeholder = "e.g., Morning HIIT"
            )

            IniTextField(
                value = "Morning Workout",
                onValueChange = {},
                label = "Routine Name",
                placeholder = "e.g., Morning HIIT"
            )

            IniTextField(
                value = "Morning Workout",
                onValueChange = {},
                label = "Routine Name",
                enabled = false
            )

            IniTextField(
                value = "",
                onValueChange = {},
                label = "Routine Name",
                placeholder = "e.g., Morning HIIT",
                isError = true,
                errorText = "Something is wrong"
            )

            IniTextField(
                value = "Morning Workout",
                onValueChange = {},
                placeholder = "e.g., Morning HIIT"
            )
        }
    }
}