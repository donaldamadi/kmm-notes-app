package com.example.notes_app.data.local

import com.example.notes_app.database.NotesAppDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(NotesAppDatabase.Schema, "notes.db")
    }
}