package com.aspark.janitriassign.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aspark.janitriassign.generateRandomColor
import com.aspark.janitriassign.model.ColorListData
import com.aspark.janitriassign.repository.ColorRepository
import com.aspark.janitriassign.room.toModel
import com.aspark.janitriassign.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ColorRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ColorListData>>(UiState.Loading)
    val uiState: StateFlow<UiState<ColorListData>> = _uiState.asStateFlow()

    private val _unSyncedCount = MutableStateFlow(0)
    val unSyncedCount: StateFlow<Int> = _unSyncedCount.asStateFlow()

    init {
//        getAllColors()
        getUnsyncedCount()
    }

    private fun getUnsyncedCount() {
        viewModelScope.launch {
            repository.unsyncedCount.collect { count ->
                _unSyncedCount.value = count
            }
        }
    }

     fun getAllDBColors() {
        viewModelScope.launch {
            repository.getAllColors().collect { colors ->
                _uiState.value = UiState.Success(ColorListData(colors))
            }
        }
    }

    fun addRandomColor() {
        viewModelScope.launch {
            try {
                val randomColor = generateRandomColor()
                val currentTime = System.currentTimeMillis()
                repository.addColor(randomColor, currentTime)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun syncColors() {
        viewModelScope.launch {
            try {
                repository.syncColors()
                fetchColors()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun fetchColors() {
        viewModelScope.launch {
//            _uiState.value = UiState.Loading


            repository.fetchRemoteColors().collect { result ->

                 when (result) {
                     is UiState.Success -> {
                         val currentColors = (_uiState.value as? UiState.Success<ColorListData>)?.data?.colors ?: emptyList()
                         Log.i("HomeViewModel", "currentColors: $currentColors")

                         val mergedColors = currentColors + result.data.filter {
                             !currentColors.any { current -> current.id == it.id }
                         }
                         _uiState.value = UiState.Success(ColorListData(mergedColors.sortedByDescending { it.id }))
//                         _uiState.value = UiState.Success(ColorListData(currentColors))
                     }

                     is UiState.Error -> {
                         _uiState.value = UiState.Error(result.message)
                     }

                     UiState.Loading -> {}
                     is UiState.Message -> {
                         _uiState.value = UiState.Message(result.message)
                     }
                 }
             }
        }
    }
}

class ViewModelFactory(
    private val repository: ColorRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        else throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}