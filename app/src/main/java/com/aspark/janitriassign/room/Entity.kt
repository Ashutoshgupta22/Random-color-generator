package com.aspark.janitriassign.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.aspark.janitriassign.model.ColorModel
import com.aspark.janitriassign.parseTime
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity("color")
data class ColorEntity(
   @PrimaryKey(autoGenerate = true) val id: Int,
    val color: String,
    val time: Long,
    @ColumnInfo(name = "is_synced") val isSynced: Boolean = false
)

fun ColorEntity.toModel() = ColorModel(
    id = id,
    color = color,
    createdAt = parseTime(time)
)

//fun ColorModel.toEntity() = ColorEntity(
//    id = id,
//    color = color,
//    time = createdAt
//)
