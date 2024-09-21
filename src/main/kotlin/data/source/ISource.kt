package data.source

import data.dto.NoteDto
import data.dto.UserInfoDto
import java.util.*

interface ISource {

    suspend fun getNote(title: String): NoteDto

    suspend fun getNotes(userId: UUID): Set<NoteDto>

    suspend fun getUser(email: String): UserInfoDto

    suspend fun getUsers(): List<String>

    suspend fun authUser(email: String, password: String): Boolean

    suspend fun createNote(userId: UUID, title: String, text: String)

    suspend fun updateNote(title: String, text: String)

    suspend fun deleteNote(title: String)
}