package com.prafullkumar.foodlog.ui.foodHistory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafullkumar.foodlog.ui.components.FoodCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodHistoryScreen(
    historyViewModel: FoodHistoryViewModel,
    navController: NavController
) {
    val history by historyViewModel.history.collectAsState()
    val selectedItems = remember { mutableStateListOf<Int>() }

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text("Food History")
            }, navigationIcon = {
                IconButton(onClick = navController::popBackStack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Pop",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            })
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = selectedItems.isNotEmpty(),
                enter = slideInVertically { height -> height } + fadeIn(),
                exit = slideOutVertically { height -> height } + fadeOut(),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        historyViewModel.deleteFoodItems(selectedItems.toList())
                        selectedItems.clear()
                    },
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete selected items",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }, floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                history,
                key = { it.id },
            ) { foodItem ->
                val isSelected = selectedItems.contains(foodItem.id)
                FoodCard(
                    foodLog = foodItem,
                    isSelected = isSelected,
                    onLongClick = {
                        if (isSelected) {
                            selectedItems.remove(foodItem.id)
                        } else {
                            selectedItems.add(foodItem.id)
                        }
                    }
                )
            }
        }


    }
}