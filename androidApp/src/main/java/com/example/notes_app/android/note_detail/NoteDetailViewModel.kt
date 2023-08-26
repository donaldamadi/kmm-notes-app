package com.example.notes_app.android.note_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes_app.domain.notes.Notes
import com.example.notes_app.domain.notes.NotesDataSource
import com.example.notes_app.domain.time.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val notesDataSource: NotesDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteTitle = savedStateHandle.getStateFlow("noteTitle", "")
    private val isNoteTitleTextFocused =
        savedStateHandle.getStateFlow("isNoteTitleTextFocused", false)
    private val noteContent = savedStateHandle.getStateFlow("noteContent", "")
    private val isNoteContentTextFocused =
        savedStateHandle.getStateFlow("isNoteContentTextFocused", false)
    private val noteColor = savedStateHandle.getStateFlow("noteColor", Notes.generateRandomColor())

    val state = combine(
        noteTitle,
        isNoteTitleTextFocused,
        noteContent,
        isNoteContentTextFocused,
        noteColor
    ) { title, isTitleFocused, content, isContentFocused, color ->
        NoteDetailState(
            noteTitle = title,
            isNoteTitleHintVisible = title.isEmpty() && !isTitleFocused,
            noteContent = content,
            isNoteContentHintVisible = content.isEmpty() && !isContentFocused,
            noteColor = color
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteDetailState())

    private val _hasNoteBeenSaved = MutableStateFlow(false)
    val hasNoteBeenSaved = _hasNoteBeenSaved.asStateFlow()

    private val _isNoteTitleEmpty = MutableStateFlow(noteTitle.value.isEmpty())
    val isNoteTitleEmpty = _isNoteTitleEmpty.asStateFlow()

    private val _isNoteContentEmpty = MutableStateFlow(noteContent.value.isEmpty())
    val isNoteContentEmpty = _isNoteContentEmpty.asStateFlow()

    private var existingNoteId: Long? = null

    init {
        savedStateHandle.get<Long>("noteId")?.let { existingNoteId ->
            if (existingNoteId == -1L) {
                return@let
            }
            this.existingNoteId = existingNoteId
            viewModelScope.launch {
                notesDataSource.getNoteById(existingNoteId)?.let { note ->
                    savedStateHandle["noteTitle"] = note.title
                    savedStateHandle["noteContent"] = note.content
                    savedStateHandle["noteColor"] = note.colorHex
                }
            }
        }
    }

    fun onNoteTitleChanged(text: String) {
        savedStateHandle["noteTitle"] = text
        if (text.isNotEmpty()) {
            _isNoteTitleEmpty.value = false
        }
    }

    fun onNoteTitleFocusChanged(isFocused: Boolean) {
        savedStateHandle["isNoteTitleTextFocused"] = isFocused
    }

    fun onNoteContentChanged(text: String) {
        savedStateHandle["noteContent"] = text
        if (text.isNotEmpty()) {
            _isNoteContentEmpty.value = false
        }
    }

    fun onNoteContentFocusChanged(isFocused: Boolean) {
        savedStateHandle["isNoteContentTextFocused"] = isFocused
    }

    fun saveNote() {

        viewModelScope.launch {
            notesDataSource.insertNotes(
                Notes(
                    id = existingNoteId,
                    title = noteTitle.value,
                    content = noteContent.value,
                    colorHex = noteColor.value,
                    created = DateTimeUtil.now()
                )
            )
            _hasNoteBeenSaved.value = true
        }
    }
}