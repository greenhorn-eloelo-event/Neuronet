package com.eloelo.events.analytics.eventsdk

import android.content.Context
import com.eloelo.events.analytics.api.EventApiImpl
import com.eloelo.events.analytics.database.EventDatabase
import com.eloelo.events.analytics.dispatcher.EventDispatcher
import com.eloelo.events.analytics.repository.AndroidEventRepository

// Actual implementation for Android's PlatformContext
actual class PlatformContext(val androidContext: Context)

// Actual implementation for Android's EventSdkBuilder
actual class EventSdkBuilder {
    private var baseUrl: String = ""
    private var appsflyerWrapper: AppsFlyerWrapper? = null

    actual fun setBaseUrl(baseUrl: String): EventSdkBuilder {
        this.baseUrl = baseUrl
        return this
    }

    actual fun setAppsflyer(appsflyer: AppsFlyerWrapper): EventSdkBuilder {
        this.appsflyerWrapper = appsflyer
        return this
    }

    actual fun build(context: PlatformContext): EventSdk {
        // Here, context.androidContext is available due to the actual class definition
        val db = EventDatabase.Companion.getDatabase(context.androidContext)
        val eventRepository = AndroidEventRepository(db)
        val eventApi = EventApiImpl(baseUrl)
        val eventDispatcher = EventDispatcher(eventRepository, eventApi)
        return AndroidEventSdk(context.androidContext, eventDispatcher, appsflyerWrapper)
    }
}