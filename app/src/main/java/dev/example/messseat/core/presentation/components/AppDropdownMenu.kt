package dev.example.messseat.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.example.messseat.ui.theme.Space
import kotlin.Boolean
import kotlin.collections.forEach

data class DropdownEntry(
    val label: String,
    val icon: ImageVector? = null,
    val destructive: Boolean = false,
    val onClick: () -> Unit,
    val badged: Long? = null,
)

@Composable
fun AppDropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismiss: () -> Unit,
    header: (@Composable () -> Unit)? = null,
    entries: List<DropdownEntry>,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 8.dp,
        modifier = modifier,
    ) {
        if (header != null) {
            Column(Modifier.padding(horizontal = Space.md, vertical = Space.sm)) { header() }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
        entries.forEach { entry ->
            DropdownMenuItem(
                text = {
                    Text(
                        entry.label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (entry.destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                    )
                },
                leadingIcon = entry.icon?.let {
                    {
                        BadgedBox(
                            badge = entry.badged?.let {badge->
                                {

                                    if (badge > 0) {
                                        Badge {
                                            Text(badge.toString())
                                        }
                                    }
                                }
                            } ?: {}
                        ) {
                            Icon(
                                it,
                                null,
                                Modifier.size(20.dp),
                                tint = if (entry.destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                },
                onClick = { onDismiss(); entry.onClick() },
            )
        }
    }
}