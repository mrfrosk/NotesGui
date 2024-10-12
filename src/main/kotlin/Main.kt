import Pages.MainPage
import Pages.enums.GlobalPages
import UiComponents.clickableText
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.dto.NoteDto
import data.dto.NotificationDto
import data.dto.Session
import data.source.SpringDataUserSource
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
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


@OptIn(InternalAPI::class)
fun main5() = runBlocking {
    val source = SpringDataUserSource()
    val client = HttpClient(CIO)
    val serverAddress = "http://localhost:8080/api"
    val id = "3045dd46-198b-49ef-b060-2d765968226b"
    val noteId = UUID.fromString("ddbc791a-18c4-402e-ad71-14b6773e8437")
    val date = Instant.now().toKotlinInstant().toLocalDateTime(TimeZone.UTC)
    val dto = NotificationDto("testText", date, false, UUID.fromString(id),noteId)
    source.authUser("test", "123")

    val request = client.post("http://localhost:8080/api/notifications/new"){
        headers.append("Authorization", "Bearer ${Session.accessToken}")
        body = Json.encodeToString(dto)
    }.status

    println(request)

}