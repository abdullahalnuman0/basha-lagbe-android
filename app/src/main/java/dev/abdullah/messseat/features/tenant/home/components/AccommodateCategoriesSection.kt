package dev.abdullah.messseat.features.tenant.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.components.AppSpacer
import dev.abdullah.messseat.features.shared.settings.components.SectionCard
import dev.abdullah.messseat.ui.theme.Space

private data class AccommodateCategory(
    val title: String,
    val subtitle: String
)

private val items = listOf(
    AccommodateCategory("Boys Hostel", "Male Hostels"),
    AccommodateCategory("Girls Hostel", "Female Hostels"),
    AccommodateCategory("Independent Flat", "Full flats"),
    AccommodateCategory("Bachelor Sublet", "Bachelors mess"),
    AccommodateCategory("Family Homes", "Family suites"),
    AccommodateCategory("Attached Bath AC", "Premium luxury"),
)

@Composable
fun AccommodateCategoriesSection(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    // Beautiful colors suitable for a white/light background
    val colorPalette = listOf(
        Color(0xFF2563EB), // Blue
        Color(0xFF7C3AED), // Purple
        Color(0xFF0F766E), // Teal
        Color(0xFFBE123C), // Rose
        Color(0xFFB45309), // Amber
        Color(0xFF4D7C0F), // Green
        Color(0xFF4338CA), // Indigo
        Color(0xFF0E7490)  // Cyan
    )

    // Generate random colors once per composition
    val randomColors = remember(items) {
        if (items.size <= colorPalette.size) {
            colorPalette.shuffled().take(items.size)
        } else {
            List(items.size) { colorPalette.random() }
        }
    }


    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(Space.md)
    ) {

        Text(
            "Accommodate Categories".uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = LocalContentColor.current.copy(0.8f)
        )

        items.chunked(3).forEachIndexed { rowIndex, chunkList ->

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.md)
            ) {
                chunkList.forEachIndexed { index, item ->
                    Item(
                        modifier = Modifier.weight(1f),
                        item = item,
                        color = randomColors[rowIndex * 3 + index],
                        onClick = onClick
                    )
                }
            }
        }
    }

}


@Composable
private fun Item(
    modifier: Modifier = Modifier,
    item: AccommodateCategory,
    color: Color,
    onClick: () -> Unit
) {


    SectionCard(modifier = modifier
        .height(100.dp), onClick = onClick) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                item.title,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                textAlign = TextAlign.Center
            )
            AppSpacer(2.dp)
            Text(
                item.subtitle,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = LocalContentColor.current.copy(0.5f)
            )
        }
    }

}