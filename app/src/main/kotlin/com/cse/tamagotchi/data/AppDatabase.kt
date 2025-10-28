package com.cse.tamagotchi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cse.tamagotchi.model.StoreItem
import com.cse.tamagotchi.model.Task

@Database(entities = [StoreItem::class, Task::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun storeItemDao(): StoreItemDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tamagotchi_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
