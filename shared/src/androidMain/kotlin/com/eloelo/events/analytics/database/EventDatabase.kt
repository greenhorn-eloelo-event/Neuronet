package com.eloelo.events.analytics.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eloelo.events.analytics.dao.EventDao
import com.eloelo.events.analytics.entity.EventDbEntity
import com.eloelo.events.analytics.entity.MapConverter

@Database(entities = [EventDbEntity::class], version = 1, exportSchema = false)
@TypeConverters(MapConverter::class)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getDatabase(context: Context): EventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Use applicationContext to avoid leaks
                    EventDatabase::class.java,
                    "event_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}