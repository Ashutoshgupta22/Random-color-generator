package com.aspark.janitriassign.model

import com.aspark.janitriassign.room.ColorEntity

data class ColorModel(
    val id: Int,
    val color: String,
    val createdAt: String
)

data class ColorListData(
    val colors: List<ColorModel>,
)
