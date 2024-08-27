package com.aspark.janitriassign.firebase

import com.aspark.janitriassign.model.ColorModel
import com.aspark.janitriassign.room.ColorEntity
import com.aspark.janitriassign.room.toModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val colorsCollection = db.collection("colors")

     suspend fun syncColors(colors: List<ColorEntity>) {
        colors.forEach { colorEntry ->
            colorsCollection.add(mapOf(
                "color" to colorEntry.color,
                "time" to colorEntry.time,
                "id" to colorEntry.id,
            )).await()
        }
    }

     suspend fun fetchColors(): List<ColorModel> {
        val snapshot = colorsCollection.get().await()
        return snapshot.documents.mapNotNull { document ->
            val color = document.getString("color")
            val time = document.getLong("time")
            val id = document.getLong("id")
            if (color != null && time != null && id != null) {
                ColorEntity(id = id.toInt(), color = color, time = time).toModel()
            } else {
                null
            }
        }
    }
}