package dev.abdullah.messseat.core.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppSpacer(
    space: Dp,
    modifier: Modifier = Modifier,
    isVertical: Boolean = true,
) {

    Spacer(if (isVertical) modifier.height(space) else modifier.width(space))

}