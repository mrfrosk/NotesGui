package data.source

import data.dto.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class SpringNoteSource : INoteSource {
    private val client = HttpClient(CIO)
    private val serverAddress = "http://localhost:8080/api"

    override suspend fun getNote(title: String): NoteDto {
        Session.checkAccessToken()
        val note = client.get("$serverAddress/notes/note/$title".encodeURLPath()) {
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
        return Json.decodeFromString<NoteDto>(note.bodyAsText())
    }

    override suspend fun getNotes(userId: UUID): Set<NoteDto> {
        val notes = client.get("$serverAddress/notes/$userId".encodeURLPath()) {
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
        return Json.decodeFromString<Set<NoteDto>>(notes.bodyAsText())
    }

    @OptIn(InternalAPI::class)
    override suspend fun createNote(userId: UUID, title: String, text: String) {
        Session.checkAccessToken()
        val request = client.post("$serverAddress/notes/note/new".encodeURLPath()) {
            body = Json.encodeToString(NewNoteDto(title, text, userId))
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
        println(request.bodyAsText())
    }

    @OptIn(InternalAPI::class)
    override suspend fun updateNote(oldTitle: String, title: String, text: String) {
//        Session.checkAccessToken()
        println("oldTitle: $oldTitle")
        println("title: $title")
        println("text: $text")
        println("AccessToken из session: ${Session.accessToken}")
        println(oldTitle)
        val response = client.put("$serverAddress/notes/note/$oldTitle".encodeURLPath()) {
            println("updateDto: ${UpdateNoteDto(title, text)}")
            println(Json.encodeToString(UpdateNoteDto(title, text)))
            body = Json.encodeToString(UpdateNoteDto(title, text))
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
        println("updateStatus: ${response.status}")
    }

    override suspend fun deleteNote(title: String) {
        Session.checkAccessToken()
        val response = client.delete("$serverAddress/notes/note/$title".encodeURLPath()) {
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
        println("status : ${response.status}")
    }

    @OptIn(InternalAPI::class)
    override suspend fun createNotification(notificationDto: NewNotificationDto) {
        val request = client.post("$serverAddress/notifications/new") {
            headers.append("Authorization", "Bearer ${Session.accessToken}")
            body = Json.encodeToString(notificationDto)
        }.status
    }

    override suspend fun getNotifications(noteId: UUID): Set<NotificationDto> {
        val notifications = client.get("$serverAddress/notifications/$noteId".encodeURLPath()) {
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }.bodyAsText()

        println(notifications)

        return Json.decodeFromString<Set<NotificationDto>>(notifications)
    }

    override suspend fun deleteNotification(notificationId: UUID) {
        client.delete("$serverAddress/notifications/notification/$notificationId".encodeURLPath()) {
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
    }

    override suspend fun deleteNotifications(noteId: UUID) {
        client.delete("$serverAddress/notifications/$noteId".encodeURLPath()) {
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
    }
}