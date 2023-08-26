package com.example.notes_app.domain.notes

interface NotesDataSource   {
    suspend fun insertNotes(notes: Notes)
    suspend fun getNoteById(id: Long): Notes?
    suspend fun getAllNotes(): List<Notes>
    suspend fun deleteNoteById(id: Long)
}