package com.example.notes_app.domain.notes

import com.example.notes_app.domain.time.DateTimeUtil

class SearchNotes {
    fun execute(notes: List<Notes>, query: String): List<Notes> {
        if (query.isBlank()) return notes
        return notes.filter { it.title.trim().contains(query, ignoreCase = true)
            || it.content.trim().contains(query, ignoreCase = true)
        }.sortedBy { DateTimeUtil.toEpochMillis(it.created) }
    }
}