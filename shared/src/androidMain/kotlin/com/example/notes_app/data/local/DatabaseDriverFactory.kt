package com.example.notes_app.data.local

import android.content.Context
import com.example.notes_app.database.NotesAppDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            NotesAppDatabase.Schema,
            context,
            "notes.db"
        )
    }
}