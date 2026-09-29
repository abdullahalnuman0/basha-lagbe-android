package dev.abdullah.messseat.presentation.owner.property_details.components.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.components.AuthInputTextField
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun AvailabilityDialog(
    roomNumber: String,
    availableSeats: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var valueText by rememberSaveable {
        mutableStateOf(availableSeats.toString())
    }

    val value = valueText.toIntOrNull()

    val validationError = when {
        value == null ->
            "Please enter a valid number"

        value < 0 ->
            "Availability cannot be negative"

        else ->
            null
    }

    val hasChanged = value != null && value != availableSeats

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,

        title = {
            Text(
                text = "Room $roomNumber Availability"
            )
        },

        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(Space.lg)
            ) {

                Text(
                    text = "Currently available: $availableSeats"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        enabled = (value ?: 0) > 0,
                        onClick = {
                            val current = value ?: 0

                            if (current > 0) {
                                valueText = (current - 1).toString()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease availability"
                        )
                    }

                    AuthInputTextField(
                        value = valueText,
                        onValueChange = { newValue ->

                            if (newValue.length <= 4 &&
                                newValue.all { it.isDigit() }
                            ) {
                                valueText = newValue
                            }
                        },
                        modifier = Modifier.width(100.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        icon = null,
                        placeholder = ""
                    )

                    IconButton(
                        onClick = {
                            val current = value ?: 0

                            valueText = (current + 1).toString()

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase availability"
                        )
                    }
                }

                validationError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

            }
        },

        confirmButton = {

            Button(
                enabled = validationError == null &&
                        hasChanged,
                onClick = {
                    value?.let(onSave)
                }
            ) {
                Text("Save")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}