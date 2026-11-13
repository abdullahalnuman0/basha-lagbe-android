package dev.example.messseat.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.example.messseat.ui.theme.Space

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "Search room number (e.g. 101, 204)...",
    shape: Shape = RoundedCornerShape(8.dp),
    onValueChange: (String) -> Unit,
    onClear: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {

    var focused by remember { mutableStateOf(false) }

    BasicTextField(
        cursorBrush= SolidColor(colors.primary),
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .onFocusChanged {
                focused = it.isFocused
            },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            color = if (focused) colors.onSurface else colors.onSurface.copy(0.7f),
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.surface, shape)
                    .clip(shape)
                    .border(
                        width = if (focused) 1.dp else 1.dp,
                        color = if (focused) colors.primary else colors.surfaceVariant,
                        shape = shape
                    )
                    .padding(
                        start = Space.md,
                        end = Space.sm
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Space.sm)
            ) {
                Icon(
                    Icons.Outlined.Search, "Search",
                    tint = colors.onBackground.copy(0.7f),
                    modifier = Modifier.size(20.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty())
                        Text(
                            placeholder,
                            style = typos.labelMedium.copy(
                                color = colors.onSurface.copy(0.5f)
                            )
                        )
                    innerTextField()
                }

                if (onClear != null && value.isNotEmpty())
                    IconButton(
                        onClick = onClear,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Clear, "Search",
                            tint = colors.onBackground.copy(0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
            }

        }
    )
}