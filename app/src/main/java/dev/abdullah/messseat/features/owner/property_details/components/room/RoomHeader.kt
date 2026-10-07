package dev.abdullah.messseat.features.owner.property_details.components.room

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.components.AppSpacer
import dev.abdullah.messseat.core.presentation.components.SearchField
import dev.abdullah.messseat.ui.theme.Space
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
