package data.source

import com.auth0.jwt.JWT
import data.dto.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import io.ktor.client.engine.cio.*
import io.ktor.server.util.*
import io.ktor.util.*
import kotlinx.datetime.*
import kotlinx.serialization.encodeToString
import java.util.UUID

class SpringDataSource : ISource {
    private val client = HttpClient(CIO)
    private val serverAddress = "http://localhost:8080/api"

    override suspend fun getNote(title: String): NoteDto {
        checkAccessToken()
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

    override suspend fun getUser(email: String): UserInfoDto {
        checkAccessToken()
        val user = client.get("$serverAddress/users/user/$email".encodeURLPath()) {
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }

        return Json.decodeFromString<UserInfoDto>(user.bodyAsText())
    }

    override suspend fun getUsers(): List<String> {
        val users = client.get("http://localhost:8080/api/users/all".encodeURLPath())
        return Json.decodeFromString<List<UserInfoDto>>(users.bodyAsText()).map { it.email }
    }

    @OptIn(InternalAPI::class)
    override suspend fun createUser(fullDto: UserFullDto) {
        checkAccessToken()
        val user = UserFullDto(fullDto.email, fullDto.name, fullDto.surname, fullDto.patronymic, fullDto.password)
        val registration = client.post("$serverAddress/users/new".encodeURLPath()) {
            body = Json.encodeToString(user)
        }

        println(registration.status)
        if (Json.decodeFromString<Boolean>(registration.bodyAsText())) {
            authUser(fullDto.email, fullDto.password)
        }

    }


    @OptIn(InternalAPI::class)
    override suspend fun authUser(email: String, password: String): Boolean {
        val response = client.post("$serverAddress/auth/login".encodeURLPath()) {
            body = Json.encodeToString(mapOf("email" to email, "password" to password))
        }.bodyAsText()
        val tokens = Json.decodeFromString<JwtDto>(response)
        Session.accessToken = tokens.accessToken
        Session.refreshToken = tokens.refreshToken
        Session.email = email
        return tokens.accessToken.isNotEmpty()

    }

    @OptIn(InternalAPI::class)
    override suspend fun createNote(userId: UUID, title: String, text: String) {
        checkAccessToken()
        client.post("$serverAddress/notes/note/new".encodeURLPath()) {
            body = Json.encodeToString(NoteDto(title, text, userId))
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun updateNote(title: String, text: String) {
        checkAccessToken()
        val response = client.put("$serverAddress/notes/note/$title".encodeURLPath()) {
            body = text
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
    }

    override suspend fun deleteNote(title: String) {
        checkAccessToken()
        val response = client.delete("$serverAddress/notes/note/$title".encodeURLPath()) {
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
        println("status : ${response.status}")
    }

    @OptIn(InternalAPI::class)
    override suspend fun createNotification(notificationDto: NotificationDto) {
        val request = client.post("$serverAddress/notifications/new") {
            headers.append("Authorization", "Bearer ${Session.accessToken}")
            body = Json.encodeToString(notificationDto)
        }.status
    }

    @OptIn(InternalAPI::class)
    override suspend fun checkAccessToken() {
        val accessToken = client.post("$serverAddress/auth/request-access-token".encodeURLPath()) {
            body = Json.encodeToString(mapOf("token" to Session.refreshToken, "email" to Session.email))
        }
        Session.accessToken = accessToken.bodyAsText()

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