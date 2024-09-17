package data.source

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
    val serverAddress = "http://localhost:8080"

    override suspend fun getNote(title: String): NoteDto {
        val note = client.get("$serverAddress/notes/note/$title".encodeURLPath())
        return Json.decodeFromString<NoteDto>(note.bodyAsText())
    }

    override suspend fun getNotes(userId: UUID): List<NoteDto> {
        val notes = client.get("$serverAddress/notes/$userId".encodeURLPath())
        return Json.decodeFromString<List<NoteDto>>(notes.bodyAsText())
    }

    override suspend fun getUsers(): List<UserInfoDto> {
        val users = client.get("http://localhost:8080/api/users/all".encodeURLPath())
        return Json.decodeFromString<List<UserInfoDto>>(users.bodyAsText())
    }

    @OptIn(InternalAPI::class)
    override suspend fun authUser(email: String, password: String): Boolean {
        val response = client.post("$serverAddress/api/auth/login".encodeURLPath()){
            body = Json.encodeToString(mapOf("email" to email, "password" to password))
        }.bodyAsText()
        val tokens = Json.decodeFromString<Map<String, String>>(response)

        return !tokens["accessToken"].isNullOrEmpty()
    }

    @OptIn(InternalAPI::class)
    override suspend fun createNote(userId: UUID, title: String, text: String) {
        client.post("$serverAddress/notes/new".encodeURLPath()){
            body = Json.encodeToString(mapOf("title" to title, "text" to text, "userId" to userId.toString()))
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun updateNote(oldTitle: String, newTitle: String, text: String) {
        client.put("$serverAddress/notes/$oldTitle".encodeURLPath()){
            body = text
        }
    }

    override suspend fun deleteNote(title: String) {
        client.delete("$serverAddress/notes/$title".encodeURLPath())
    }
}