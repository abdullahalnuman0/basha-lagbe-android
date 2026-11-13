package dev.example.messseat.features.owner.property_details.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.example.messseat.core.presentation.components.AppSpacer
import dev.example.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@Composable
fun PropertyAbout(
    modifier: Modifier = Modifier,
    desc: String,
    charLimit: Int = 180,
    shape: Shape = RectangleShape,
    ) {
    var isExpanded by remember { mutableStateOf(false) }
    val isTruncated = desc.length > charLimit

    Surface(
        shadowElevation = 1.dp,
        shape = shape
    ) {
        Column(
            modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .background(colors.surface)
                .padding(Space.screen),
        ) {

            Text(
                "About this Property",
                style = typos.titleLarge
            )
            AppSpacer(Space.sm)
            ExpandableText(
                text = if (isExpanded || !isTruncated) desc else desc.take(charLimit) + "...",
                isExpanded = isExpanded,
                isTruncated = isTruncated,
                onToggle = { isExpanded = !isExpanded },
            )
        }
    }
}

@Composable
private fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    isExpanded: Boolean,
    isTruncated: Boolean,
    onToggle: () -> Unit,
) {

    val annotatedText = buildAnnotatedString {
        withStyle(
            SpanStyle(
                color = colors.onSurface.copy(0.7f),
            )
        ){
            append(text)
        }

        // Only show button when text actually needs expanding
        if (isTruncated) {
            append(" ")

            withStyle(
                SpanStyle(
                    color = colors.primary,
                    fontWeight = FontWeight.Medium
                )
            ) {
                pushLink(
                    LinkAnnotation.Clickable(
                        tag = "read_more",
                        linkInteractionListener = { onToggle() }
                    )
                )

                append(
                    if (isExpanded) "Read less" else "Read more"
                )

                pop()
            }
        }
    }
    Column(
        modifier.animateContentSize()
    ) {
        Text(
            text = annotatedText,
            style = typos.bodyMedium,
        )
    }
}