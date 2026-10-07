package dev.abdullah.messseat.features.common.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.ui.theme.AppThemeExt

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    borderStroke: BorderStroke?=null,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = MaterialTheme.shapes.large
    val colors = MaterialTheme.colorScheme
    val border = borderStroke ?: BorderStroke(1.dp, AppThemeExt.colors.cardBorder)
    val body: @Composable () -> Unit = { Column(Modifier.padding(contentPadding), content = content) }
    if (onClick != null) {
        Surface(onClick = onClick, modifier = modifier.fillMaxWidth(), shape = shape, color = colors.surface, border = border, content = body)
    } else {
        Surface(modifier = modifier.fillMaxWidth(), shape = shape, color = colors.surface, border = border, content = body)
    }
}

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier, action: @Composable (() -> Unit)? = null) {
    Row(modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f).semantics { heading() },
        )
        action?.invoke()
    }
}