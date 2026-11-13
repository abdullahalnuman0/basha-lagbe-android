package dev.example.messseat.features.owner.property_details.components.room

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.example.messseat.core.presentation.components.AppSpacer
import dev.example.messseat.core.presentation.components.SearchField
import dev.example.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos


@Composable
fun RoomHeader(
    modifier: Modifier = Modifier,
    totalRoom: Int,
    searchText: String,
    onAdd: () -> Unit,
    onSearchChange: (String) -> Unit,
    onSearchClear: () -> Unit
) {


 Column(modifier) {
     Row(
         verticalAlignment = Alignment.CenterVertically
     ) {

         Column(
             Modifier.weight(1f)
         ) {

             Text(
                 "Rooms ($totalRoom)",
                 style = typos.titleLarge
             )
             AppSpacer(Space.xs)
             Text(
                 "Manage capacity, rent and availability",
                 style = typos.labelMedium.copy(
                     color = colors.onBackground.copy(0.7f)
                 )
             )
         }

         Button(onClick = onAdd) {
             Icon(Icons.Outlined.Add, "AddRoom")
             Text("Add Room")
         }
     }

     AppSpacer(Space.sm)

     SearchField(
         value = searchText,
         onValueChange = onSearchChange,
         onClear = onSearchClear
     )
 }

}
