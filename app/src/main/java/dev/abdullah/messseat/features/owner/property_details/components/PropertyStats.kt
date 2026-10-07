package dev.abdullah.messseat.features.owner.property_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.components.AppCard
import dev.abdullah.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@Composable
fun PropertyStats(
    modifier: Modifier = Modifier,
    stats: List<Triple<String, String, ImageVector>>
) {

    Column(
        modifier
            .fillMaxSize()
            .padding(Space.screen),
        verticalArrangement = Arrangement.spacedBy(Space.listGap)
    ) {
        stats.chunked(2).forEach { rowItem ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.listGap)
            ) {
                rowItem.forEach {
                    StatsCard(
                        modifier = Modifier.weight(1f),
                        stat = it
                    )
                }
            }
        }
    }

}

@Composable
private fun StatsCard(
    modifier: Modifier = Modifier,
    stat: Triple<String, String, ImageVector>
) {

    AppCard(modifier) {
        Row(
            Modifier.padding(Space.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space.sm)
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .background(colors.primary.copy(0.1f), RoundedCornerShape(8.dp))
                    .padding(Space.xs),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    stat.third, stat.first,
                    tint = LocalContentColor.current.copy(0.7f)
                )
            }
            Column {
                Text(
                    stat.second,
                    style = typos.titleLarge
                )
                Text(
                    stat.first,
                    style = typos.labelMedium,
                    modifier = Modifier.alpha(0.7f)
                )
            }
        }
    }

}

