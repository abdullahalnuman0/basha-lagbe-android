package dev.abdullah.messseat.features.owner.property_details.components

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.components.AppSpacer
import dev.abdullah.messseat.ui.theme.Space
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@Composable
fun PropertyLocationContact(
    modifier: Modifier = Modifier,
    address: String,
    institution: String,
    phone: String
) {

    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    Surface(
        shadowElevation = 1.dp
    ) {
        Column(
            modifier
                .fillMaxWidth()
                .background(colors.surface)
                .padding(Space.screen),
        ) {

            Text(
                "About this Property",
                style = typos.titleLarge
            )
            AppSpacer(Space.sm)

            Row(
                horizontalArrangement = Arrangement.spacedBy(Space.sm)
            ) {

                Icon(
                    Icons.Outlined.LocationOn, "Address",
                    Modifier.alpha(0.7f)
                )

                Column() {
                    Text(
                        address,
                        style = typos.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )

                    if (institution.isNotEmpty()) {
                        AppSpacer(Space.xs)
                        Text(
                            "Nearby: $institution",
                            style = typos.labelSmall,
                            modifier = Modifier.alpha(0.7f)
                        )
                    }
                }
            }

            AppSpacer(Space.sm)
            PhoneCard(
                phone = phone,
                onCopy = {
                    scope.launch {
                        clipboard.setClipEntry(
                            ClipEntry(
                                ClipData.newPlainText("Phone number", phone)
                            )
                        )
                        Toast.makeText(
                            context,
                            "Phone number copied",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )

        }
    }
}

@Composable
private fun PhoneCard(
    modifier: Modifier = Modifier,
    label: String = "Manager / Owner Contact",
    phone: String,
    onCall: (() -> Unit)? = null,
    onCopy: (() -> Unit)? = null,
) {

    Card(
        modifier,
        colors = CardDefaults.cardColors(
            containerColor = colors.primary.copy(0.05f),
            contentColor = colors.onBackground
        )
    ) {
        Row(
            modifier = Modifier.padding(Space.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space.md)
        ) {

            Box(
                Modifier
                    .size(40.dp)
                    .background(colors.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Call, "Call",
                    tint = colors.background
                )
            }

            Column(Modifier.weight(1f)) {
                Text(
                    label,
                    style = typos.labelMedium.copy(
                        color = colors.onBackground.copy(0.7f)
                    )
                )
                AppSpacer(Space.xs)
                Text(
                    phone,
                    style = typos.titleMedium
                )
            }

            onCall?.let {
                Button(onClick = onCall) {
                    Text("Call")
                }
            }

            onCopy?.let {
                IconButton(onClick = onCopy) {
                    Icon(
                        Icons.Outlined.ContentCopy, "Copy",
                        Modifier.size(20.dp)
                    )
                }
            }


        }
    }

}