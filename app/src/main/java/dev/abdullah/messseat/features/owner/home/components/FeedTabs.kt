package dev.abdullah.messseat.features.owner.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun FeedTabs(
    totalRoom: Long,
    approved: Long,
    pending: Long,
    selected: Int, onSelect: (Int) -> Unit
) {
    val tabs = listOf(
        "All${if (totalRoom>0) " ($totalRoom)" else ""}",
        "Approved${if (approved > 0) " ($approved)" else ""}",
        "Pending${if (pending > 0) " ($pending)" else ""}"
    )
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Space.sm)
    ) {
        tabs.forEachIndexed { index, label ->
            FilterChip(
                selected = index == selected,
                onClick = { onSelect(index) },
                label = { Text(label, style = MaterialTheme.typography.labelLarge) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}