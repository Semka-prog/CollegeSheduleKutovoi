package com.example.collegeshedulekutovoi.ui.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun GroupSearchDropdown(
    searchText: String,
    filteredGroups: List<String>,
    selectedGroup: String?,
    isLoading: Boolean,
    errorMessage: String?,
    onSearchTextChanged: (String) -> Unit,
    onGroupSelected: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    
    Box(modifier = modifier) {
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                onSearchTextChanged(it)
                isDropdownExpanded = true
            },
            label = { Text("Выберите группу") },
            placeholder = { Text("ИС-12, СВ-21...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            singleLine = true,
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { 
                        onClear()
                        isDropdownExpanded = false
                    }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Очистить"
                        )
                    }
                }
            },
            isError = errorMessage != null
        )
        
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 48.dp)
            )
        }
        
        if (isDropdownExpanded && searchText.isNotEmpty() && !isLoading) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 60.dp)
                    .zIndex(1f),
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 8.dp
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    if (filteredGroups.isEmpty()) {
                        item {
                            Text(
                                text = "Группы не найдены",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(filteredGroups) { group ->
                            GroupDropdownItem(
                                group = group,
                                isSelected = group == selectedGroup,
                                onClick = {
                                    onGroupSelected(group)
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
        
        if (isLoading) {
            Text(
                text = "Загрузка...",
                modifier = Modifier
                    .padding(start = 16.dp, top = 48.dp)
                    .align(Alignment.TopStart),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun GroupDropdownItem(
    group: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .padding(16.dp)
    ) {
        Text(
            text = group,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}
