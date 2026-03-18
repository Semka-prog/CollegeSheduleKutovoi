package com.example.collegeshedulekutovoi.ui.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collegeshedulekutovoi.viewmodel.GroupSelectionViewModel

@Composable
fun GroupSelectionScreen(
    onGroupSelected: (String) -> Unit,
    viewModel: GroupSelectionViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Выбор группы",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            text = "Выберите вашу учебную группу для просмотра расписания",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        GroupSearchDropdown(
            searchText = state.searchText,
            filteredGroups = state.filteredGroups,
            selectedGroup = state.selectedGroup,
            isLoading = state.isLoading,
            errorMessage = state.errorMessage,
            onSearchTextChanged = { viewModel.onSearchTextChanged(it) },
            onGroupSelected = { viewModel.selectGroup(it) },
            onClear = { viewModel.clearSearch() },
            modifier = Modifier.fillMaxWidth()
        )
        
        Button(
            onClick = {
                state.selectedGroup?.let { onGroupSelected(it) }
            },
            enabled = state.selectedGroup != null && !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .padding(top = 16.dp)
        ) {
            Text(text = "Продолжить")
        }
        
        if (state.selectedGroup != null) {
            Text(
                text = "Выбранная группа: ${state.selectedGroup}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
