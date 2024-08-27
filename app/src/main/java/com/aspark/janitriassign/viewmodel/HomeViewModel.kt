package com.aspark.janitriassign.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aspark.janitriassign.generateRandomColor
import com.aspark.janitriassign.model.ColorListData
import com.aspark.janitriassign.repository.ColorRepository
import com.aspark.janitriassign.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ColorRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ColorListData>>(UiState.Loading)
    val uiState: StateFlow<UiState<ColorListData>> = _uiState.asStateFlow()

    private val _unSyncedCount = MutableStateFlow(0)
    val unSyncedCount: StateFlow<Int> = _unSyncedCount.asStateFlow()

    init {
        getAllColors()
//        viewModelScope.launch {
//            combine(
//                repository.allColors,
//                repository.unsyncedCount
//            ) { colors, unsyncedCount ->
//                ColorListData(colors, unsyncedCount)
//            }.collect { data ->
//                _uiState.value = UiState.Success(data)
//            }
//        }
//        fetchColors()
    }

    fun getAllColors() {

        viewModelScope.launch {
//            _uiState.value = UiState.Loading

            _unSyncedCount.value = repository.unsyncedCount.first()
            repository.getAllColors().collect { colors ->
                _uiState.value = UiState.Success(ColorListData(colors, _unSyncedCount.value))
            }

//            _uiState.value = UiState.Success(ColorListData(emptyList(), _unSyncedCount.value))
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
            _uiState.value = UiState.Loading
            try {
                repository.syncColors()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

//    private fun fetchColors() {
//        viewModelScope.launch {
//            _uiState.value = UiState.Loading
//            try {
//                repository.fetchRemoteColors()
//            } catch (e: Exception) {
//                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
//            }
//        }
//    }
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