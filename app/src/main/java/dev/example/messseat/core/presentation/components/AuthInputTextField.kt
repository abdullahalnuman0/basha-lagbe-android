package dev.example.messseat.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.example.messseat.R
import dev.example.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthInputTextField(
    modifier: Modifier = Modifier,
    value: String,
    labelAlpha: Float = 1f,
    label: String? = null,
    placeholder: String = "Enter your full name",
    error: String? = null,
    wordCount: String? = null,
    icon: Painter? = painterResource(R.drawable.solar_user),
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 4,
    shape: Int = 35,
    isPassword: Boolean = false,
    isPhone: Boolean = false,
    readOnly: Boolean = false,
    isDropDown: Boolean = false,
    dropDownState: Boolean = false,
    isEnable: Boolean = true,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    )
) {

    val unfocusedColor = color.onBackground.copy(0.3f)
    var eyeClosed by remember { mutableStateOf(true) }

    val leadingIcon: (@Composable () -> Unit)? = icon?.let {
        if (isPhone) {
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 12.dp, end = 6.dp)
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+880",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = unfocusedColor
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "|",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = unfocusedColor
                        )
                    )
                }
            }
        } else {
            {
                Icon(icon, placeholder)
            }
        }
    }

    val trailingIcon: (@Composable () -> Unit)? = if (isPassword) {
        {
            IconButton(
                onClick = {
                    eyeClosed = !eyeClosed
                }
            ) {
                if (eyeClosed) {
                    Icon(
                        painter = painterResource(
                            R.drawable.solar_eye_closed
                        ),
                        contentDescription = "Show password"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.RemoveRedEye,
                        contentDescription = "Hide password"
                    )

                }
            }
        }
    } else if (isDropDown) {
        {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownState)
        }
    } else null

    val visualTransformation =
        if (isPassword && eyeClosed) PasswordVisualTransformation()
        else VisualTransformation.None

    val supportingText: (@Composable () -> Unit)? = if (error != null) {
        {
            Text(error)
        }
    } else null


    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        label?.let {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (isEnable) color.onBackground.copy(labelAlpha) else color.onBackground.copy(0.5f)
                )
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(shape),
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            isError = error != null,
            readOnly = readOnly,
            enabled = isEnable,
            supportingText = supportingText,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = unfocusedColor,
                focusedBorderColor = color.primary.copy(0.6f),

                unfocusedLeadingIconColor = unfocusedColor,
                focusedLeadingIconColor = unfocusedColor,

                unfocusedPlaceholderColor = unfocusedColor,
                focusedPlaceholderColor = unfocusedColor,

                unfocusedTrailingIconColor = unfocusedColor,
                focusedTrailingIconColor = unfocusedColor
            ),
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,

            keyboardOptions = keyboardOptions
        )

        wordCount?.let {
            Text(
                text = wordCount,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = color.onBackground.copy(0.5f)
                ),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = Space.sm)
            )
        }

    }


}