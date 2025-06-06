package com.eloelo.events.analytics.eventsdk

// Expected declaration for PlatformContext
expect class PlatformContext

// Expected declaration for EventSdkBuilder
expect class EventSdkBuilder {
    fun setBaseUrl(baseUrl: String): EventSdkBuilder
    fun setAppsflyer(appsflyer: AppsFlyerWrapper): EventSdkBuilder
    fun build(context: PlatformContext): EventSdk
}