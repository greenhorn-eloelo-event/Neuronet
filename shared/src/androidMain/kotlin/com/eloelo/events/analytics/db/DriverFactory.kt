//package com.eloelo.events.analytics.db
//
//import android.content.Context
//import app.cash.sqldelight.db.SqlDriver
//import com.eloelo.events.analytics.database.EventDatabase
//
//
//actual class DriverFactory(private val context: Context) {
//    actual fun createDriver(): SqlDriver {
//        return AndroidSqliteDriver(EventDatabase.Schema, context, "event.db")
//    }
//}