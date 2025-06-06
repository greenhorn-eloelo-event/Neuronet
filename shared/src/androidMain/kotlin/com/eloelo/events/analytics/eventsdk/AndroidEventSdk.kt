package com.eloelo.events.analytics.eventsdk

import android.content.Context
import com.eloelo.events.analytics.constant.Constants
import com.eloelo.events.analytics.dispatcher.EventDispatcher
import com.eloelo.events.analytics.model.Event
import com.eloelo.events.analytics.worker.EventUploadWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AndroidEventSdk(
    private val context: Context,
    private val eventDispatcher: EventDispatcher,
    private val appsFlyerWrapper: AppsFlyerWrapper?,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) : EventSdk {

    init {
        // Observe pending event count to trigger WorkManager
        scope.launch {
            eventDispatcher.pendingEventCount
                .collect { count ->
                    if (count >= 10) { // Batch size
                        println("Event count reached 10 on Android. Enqueuing work.")
                        EventUploadWorker.Companion.enqueueWork(context)
                    }
                }
        }
    }

    override fun trackEvent(eventName: String, isUserLogin : Boolean, payload: MutableMap<String, String>) {
        val eventTs =
            payload[Constants.TIME_STAMP] ?: System.currentTimeMillis()
                .toString()
        val augmentedPayload = payload.toMutableMap()
        appsFlyerWrapper?.let {
            val afId = it.getAppsFlyerUID(PlatformContext(context))
            if (afId != null) {
                augmentedPayload[Constants.APPS_FLYER_ID] = afId
            }
        }

        scope.launch {
            val event = Event(
                eventName = eventName,
                isUserLogin = isUserLogin,
                payload = payload,
                timestamp = eventTs,
                sessionTimeStamp = ""
            )
            eventDispatcher.addEvent(event)
            println("Event tracked: $eventName")
        }
    }

    override fun forceUploadEvents() {
        println("Force uploading events on Android...")
        EventUploadWorker.Companion.enqueueWork(context)
    }

    override fun getPendingEventCount(): Long {
        // A direct read might not be up-to-date immediately due to coroutine nature
        // For a real-time count, you'd observe the flow
        return eventDispatcher.pendingEventCount.value
    }
}