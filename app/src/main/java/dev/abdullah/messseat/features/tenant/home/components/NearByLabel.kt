package dev.abdullah.messseat.features.tenant.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NearByLabel(
    modifier: Modifier = Modifier,
    onSearch: () -> Unit
) {

    Row(
        modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            "Nearby rooms",
            style = MaterialTheme.typography.headlineMedium
        )

        TextButton(onClick = onSearch) {
            Text(
                "See all",
                style = MaterialTheme.typography.labelMedium
            )
            Icon(
                Icons.AutoMirrored.Default.ArrowRightAlt, "See all",
                Modifier.size(20.dp)
            )
        }

    }

}