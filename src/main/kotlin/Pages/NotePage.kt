package Pages

import Pages.enums.NotePages
import UiComponents.clickableText
import UiComponents.robotoText
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.dto.NoteDto
import data.dto.UserInfoDto
import data.source.SpringDataSource
import kotlinx.coroutines.launch
import java.util.*

class NotePage {
    private val source = SpringDataSource()

    @Composable
    fun draw(userInfoDto: UserInfoDto) {
        val notes by remember {
            mutableStateOf(mutableSetOf<NoteDto>())
        }
        val scope = rememberCoroutineScope()
        var isLoad by remember { mutableStateOf(false) }
        val currentPage = remember { mutableStateOf(NotePages.Create) }
        val selectNote = remember { mutableStateOf<NoteDto?>(null) }
        val updateNote = suspend {
            notes.clear()
            notes.addAll(source.getNotes(userInfoDto.id))
            isLoad = true
        }

        Column {
            scope.launch {
                updateNote()
            }
            robotoText("Заметки")
            if (isLoad) {
                listOfNote(notes, selectNote, currentPage)
            }

            when (currentPage.value) {
                NotePages.Create -> newNote(userInfoDto.id, suspend { isLoad = false })
                NotePages.Info -> detailNoteInfo(selectNote, suspend {
                    isLoad = false
                }) {
                    isLoad = false
                    currentPage.value = NotePages.Create
                }
            }

        }
    }

    @Composable
    private fun detailNoteInfo(
        noteState: MutableState<NoteDto?>,
        onUpdate: suspend () -> Unit,
        onDelete: suspend () -> Unit
    ) {
        val scope = rememberCoroutineScope()

        if (noteState.value != null) {
            var title by remember { mutableStateOf(noteState.value!!.title) }
            var text by remember { mutableStateOf(noteState.value!!.text) }
            Column(Modifier.fillMaxSize()) {
                OutlinedTextField(title, onValueChange = { title = it }, label = { robotoText("Название") })
                OutlinedTextField(text, onValueChange = { text = it }, label = { robotoText("Текст") })
                Row {
                    Button({
                        scope.launch {
                            source.updateNote(noteState.value!!.title, title, text)
                            onUpdate()
                        }
                    }) {
                        robotoText("Обновить", color = Color.White)
                    }

                    Spacer(Modifier.width(20.dp))

                    Button({
                        scope.launch {
                            source.deleteNote(noteState.value!!.title)
                            onDelete()
                        }
                    }) {
                        robotoText("Удалить", color = Color.White)
                    }
                }
            }
        }
    }

    @Composable
    private fun newNote(userId: UUID, onUpdate: suspend () -> Unit) {
        var createNewNote by remember { mutableStateOf(false) }
        var noteName by remember { mutableStateOf("") }
        var noteText by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        val onClick = {
            if (noteName.isNotEmpty() && noteText.isNotEmpty()) {
                scope.launch {
                    source.createNote(userId, noteName, noteText)
                    onUpdate()
                }
            }

            createNewNote = !createNewNote
        }
        Column {
            if (createNewNote) {
                OutlinedTextField(
                    value = noteName,
                    onValueChange = { noteName = it },
                    placeholder = { robotoText("Название") }
                )
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    placeholder = { robotoText("Текст") }
                )
            }

            Button(onClick) {
                robotoText("Создать заметку", color = Color.White)
            }

        }
    }

    @Composable
    private fun listOfNote(list: Set<NoteDto>, noteState: MutableState<NoteDto?>, pageState: MutableState<NotePages>) {
        val scope = rememberCoroutineScope()
        Column {
            for (note in list) {
                Row {
                    clickableText("Название: ${note.title}", action = {
                        scope.launch { noteState.value = source.getNote(note.title) }
                        if (pageState.value == NotePages.Create) {
                            pageState.value = NotePages.Info
                        } else {
                            pageState.value = NotePages.Create
                        }
                    })
                }
            }
        }
    }

}