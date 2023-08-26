package com.example.notes_app.android.note_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NoteDetailScreen(
    navController: NavController,
    noteId: Long,
    viewModel: NoteDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val hasNoteBeenSaved by viewModel.hasNoteBeenSaved.collectAsState()
    val isNoteTitleEmpty by viewModel.isNoteTitleEmpty.collectAsState()
    val isNoteContentEmpty by viewModel.isNoteContentEmpty.collectAsState()

    val scaffoldState = rememberScaffoldState()
    var showSnackBar by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = hasNoteBeenSaved) {
        if (hasNoteBeenSaved) {
            navController.popBackStack()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isNoteTitleEmpty || isNoteContentEmpty) {
                        showSnackBar = true

                        GlobalScope.launch {
                            delay(3000)
                            showSnackBar = false
                        }
                    } else run {
                        viewModel.saveNote()
                    }
                },
                backgroundColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Note",
                    tint = Color.White
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(Color(state.noteColor))
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TransparentHIntTextField(
                text = state.noteTitle,
                hint = "Enter a title...",
                isHintVisible = state.isNoteTitleHintVisible,
                onValueChanged = viewModel::onNoteTitleChanged,
                onFocusChanged = {
                    viewModel.onNoteTitleFocusChanged(it.isFocused)
                },
                singleLine = true,
                textStyle = TextStyle(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHIntTextField(
                text = state.noteContent,
                hint = "Enter Content...",
                isHintVisible = state.isNoteContentHintVisible,
                onValueChanged = viewModel::onNoteContentChanged,
                onFocusChanged = {
                    viewModel.onNoteContentFocusChanged(it.isFocused)
                },
                singleLine = false,
                textStyle = TextStyle(fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )
        }
    }
    if (showSnackBar) {
        Snackbar(
            action = {
                Text(
                    text = "Dismiss",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            showSnackBar = false
                        }
                )
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Please fill in all fields",
                style = MaterialTheme.typography.body2
            )
        }

    }

}