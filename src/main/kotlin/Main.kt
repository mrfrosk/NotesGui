import Pages.MainPage
import Pages.enums.GlobalPages
import UiComponents.Month
import UiComponents.clickableText
import UiComponents.datePicker
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.dto.NoteDto
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


@Composable
@Preview
fun App() {

    mainScreen()
}

@Composable
fun mainScreen() {
    var page by remember { mutableStateOf(GlobalPages.Note) }
    Row {
        Column {
            clickableText("Заметки", action = { page = GlobalPages.Note })
            clickableText("Тикеты", action = { page = GlobalPages.Ticket })
        }
        when (page) {
            GlobalPages.Note -> ticketPage()
            GlobalPages.Ticket -> ticketPage()
            GlobalPages.Auth -> TODO()
        }
    }
}


@Composable
fun ticketPage() {
    Column {
        Text("Тикеты")
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        val mainPage = MainPage()
        mainPage.draw()
        println(LocalDateTime.now())
    }
}

@OptIn(InternalAPI::class)
fun main2() {
    runBlocking {
        val client = HttpClient(CIO)
        val serverAddress = "http://localhost:8080"
        val token =
            "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJteU1haWwxMiIsImlhdCI6MTcyNjY1NTc3MSwiZXhwIjoxODEzMDU1NzcxfQ.kJrVu3UlYVF7OPA9rCGb5ksbCeS3VK0_qud7pprHA_lQfRkhV845hDeku1jFZDgw4eE5MJL8Ne8GE7sdnBw8Mg"
        val note = client.post("http://localhost:8080/api/notes/new".encodeURLPath()) {
            headers.append("Authorization", token)
            body = Json.encodeToString(NoteDto("as", "as1as", UUID.fromString("6821b6ae-0408-4008-afd6-725b17ff4f39")))
        }
    }
}

