package com.eloelo.events.analytics.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.eloelo.events.analytics.api.EventApiImpl
import com.eloelo.events.analytics.database.EventDatabase
import com.eloelo.events.analytics.dispatcher.EventDispatcher
import com.eloelo.events.analytics.repository.AndroidEventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class EventUploadWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    // This is a simplified way to get dependencies. In a real app, use a DI framework.
    private val eventRepository: AndroidEventRepository by lazy {
        AndroidEventRepository(EventDatabase.Companion.getDatabase(appContext))
    }
    private val eventApi: EventApiImpl by lazy {
        EventApiImpl("https://your-backend-api.com") // Replace with your actual base URL
    }
    private val eventDispatcher: EventDispatcher by lazy {
        EventDispatcher(eventRepository, eventApi)
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        println("EventUploadWorker running...")
        val networkStatus = inputData.getBoolean(KEY_HAS_NETWORK, true) // Assume network is available if not specified
        if (!networkStatus && !isNetworkAvailable(applicationContext)) {
            println("No network connectivity. Retrying later.")
            return@withContext Result.retry()
        }

        return@withContext try {
            eventDispatcher.triggerEventUpload()
            Result.success()
        } catch (e: Exception) {
            println("Event upload failed: ${e.message}. Retrying.")
            Result.retry() // Implement exponential backoff for retries
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        // You would typically use ConnectivityManager here
        // For simplicity, we're relying on WorkManager's network constraints for now.
        // Or you can implement a more robust check if needed.
        return true
    }

    companion object {
        const val WORK_TAG = "EventUploadWorkerTag"
        const val KEY_HAS_NETWORK = "hasNetwork"

        fun enqueueWork(context: Context, hasNetwork: Boolean = true) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val uploadRequest = OneTimeWorkRequestBuilder<EventUploadWorker>()
                .setConstraints(constraints)
                .addTag(WORK_TAG)
                .setInputData(Data.Builder().putBoolean(KEY_HAS_NETWORK, hasNetwork).build())
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    10, // Initial delay
                    TimeUnit.SECONDS
                )
                .build()

            WorkManager.getInstance(context).enqueue(uploadRequest)
        }
    }
}