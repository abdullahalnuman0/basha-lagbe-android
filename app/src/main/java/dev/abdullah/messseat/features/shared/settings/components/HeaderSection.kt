package dev.abdullah.messseat.features.shared.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.abdullah.messseat.R

import androidx.compose.material3.MaterialTheme.typography as typos

@Composable
fun HeaderSection(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {

        Text(
            stringResource(R.string.app_name).uppercase(),
            style = typos.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            "Settings",
            style = typos.headlineLarge
        )

    }
}