package data.source

import data.dto.Jwt
import data.dto.JwtDto
import data.dto.NoteDto
import data.dto.UserInfoDto
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import io.ktor.client.engine.cio.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import java.util.UUID

class SpringDataSource: ISource {
    private val client = HttpClient(CIO)
    private val serverAddress = "http://localhost:8080/api"

    override suspend fun getNote(title: String): NoteDto {
        val note = client.get("$serverAddress/notes/note/$title".encodeURLPath()){
            headers.append("Authorization", "Bearer ${Jwt.accessToken}")
        }
        return Json.decodeFromString<NoteDto>(note.bodyAsText())
    }

    override suspend fun getNotes(userId: UUID): List<NoteDto> {
        val notes = client.get("$serverAddress/notes/all/$userId".encodeURLPath()){
            headers.append("Authorization", "Bearer ${Jwt.accessToken}")
        }
        return Json.decodeFromString<List<NoteDto>>(notes.bodyAsText())
    }

    override suspend fun getUser(email: String): UserInfoDto {
        val user = client.get("$serverAddress/users/user/$email".encodeURLPath())
        return Json.decodeFromString<UserInfoDto>(user.bodyAsText())
    }

    override suspend fun getUsers(): List<String> {
        val users = client.get("http://localhost:8080/api/users/all".encodeURLPath())
        return Json.decodeFromString<List<String>>(users.bodyAsText())
    }

    @OptIn(InternalAPI::class)
    override suspend fun authUser(email: String, password: String): Boolean {
        val response = client.post("$serverAddress/auth/login".encodeURLPath()){
            body = Json.encodeToString(mapOf("email" to email, "password" to password))
        }.bodyAsText()
        val tokens = Json.decodeFromString<JwtDto>(response)
        Jwt.accessToken = tokens.accessToken
        Jwt.refreshToken = tokens.refreshToken
        return tokens.accessToken.isNotEmpty()
    }

    @OptIn(InternalAPI::class)
    override suspend fun createNote(userId: UUID, title: String, text: String) {
        val reqeust = client.post("$serverAddress/notes/new".encodeURLPath()){
//            body = Json.encodeToString(mapOf("title" to title, "text" to text, "userId" to userId.toString()))
            body = Json.encodeToString(NoteDto(title, text, userId))
            headers.append("Authorization", "Bearer ${Jwt.accessToken}")
        }
        println("status: ${reqeust.status}")
    }

    @OptIn(InternalAPI::class)
    override suspend fun updateNote(title: String, text: String) {
        client.put("$serverAddress/notes/note/$title".encodeURLPath()){
            body = text
            headers.append("Authorization", "Bearer ${Jwt.accessToken}")
        }
    }

    override suspend fun deleteNote(title: String) {
        client.delete("$serverAddress/notes/$title".encodeURLPath())
    }
}