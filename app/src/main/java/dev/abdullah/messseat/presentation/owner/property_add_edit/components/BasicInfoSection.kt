package dev.abdullah.messseat.presentation.owner.property_add_edit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import dev.abdullah.messseat.core.presentation.components.AuthInputTextField
import dev.abdullah.messseat.core.presentation.components.AppDivider
import dev.abdullah.messseat.core.presentation.widgets.PageHeader
import dev.abdullah.messseat.presentation.owner.property_add_edit.PropertyEditorUiState
import dev.abdullah.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos


@Composable
fun BasicInfoSection(
    modifier: Modifier = Modifier,
    state: PropertyEditorUiState,
    onTitleChange: (String) -> Unit,
    onDescChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit
) {

    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Spacer(Modifier.height(Space.md))

        PageHeader(
            title = "Tell us about your place",
            subtitle = "Start with the basic information should know.",
            titleStyle = typos.headlineMedium,
            subtitleStyle = typos.bodyMedium.copy(
                color = colors.onBackground.copy(0.7f)
            ),
        )

        Spacer(Modifier.height(Space.xxxl))

        AuthInputTextField(
            value = state.contactNumber,
            error = state.contactNumberError,
            onValueChange = onPhoneChange,
            label = "Contact Number",
            placeholder = "017XXXXXXXX",
            isPhone = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(Space.lg))
        AppDivider(Modifier.padding(horizontal = Space.md))
        Spacer(Modifier.height(Space.lg))

        AuthInputTextField(
            value = state.title,
            error = state.titleError,
            onValueChange = onTitleChange,
            label = "Listing Title",
            placeholder = "Use a short and clear name",
            icon = null
        )

        Spacer(Modifier.height(Space.lg))

        AuthInputTextField(
            value = state.description,
            onValueChange = onDescChange,
            label = "Property Description",
            placeholder = "Highlight special amenities and nearby landmarks",
            wordCount = "${state.description.length} / 500",
            icon = null,
            singleLine = false,
            minLines = 3,
            shape = 20,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.None
            )
        )

    }

}