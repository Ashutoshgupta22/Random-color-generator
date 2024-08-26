package com.aspark.janitriassign

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

fun generateRandomColor(): String {
    return String.format("#%06X", Random.nextInt(0xFFFFFF + 1))
}

fun parseTime(time: Long): String{
    val date = Date(time)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}