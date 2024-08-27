package com.aspark.janitriassign.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ColorDao {

    @Query("SELECT * FROM color")
    fun getAllColors(): Flow<List<ColorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColor(color: ColorEntity)

//    @Query("SELECT EXISTS(SELECT 1 FROM color WHERE id = :colorId)")
//    suspend fun isColorPresent(colorId: Int): Boolean

    @Query("SELECT COUNT(*) FROM color WHERE is_synced = 0")
    fun getUnsyncedCount(): Flow<Int>

    @Query("UPDATE color SET is_synced = 1 WHERE is_synced = 0")
    suspend fun markAllAsSynced(): Int
}