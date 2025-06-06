package com.eloelo.events.analytics.api

import com.eloelo.events.analytics.model.Event

interface EventApi {
    suspend fun sendEvents(events: List<Event>): Boolean
}