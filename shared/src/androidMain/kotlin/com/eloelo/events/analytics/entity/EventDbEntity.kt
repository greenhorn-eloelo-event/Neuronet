package com.eloelo.events.analytics.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.eloelo.events.analytics.model.Event
import kotlinx.serialization.json.Json

@Entity(tableName = "events")
@TypeConverters(MapConverter::class)
data class EventDbEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Long = 0L,
    @ColumnInfo("event_name")  val eventName: String,
    @ColumnInfo("is_user_login")  val isUserLogin: Boolean,
    @ColumnInfo("payload")  val payload: Map<String, String>,
    @ColumnInfo("time_stamp")  val timestamp: String,
    @ColumnInfo("session_time_stamp")  val sessionTimeStamp: String,
    @ColumnInfo("isSynced")  val isSynced: Boolean
)

fun Event.toDbEntity(): EventDbEntity {
    return EventDbEntity(id, eventName, isUserLogin, payload, timestamp, sessionTimeStamp, isSynced)
}

fun EventDbEntity.toEvent(): Event {
    return Event(id, eventName, isUserLogin, payload, timestamp, sessionTimeStamp, isSynced)
}

class MapConverter {
    @TypeConverter
    fun fromMap(map: Map<String, String>): String {
        return Json.encodeToString(map)
    }

    @TypeConverter
    fun toMap(mapString: String): Map<String, String> {
        return Json.decodeFromString(mapString)
    }
}
