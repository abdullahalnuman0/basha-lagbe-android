package dev.example.messseat.features.shared.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrightnessAuto
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.example.messseat.domain.enums.ThemeMode

@Composable
fun AppearanceSection(
    modifier: Modifier = Modifier,
    selectedTheme: ThemeMode,
    onThemeMode: (ThemeMode) -> Unit
) {
    Column(modifier) {

        SectionLabel("Appearance")
        Spacer(Modifier.height(4.dp))
        SectionCard {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                val options = listOf(
                    Triple(ThemeMode.SYSTEM, "System", Icons.Rounded.BrightnessAuto),
                    Triple(ThemeMode.LIGHT, "Light", Icons.Rounded.LightMode),
                    Triple(ThemeMode.DARK, "Dark", Icons.Rounded.DarkMode),
                )
                options.forEachIndexed { index, (mode, label, icon) ->
                    SegmentedButton(
                        selected = selectedTheme == mode,
                        onClick = { onThemeMode(mode) },
                        shape = SegmentedButtonDefaults.itemShape(index, options.size),
                        icon = {
                            Icon(
                                icon,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                    ) { Text(label) }
                }
            }
        }
    }
}