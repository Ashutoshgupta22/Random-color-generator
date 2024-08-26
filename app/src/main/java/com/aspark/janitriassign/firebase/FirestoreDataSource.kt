package com.aspark.janitriassign.firebase

import com.aspark.janitriassign.room.ColorEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val colorsCollection = db.collection("colors")

     suspend fun syncColors(colors: List<ColorEntity>) {
        colors.forEach { colorEntry ->
            colorsCollection.add(mapOf(
                "color" to colorEntry.color,
                "time" to colorEntry.time
            )).await()
        }
    }
//
//     suspend fun fetchColors(): List<ColorEntity> {
//        val snapshot = colorsCollection.get().await()
//        return snapshot.documents.mapNotNull { document ->
//            val color = document.getString("color")
//            val time = document.getLong("time")
//            if (color != null && time != null) {
//                ColorEntity(color = color, time = time, isSynced = true)
//            } else {
//                null
//            }
//        }
//    }
}