package com.aspark.janitriassign.repository

import android.util.Log
import com.aspark.janitriassign.firebase.FirestoreDataSource
import com.aspark.janitriassign.model.ColorModel
import com.aspark.janitriassign.room.ColorDao
import com.aspark.janitriassign.room.ColorEntity
import com.aspark.janitriassign.room.toModel
import com.aspark.janitriassign.ui.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class ColorRepository(
    private val colorDao: ColorDao,
    private val remoteDataSource: FirestoreDataSource
) {
    val unsyncedCount: Flow<Int> = colorDao.getUnsyncedCount()

    fun getAllColors(): Flow<List<ColorModel>> {
        return colorDao.getAllColors().map {
            it.map { color -> color.toModel() }
        }
    }

    suspend fun addColor(color: String, time: Long) {
        val colorEntry = ColorEntity(color = color, time = time)
        colorDao.insertColor(colorEntry)
    }

    suspend fun syncColors() {

        val list = colorDao.getAllColors().first()
        val unsyncedColors = list.filter { !it.isSynced }

        Log.i("ColorRepo", "syncColors: $unsyncedColors")
        if (unsyncedColors.isNotEmpty()) remoteDataSource.syncColors(unsyncedColors)

        Log.i("ColorRepo", "syncColors: mark all synced")
        colorDao.markAllAsSynced()
    }

     fun fetchRemoteColors(): Flow<UiState<List<ColorModel>>> = flow {
        try {
            val colors = remoteDataSource.fetchColors()

            if (colors.isEmpty()) emit(UiState.Message("Add some colors to sync"))
            else emit(UiState.Success(colors))
        } catch (e: Exception) {
            Log.i("ColorRepo", "fetchRemoteColors: ${e.message}")
//            emit(UiState.Error(e.message ?: "Unknown error occurred"))
        }
    }.catch {
        emit(UiState.Error(it.message ?: "Unknown error occurred"))
     }
}