package dev.abdullah.messseat.presentation.auth.signup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.presentation.auth.AuthInputTextField
import androidx.compose.material3.MaterialTheme.colorScheme as color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CustomDropdownMenu(
    modifier: Modifier = Modifier,
    label: String,
    subLabel: String? = null,
    value: String,
    error: String?,
    placeholder: String,
    icon: Painter,
    isExpanded: Boolean,
    values: List<String>,
    readOnly: Boolean = false,
    isEnable: Boolean = true,
    onValueChange: (String) -> Unit = {},
    onItemSelected: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    )

) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (value.isNotEmpty() || isEnable) color.onBackground else color.onBackground.copy(
                    0.5f
                )
            )
        )
        if (value.isEmpty() && subLabel != null) {
            Text(
                text = subLabel,
                style = MaterialTheme.typography.bodySmall.copy(color = color.onBackground.copy(0.5f))
            )
        }
    }
    Spacer(modifier = Modifier.height(6.dp))

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange
    ) {

        AuthInputTextField(
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            value = value,
            error = error,
            onValueChange = onValueChange,
            placeholder = placeholder,
            icon = icon,
            readOnly = readOnly,
            isDropDown = readOnly && isEnable,
            dropDownState = isExpanded,
            isEnable = isEnable,
            keyboardOptions = keyboardOptions
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.background(color.surface)
        ) {
            values.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (value == item) FontWeight.Bold else FontWeight.Normal,
                                color = if (value == item) color.primary else color.onSurface
                            )
                        )
                    },
                    onClick = { onItemSelected(item) }
                )
            }
        }
    }

}