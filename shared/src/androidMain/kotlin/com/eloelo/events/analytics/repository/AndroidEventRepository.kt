package com.eloelo.events.analytics.repository

import com.eloelo.events.analytics.database.EventDatabase
import com.eloelo.events.analytics.model.Event
import com.eloelo.events.analytics.entity.toDbEntity
import com.eloelo.events.analytics.entity.toEvent

class AndroidEventRepository(private val db: EventDatabase) : EventRepository {
    override suspend fun insertEvent(event: Event) {
        db.eventDao().insertEvent(event.toDbEntity())
    }

    override suspend fun getUnsyncedEvents(limit: Int): List<Event> {
        return db.eventDao().getUnsyncedEvents(limit).map { it.toEvent() }
    }

    override suspend fun markEventsAsSynced(eventIds: List<Long>) {
        db.eventDao().markEventsAsSynced(eventIds)
    }

    override suspend fun deleteSyncedEvents(eventIds: List<Long>) {
        db.eventDao().deleteSyncedEvents(eventIds)
    }

    override suspend fun getEventCount(): Long {
        return db.eventDao().getEventCount()
    }
}