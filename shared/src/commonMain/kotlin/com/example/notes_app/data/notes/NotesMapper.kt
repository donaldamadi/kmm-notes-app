package com.example.notes_app.data.notes
import com.example.notes_app.domain.notes.Notes
import database.NoteEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun NoteEntity.toNotes() : Notes {
    return Notes(
        id = id,
        title = title,
        content = content,
        colorHex = colorHex,
        created = Instant.fromEpochMilliseconds(created).toLocalDateTime(TimeZone.currentSystemDefault())
    )
}