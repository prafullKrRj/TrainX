package com.prafullkumar.foodlog.ui.foodHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.foodlog.domain.FoodLogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class FoodHistoryViewModel(
    private val repository: FoodLogRepository
) : ViewModel(), KoinComponent {
    fun deleteFoodItems(selectedItems: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFoodLogs(selectedItems)
        }
    }

    val history =
        repository.getAllFoods().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}