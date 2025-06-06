package com.eloelo.events.analytics.api

import com.eloelo.events.analytics.model.Event
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class EventApiImpl(private val baseUrl: String) : EventApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    override suspend fun sendEvents(events: List<Event>): Boolean {
        return try {
            val response = httpClient.post("$baseUrl/events") {
                contentType(ContentType.Application.Json)
                setBody(events)
            }
            response.status.value == 200 // Or whatever your success status is
        } catch (e: Exception) {
            // Log the error
            println("Error sending events: ${e.message}")
            false
        }
    }
}