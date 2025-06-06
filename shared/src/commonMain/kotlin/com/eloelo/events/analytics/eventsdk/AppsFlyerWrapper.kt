package com.eloelo.events.analytics.eventsdk

/**
 * Expected declaration for an AppsFlyer wrapper.
 * This class will hold the platform-specific AppsFlyer instance.
 * Add common methods here if your shared SDK needs to interact with AppsFlyer directly
 * (e.g., getting AppsFlyer ID, setting customer user ID).
 */
expect class AppsFlyerWrapper {
    fun getAppsFlyerUID(context: PlatformContext): String? // Example: getting AppsFlyer ID
    fun start(context: PlatformContext) // Example: starting AppsFlyer
}