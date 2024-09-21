package data.source

import data.dto.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import io.ktor.client.engine.cio.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import java.util.UUID

class SpringDataSource : ISource {
    private val client = HttpClient(CIO)
    private val serverAddress = "http://localhost:8080/api"

    override suspend fun getNote(title: String): NoteDto {
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
        val user = client.get("$serverAddress/users/user/$email".encodeURLPath())
        return Json.decodeFromString<UserInfoDto>(user.bodyAsText())
    }

    override suspend fun getUsers(): List<String> {
        val users = client.get("http://localhost:8080/api/users/all".encodeURLPath())
        return Json.decodeFromString<List<UserInfoDto>>(users.bodyAsText()).map { it.email }
    }

    @OptIn(InternalAPI::class)
    override suspend fun createUser(fullDto: UserFullDto) {
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
        val reqeust = client.post("$serverAddress/notes/new".encodeURLPath()) {
            body = Json.encodeToString(NoteDto(title, text, userId))
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun updateNote(title: String, text: String) {
        client.put("$serverAddress/notes/$title".encodeURLPath()) {
            body = text
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
    }

    override suspend fun deleteNote(title: String) {
        client.delete("$serverAddress/notes/$title".encodeURLPath()) {
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }
    }
}