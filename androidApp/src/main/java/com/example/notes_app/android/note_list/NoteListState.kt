package com.example.notes_app.android.note_list

import com.example.notes_app.domain.notes.Notes

data class NoteListState(
    val notes: List<Notes> = emptyList(),
    val searchText: String = "",
    val isSearchActive: Boolean = false,
)
