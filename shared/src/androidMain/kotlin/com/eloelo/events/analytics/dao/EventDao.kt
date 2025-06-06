package com.eloelo.events.analytics.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eloelo.events.analytics.entity.EventDbEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventDbEntity)

    @Query("SELECT * FROM events WHERE isSynced = 0 ORDER BY time_stamp ASC LIMIT :limit")
    suspend fun getUnsyncedEvents(limit: Int): List<EventDbEntity>

    @Query("UPDATE events SET isSynced = 1 WHERE id IN (:eventIds)")
    suspend fun markEventsAsSynced(eventIds: List<Long>)

    @Query("DELETE FROM events WHERE id IN (:eventIds)")
    suspend fun deleteSyncedEvents(eventIds: List<Long>)

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getEventCount(): Long
}