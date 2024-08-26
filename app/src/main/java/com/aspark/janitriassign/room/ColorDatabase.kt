package com.aspark.janitriassign.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ColorEntity::class],
    version = 2)
abstract class ColorDatabase : RoomDatabase() {
    abstract fun colorDao(): ColorDao

    companion object {
        @Volatile private var instance: ColorDatabase? = null

        fun getDatabase(context: Context): ColorDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, ColorDatabase::class.java,
                "color.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}