package dev.example.messseat.features.tenant.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.example.messseat.core.presentation.components.SearchField
import dev.example.messseat.features.tenant.search.RoomSearchFilter
import dev.example.messseat.ui.theme.Space

@Composable
fun SearchHeader(
    keyword: String,
    filter: RoomSearchFilter,
    onKeywordChange: (String) -> Unit,
    onSearch: () -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Space.screen,
                end = Space.screen,
                bottom = Space.sm
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        SearchField(
            value = keyword,
            onValueChange = onKeywordChange,
            onClear = {
                onKeywordChange("")
                onSearch()
            },
            placeholder = "Search rooms...",
            modifier = Modifier
                .weight(1f)
                .animateContentSize(),
            shape = CircleShape,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch() }
            )
        )

        AnimatedVisibility(keyword.trim().isNotEmpty()) {
            IconButton(
                onClick = onSearch
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }

        BadgedBox(
            badge = {
                val count = filter.activeFilterCount()

                if (count > 0) {
                    Badge {
                        Text(count.toString())
                    }
                }
            }
        ) {
            IconButton(
                onClick = onFilterClick
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filters"
                )
            }
        }
    }
}
