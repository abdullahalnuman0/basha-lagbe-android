package dev.abdullah.messseat.core.presentation.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun PageHeader(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    titleStyle: TextStyle? = null,
    subtitleStyle: TextStyle? = null,
    textAlign: TextAlign? = null,
) {

    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = title,
            style = titleStyle ?: MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 36.sp
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = textAlign
        )

        Text(
            text = subtitle,
            style = subtitleStyle ?: MaterialTheme.typography.bodyMedium.copy(
                lineHeight = 22.sp,
                fontSize = 15.sp
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = textAlign
        )

    }

}