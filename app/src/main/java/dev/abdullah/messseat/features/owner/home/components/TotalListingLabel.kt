package dev.abdullah.messseat.features.owner.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TotalListingLabel(modifier: Modifier = Modifier, total: Long) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Your Rooms",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Normal
            )
        )

        Text(
            text = "$total total",
            style = MaterialTheme.typography.bodyLarge.copy(
//                color = MaterialTheme.colorScheme.primary,
                color = LocalContentColor.current.copy(0.5f)
            )
        )
    }


}