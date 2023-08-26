package com.example.notes_app.android.note_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes_app.domain.notes.Notes
import com.example.notes_app.domain.notes.NotesDataSource
import com.example.notes_app.domain.notes.SearchNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val notesDataSource: NotesDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val searchNotes = SearchNotes()

    private val notes = savedStateHandle.getStateFlow("notes", emptyList<Notes>())
    private val searchText = savedStateHandle.getStateFlow("searchText", "")
    private val isSearchActive = savedStateHandle.getStateFlow("isSearchActive", false)

    val state = combine(
        notes,
        searchText,
        isSearchActive
    ) { notes, searchText, isSearchActive ->
        NoteListState(
            notes = searchNotes.execute(notes, searchText),
            searchText = searchText,
            isSearchActive = isSearchActive
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteListState())

    /// This is a temporary solution to populate the database with some notes
//    init {
//        viewModelScope.launch {
//            (1..10).forEach {
//                println("Inserting note $it")
//                notesDataSource.insertNotes(
//                    Notes(
//                        id = null,
//                        title = "Note $it",
//                        content = "Content $it",
//                        colorHex = RedOrangeHex,
//                        created = DateTimeUtil.now()
//                    )
//                )
//            }
//        }
//    }

    fun loadNotes() {
        viewModelScope.launch {
            savedStateHandle["notes"] = notesDataSource.getAllNotes()
        }
    }

    fun onSearchTextChanged(searchText: String) {
        savedStateHandle["searchText"] = searchText
    }

    fun onToggleSearch() {
        savedStateHandle["isSearchActive"] = !isSearchActive.value
        if (!isSearchActive.value) {
            savedStateHandle["searchText"] = ""
        }
    }

    fun deleteNoteById(id: Long) {
        viewModelScope.launch {
            notesDataSource.deleteNoteById(id)
            loadNotes()
        }
    }
}