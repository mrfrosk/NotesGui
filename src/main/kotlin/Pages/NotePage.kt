package Pages

import Pages.enums.NotePages
import UiComponents.clickableText
import UiComponents.datePicker
import UiComponents.robotoText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import data.dto.NoteDto
import data.dto.NotificationDto
import data.dto.Session
import data.source.SpringDataSource
import kotlinx.coroutines.launch
import kotlinx.datetime.*

class NotePage {
    private val source = SpringDataSource()

    @Composable
    fun draw() {
        val notes by remember {
            mutableStateOf(mutableSetOf<NoteDto>())
        }
        val scope = rememberCoroutineScope()
        var isLoad by remember { mutableStateOf(false) }
        val currentPage = remember { mutableStateOf(NotePages.Create) }
        val selectedNote = remember { mutableStateOf<NoteDto?>(null) }
        val updateNote = suspend {
            notes.clear()
            val email = Session.email
            val id = source.getUser(email!!).id
            notes.addAll(source.getNotes(id))
            isLoad = true

        }

        Column {
            scope.launch {
                updateNote()
            }
            robotoText("Заметки")
            if (isLoad) {
                listOfNote(notes, selectedNote, currentPage)
            }

            when (currentPage.value) {
                NotePages.Create -> newNote(Session.email!!, suspend { isLoad = false })
                NotePages.Info -> detailNoteInfo(selectedNote, suspend {
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
        var callDialog by remember { mutableStateOf(false) }
        if (noteState.value != null) {
            var title by remember { mutableStateOf(noteState.value!!.title) }
            var text by remember { mutableStateOf(noteState.value!!.text) }
            Column(Modifier.fillMaxSize()) {
                OutlinedTextField(title, onValueChange = { title = it }, label = { robotoText("Название") })
                OutlinedTextField(
                    text,
                    onValueChange = { text = it },
                    label = { robotoText("Текст") },
                    modifier = Modifier.fillMaxSize(0.7f)
                )
                Row {
                    Button({
                        scope.launch {
                            source.updateNote(title, text)
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

                    IconButton(onClick = { callDialog = true }) {
                        Icon(Icons.Sharp.Notifications, contentDescription = null, tint = Color.Black)
                    }
                    if (callDialog) {
                        addNotification({ callDialog = false }, noteState.value!!)

                    }
                }
            }
        }
    }

    @Composable
    private fun newNote(email: String, onUpdate: suspend () -> Unit) {
        var createNewNote by remember { mutableStateOf(false) }
        var noteName by remember { mutableStateOf("") }
        var noteText by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        val onClick = {
            if (noteName.isNotEmpty()) {
                scope.launch {
                    val id = source.getUser(email).id
                    source.createNote(id, noteName, noteText)
                    onUpdate()
                    noteName = ""
                    noteText = ""
                }
            }

            createNewNote = !createNewNote
        }
        Column {
            if (createNewNote) {
                OutlinedTextField(
                    value = noteName,
                    onValueChange = { noteName = it },
                    label = { robotoText("Название") }
                )
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { robotoText("Название") },
                    modifier = Modifier.fillMaxSize(0.7f)
                )
            }
            Button(onClick) {
                robotoText("Создать заметку", color = Color.White)
            }

        }
    }


    @Composable
    fun addNotification(onDismissRequest: () -> Unit, note: NoteDto) {
        var notificationText by remember { mutableStateOf("") }
        val notificationDate = remember {
            mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.UTC).date)
        }
        var repeat by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        notificationText,
                        { notificationText = it },
                        placeholder = { robotoText("Текст") })

                    datePicker(notificationDate)
                    Row {
                        robotoText("Повторять")
                        Checkbox(repeat, onCheckedChange = { repeat = !repeat })
                    }

                    Button({
                        onDismissRequest()
                        scope.launch {
                            source.createNotification(
                                NotificationDto(
                                    notificationText,
                                    LocalDateTime(notificationDate.value, LocalTime(0, 0)),
                                    repeat,
                                    note.id!!
                                )
                            )
                        }
                    }) {
                        robotoText("Создать", color = Color.White)
                    }
                }
            }

        }
    }
}

@Composable
private fun listOfNote(
    noteSet: Set<NoteDto>,
    noteState: MutableState<NoteDto?>,
    pageState: MutableState<NotePages>
) {
    Column {
        for (note in noteSet) {
            Row {
                clickableText("Название: ${note.title}", action = {
                    noteState.value = noteSet.find { it.title == note.title }!!
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

