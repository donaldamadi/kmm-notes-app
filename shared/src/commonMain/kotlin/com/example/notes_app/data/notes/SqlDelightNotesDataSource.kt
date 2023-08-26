package com.example.notes_app.data.notes

import com.example.notes_app.database.NotesAppDatabase
import com.example.notes_app.domain.notes.Notes
import com.example.notes_app.domain.notes.NotesDataSource
import com.example.notes_app.domain.time.DateTimeUtil

class SqlDelightNotesDataSource(db: NotesAppDatabase) : NotesDataSource {

    private val queries = db.notesQueries

    override suspend fun insertNotes(notes: Notes) {
        queries.insertNote(
            id = notes.id,
            title = notes.title,
            content = notes.content,
            colorHex = notes.colorHex,
            created = DateTimeUtil.toEpochMillis(notes.created)
         )
    }

    override suspend fun getNoteById(id: Long): Notes? {
        return queries
            .getNoteById(id)
            .executeAsOneOrNull()
            ?.toNotes()
    }

    override suspend fun getAllNotes(): List<Notes> {
       return queries
              .getAllNotes()
              .executeAsList()
              .map { it.toNotes() }
    }

    override suspend fun deleteNoteById(id: Long) {
       queries.deletNoteById(id)
    }
}