package com.eloelo.events.analytics.repository

import com.eloelo.events.analytics.model.Event

interface EventRepository {
    suspend fun insertEvent(event: Event)
    suspend fun getUnsyncedEvents(limit: Int): List<Event>
    suspend fun markEventsAsSynced(eventIds: List<Long>)
    suspend fun deleteSyncedEvents(eventIds: List<Long>)
    suspend fun getEventCount(): Long
}